

package uta.cse3310;


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
