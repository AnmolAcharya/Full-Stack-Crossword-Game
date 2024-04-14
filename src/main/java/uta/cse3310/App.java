
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

  public Lobby lobby = new Lobby();
  public ArrayList<String> usernames = new ArrayList<String>();
  public Map<WebSocket, Player> activeConnections = new HashMap<WebSocket, Player>();
  public Map<String, Player> activeSessions = new HashMap<String, Player>();
  public Map<String, Game> activeGames = new HashMap<String, Game>();
  public ArrayList<String> words = new ArrayList<String>();

  public String id = null;

  public ArrayList<String> getWords() {
    String str;
    try {
      BufferedReader reader = new BufferedReader(new FileReader("src/filtered_words.txt"));
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
    jsonString = gson.toJson("New Server Connection");
    handleNewConnection(conn, gson);

    broadcast(jsonString);

  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println(conn + " has closed");
    Player disconectedPlayer = activeConnections.get(conn);

    
    // remove player from server
    activeConnections.remove(conn);
    activeSessions.remove(disconectedPlayer.uid);
    // remove players username from list
    usernames.remove(disconectedPlayer.userName);
    
    System.out.println(disconectedPlayer + " removed"); // removed username from list
    
    Gson gson = new Gson();
    JsonObject jsonObject = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    
    if(disconectedPlayer.gameId != null){
      Game G = activeGames.get(disconectedPlayer.gameId);
      // remove player from game and update joinability
      G.removePlayer(disconectedPlayer);
      G.updateJoinable();

      jsonObject.addProperty("numPlayer", G.players.size());
      if (G.joinable) {
        jsonObject.addProperty("function", "update");
        // Add players from game to JSON
        for (int i = 0; i < G.players.size(); i++) {
          String jsonPlayer = gson.toJson(G.players.get(i));
          jsonArray.add(jsonPlayer);
        }
        jsonObject.addProperty("players", gson.toJson(jsonArray));
      } else {
        jsonObject.addProperty("function", "remove");
      }
  
      // if no players in game
      if (G.players.size() == 0) {
        // obliterate game from existence
        activeGames.remove(G.gameId);
        G = null;
      }

    }
    
    // prepare JSON message
    jsonObject.addProperty("screen", "lobby");
    jsonObject.addProperty("type", "updateGameList");
    jsonObject.addProperty("uid", disconectedPlayer.uid);
    jsonObject.addProperty("userState", "leave");
    jsonObject.addProperty("gameId", disconectedPlayer.gameId);
    
    // broadcast JSON message
    broadcast(jsonObject.toString());

    // absolutley extirpate the disconnected player off the face of the earth
    disconectedPlayer = null;
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    System.out.println(conn + ": " + message);

    // Bring in the data from the webpage
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    handleMessage(gson, message, conn);

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

  public void handleNewConnection(WebSocket conn, Gson gson) {
    String uid = generateUniqueID();
    id = uid;
    Player newPlayer = new Player(uid);
    activeConnections.put(conn, newPlayer);
    activeSessions.put(uid, newPlayer);
    lobby.updateLobby(activeGames);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("screen", "landing");
    jsonObject.addProperty("type", "newSession");
    jsonObject.addProperty("uid", uid);
    jsonObject.addProperty("lobby", gson.toJson(lobby));
    // Send UID and lobby info to the client
    conn.send(jsonObject.toString());
  }

  public void handleMessage(Gson gson, String jsonMessage, WebSocket conn) {
    JsonElement element = JsonParser.parseString(jsonMessage);
    JsonObject message = element.getAsJsonObject();

    String screen = message.get("screen").getAsString();
    String type = message.get("type").getAsString();
    String uid;
    String gameId;
    JsonObject jsonObject = new JsonObject();

    // Decipher Message
    switch (screen) {
      case "landing":
        // validate username
        String username = message.get("username").getAsString();
        uid = message.get("uid").getAsString();
        if (type.equals("validateUsername")) {
          // create JSON object
          jsonObject = new JsonObject();
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
          uid = message.get("uid").getAsString();
          // retrieve player
          Player p = activeSessions.get(uid);
          // create new game object
          Game G = new Game(p, words);
          p.gameId = G.gameId;

          // add game to active games map and update lobby
          activeGames.put(G.gameId, G);
          lobby.updateLobby(activeGames);

          jsonObject = new JsonObject();
          JsonArray jsonArray = new JsonArray();
          // prepare JSON message
          jsonObject.addProperty("screen", "lobby");
          jsonObject.addProperty("type", "updateGameList");
          jsonObject.addProperty("uid", uid);
          jsonObject.addProperty("userState", "join");
          jsonObject.addProperty("function", "add");
          jsonArray.add(gson.toJson(p));
          jsonObject.addProperty("players", gson.toJson(jsonArray));
          jsonObject.addProperty("gameId", G.gameId);
          jsonObject.addProperty("numPlayer", G.players.size());
          jsonObject.addProperty("gameTitle", G.gameTitle);

          // broadcast JSON message
          broadcast(jsonObject.toString());

        } else if (type.equals("joinGame")) {
          // get user id of player who joined game, and the game id of the game they've
          // joined
          uid = message.get("uid").getAsString();
          gameId = message.get("gameId").getAsString();
          Player player = activeSessions.get(uid);
          Game G = activeGames.get(gameId);
          player.gameId = G.gameId;

          // add player to game and update joinability
          G.addPlayer(player);
          G.updateJoinable();

          jsonObject = new JsonObject();
          JsonArray jsonArray = new JsonArray();
          // prepare JSON message
          jsonObject.addProperty("screen", "lobby");
          jsonObject.addProperty("type", "updateGameList");
          jsonObject.addProperty("uid", uid);
          jsonObject.addProperty("userState", "join");
          // If game is not joinable remove game from list, else just update game list
          if (G.players.size() < 4) {
            jsonObject.addProperty("function", "update");
          } else {
            jsonObject.addProperty("function", "remove");
          }
          // Add players from game to JSON
          for (int i = 0; i < G.players.size(); i++) {
            String jsonPlayer = gson.toJson(G.players.get(i));
            jsonArray.add(jsonPlayer);
          }
          jsonObject.addProperty("players", gson.toJson(jsonArray));
          jsonObject.addProperty("gameId", G.gameId);
          jsonObject.addProperty("numPlayer", G.players.size());
          // broadcast JSON message
          broadcast(jsonObject.toString());
        } else if (type.equals("leaveGame")) {
          // get player that's leaving game, and the game they are leaving
          uid = message.get("uid").getAsString();
          gameId = message.get("gameId").getAsString();
          Player player = activeSessions.get(uid);
          Game G = activeGames.get(gameId);
          player.gameId = null;

          // remove player from game and update joinability
          G.removePlayer(player);
          G.updateJoinable();

          jsonObject = new JsonObject();
          JsonArray jsonArray = new JsonArray();
          // prepare JSON message
          jsonObject.addProperty("screen", "lobby");
          jsonObject.addProperty("type", "updateGameList");
          jsonObject.addProperty("uid", uid);
          jsonObject.addProperty("userState", "leave");
          jsonObject.addProperty("gameId", G.gameId);
          jsonObject.addProperty("numPlayer", G.players.size());
          if (G.joinable) {
            jsonObject.addProperty("function", "update");
            jsonObject.addProperty("gameTitle", G.gameTitle);
            // Add players from game to JSON
            for (int i = 0; i < G.players.size(); i++) {
              String jsonPlayer = gson.toJson(G.players.get(i));
              jsonArray.add(jsonPlayer);
            }
            jsonObject.addProperty("players", gson.toJson(jsonArray));
          } else {
            jsonObject.addProperty("function", "remove");
          }

          // if no players in game
          if (G.players.size() == 0) {
            // obliterate game from existence
            activeGames.remove(G.gameId);
            G = null;
          }

          // broadcast JSON message
          broadcast(jsonObject.toString());

        }
        break;
      case "ready":
        uid = message.get("uid").getAsString();
        gameId = message.get("gameId").getAsString();
        Player player = activeSessions.get(uid);
        Game G = activeGames.get(gameId);

        player.ready = message.get("ready").getAsBoolean();

        jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        // prepare JSON message
        jsonObject.addProperty("screen", "ready");
        jsonObject.addProperty("type", "updateStatus");
        // Add players from game to JSON
        // check if game can start
        int readyPlayerCount = 0;
        boolean canStartGame = false;
        for (int i = 0; i < G.players.size(); i++) {
          if (G.players.get(i).ready == true) {
            readyPlayerCount++;
          }
          String jsonPlayer = gson.toJson(G.players.get(i));
          jsonArray.add(jsonPlayer);
        }
        if(G.players.size() == 2 && readyPlayerCount == 2) {
          canStartGame = true;
        } else if(readyPlayerCount >2) {
          canStartGame = true;
        }
        if(canStartGame) {
          G.startGame();
          jsonObject.addProperty("gameData", gson.toJson(G));
        }
        
        jsonObject.addProperty("start", canStartGame);
        jsonObject.addProperty("players", gson.toJson(jsonArray));
        jsonObject.addProperty("gameId", G.gameId);
        // broadcast JSON message
        broadcast(jsonObject.toString());

        break;
      case "game":
        if (type.equals("chatRoom")) {
          uid = message.get("uid").getAsString();
          gameId = message.get("gameId").getAsString();
          String text = message.get("message").getAsString();
          Player p = activeSessions.get(uid);
          String userName = p.userName;

          jsonObject = new JsonObject();
          jsonObject.addProperty("screen", "game");
          jsonObject.addProperty("type", "chatRoom");
          jsonObject.addProperty("gameId", gameId);
          jsonObject.addProperty("userName", userName);
          jsonObject.addProperty("textToAdd", text);

          System.out.println(jsonObject.toString());
          broadcast(jsonObject.toString());

        }
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
    A.getWords();
    A.start();
    System.out.println("websocket Server started on port: " + port);

  }
}
