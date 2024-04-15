package uta.cse3310;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Collections;
public class Game {
	
	public String gameId;
	public String gameTitle;
	public boolean joinable;
	public Lobby lobby;
	public Grid grid;
	public ArrayList<Player> players;
	public ArrayList<Player> leaderboard;
	public GameClock gameClock;

	private final String[] colors = {"red", "blue", "pink", "green"};
	private final boolean[] colorInUse = new boolean[colors.length];
	
	public Game(Player player, ArrayList<String> words, Lobby lobby){
		this.gameId = generateUniqueID();
		this.gameTitle = gameId.substring(0, 4);
		this.joinable = true;
		this.lobby = lobby;
		this.grid = new Grid();
		this.grid.generateWordBank(words);
		this.grid.fillGrid(grid.wordBank);
		this.players = new ArrayList<Player>();
		this.leaderboard = players;
		this.gameClock = new GameClock(5);

		
		// reset score, assign color and set color in use, the rest not in use
		player.currentScore = 0;
		player.color = "red";
		colorInUse[0] = true;
		for (int i = 1; i < colorInUse.length; i++) {
            colorInUse[i] = false;
        }
		players.add(player);
	}

	public void startGame(){
		joinable = false;
		gameClock.startTimer();
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
	
	public void checkEndGame(){}

	public void endGame(Lobby lobby){
		for(Player player:players){
			player.updateHighScore();
		}
		lobby.updateAllTimeLeaderboard(players);
	}	
}
