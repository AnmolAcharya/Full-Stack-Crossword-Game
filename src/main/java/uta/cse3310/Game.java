package uta.cse3310;

import java.util.ArrayList;
import java.util.UUID;

public class Game {
	
	public String gameId;
	public String gameTitle;
	public boolean joinable;
	public Grid grid;
	public ArrayList<Player> players;
	public ArrayList<Player> leaderboard;
	public GameClock gameClock;

	private final String[] colors = {"red", "blue", "pink", "green"};
	private final boolean[] colorInUse = new boolean[colors.length];
	
	public Game(Player player){
		this.gameId = generateUniqueID();
		this.joinable = true;
		this.players = new ArrayList<Player>();
		this.leaderboard = new ArrayList<Player>();
		this.gameClock = new GameClock(5);
		this.gameTitle = gameId.substring(0, 4);
		
		// assign color, and set color in use, the rest not in use
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
	
	public void updateLeaderboard(Player player, int points){
		player.updateScore(points);
		//Collections.sort(players,(p1,p2)-> p1.currentScore-p2.currentScore);
	}

	public void updateAllTimeLeaderboard(){
		for(int i = 0; i < players.size(); i++) {
			players.get(i).updateHighscore();
			//Lobby.allTimeLeaderboard.add(players.get(i).highscore);
		}
	}

	public void updateJoinable() {
		if(players.size() == 0){
			joinable = false;
		} else {
			joinable = true;
		}
	}

	public void endGame(){
		updateAllTimeLeaderboard();
	}	
}
