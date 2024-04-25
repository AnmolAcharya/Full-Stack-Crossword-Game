package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;



public class AppTest extends TestCase {

    App app = new App(9002);
    Gson gson = new Gson();
    WebSocket webSocketMock = Mockito.mock(WebSocket.class);

    public AppTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(AppTest.class);

    }

    public void testHandleMessage() {
    	app.getWords();
        app.start();
    	Player firstPlayer = new Player("xxaa");
        Player secondPlayer = new Player("xxbb");

        app.activeSessions.put("xxaa", firstPlayer);
        app.activeSessions.put("xxbb", secondPlayer);

        String firstPlayerUN = "aa";
        String firstPlayerID = "xxaa";
        assertTrue("true", app.validateUsername(firstPlayerUN, firstPlayerID));

        String secondPlayerUN = "aa";
        String secondPlayerID = "xxbb";
        assertFalse("false", app.validateUsername(secondPlayerUN, secondPlayerID));

               // Add your assertions or test logic here
        String jsonMessage = "{\"screen\":\"lobby\",\"type\":\"createGame\",\"uid\":\"xxaa\"}";
        
        assertTrue("true", app.activeGames.size() == 0);
        app.handleMessage(gson, jsonMessage, webSocketMock);
        
        assertTrue("true", app.activeGames.size() == 1);

 
        String gameId = app.activeGames.keySet().stream().findFirst().orElse(null);
        String jsonMessage2 = "{\"screen\":\"lobby\",\"type\":\"joinGame\",\"uid\":\"xxbb\", \"gameId\":\""+gameId +"\"}";
        assertTrue("true", app.activeGames.get(gameId).players.size() == 1);
        app.handleMessage(gson, jsonMessage2, webSocketMock);
        assertTrue("true", app.activeGames.get(gameId).players.size() == 2);
        
        String jsonMessage3 = "{\"screen\":\"lobby\",\"type\":\"leaveGame\",\"uid\":\"xxbb\", \"gameId\":\""+gameId +"\"}";
        assertTrue("players before leaveGame", app.activeGames.get(gameId).players.size() == 2);
        app.handleMessage(gson, jsonMessage3, webSocketMock);
        assertTrue("players after leaveGame", app.activeGames.get(gameId).players.size() == 1);


    }

    
        
}
