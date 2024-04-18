 package uta.cse3310;

 import junit.framework.Test;
 import junit.framework.TestCase;
 import junit.framework.TestSuite;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.UUID;
 import java.util.Collections;
 import java.io.IOException;
 import java.io.BufferedReader;
 import java.io.FileReader;

 /**
  * Unit test for Game class.
  */
 public class GameUnitTest extends TestCase {
     public GameUnitTest(String testName){
        super(testName);
     }

     public static Test suite(){
        return new TestSuite(GameUnitTest.class);
     }

     // testing adding and removing a player from a game
     public void testAddandRemovePlayer(){
        Player p1 = new Player("1");
        ArrayList<String> words = new ArrayList<String>();
        try{
            String str;
            BufferedReader reader = new BufferedReader(new FileReader("filtered_words.txt"));
            while((str = reader.readLine())!=null)
                words.add(str);
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        Lobby lobby = new Lobby();
        App A = new App(20);
        Game test = new Game(p1, words, lobby, A);

        Player p2 = new Player("2");
        test.addPlayer(p2);
        assertTrue("Player Not Found",test.players.contains(p2));
        test.removePlayer(p2);
        assertFalse("Player Found",test.players.contains(p2));
    }

    // testing updating the leaderboard
    public void testUpdateLeaderboard(){
        Player p1 = new Player("1");
        ArrayList<String> words = new ArrayList<String>();
        try{
            String str;
            BufferedReader reader = new BufferedReader(new FileReader("filtered_words.txt"));
            while((str = reader.readLine())!=null)
                words.add(str);
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        Lobby lobby = new Lobby();
        App A = new App(20);
        Game test = new Game(p1, words, lobby, A);

        Player p2 = new Player("2");
        test.addPlayer(p2);
        test.updateLeaderboard(p1);
        test.updateLeaderboard(p2);
        test.updateLeaderboard(p2);
        assertTrue("Leaderborad in incorect order",test.leaderboard.get(0) == p2);
    }

    // testing checking to end the game
    public void testCheckEndGame(){
        Player p1 = new Player("1");
        ArrayList<String> words = new ArrayList<String>();
        try{
            String str;
            BufferedReader reader = new BufferedReader(new FileReader("filtered_words.txt"));
            while((str = reader.readLine())!=null)
                words.add(str);
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        Lobby lobby = new Lobby();
        App A = new App(20);
        Game test = new Game(p1, words, lobby, A);

        boolean allWordsFound = test.checkEndGame();
        assertFalse("Game ended incorrectly", allWordsFound);

        test.grid.wordBank.replaceAll((k,v)->v=true);    
        allWordsFound = test.checkEndGame();
        assertTrue("Game did not end correctly", allWordsFound);
    }
}