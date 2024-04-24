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
public class PlayerTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PlayerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PlayerTest.class );
    }
	
  	
	public void testUpdateHighScore(){
	
		Player player = new Player("aa");
		
		player.updateScore();
		player.updateScore();
		
		assertEquals(2, player.currentScore);
		
		player.updateScore();
		player.updateHighScore();
		
		assertEquals(3, player.highScore);
		
		
	
	}
  
   
		
}
