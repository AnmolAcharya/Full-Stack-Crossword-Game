package uta.cse3310;
import java.lang.Math;
import java.util.*;

public class Grid{
	
	public Letter[][] grid = new Letter[25][25];
	public ArrayList<String> wordBank;
	public double density = .7;
    public String[] directions = {"N","S","E","W","NW","SW","NE","SE"};
	public Grid(){
		//constructor gives the wordbank memory
        wordBank = new ArrayList<String>();
		//constructor initializes an empty grid
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
	
	//checks if word is valid in the word bank
    public boolean checkWord(Letter first, Letter last, String direction){
        //variables for computation
		float d;
        int i;
        StringBuilder sb = new StringBuilder();
        switch(direction){
			//runs if horizontal selection
            case "horizontal":
				//if first letter selected is rightmost
                if(first.coordinate[0] > last.coordinate[0]){
					//find distance
                    d = first.coordinate[0]-last.coordinate[0];
                    //stringbuilder builds a string
					for(i = 0; i <= d; i++)
                        sb.append(this.grid[(int)first.coordinate[0]-i][(int)first.coordinate[1]].letter);
                    //make string and reset the stringbuilder
					String ws = sb.toString();
                    sb.delete(0,sb.length());
					//if does not contain
                    if(!this.wordBank.contains(ws)){
						//check for backward selection by user
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[0]+i][(int)last.coordinate[1]].letter);
                        ws = sb.toString();
						//if still does not contain, return false
                        if(!this.wordBank.contains(ws))
                            return false;
                    }
                    else
                        return true;
                }
				//if last selected letter rightmost
                else if(last.coordinate[0] > first.coordinate[0]){
					//find distance
                    d = last.coordinate[0] - first.coordinate[0];
                    //check for valid word
					for(i = 0; i <= d; i++)
                        sb.append(this.grid[(int)first.coordinate[0]+i][(int)first.coordinate[1]].letter);
                    //build string and reset builder
					String ws = sb.toString();
                    sb.delete(0,sb.length());
					//if wordbank does not contain
                    if(!this.wordBank.contains(ws)){
						//check for backwards selection by user
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[0]-i][(int)last.coordinate[1]].letter);
                        ws = sb.toString();
						//if still does not contain, return false
                        if(!this.wordBank.contains(ws))
                            return false;
                    }
					//return true if passed
                    else 
                        return true;
                }
            break;
			
			//check selection designated vertical
            case "vertical":
				//if first lower than last
                if(first.coordinate[1] > last.coordinate[1]){
					//find distance
                    d = first.coordinate[1]-last.coordinate[1];
                    //check if word valid
					for(i = 0; i <= d; i++){
                        sb.append(this.grid[(int)first.coordinate[0]][(int)first.coordinate[1]-i].letter);
                    }
					//build string and reset builder
                    String ws = sb.toString();
                    sb.delete(0,sb.length());
					//if wordbank does not contain
                    if(!this.wordBank.contains(ws)){
						//check for backwards selection by user
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[0]][(int)last.coordinate[1]+i].letter);
                        ws = sb.toString();
						//if still does not contain, return false
                        if(!this.wordBank.contains(ws))
                            return false;
                    }
                    else
						//return true if passed
                        return true;
                }

				//if last is lower than first
                else if(last.coordinate[1] > first.coordinate[1]){
					//find distance
                    d = last.coordinate[1] - first.coordinate[1];
                    //check word
					for(i = 0; i <= d; i++)
                        sb.append(this.grid[(int)first.coordinate[0]][(int)first.coordinate[1]+i].letter);
                    //build string and reset builder
					String ws = sb.toString();
                    sb.delete(0,sb.length());
					//if wordbank does not contain
                    if(!this.wordBank.contains(ws)){
						//check for backwards selection by user
                        for(i = 0; i <= d; i++)
                            sb.append(this.grid[(int)last.coordinate[0]][(int)last.coordinate[1]-i].letter);
                        ws = sb.toString();
						//if still does not contain, return false
                        if(!this.wordBank.contains(ws))
                            return false;
                    }
					//return true if passed
                    else 
                        return true;
                }
            break;

			//diagonal check. 8 different possible cases, forwards and backwards checks
            case "diagonal":
				//if first lower than last
                if(first.coordinate[1]>last.coordinate[1]){
					//and first rightmost
                    if(first.coordinate[0]>last.coordinate[0]){
						//check word
                        for(i = 0; i <=((int)first.coordinate[1]-(int)last.coordinate[1]); i++){
                            sb.append(this.grid[(int)first.coordinate[0]-i][(int)first.coordinate[1]-i].letter);
                        }
                        String ws = sb.toString();
                        sb.delete(0,sb.length());
						//if does not contain, check it backwards
                        if(!this.wordBank.contains(ws)){
                            for(i = 0; i <= ((int)first.coordinate[1]-(int)last.coordinate[1]); i++)
                                sb.append(this.grid[(int)last.coordinate[0]+i][(int)last.coordinate[1]+i].letter);
                            ws = sb.toString();
                            if(!this.wordBank.contains(ws)) 
                                return false;
                        }
                    }
					//else if first is leftmost
                    else if(first.coordinate[0]<last.coordinate[0]){
						//check word
                        for(i = 0; i <=((int)first.coordinate[1]-(int)last.coordinate[1]); i++){
                            sb.append(this.grid[(int)first.coordinate[0]+i][(int)first.coordinate[1]-i].letter);
                        }
                        String ws = sb.toString();
                        sb.delete(0,sb.length());
						//if does not contain, check backwards
                        if(!this.wordBank.contains(ws)){
                            for(i = 0; i <= ((int)first.coordinate[1]-(int)last.coordinate[1]); i++)
                                sb.append(this.grid[(int)last.coordinate[0]-i][(int)last.coordinate[1]+i].letter);
                            ws = sb.toString();
                            if(!this.wordBank.contains(ws)) 
                                return false;
                        }
                    }
                }
				//else if last is lower than first
                else if(last.coordinate[1]>first.coordinate[1]){
					//and last is leftmost
                    if(first.coordinate[0]>last.coordinate[0]){
						//check word
                        for(i = 0; i <=((int)last.coordinate[1]-(int)first.coordinate[1]); i++){
                            sb.append(this.grid[(int)last.coordinate[0]+i][(int)last.coordinate[1]-i].letter);
                        }
                        String ws = sb.toString();
                        sb.delete(0,sb.length());
						//if does not contain, check backwards
                        if(!this.wordBank.contains(ws)){
                            for(i = 0; i <= ((int)last.coordinate[1]-(int)first.coordinate[1]); i++)
                                sb.append(this.grid[(int)first.coordinate[0]-i][(int)first.coordinate[1]+i].letter);
                            ws = sb.toString();
                            if(!this.wordBank.contains(ws)) 
                                return false;
                        }
                    }
					//else if last is rightmost
                    else if(first.coordinate[0]<last.coordinate[0]){
						//check word
                        for(i = 0; i <=((int)last.coordinate[1]-(int)first.coordinate[1]); i++){
                            sb.append(this.grid[(int)last.coordinate[0]-i][(int)last.coordinate[1]-i].letter);
                        }
                        String ws = sb.toString();
                        sb.delete(0,sb.length());
						//if does not contain, check backwards
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
		//if passed, return true. a truly exhaustive check.
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
			j = r.nextInt(0,(possibleWords.size()-1));
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
	
	//validates the grid for a certain word at a certain point in a certain direction.
    public boolean validateGrid(int p, int q, char[] word, String direction){
		//switch on direction
        switch(direction){
            case "N":
				//check if the word can possibly be placed at this point on the grid
				//i do this BEFORE adding the word because adding letters to the grid
				//without checking first is careless.
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

	//adds a word to the grid after it's been validated to fit at that spot.
    public boolean addWordToGrid(int p, int q, char[] word, String direction){
		//switch on the direction
        switch(direction){
			//add the word to the grid in the direction prescribed.
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

	//fill the grid.
	public Letter[][] fillGrid(ArrayList<String> wordBank){
		//if no wordbank, return null.
        if(wordBank.isEmpty()) return null;
		//variables for computation
        Random p = new Random();
        Random q = new Random();
        boolean hmm = false;
        boolean hit = false;
        String[] failures = new String[10];
        int failindex = 0;
		//for every word in the wordbank
        for(String i:wordBank){
			//turn it to a char so we can operate on it more easily.
            char[] ichar = i.toCharArray();
			//no hit
            hit = false;
			//reset failcounter
            int failcounter = 0;
            while(!hit){
				//if fail too many times, just give up and move to next word.
                if(failcounter == 100){
                    failures[failindex] = i;
                    failindex++;
                    //100 fails, word must be too big for remaining area.
                    break;
                }
				//random direction selection.
                int r = p.nextInt(0,7);
                String startd = directions[r];
                String d = startd;
				//random point selection.
                int a = p.nextInt(0,24);
                int b = q.nextInt(0,24);
				//if spot already taken, find a new one.
                if(this.grid[a][b].letter != ' ') continue;
				//check if grid is valid at this point and direction.
                if((hmm = validateGrid(a, b, ichar, d)) == false){
					//if not increment direction ringbuffer
                    r = (r+1)%8;
                    d = directions[r];
					//enter while loop to check all available directions until we find one
					//or exhaust all options and fail.
                    while(((hmm = validateGrid(a, b, ichar, d))==false)&&(!d.equals(startd))){
                        r = (r+1)%8;
                        d = directions[r];
                    }
                }
				//if we fail, increment failcounter and try again
                if(!hmm){
                    failcounter++;
                    continue;
                }
				//if we succeed, add the word to the grid!
                else if(hmm){
                    addWordToGrid(a, b, ichar, d);
					//set hit to true to break the loop.
                    hit = true;
                }
            }
        }
		//after the grid is filled with words from the wordbank
		//add a bunch of random letters. This is a word search after all.
        for(int j = 0; j < 25; j++){
            for(int k = 0; k < 25; k++){
                if(this.grid[j][k].letter == ' '){
                    Letter l = new Letter((char)p.nextInt(97,122),j,k);
                    this.grid[j][k] = l;
                }
            }
        }
		//remove failed words from the wordbank.
        for(int o = 0; o < failindex; o++){
            this.wordBank.remove(failures[o]);
        }
		//return the grid for script purposes... potentially.
		return this.grid;
	}
}