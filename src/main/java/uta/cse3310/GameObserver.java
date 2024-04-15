package uta.cse3310;

// Will allow every game object to directly access the WebSockets broadcast method, for when the game ends
public interface GameObserver {
    void notifyGameEnd(Game game);
}
