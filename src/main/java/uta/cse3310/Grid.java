package uta.cse3310;

public class Grid{
	
	public Letter[][] grid;
	public String[] wordBank;
	
	public Grid(){

	}
	
	public boolean validateSelection(Letter first, Letter last){
		return false;
	}
	
	public boolean checkHorizontal(Letter first, Letter last){
		return false;
	}
	
	public boolean checkVertical(Letter first, Letter last){
		return false;
	}
	
	public boolean checkDiagonal(Letter first, Letter last){
		return false;
	}
	
	public String[] generateWordBank(){
		return wordBank;
	}
	
	public Letter[][] fillGrid(){
		return grid;
	}
	
}
