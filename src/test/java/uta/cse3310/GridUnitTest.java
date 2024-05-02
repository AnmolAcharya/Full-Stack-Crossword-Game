
package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class GridUnitTest extends TestCase {
    /*
     * Tests every non-trivial method in the Grid class.
     * Which is all of them. Except one. Awesome.
     */
    public GridUnitTest(String testName){
        super(testName);
    }

    public static Test suite(){
        return new TestSuite(GridUnitTest.class);
    }

    Grid g = new Grid();

    public void testCheckHorizontal(){
        Letter l = new Letter(5,0);
        Letter m = new Letter(10,0);
        assertTrue("You messed up!",g.checkHorizontal(l,m));
        l = new Letter(5,1);
        m = new Letter(10,0);
        assertFalse("You messed up!",g.checkHorizontal(l, m));
    }
    
    public void testCheckVertical(){
        Letter l = new Letter(0,5);
        Letter m = new Letter(0,10);
        assertTrue("You messed up!",g.checkVertical(l,m));
        l = new Letter(1,5);
        m = new Letter(0,10);
        assertFalse("You messed up!",g.checkVertical(l, m));
    }

    public void testCheckDiagonal(){
        Letter l = new Letter(0,0);
        Letter m = new Letter(5,5);
        assertTrue("You messed up!",g.checkDiagonal(l,m));
        l = new Letter(0,0);
        m = new Letter(5,6);
        assertFalse("You messed up!",g.checkDiagonal(l, m));
    }

    public void testCheckWord(){
        g.wordBank.put("hello",false);
        g.grid[1][0].letter = 'h';
        g.grid[2][0].letter = 'e';
        g.grid[3][0].letter = 'l';
        g.grid[4][0].letter = 'l';
        g.grid[5][0].letter = 'o';
        Letter a = new Letter(0,1);
        Letter b = new Letter(0,5);
        assertTrue("Should pass",g.checkWord(a,b,"vertical"));
        a = new Letter(0,2);
        b = new Letter(0,5);
        assertFalse("Should fail",g.checkWord(a,b,"vertical"));
        g.clearGrid();
        g.grid[0][1].letter = 'h';
        g.grid[0][2].letter = 'e';
        g.grid[0][3].letter = 'l';
        g.grid[0][4].letter = 'l';
        g.grid[0][5].letter = 'o';
        a = new Letter(1,0);
        b = new Letter(5,0);
        assertTrue("Should pass",g.checkWord(a,b,"horizontal"));
        a = new Letter(2,0);
        b = new Letter(5,0);
        assertFalse("Should fail",g.checkWord(a,b,"horizontal"));
        g.clearGrid();
        g.grid[0][0].letter = 'h';
        g.grid[1][1].letter = 'e';
        g.grid[2][2].letter = 'l';
        g.grid[3][3].letter = 'l';
        g.grid[4][4].letter = 'o';
        a = new Letter(0,0);
        b = new Letter(4,4);
        assertTrue("Should pass",g.checkWord(a,b,"diagonal"));
        a = new Letter(1,1);
        b = new Letter(4,5);
        assertFalse("Should fail",g.checkWord(a,b,"diagonal"));
        g.clearGrid();
    }

    public void testValidateSelection(){
        g.wordBank.put("hello",false);
        g.grid[1][0].letter = 'h';
        g.grid[2][0].letter = 'e';
        g.grid[3][0].letter = 'l';
        g.grid[4][0].letter = 'l';
        g.grid[5][0].letter = 'o';
        Letter a = new Letter(0,1);
        Letter b = new Letter(0,5);
        assertTrue("Should pass",g.validateSelection(a,b));
        a = new Letter(0,2);
        b = new Letter(0,5);
        assertFalse("Should fail",g.validateSelection(a,b));
        g.clearGrid();
        g.grid[0][1].letter = 'h';
        g.grid[0][2].letter = 'e';
        g.grid[0][3].letter = 'l';
        g.grid[0][4].letter = 'l';
        g.grid[0][5].letter = 'o';
        a = new Letter(1,0);
        b = new Letter(5,0);
        assertTrue("Should pass",g.validateSelection(a,b));
        a = new Letter(2,0);
        b = new Letter(5,0);
        assertFalse("Should fail",g.validateSelection(a,b));
        g.clearGrid();
        g.grid[0][0].letter = 'h';
        g.grid[1][1].letter = 'e';
        g.grid[2][2].letter = 'l';
        g.grid[3][3].letter = 'l';
        g.grid[4][4].letter = 'o';
        a = new Letter(0,0);
        b = new Letter(4,4);
        assertTrue("Should pass",g.validateSelection(a,b));
        a = new Letter(1,1);
        b = new Letter(4,5);
        assertFalse("Should fail",g.validateSelection(a,b));
        g.clearGrid();
    }

    public void testGenerateWordBank(){
        ArrayList<String> possibleWords = new ArrayList<String>();
        try{
            String str;
            BufferedReader reader = new BufferedReader(new FileReader("filtered_words.txt"));
            while((str = reader.readLine())!=null)
                possibleWords.add(str);
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        assertNotNull("Should fill the wordbank.",g.generateWordBank(possibleWords));
        System.out.println(g.wordBank);
    }

    public void testValidateGrid(){
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                if(j==5) continue;
                Letter l = new Letter('*',0,0);
                g.grid[i][j] = l;
            }
        }
        g.max = 17;
        assertTrue("Should pass",g.validateGrid(5,0,"hello".toCharArray(),"S"));
        assertFalse("Should fail",g.validateGrid(5,0,"hello".toCharArray(),"E"));
        assertFalse("Should fail",g.validateGrid(5,0,"hello".toCharArray(),"W"));
        assertFalse("Should fail",g.validateGrid(5,0,"hello".toCharArray(),"N"));

        assertTrue("Should pass",g.validateGrid(5,19,"hello".toCharArray(),"N"));
        assertFalse("Should fail",g.validateGrid(5,19,"hello".toCharArray(),"NE"));
        assertFalse("Should fail",g.validateGrid(5,19,"hello".toCharArray(),"SE"));
        assertFalse("Should fail",g.validateGrid(5,19,"hello".toCharArray(),"SW"));
        g.clearGrid();
        
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                if(i==5) continue;
                Letter l = new Letter('*',0,0);
                g.grid[i][j] = l;
            }
        }
        assertTrue("Should pass",g.validateGrid(0,5,"hello".toCharArray(),"E"));
        assertFalse("Should fail",g.validateGrid(0,5,"hello".toCharArray(),"S"));
        assertFalse("Should fail",g.validateGrid(0,5,"hello".toCharArray(),"W"));
        assertFalse("Should fail",g.validateGrid(0,5,"hello".toCharArray(),"N"));

        g.numHoriz = 0;

        assertTrue("Should pass",g.validateGrid(19,5,"hello".toCharArray(),"W"));
        assertFalse("Should fail",g.validateGrid(19,5,"hello".toCharArray(),"SE"));
        assertFalse("Should fail",g.validateGrid(19,5,"hello".toCharArray(),"NW"));
        assertFalse("Should fail",g.validateGrid(19,5,"hello".toCharArray(),"NE"));

        g.clearGrid();

        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                if(i-j==0) continue;
                Letter l = new Letter('*',0,0);
                g.grid[i][j] = l;
            }
        }
        
        assertTrue("Should pass",g.validateGrid(19,19,"hello".toCharArray(),"NW"));
        assertFalse("Should fail",g.validateGrid(19,19,"hello".toCharArray(),"SE"));
        assertFalse("Should fail",g.validateGrid(19,19,"hello".toCharArray(),"SW"));
        assertFalse("Should fail",g.validateGrid(19,19,"hello".toCharArray(),"NE"));

        g.numDiagR=0;

        assertTrue("Should pass",g.validateGrid(0,0,"hello".toCharArray(),"SE"));
        assertFalse("Should fail",g.validateGrid(0,0,"hello".toCharArray(),"SW"));
        assertFalse("Should fail",g.validateGrid(0,0,"hello".toCharArray(),"NW"));
        assertFalse("Should fail",g.validateGrid(0,0,"hello".toCharArray(),"NE"));

        g.numDiagR = 0;

        assertTrue("Should pass",g.validateGrid(10,10,"hello".toCharArray(),"SE"));
        assertFalse("Should fail",g.validateGrid(10,10,"hello".toCharArray(),"SW"));
        g.numDiagR=0;
        assertTrue("Should fail",g.validateGrid(10,10,"hello".toCharArray(),"NW"));
        assertFalse("Should fail",g.validateGrid(10,10,"hello".toCharArray(),"NE"));

        g.numDiagR=0;

        g.clearGrid();
        g.max = 0;
    }

    public void testAddWordToGrid(){
        assertTrue("Should pass.",g.addWordToGrid(10,10,"hello".toCharArray(),"SE"));
        assertTrue("Should pass.",g.addWordToGrid(9,1,"hello".toCharArray(),"S"));
        assertTrue("Should pass.",g.addWordToGrid(12,2,"hello".toCharArray(),"E"));
        assertTrue("Should pass.",g.addWordToGrid(10,14,"hello".toCharArray(),"SW"));
        assertTrue("Should pass.",g.addWordToGrid(2,6,"hello".toCharArray(),"NE"));
        assertTrue("Should pass.",g.addWordToGrid(19,19,"hello".toCharArray(),"NW"));
        assertTrue("Should pass.",g.addWordToGrid(16,5,"hello".toCharArray(),"W"));
        assertTrue("Should pass.",g.addWordToGrid(12,11,"hello".toCharArray(),"N"));

        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                System.out.print(g.grid[i][j].letter);
            }
            System.out.println();
        }

        g.clearGrid();
    }

    public void testFillGrid(){
        ArrayList<String> possibleWords = new ArrayList<String>();
        try{
            String str;
            BufferedReader reader = new BufferedReader(new FileReader("filtered_words.txt"));
            while((str = reader.readLine())!=null)
                possibleWords.add(str);
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        assertNotNull("Should fill word bank.",g.generateWordBank(possibleWords));
        assertNotNull("Should fill grid.",g.fillGrid(g.wordBank));
        
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                System.out.print(g.grid[i][j].letter);
            }
            System.out.println();
        }
    }
}

