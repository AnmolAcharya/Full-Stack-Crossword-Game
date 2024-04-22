package uta.cse3310;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Collections;

public class Game {
	
	public String gameId;
	public String gameTitle;
	public boolean joinable;
	public Grid grid;
	public ArrayList<Player> players;
	public ArrayList<Player> leaderboard;
	public GameClock gameClock;
	public boolean inProgress;

	private transient GameObserver observer;

	private final String[] colors = {"red", "blue", "pink", "green"};
	private final boolean[] colorInUse = new boolean[colors.length];
	
	public Game(Player player, ArrayList<String> words, Lobby lobby, App app){
		this.gameId = generateUniqueID();
		this.gameTitle = gameId.substring(0, 4);
		this.joinable = true;
		this.grid = new Grid();
		this.grid.generateWordBank(words);
		this.grid.fillGrid(grid.wordBank);
		this.players = new ArrayList<Player>();
		this.leaderboard = players;
		this.gameClock = new GameClock(5);
		this.observer = app;
		this.inProgress = false;

		// reset score, assign color and set color in use, the rest not in use
		player.currentScore = 0;
		player.color = "red";
		colorInUse[0] = true;
		for (int i = 1; i < colorInUse.length; i++) {
            colorInUse[i] = false;
        }
		players.add(player);
	}
	
	public Game(){
		this.joinable = true;
	}

	public void startGame(){
		joinable = false;
		inProgress = true;
		gameClock.startTimer(this::endGame);
	}
	
	public static String generateUniqueID() {
   	 return UUID.randomUUID().toString();
  	}
	
	public void addPlayer(Player player){
		// assign first available color to player
		for (int i = 0; i < colorInUse.length; i++) {
            if (!colorInUse[i]) {
                player.color = colors[i];
                colorInUse[i] = true;
                break;
            }
        }
		player.currentScore = 0;
		players.add(player);
		return;
	}
	
	public void removePlayer (Player player){
		// free color from use
		for (int i = 0; i < colors.length; i++) {
            if (colors[i].equals(player.color)) {
                colorInUse[i] = false;
                break;
            }
        }

		// reset player to not ready
		player.ready = false;
		player.gameId = null;
		player.currentScore = 0;
		players.remove(player);
		return;
	}
	
	public void updateLeaderboard(Player player){
		player.updateScore();
		Collections.sort(leaderboard,(p1,p2) -> Integer.compare(p2.currentScore, p1.currentScore));
	}

	public void updateJoinable() {
		if(players.size() == 0){
			joinable = false;
		} else {
			joinable = true;
		}
	}
	
	public boolean checkEndGame() {
		boolean allWordsFound = true;
        
        for (Boolean found : grid.wordBank.values()) {
            if (!found) { // If any word is not found, set allFound to false
                allWordsFound = false;
                break;
            }
        }

        if (allWordsFound) {
            endGame();
        }

		return allWordsFound;
	}

	public void endGame(){
		for(Player player:players){
			player.updateHighScore();
			player.gameId = null;
			player.ready = false;
		}
		
		observer.notifyGameEnd(this);
	}	
}
