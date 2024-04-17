
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

public class App extends WebSocketServer implements GameObserver {

  public ArrayList<String> usernames = new ArrayList<String>();
  public Map<WebSocket, Player> activeConnections = new HashMap<WebSocket, Player>();

  public Map<String, Player> activeSessions = new HashMap<String, Player>();
  public Map<String, Game> activeGames = new HashMap<String, Game>();
  public ArrayList<String> words = new ArrayList<String>();
  public Lobby lobby = new Lobby();
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
  public void notifyGameEnd(Game game) {
    JsonObject jsonObject = new JsonObject();
    Gson gson = new Gson();
    // prepare JSON message
    jsonObject.addProperty("screen", "game");
    jsonObject.addProperty("type", "endGame");
    jsonObject.addProperty("gameId", game.gameId);
    jsonObject.addProperty("leaderboard", gson.toJson(game.leaderboard));
    broadcast(jsonObject.toString());

    // update all time leaderboard
    System.out.println("Game Ended: " + game.gameTitle);
    lobby.updateAllTimeLeaderboard(game.leaderboard);
    jsonObject = new JsonObject();
    // prepare JSON message
    jsonObject.addProperty("screen", "lobby");
    jsonObject.addProperty("type", "updateAllTimeLeaderboard");
    jsonObject.addProperty("leaderboard", gson.toJson(lobby.allTimeLeaderboard));
    broadcast(jsonObject.toString());

    // liquitate the game, then dump it into the ocean to never be heard of again
    activeGames.remove(game.gameId);
    game = null;
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {

    System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");

    Gson gson = new Gson();

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

    if (disconectedPlayer.gameId != null) {
      Game G = activeGames.get(disconectedPlayer.gameId);
      // remove player from game and update joinability
      G.removePlayer(disconectedPlayer);

      if(G.inProgress) {
        // prepare JSON message
        jsonObject.addProperty("screen", "game");
        jsonObject.addProperty("type", "leaveGame");
        jsonObject.addProperty("uid", disconectedPlayer.uid);
        jsonObject.addProperty("gameId", G.gameId);
        jsonObject.addProperty("leaderboard", gson.toJson(G.leaderboard));
      } else {
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
        // prepare JSON message
        jsonObject.addProperty("screen", "lobby");
        jsonObject.addProperty("type", "updateGameList");
        jsonObject.addProperty("uid", disconectedPlayer.uid);
        jsonObject.addProperty("userState", "leave");
        jsonObject.addProperty("gameId", disconectedPlayer.gameId);
      }

      // if no players in game
      if (G.players.size() == 0) {
        // obliterate game from existence
        activeGames.remove(G.gameId);
        G = null;
      }

    }

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
    jsonObject.addProperty("allTimeLeaderboard", gson.toJson(lobby.allTimeLeaderboard));
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
          Game G = new Game(p, words, lobby, this);
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
        if ((G.players.size() == 2 || G.players.size() == 3) && readyPlayerCount >= 2) {
          canStartGame = true;
        } else if (readyPlayerCount > 2) {
          canStartGame = true;
        }
        if (canStartGame) {
          G.startGame();
          jsonObject.addProperty("gameData", gson.toJson(G));
        }
        jsonObject.addProperty("start", canStartGame);
        jsonObject.addProperty("players", gson.toJson(jsonArray));
        jsonObject.addProperty("gameId", G.gameId);
        // broadcast JSON message
        broadcast(jsonObject.toString());

        // update concurrent leaderboard at game start
        if(canStartGame) {
          jsonObject = new JsonObject();
          // prepare JSON message
          jsonObject.addProperty("screen", "lobby");
          jsonObject.addProperty("type", "updateConcurrentLeaderboard");
          jsonObject.addProperty("leaderboard", gson.toJson(lobby.concurrentLeaderboard));
          // broadcast JSON message
          broadcast(jsonObject.toString());
        }

        break;
      case "game":
        if (type.equals("chatRoom")) {
          uid = message.get("uid").getAsString();
          gameId = message.get("gameId").getAsString();
          String text = message.get("message").getAsString();
          Player p = activeSessions.get(uid);
          String userName = p.userName;
          String playerColor = p.color;

          jsonObject = new JsonObject();
          jsonObject.addProperty("screen", "game");
          jsonObject.addProperty("type", "chatRoom");
          jsonObject.addProperty("gameId", gameId);
          jsonObject.addProperty("userName", userName);
          jsonObject.addProperty("color", playerColor);
          jsonObject.addProperty("textToAdd", text);

          broadcast(jsonObject.toString());

        } else if (type.equals("letterSelection")) {
          uid = message.get("uid").getAsString();
          gameId = message.get("gameId").getAsString();
          ArrayList<Integer> letterCoordinate = new ArrayList<Integer>();
          jsonArray = new JsonArray();
          jsonArray = message.get("letterCoordinate").getAsJsonArray();
          for (JsonElement jsonElement : jsonArray) {
            letterCoordinate.add(jsonElement.getAsInt());
          }
          Player p = activeSessions.get(uid);
          Game g = activeGames.get(gameId);

          // add player to letters selections list
          Letter letter = g.grid.grid[letterCoordinate.get(1)][letterCoordinate.get(0)];
          letter.selections.add(p);

          jsonObject = new JsonObject();
          jsonObject.addProperty("screen", "game");
          jsonObject.addProperty("type", "letterSelection");
          jsonObject.addProperty("gameId", gameId);
          jsonObject.addProperty("letter", gson.toJson(letter));
          broadcast(jsonObject.toString());

        } else if (type.equals("validateWord")) {
          uid = message.get("uid").getAsString();
          gameId = message.get("gameId").getAsString();
          ArrayList<Integer> firstCoordinate = new ArrayList<Integer>();
          ArrayList<Integer> secondCoordinate = new ArrayList<Integer>();
          jsonArray = new JsonArray();
          jsonArray = message.get("firstCoordinate").getAsJsonArray();
          for (JsonElement jsonElement : jsonArray) {
            firstCoordinate.add(jsonElement.getAsInt());
          }
          jsonArray = new JsonArray();
          jsonArray = message.get("secondCoordinate").getAsJsonArray();
          for (JsonElement jsonElement : jsonArray) {
            secondCoordinate.add(jsonElement.getAsInt());
          }
          Player p = activeSessions.get(uid);
          Game g = activeGames.get(gameId);

          Letter firstLetter = g.grid.grid[firstCoordinate.get(1)][firstCoordinate.get(0)];
          Letter secondLetter = g.grid.grid[secondCoordinate.get(1)][secondCoordinate.get(0)];

          jsonObject = new JsonObject();
          jsonObject.addProperty("screen", "game");

          if (g.grid.validateSelection(firstLetter, secondLetter)) {
            g.updateLeaderboard(p);
            g.checkEndGame();
            firstLetter.selections.clear();
            secondLetter.selections.clear();

            jsonObject.addProperty("type", "validWord");
            jsonObject.addProperty("firstLetter", gson.toJson(firstLetter));
            jsonObject.addProperty("secondLetter", gson.toJson(secondLetter));
            jsonObject.addProperty("wordBank", gson.toJson(g.grid.wordBank));
            jsonObject.addProperty("leaderboard", gson.toJson(g.leaderboard));
            jsonObject.addProperty("playerColor", p.color);
          } else {
            firstLetter.selections.remove(p);
            jsonObject.addProperty("type", "invalidWord");
            jsonObject.addProperty("firstLetter", gson.toJson(firstLetter));
          }

          jsonObject.addProperty("gameId", gameId);

          broadcast(jsonObject.toString());
        } else if (type.equals("leaveGame")) {
          // get player that's leaving game, and the game they are leaving
          uid = message.get("uid").getAsString();
          gameId = message.get("gameId").getAsString();
          Player p = activeSessions.get(uid);
          Game g = activeGames.get(gameId);

          // remove player from game
          g.removePlayer(p);
          p.gameId = null;

          jsonObject = new JsonObject();
          // prepare JSON message
          jsonObject.addProperty("screen", "game");
          jsonObject.addProperty("type", "leaveGame");
          jsonObject.addProperty("uid", p.uid);
          jsonObject.addProperty("gameId", g.gameId);
          jsonObject.addProperty("leaderboard", gson.toJson(g.leaderboard));

          // if no players in game
          if (g.players.size() == 0) {
            // obliterate game from existence
            activeGames.remove(g.gameId);
            g = null;
          }

          // broadcast JSON message
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
    // Environment variables
    String testGrid = System.getenv("TEST_GRID");
    String httpPort = System.getenv("HTTP_PORT");
    String websocketPort = System.getenv("WEBSOCKET_PORT");
    String version = System.getenv("VERSION");
    
    // Set up the http server
    int port = (httpPort != null ? Integer.parseInt(httpPort) : 9002);
    HttpServer H = new HttpServer(port, "./html");
    H.start();
    System.out.println("http Server started on port:" + port);

    // create and start the websocket server

    port = (websocketPort != null ? Integer.parseInt(websocketPort) : 9102);
    App A = new App(port);
    A.getWords();
    A.start();
    System.out.println("websocket Server started on port: " + port);

  }
}
