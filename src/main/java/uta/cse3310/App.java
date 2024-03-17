

package uta.cse3310;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class App extends WebSocketServer {
 

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

    
    

  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
 
    
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    

  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
   
  }

  @Override
  public void onStart() {
   
  }

  public static void main(String[] args) {


  }
}
