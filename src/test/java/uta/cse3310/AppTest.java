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

public class AppTest extends TestCase {

    public AppTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

   App app = new App(9002); 	

    public void testValidateUsername() {
    	
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
    }
    
    
    
    
    
}

