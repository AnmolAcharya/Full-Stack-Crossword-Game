package uta.cse3310;

public class Player {
	public String uid;
	public String gameId;
	public String userName;
	public boolean ready;
	public String color;
	public int highScore;
	public int currentScore;

	public Player(String uid) {
		this.uid = uid;
		this.currentScore = 0;
		this.highScore = 0;
		this.ready = false;
	}

	public void ready() {
		ready = !ready;
	}

	//call with number of points to add or with 0 to reset the current score
	public void updateScore() {
		currentScore++;
	}

	// highscore treated as most words found by player among all played games in a session
	public void updateHighScore() {
		highScore += currentScore;
	}
}


