package uta.cse3310;
import java.util.ArrayList;

public class Letter {
    public char letter;
    //coordinate [0] will be x, [1] will be y
    public float[] coordinate = new float[2];
    // track who has the letter selected
    public ArrayList<Player> selections = new ArrayList<Player>();

    public Letter(char l,float coorda, float coordb) {
        this.letter = l;
        this.coordinate[0] = coorda;
        this.coordinate[1] = coordb;
    }

    public Letter(float coorda, float coordb){
        this.coordinate[0] = coorda;
        this.coordinate[1] = coordb;
    }

    public float[] getCoordiates() {
        return this.coordinate;
    }
}

