package uta.cse3310;

public class Grid{
	
	public char[][] grid;
	public String[] wordBank;
	
	public Grid(){

	}
	
	public boolean validateSelection(){
		return false;
	}
	
	public boolean checkHorizontal(){
		return false;
	}
	
	public boolean checkVertical(){
		return false;
	}
	
	public boolean checkDiagonal(){
		return false;
	}
	
	public String[] generateWordBank(){
		return wordBank;
	}
	
	public char[][] fillGrid(){
		return grid;
	}
	
}
