package uta.cse3310;
import java.lang.Math;
import java.util.*;

public class Grid{
	

    /*
     * Hi! Welcome to the Grid class. This is where all
     * of the functionality required to run the game lies.
     * Here's a little primer on how to use objects from this class!
     * 
     * validateSelection(Letter first, Letter last) is a method that
     * takes in 2 letters and returns whether or not the word between those
     * 2 letters is A) a valid selection and B) a word inside the word bank.
     * Returns a boolean true or false.
     * 
     * gimmeAHint() is a method that returns a random starting letter of a word
     * currently found in the grid. Use it to generate hints for users.
     * Returns a Letter.
     * 
     * generateWordBank(ArrayList<String> possibleWords) is a method that creates the word bank
     * for a given word grid. Send in a large list of possible words so that it can generate 
     * a word bank for you.
     * Returns a HashMap<String, Boolean> wordBank.
     * 
     * fillGrid(HashMap<String, Boolean> wordBank) is a method that generates a word search grid
     * for use in a game. Feed it a wordBank (typically its own word bank) and it will give you
     * a grid of specified density for use in a game.
     * Returns a Letter[20][20] grid
     * 
     * Some important non-method members of the Grid class:
     * wordBank: HashMap<String, Boolean>
     * grid: Letter[20][20]
     */
	public Letter[][] grid = new Letter[20][20];
	public HashMap<String, Boolean> wordBank;
	public double density = .7;
    public String[] directions = {"N","S","E","W","NW","SW","NE","SE"};
    public float currentDens = 0;
    public int wordCount = 0;
    public int numCross = 0;
    public float numDiagR = 0;
    public float numDiagL = 0;
    public float numVert = 0;
    public float numHoriz = 0;
    public ArrayList<Letter> hints;
	public Grid(){
        wordBank = new HashMap<String,Boolean>();
        hints = new ArrayList<Letter>();
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                Letter l = new Letter(' ',0,0);
                this.grid[i][j] = l;
            }
        }
	}

    public boolean clearGrid(){
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                Letter l =  new Letter(' ',0,0);
                this.grid[i][j] = l;
            }
        }
        return true;
    }

	//validates that a word is a correct selection
	//but it doesn't validate that the word itself is in the word bank	
	public boolean validateSelection(Letter first, Letter last){
		//check if it's a horizontal selection
		if(checkHorizontal(first,last)){
			return checkWord(first,last,"horizontal");
        }
		//check if it's a vertical selection
		else if(checkVertical(first,last)){
            return checkWord(first,last,"vertical");
        }
		//check if it's a diagonal selection
		else if(checkDiagonal(first, last)){
            return checkWord(first,last,"diagonal");
        }
		//if it's none of these, return that it's not a valid selection or word.
		return false;
	}
	
    public boolean checkWord(Letter first, Letter last, String direction){
        float d;
        int i;
        StringBuilder sb = new StringBuilder();
        String ws = new String();
        switch(direction){
            case "horizontal":
                if(first.coordinate[0] > last.coordinate[0]){
                    d = first.coordinate[0]-last.coordinate[0];
                    for(i = 0; i <= d; i++)
                        sb.append(this.grid[(int)first.coordinate[1]][(int)first.coordinate[0]-i].letter);
                    ws = sb.toString();
                    sb.delete(0,sb.length());
                    if(!this.wordBank.containsKey(ws)){
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[1]][(int)last.coordinate[0]+i].letter);
                        ws = sb.toString();
                        if(!this.wordBank.containsKey(ws))
                            return false;
                    }
                    else{
                        if(!hints.remove(first)) hints.remove(last);
                        this.wordBank.put(ws,true);
                        return true;
                    }
                }
                else if(last.coordinate[0] > first.coordinate[0]){
                    d = last.coordinate[0] - first.coordinate[0];
                    for(i = 0; i <= d; i++){
                        sb.append(this.grid[(int)first.coordinate[1]][(int)first.coordinate[0]+i].letter);
                    }
                    ws = sb.toString();
                    sb.delete(0,sb.length());
                    if(!this.wordBank.containsKey(ws)){
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[1]][(int)last.coordinate[0]-i].letter);
                        ws = sb.toString();
                        if(!this.wordBank.containsKey(ws))
                            return false;
                    }
                    else{
                        if(!hints.remove(first)) hints.remove(last);
                        this.wordBank.put(ws,true);
                        return true;
                    }
                }
            break;
            case "vertical":
                if(first.coordinate[1] > last.coordinate[1]){
                    d = first.coordinate[1]-last.coordinate[1];
                    for(i = 0; i <= d; i++){
                        sb.append(this.grid[(int)first.coordinate[1]-i][(int)first.coordinate[0]].letter);
                    }
                    ws = sb.toString();
                    sb.delete(0,sb.length());
                    if(!this.wordBank.containsKey(ws)){
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[1]+i][(int)last.coordinate[0]].letter);
                        ws = sb.toString();
                        if(!this.wordBank.containsKey(ws))
                            return false;
                    }
                    else{
                        if(!hints.remove(first)) hints.remove(last);
                        this.wordBank.put(ws,true);
                        return true;
                    }
                }
                else if(last.coordinate[1] > first.coordinate[1]){
                    d = last.coordinate[1] - first.coordinate[1];
                    for(i = 0; i <= d; i++)
                        sb.append(this.grid[(int)first.coordinate[1]+i][(int)first.coordinate[0]].letter);
                    ws = sb.toString();
                    sb.delete(0,sb.length());
                    if(!this.wordBank.containsKey(ws)){
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[1]-i][(int)last.coordinate[0]].letter);
                        ws = sb.toString();
                        if(!this.wordBank.containsKey(ws))
                            return false;
                    }
                    else{
                        if(!hints.remove(first)) hints.remove(last);
                        this.wordBank.put(ws,true);
                        return true;
                    }
                }
            break;
            case "diagonal":
                if(first.coordinate[1]>last.coordinate[1]){
                    if(first.coordinate[0]>last.coordinate[0]){
                        for(i = 0; i <=((int)first.coordinate[1]-(int)last.coordinate[1]); i++){
                            sb.append(this.grid[(int)first.coordinate[1]-i][(int)first.coordinate[0]-i].letter);
                        }
                        ws = sb.toString();
                        sb.delete(0,sb.length());
                        if(!this.wordBank.containsKey(ws)){
                            for(i = 0; i <= ((int)first.coordinate[1]-(int)last.coordinate[1]); i++)
                                sb.append(this.grid[(int)last.coordinate[1]+i][(int)last.coordinate[0]+i].letter);
                            ws = sb.toString();
                            if(!this.wordBank.containsKey(ws)) 
                                return false;
                        }
                    }
                    else if(first.coordinate[0]<last.coordinate[0]){
                        for(i = 0; i <=((int)first.coordinate[1]-(int)last.coordinate[1]); i++){
                            sb.append(this.grid[(int)first.coordinate[1]-i][(int)first.coordinate[0]+i].letter);
                        }
                        ws = sb.toString();
                        sb.delete(0,sb.length());
                        if(!this.wordBank.containsKey(ws)){
                            for(i = 0; i <= ((int)first.coordinate[1]-(int)last.coordinate[1]); i++)
                                sb.append(this.grid[(int)last.coordinate[1]+i][(int)last.coordinate[0]-i].letter);
                            ws = sb.toString();
                            if(!this.wordBank.containsKey(ws)) 
                                return false;
                        }
                    }
                }
                else if(last.coordinate[1]>first.coordinate[1]){
                    if(first.coordinate[0]>last.coordinate[0]){
                        for(i = 0; i <=((int)last.coordinate[1]-(int)first.coordinate[1]); i++){
                            sb.append(this.grid[(int)last.coordinate[1]-i][(int)last.coordinate[0]+i].letter);
                        }
                        ws = sb.toString();
                        sb.delete(0,sb.length());
                        if(!this.wordBank.containsKey(ws)){
                            for(i = 0; i <= ((int)last.coordinate[1]-(int)first.coordinate[1]); i++)
                                sb.append(this.grid[(int)first.coordinate[1]+i][(int)first.coordinate[0]-i].letter);
                            ws = sb.toString();
                            if(!this.wordBank.containsKey(ws)) 
                                return false;
                        }
                    }
                    else if(first.coordinate[0]<last.coordinate[0]){
                        for(i = 0; i <=((int)last.coordinate[1]-(int)first.coordinate[1]); i++){
                            sb.append(this.grid[(int)last.coordinate[1]-i][(int)last.coordinate[0]-i].letter);
                        }
                        ws = sb.toString();
                        sb.delete(0,sb.length());
                        if(!this.wordBank.containsKey(ws)){
                            for(i = 0; i <= ((int)last.coordinate[1]-(int)first.coordinate[1]); i++)
                                sb.append(this.grid[(int)first.coordinate[1]+i][(int)first.coordinate[0]+i].letter);
                            ws = sb.toString();
                            if(!this.wordBank.containsKey(ws)) 
                                return false;
                        }
                    }
                }
            break;
        }
        if(!hints.remove(first)) hints.remove(last);
        this.wordBank.put(ws,true);
        return true;
    }

	public boolean checkHorizontal(Letter first, Letter last){
		//if the x values are not the same, but the y values are the same
		//it is a horizontal pick, return true
		if(((first.coordinate[0]-last.coordinate[0])!=0)&&((first.coordinate[1]-last.coordinate[1])==0))
			return true;
		return false;
	}
	
	public boolean checkVertical(Letter first, Letter last){
		//if the y values are not the same, but the x values are the same
		//it is a vertical pick, return true
		if(((first.coordinate[0]-last.coordinate[0])==0)&&((first.coordinate[1]-last.coordinate[1])!=0))
			return true;
		return false;
	}
	
	public boolean checkDiagonal(Letter first, Letter last){
		//check if denom is 0, just in case, because if it is we will crash
		//with a div by 0 error.
		if(first.coordinate[0]-last.coordinate[0] == 0)
			return false;
		//check if the slope of the line created by the 2 points = 1
		//if so, return true, it is diagonal. I use absolute value so that the order of the 
		//coordinates doesnt matter.
		if(Math.abs(((last.coordinate[1]-first.coordinate[1]))/((last.coordinate[0]-first.coordinate[0])))==1)
			return true;
		return false;
	}

    public Letter gimmeAHint(){
        Letter a = this.hints.get(new Random().nextInt(hints.size()));
        return a;
    }
	
	public HashMap<String,Boolean> generateWordBank(ArrayList<String> possibleWords){
		//variables for creation
		int j = 0;
		//rng
		Random r = new Random();
		//while we're below capacity
		while(currentDens < density){
			//make a random integer in the range of the size of the word file
			j = r.nextInt((possibleWords.size()));
			//add the string located at the index of j
			this.wordBank.put(possibleWords.get(j),false);
            this.wordCount++;
			//add load to capacity
			this.currentDens += ((double)possibleWords.get(j).length()/400.0);
			//remove word
            possibleWords.remove(j);
		}
		//return our shiny new word bank
		return this.wordBank;
	}
	
    public boolean validateGrid(int p, int q, char[] word, String direction){
        boolean crossedWord = false;
        switch(direction){
            case "N":
                for(int i = 0; i < word.length; i++){
                    if(q<0)
                        return false;
                    if((this.grid[q][p].letter!=' ' && this.grid[q][p].letter != word[i])) return false;
                    if(this.grid[q][p].letter == word[i]) crossedWord = true;
                    q--;
                }
                this.numVert++;
                break;
            case "S":
                for(int i = 0; i< word.length; i++){
                    if(q>19)
                        return false;
                        if((this.grid[q][p].letter!=' ' && this.grid[q][p].letter != word[i])) return false;
                        if(this.grid[q][p].letter == word[i]) crossedWord = true;
                    q++;
                }
                this.numVert++;
                break;
            case "E":
                for(int i = 0; i < word.length; i++){
                    if(p>19)
                        return false;
                    if((this.grid[q][p].letter!=' ' && this.grid[q][p].letter != word[i])) return false;
                    if(this.grid[q][p].letter == word[i]) crossedWord = true;
                    p++;
                }
                this.numHoriz++;
                break;
            case "W":
                for(int i = 0; i < word.length; i++){
                    if(p<0)
                        return false;
                    if((this.grid[q][p].letter!=' ' && this.grid[q][p].letter != word[i])) return false;
                    if(this.grid[q][p].letter == word[i]) crossedWord = true;
                    p--;
                }
                this.numHoriz++;
                break;
            case "NW":
                for(int i = 0; i < word.length; i++){
                    if(p<0||q<0)
                        return false;
                    if((this.grid[q][p].letter!=' '&& this.grid[q][p].letter != word[i])) return false;
                    if(this.grid[q][p].letter == word[i]) crossedWord = true;
                    p--;
                    q--;
                }
                this.numDiagR++;
                break;
            case "SW":
                for(int i = 0; i < word.length; i++){
                    if(p<0||q>19)
                        return false;
                    if((this.grid[q][p].letter!=' ' && this.grid[q][p].letter != word[i])) return false;
                    if(this.grid[q][p].letter == word[i]) crossedWord = true;
                    p--;
                    q++;
                }
                this.numDiagL++;
                break;
            case "NE":
                for(int i = 0; i < word.length; i++){
                    if(p>19||q<0)
                        return false;
                    if((this.grid[q][p].letter!=' ' && this.grid[q][p].letter != word[i])) return false;
                    if(this.grid[q][p].letter == word[i]) crossedWord = true;
                    p++;
                    q--;
                }
                this.numDiagL++;
                break;
            case "SE":
                for(int i = 0; i < word.length; i++){
                    if(p>19||q>19)
                        return false;
                    if((this.grid[q][p].letter!=' ' && this.grid[q][p].letter != word[i])) return false;
                    if(this.grid[q][p].letter == word[i]) crossedWord = true;
                    p++;
                    q++;
                }
                this.numDiagR++;
                break;
        }
        if(crossedWord) this.numCross++;
        return true;
    }

    public boolean addWordToGrid(int p, int q, char[] word, String direction){
        switch(direction){
            case "N":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    if (i == 0) this.hints.add(l);
                    this.grid[q][p] = l;
                    q--;
                }
                break;
            case "S":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    if (i == 0) this.hints.add(l);
                    this.grid[q][p] = l;
                    q++;
                }
                break;
            case "E":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    if (i == 0) this.hints.add(l);
                    this.grid[q][p] = l;
                    p++;
                }
                break;
            case "W":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    if (i == 0) this.hints.add(l);
                    this.grid[q][p] = l;
                    p--;
                }
                break;
            case "NW":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    if (i == 0) this.hints.add(l);
                    this.grid[q][p] = l;
                    q--;
                    p--;
                }
                break;
            case "SW":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    if (i == 0) this.hints.add(l);
                    this.grid[q][p] = l;
                    q++;
                    p--;
                }
                break;
            case "NE":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    if (i == 0) this.hints.add(l);
                    this.grid[q][p] = l;
                    q--;
                    p++;
                }
                break;
            case "SE":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    if (i == 0) this.hints.add(l);
                    this.grid[q][p] = l;
                    q++;
                    p++;
                }
                break;
        }
        return true;
    }

	public Letter[][] fillGrid(HashMap<String, Boolean> wordBank){
        if(wordBank.isEmpty()) return null;
        Random p = new Random();
        Random q = new Random();
        boolean hmm = false;
        boolean hit = false;
        String[] failures = new String[10];
        int failindex = 0;
        for(Map.Entry<String, Boolean> i:wordBank.entrySet()){
            char[] ichar = i.getKey().toCharArray();
            hit = false;
            int failcounter = 0;
            while(!hit){
                if(failcounter == 100){
                    failures[failindex] = i.getKey();
                    failindex++;
                    this.currentDens-=((double)ichar.length/400.0);
                    this.wordCount--;
                    //100 fails, word must be too big for remaining area.
                    break;
                }
                int r = p.nextInt(8);
                String startd = directions[r];
                String d = startd;
                int a = p.nextInt(20);
                int b = q.nextInt(20);
                if(this.grid[a][b].letter != ' ') continue;
                if((hmm = validateGrid(a, b, ichar, d)) == false){
                    r = (r+1)%8;
                    d = directions[r];
                    while(((hmm = validateGrid(a, b, ichar, d))==false)&&(!d.equals(startd))){
                        r = (r+1)%8;
                        d = directions[r];
                    }
                }
                if(!hmm){
                    failcounter++;
                    continue;
                }
                else if(hmm){
                    addWordToGrid(a, b, ichar, d);
                    hit = true;
                }
            }
        }
        for(int j = 0; j < 20; j++){
            for(int k = 0; k < 20; k++){
                if(this.grid[j][k].letter == ' '){
                    Letter l = new Letter((char)(p.nextInt(26)+97),k,j);
                    this.grid[j][k] = l;
                }
            }
        }
        for(int o = 0; o < failindex; o++){
            this.wordBank.remove(failures[o]);
        }
        this.numDiagL = (this.numDiagL/wordCount)*(100);
        this.numDiagR = (this.numDiagR/wordCount)*(100);
        this.numHoriz = (this.numHoriz/wordCount)*(100);
        this.numVert = (this.numVert/wordCount)*(100);
        return this.grid;
	}
}