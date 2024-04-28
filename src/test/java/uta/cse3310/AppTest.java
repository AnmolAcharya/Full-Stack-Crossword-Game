package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import org.mockito.Mockito;
import com.google.gson.Gson;

public class AppTest extends TestCase {

    

    public AppTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

  

    public void testValidateUsername() {
    
    	App app =  new App(9002);
    	Gson gson = new Gson();
    	WebSocket webSocketMock = Mockito.mock(WebSocket.class);
    	app.getWords();
    	
        Player firstPlayer = new Player("xxaa");
        Player secondPlayer = new Player("xxbb");

        app.activeSessions.put("xxaa", firstPlayer);
        app.activeSessions.put("xxbb", secondPlayer);

        String firstPlayerUN = "aa";
        String firstPlayerID = "xxaa";
        assertTrue("Username validation failed", app.validateUsername(firstPlayerUN, firstPlayerID));

        String secondPlayerUN = "aa";
        String secondPlayerID = "xxbb";
        assertFalse("Username validation failed", app.validateUsername(secondPlayerUN, secondPlayerID));
    }

    public void testhandleMessage() {
    	App app =  new App(9002);
    	Gson gson = new Gson();
    	WebSocket webSocketMock = Mockito.mock(WebSocket.class);
    	app.getWords();
    	
        Player firstPlayer = new Player("xxaa");
        Player secondPlayer = new Player("xxbb");

        app.activeSessions.put("xxaa", firstPlayer);
        app.activeSessions.put("xxbb", secondPlayer);

        String firstPlayerUN = "aa";
        String firstPlayerID = "xxaa";

        String secondPlayerUN = "aa";
        String secondPlayerID = "xxbb";
        
        String jsonMessage = "{\"screen\":\"lobby\",\"type\":\"createGame\",\"uid\":\"xxaa\"}";

        assertTrue("Active games size before creation is not 0", app.activeGames.size() == 0);
        app.handleMessage(gson, jsonMessage, webSocketMock);
        assertTrue("Active games size after creation is not 1", app.activeGames.size() == 1);
    
        String gameId = app.activeGames.keySet().stream().findFirst().orElse(null);
        String jsonMessage2 = "{\"screen\":\"lobby\",\"type\":\"joinGame\",\"uid\":\"xxbb\", \"gameId\":\"" + gameId + "\"}";
        assertTrue("Players size before joining is not 1", app.activeGames.get(gameId).players.size() == 1);
        app.handleMessage(gson, jsonMessage2, webSocketMock);
        assertTrue("Players size after joining is not 2", app.activeGames.get(gameId).players.size() == 2);
    
        
        String jsonMessage3 = "{\"screen\":\"lobby\",\"type\":\"leaveGame\",\"uid\":\"xxbb\", \"gameId\":\"" + gameId + "\"}";
        assertTrue("Players size before leaving is not 2", app.activeGames.get(gameId).players.size() == 2);
        app.handleMessage(gson, jsonMessage3, webSocketMock);
        assertTrue("Players size after leaving is not 1", app.activeGames.get(gameId).players.size() == 1);
    
    }


}
