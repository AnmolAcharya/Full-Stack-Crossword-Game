
package uta.cse3310;

import uta.cse3310.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import java.util.LinkedHashMap;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.UUID;

public class App extends WebSocketServer {

  public ArrayList<String> usernames = new ArrayList<String>();
  private Map<String, Player> activeSessions = new HashMap<String, Player>();
  private Map<String, Game> activeGames = new HashMap<String, Game>();
  public ArrayList<String> words = new ArrayList<String>();

  private Lobby lobby;

  public ArrayList<String> getWords() {
    String str;
    try {
      BufferedReader reader = new BufferedReader(new FileReader("filtered_words.txt"));
      while ((str = reader.readLine()) != null)
        this.words.add(str);
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return this.words;
  }

  public App(int port) {
    super(new InetSocketAddress(port));
  }

  public App(InetSocketAddress address) {
    super(address);
  }

  public App(int port, Draft_6455 draft) {
    super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {

    System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");

    // ServerEvent E = new ServerEvent();

    // allows the websocket to give us the Game when a message arrives
    // conn.setAttachment(G);

    Gson gson = new Gson();
    // Note only send to the single connection
    // conn.send(gson.toJson(E));
    // System.out.println(gson.toJson(E));

    // The state of the game has changed, so lets send it to everyone
    String jsonString;
    jsonString = gson.toJson("test Onopen");
    handleNewConnection(conn);

    System.out.println(jsonString);
    broadcast(jsonString);

  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println(conn + " has closed");
    // Retrieve the game tied to the websocket connection
    Game G = conn.getAttachment();
    G = null;
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    // send out the game state every time
    // to everyone
    String jsonString;
    jsonString = gson.toJson(G);
    broadcast(jsonString);
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    System.out.println(conn + ": " + message);

    // Bring in the data from the webpage
    // A UserEvent is all that is allowed at this point
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    handleMessage(gson, message, conn);
    // UserEvent U = gson.fromJson(message, UserEvent.class);
    // System.out.println(U.Button);

    // Get our Game Object
    // Game G = conn.getAttachment();
    // G.Update(U);

    // Confirm message recieved
    String jsonString;
    jsonString = gson.toJson("Server recieved message");
    broadcast(jsonString);
  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    System.out.println(conn + ": " + message);
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    ex.printStackTrace();
    if (conn != null) {
      // some errors like port binding failed may not be assignable to a specific
      // websocket
    }
  }

  @Override
  public void onStart() {
    System.out.println("Server started!");
    setConnectionLostTimeout(0);
  }

  public static String generateUniqueID() {
    return UUID.randomUUID().toString();
  }

  public void handleNewConnection(WebSocket conn) {
    String uid = generateUniqueID();
    Player newPlayer = new Player(uid);
    activeSessions.put(uid, newPlayer);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("screen", "landing");
    jsonObject.addProperty("type", "newSession");
    jsonObject.addProperty("uid", uid);
    // Send UID to the client
    conn.send(jsonObject.toString());
  }

  public void handleMessage(Gson gson, String jsonMessage, WebSocket conn) {
    JsonElement element = JsonParser.parseString(jsonMessage);
    JsonObject message = element.getAsJsonObject();

    String screen = message.get("screen").getAsString();
    String type = message.get("type").getAsString();

    // Decipher Message
    switch (screen) {
      case "landing":
        // validate username
        String username = message.get("username").getAsString();
        String uid = message.get("uid").getAsString();
        if (type.equals("validateUsername")) {
          // create JSON object
          JsonObject jsonObject = new JsonObject();
          jsonObject.addProperty("screen", "landing");
          jsonObject.addProperty("type", "validateUsername");
          jsonObject.addProperty("uid", uid);
          jsonObject.addProperty("username", username);
          jsonObject.addProperty("valid", validateUsername(username, uid));

          // send validation status to requesting client
          conn.send(jsonObject.toString());

        }
        break;
      case "lobby":
        if (type.equals("createGame")) {
          String userid = message.get("uid").getAsString();
          Player p = activeSessions.get(userid);
          Game G = new Game(p);

          activeGames.put(G.gameId, G);

          JsonObject jsonObject = new JsonObject();
          JsonArray jsonArray = new JsonArray();

          jsonObject.addProperty("screen", "lobby");
          jsonObject.addProperty("type", "updateGameList");
          jsonObject.addProperty("function", "add");
          jsonArray.add(gson.toJson(p));
          jsonObject.addProperty("players", gson.toJson(jsonArray));
          jsonObject.addProperty("gameId", G.gameId);
          jsonObject.addProperty("numPlayer", G.players.size());

          System.out.println(jsonObject.toString());
          broadcast(jsonObject.toString());

        } else if (type.equals("joinGame")) {
          String userid = message.get("uid").getAsString();
          String gameId = message.get("gameId").getAsString();
          Player player = activeSessions.get(userid);
          Game G = activeGames.get(gameId);

          G.addPlayer(player);

          JsonObject jsonObject = new JsonObject();
          JsonArray jsonArray = new JsonArray();

          jsonObject.addProperty("screen", "lobby");
          jsonObject.addProperty("type", "updateGameList");
          jsonObject.addProperty("function", "update");
          for(int i=0; i<G.players.size(); i++) {
            String jsonPlayer = gson.toJson(player);
            jsonArray.add(jsonPlayer);
          }
          jsonObject.addProperty("players", gson.toJson(jsonArray));
          jsonObject.addProperty("gameId", G.gameId);
          jsonObject.addProperty("numPlayer", G.players.size());

          System.out.println(jsonObject.toString());
          broadcast(jsonObject.toString());
        } else if (type.equals("leaveGame")) {
          String userid = message.get("uid").getAsString();
          String gameId = message.get("gameId").getAsString();
          Player player = activeSessions.get(userid);
          Game G = activeGames.get(gameId);

          G.removePlayer(player);

          JsonObject jsonObject = new JsonObject();
          jsonObject.addProperty("screen", "lobby");
          jsonObject.addProperty("type", "updateGameList");

          if (G.players.size() == 0) {
            jsonObject.addProperty("function", "remove");
            jsonObject.addProperty("gameId", G.gameId);
            jsonObject.addProperty("numPlayer", G.players.size());

            activeGames.remove(G.gameId);
            G = null;

          } else {
            jsonObject.addProperty("function", "update");
            jsonObject.addProperty("gameId", G.gameId);
            jsonObject.addProperty("numPlayer", G.players.size());

          }
          System.out.println(jsonObject.toString());
          broadcast(jsonObject.toString());

        }

        break;
      case "game":

        break;
    }
  }

  public boolean validateUsername(String username, String uid) {
    if (usernames.contains(username) || username == null)
      return false;

    Player player = activeSessions.get(uid);
    player.userName = username;
    usernames.add(username);
    return true;
  }

  public static void main(String[] args) {
    // Set up the http server
    int port = 9080;
    HttpServer H = new HttpServer(port, "./html");
    H.start();
    System.out.println("http Server started on port:" + port);

    // create and start the websocket server

    port = 9880;
    App A = new App(port);
    A.start();
    System.out.println("websocket Server started on port: " + port);

  }
}
