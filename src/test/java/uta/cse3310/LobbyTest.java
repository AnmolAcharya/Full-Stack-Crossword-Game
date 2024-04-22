package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit test for simple App.
 */
public class LobbyTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LobbyTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( LobbyTest.class );
    }
	
    Lobby testLobby = new Lobby();
    /**
     * Rigourous Test :-)
     */
    public void testUpdateLobby()
    {
    	Game a = new Game();
    	Game b = new Game();
    	Game c = new Game();
    	
    	Map<String, Game> games = new HashMap<>();
    	
    	games.put("aa", a);
    	games.put("bb", b);
    	games.put("cc", c);
    	
    	testLobby.updateLobby(games);
    	
    	assertTrue("Problem in adding joinable games" , testLobby.joinableGames.size() == 3);
    	
    	a.joinable = false;
    	
    	testLobby.updateLobby(games);
    	
    	assertFalse("Problem in removing non-joinable games" , testLobby.joinableGames.size() == 3);
    	
    }
    
   public void testAllTimeLeaderboard() {
    Player a = new Player("aa");
    Player b = new Player("bb");
    ArrayList<Player> players = new ArrayList<>();
    players.add(a);
    players.add(b);
    
    // Initial update
    testLobby.updateAllTimeLeaderboard(players);
    
    // Check if all-time scores are updated correctly
    assertEquals("Expected size of allTimeScores is 2", 2, testLobby.allTimeScores.size());
    
    // Update high scores
    b.highScore = 10;
    a.highScore = 5;
    
    // Update all-time leaderboard again
    testLobby.updateAllTimeLeaderboard(players);
    
    int highestScore = testLobby.allTimeLeaderboard.get(0).score;
    
    // Verify that the highest score is 10
    assertTrue("true", highestScore == 10);
}

		
}
