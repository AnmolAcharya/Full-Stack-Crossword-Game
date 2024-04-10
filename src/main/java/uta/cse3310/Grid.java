package uta.cse3310;
import java.lang.Math;
import java.util.*;
import java.util.Random;

public class Grid{
	
	public Letter[][] grid = new Letter[25][25];
	public ArrayList<String> wordBank;
	public double density = .7;
    public String[] directions = {"N","S","E","W","NW","SW","NE","SE"};
	public Grid(){
        wordBank = new ArrayList<String>();
        for(int i = 0; i < 25; i++){
            for(int j = 0; j < 25; j++){
                Letter l = new Letter(' ',0,0);
                this.grid[i][j] = l;
            }
        }
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
        int j;
        StringBuilder sb = new StringBuilder();
        switch(direction){
            case "horizontal":
                if(first.coordinate[0] > last.coordinate[0]){
                    d = first.coordinate[0]-last.coordinate[0];
                    for(i = 0; i <= d; i++)
                        sb.append(this.grid[(int)first.coordinate[0]-i][(int)first.coordinate[1]].letter);
                    String ws = sb.toString();
                    sb.delete(0,sb.length());
                    if(!this.wordBank.contains(ws)){
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[0]+i][(int)last.coordinate[1]].letter);
                        ws = sb.toString();
                        if(!this.wordBank.contains(ws))
                            return false;
                    }
                    else
                        return true;
                }
                else if(last.coordinate[0] > first.coordinate[0]){
                    d = last.coordinate[0] - first.coordinate[0];
                    for(i = 0; i <= d; i++)
                        sb.append(this.grid[(int)first.coordinate[0]+i][(int)first.coordinate[1]].letter);
                    String ws = sb.toString();
                    sb.delete(0,sb.length());
                    if(!this.wordBank.contains(ws)){
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[0]-i][(int)last.coordinate[1]].letter);
                        ws = sb.toString();
                        if(!this.wordBank.contains(ws))
                            return false;
                    }
                    else 
                        return true;
                }
            break;
            case "vertical":
                if(first.coordinate[1] > last.coordinate[1]){
                    d = first.coordinate[1]-last.coordinate[1];
                    for(i = 0; i <= d; i++){
                        sb.append(this.grid[(int)first.coordinate[0]][(int)first.coordinate[1]-i].letter);
                    }
                    String ws = sb.toString();
                    sb.delete(0,sb.length());
                    if(!this.wordBank.contains(ws)){
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[0]][(int)last.coordinate[1]+i].letter);
                        ws = sb.toString();
                        if(!this.wordBank.contains(ws))
                            return false;
                    }
                    else
                        return true;
                }
                else if(last.coordinate[1] > first.coordinate[1]){
                    d = last.coordinate[1] - first.coordinate[1];
                    for(i = 0; i <= d; i++)
                        sb.append(this.grid[(int)first.coordinate[0]][(int)first.coordinate[1]+i].letter);
                    String ws = sb.toString();
                    sb.delete(0,sb.length());
                    if(!this.wordBank.contains(ws)){
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[0]][(int)last.coordinate[1]-i].letter);
                        ws = sb.toString();
                        if(!this.wordBank.contains(ws))
                            return false;
                    }
                    else 
                        return true;
                }
            break;
            case "diagonal":
                if(first.coordinate[1]>last.coordinate[1]){
                    if(first.coordinate[0]>last.coordinate[0]){
                        for(i = 0; i <=((int)first.coordinate[1]-(int)last.coordinate[1]); i++){
                            sb.append(this.grid[(int)first.coordinate[0]-i][(int)first.coordinate[1]-i].letter);
                        }
                        String ws = sb.toString();
                        sb.delete(0,sb.length());
                        if(!this.wordBank.contains(ws)){
                            for(i = 0; i <= ((int)first.coordinate[1]-(int)last.coordinate[1]); i++)
                                sb.append(this.grid[(int)last.coordinate[0]+i][(int)last.coordinate[1]+i].letter);
                            ws = sb.toString();
                            if(!this.wordBank.contains(ws)) 
                                return false;
                        }
                    }
                    else if(first.coordinate[0]<last.coordinate[0]){
                        for(i = 0; i <=((int)first.coordinate[1]-(int)last.coordinate[1]); i++){
                            sb.append(this.grid[(int)first.coordinate[0]+i][(int)first.coordinate[1]-i].letter);
                        }
                        String ws = sb.toString();
                        sb.delete(0,sb.length());
                        if(!this.wordBank.contains(ws)){
                            for(i = 0; i <= ((int)first.coordinate[1]-(int)last.coordinate[1]); i++)
                                sb.append(this.grid[(int)last.coordinate[0]-i][(int)last.coordinate[1]+i].letter);
                            ws = sb.toString();
                            if(!this.wordBank.contains(ws)) 
                                return false;
                        }
                    }
                }
                else if(last.coordinate[1]>first.coordinate[1]){
                    if(first.coordinate[0]>last.coordinate[0]){
                        for(i = 0; i <=((int)last.coordinate[1]-(int)first.coordinate[1]); i++){
                            sb.append(this.grid[(int)last.coordinate[0]+i][(int)last.coordinate[1]-i].letter);
                        }
                        String ws = sb.toString();
                        sb.delete(0,sb.length());
                        if(!this.wordBank.contains(ws)){
                            for(i = 0; i <= ((int)last.coordinate[1]-(int)first.coordinate[1]); i++)
                                sb.append(this.grid[(int)first.coordinate[0]-i][(int)first.coordinate[1]+i].letter);
                            ws = sb.toString();
                            if(!this.wordBank.contains(ws)) 
                                return false;
                        }
                    }
                    else if(first.coordinate[0]<last.coordinate[0]){
                        for(i = 0; i <=((int)last.coordinate[1]-(int)first.coordinate[1]); i++){
                            sb.append(this.grid[(int)last.coordinate[0]-i][(int)last.coordinate[1]-i].letter);
                        }
                        String ws = sb.toString();
                        System.out.println(ws);
                        sb.delete(0,sb.length());
                        if(!this.wordBank.contains(ws)){
                            for(i = 0; i <= ((int)last.coordinate[1]-(int)first.coordinate[1]); i++)
                                sb.append(this.grid[(int)first.coordinate[0]+i][(int)first.coordinate[1]+i].letter);
                            ws = sb.toString();
                            System.out.println(ws);
                            if(!this.wordBank.contains(ws)) 
                                return false;
                        }
                    }
                }
            break;
        }
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
	
	public ArrayList<String> generateWordBank(ArrayList<String> possibleWords){
		//variables for creation
		int i = 0;
		int j = 0;
		float currentDens = 0;
		//rng
		Random r = new Random();
		//while we're below capacity
		while(currentDens < density){
			//make a random integer in the range of the size of the word file
			j = r.nextInt((possibleWords.size()));
			//add the string located at the index of j
			this.wordBank.add(possibleWords.get(j));
			//remove that word from the possible list of words
			possibleWords.remove(j);
			//add load to capacity
			currentDens += ((double)wordBank.get(i).length()/625.0);
			//increment i to help add next word
            i++;
		}
		//return our shiny new word bank
		return this.wordBank;
	}
	
    public boolean validateGrid(int p, int q, char[] word, String direction){
        switch(direction){
            case "N":
                for(int i = 0; i < word.length; i++){
                    if(q<0)
                        return false;
                    if((this.grid[p][q].letter!=' ')&&(!(this.grid[p][q].letter == word[i])))
                        return false;
                    q--;
                }
                break;
            case "S":
                for(int i = 0; i< word.length; i++){
                    if(q>24)
                        return false;
                    if((this.grid[p][q].letter!=' ')&&(!(this.grid[p][q].letter == word[i])))
                        return false;
                    q++;
                }
                break;
            case "E":
                for(int i = 0; i < word.length; i++){
                    if(p>24)
                        return false;
                    if((this.grid[p][q].letter!=' ')&&(!(this.grid[p][q].letter == word[i])))
                        return false;
                    p++;
                }
                break;
            case "W":
                for(int i = 0; i < word.length; i++){
                    if(p<0)
                        return false;
                    if((this.grid[p][q].letter!=' ')&&(!(this.grid[p][q].letter == word[i])))
                        return false;
                    p--;
                }
                break;
            case "NW":
                for(int i = 0; i < word.length; i++){
                    if(p<0||q<0)
                        return false;
                    if((this.grid[p][q].letter!=' ')&&(!(this.grid[p][q].letter == word[i])))
                        return false;
                    p--;
                    q--;
                }
                break;
            case "SW":
                for(int i = 0; i < word.length; i++){
                    if(p<0||q>24)
                        return false;
                    if((this.grid[p][q].letter!=' ')&&(!(this.grid[p][q].letter == word[i])))
                        return false;
                    p--;
                    q++;
                }
                break;
            case "NE":
                for(int i = 0; i < word.length; i++){
                    if(p>24||q<0)
                        return false;
                    if((this.grid[p][q].letter!=' ')&&(!(this.grid[p][q].letter == word[i])))
                        return false;
                    p++;
                    q--;
                }
                break;
            case "SE":
                for(int i = 0; i < word.length; i++){
                    if(p>24||q>24)
                        return false;
                    if((this.grid[p][q].letter!=' ')&&(!(this.grid[p][q].letter == word[i])))
                        return false;
                    p++;
                    q++;
                }
                break;
        }
        return true;
    }

    public boolean addWordToGrid(int p, int q, char[] word, String direction){
        switch(direction){
            case "N":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    this.grid[p][q] = l;
                    q--;
                }
                break;
            case "S":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    this.grid[p][q] = l;
                    q++;
                }
                break;
            case "E":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    this.grid[p][q] = l;
                    p++;
                }
                break;
            case "W":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    this.grid[p][q] = l;
                    p--;
                }
                break;
            case "NW":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    this.grid[p][q] = l;
                    q--;
                    p--;
                }
                break;
            case "SW":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    this.grid[p][q] = l;
                    q++;
                    p--;
                }
                break;
            case "NE":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    this.grid[p][q] = l;
                    q--;
                    p++;
                }
                break;
            case "SE":
                for(int i = 0; i < word.length; i++){
                    Letter l = new Letter(word[i],p,q);
                    this.grid[p][q] = l;
                    q++;
                    p++;
                }
                break;
        }
        return true;
    }

	public Letter[][] fillGrid(ArrayList<String> wordBank){
        if(wordBank.isEmpty()) return null;
        Random p = new Random();
        Random q = new Random();
        boolean hmm = false;
        boolean hit = false;
        String[] failures = new String[10];
        int failindex = 0;
        for(String i:wordBank){
            char[] ichar = i.toCharArray();
            hit = false;
            int failcounter = 0;
            while(!hit){
                if(failcounter == 100){
                    failures[failindex] = i;
                    failindex++;
                    //100 fails, word must be too big for remaining area.
                    break;
                }
                int r = p.nextInt(8);
                String startd = directions[r];
                String d = startd;
                int a = p.nextInt(25);
                int b = q.nextInt(25);
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
        for(int j = 0; j < 25; j++){
            for(int k = 0; k < 25; k++){
                if(this.grid[j][k].letter == ' '){
                    Letter l = new Letter((char)(p.nextInt(26)+97),j,k);
                    this.grid[j][k] = l;
                }
            }
        }
        for(int o = 0; o < failindex; o++){
            this.wordBank.remove(failures[o]);
        }
		return this.grid;
	}
}