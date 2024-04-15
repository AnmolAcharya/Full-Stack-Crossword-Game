package uta.cse3310;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class Lobby{
	
	public ArrayList<Game> joinableGames;
	public Map<String, Map<String, Integer>> allTimeLeaderboard;
	public Map<String, ArrayList<Player>> concurrentLeaderboard;
	
	public Lobby(Map<String, Map<String, Integer>> allTimeLeaderboard){
		this.joinableGames = new ArrayList<Game>();
		this.allTimeLeaderboard = new HashMap<String, Map<String, Integer>>();
		this.concurrentLeaderboard = new HashMap<String, ArrayList<Player>>();
	}

	// Method to update joinable games and concurrent leaderboard
    public void updateLobby(Map<String, Game> activeGames) {
        joinableGames.clear();
        concurrentLeaderboard.clear();
        //Collections.sort(allTimeLeaderboard,(p1,p2)-> p1.highscore-p2.highscore);

        for (Map.Entry<String, Game> entry : activeGames.entrySet()) {
            Game game = entry.getValue();
            if (game.joinable) {
                // If game is joinable add it to the list of joinable games
                joinableGames.add(game);
            } else {
                // If game is not joinable add its leaderboard info to the concurrent leaderboard
                concurrentLeaderboard.put(game.gameId, game.leaderboard);
            }
        }
        
    }
    
    
    public static void updateAllTimeLeaderboard(ArrayList<Player> players){
    	for(Player player: players){
    		App.allTimeLeaderBoard.get(player.uid).put(player.userName, player.highScore);
    	}
    }

}


