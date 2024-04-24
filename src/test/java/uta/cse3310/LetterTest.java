package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.ArrayList;

public class LetterTest extends TestCase {
    public LetterTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(LetterTest.class);
    }

    public void testConstructorWithLetter() {

        char letter = 'A';
        float x = 1.0f;
        float y = 2.0f;


        Letter testLetter = new Letter(letter, x, y);

    
        assertEquals(letter, testLetter.letter);
        assertEquals(x, testLetter.coordinate[0]);
        assertEquals(y, testLetter.coordinate[1]);
    }

    public void testConstructorWithoutLetter() {
        
        float x = 3.0f;
        float y = 4.0f;

        
        Letter testLetter = new Letter(x, y);

        
        assertEquals('\u0000', testLetter.letter); 
        assertEquals(x, testLetter.coordinate[0]);
        assertEquals(y, testLetter.coordinate[1]);
    }

    public void testGetCoordinates() {
        
        float x = 5.0f;
        float y = 6.0f;
        Letter testLetter = new Letter('B', x, y);

      
        float[] coordinates = testLetter.getCoordiates();

       
        assertEquals(x, coordinates[0]);
        assertEquals(y, coordinates[1]);
    }
}

