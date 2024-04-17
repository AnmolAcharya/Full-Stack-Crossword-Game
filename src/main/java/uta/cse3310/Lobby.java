package uta.cse3310;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.AbstractMap;

public class Lobby {

    public ArrayList<Game> joinableGames;
    public transient PriorityQueue<Map.Entry<String, Integer>> leaderboardSortingQueue;
    public transient Map<String, Integer> allTimeScores; // Map from user ID to score
    public transient Map<String, String> userIdToUsername; // Map from user ID to username
    public ArrayList<Map.Entry<String, Integer>> allTimeLeaderboard;
    public Map<String, ArrayList<Player>> concurrentLeaderboard;

    public Lobby() {
        this.joinableGames = new ArrayList<Game>();
        this.allTimeScores = new HashMap<>();
        this.userIdToUsername = new HashMap<>();
        this.leaderboardSortingQueue = new PriorityQueue<>(20, (a, b) -> b.getValue().compareTo(a.getValue()));
        this.allTimeLeaderboard = new ArrayList<Map.Entry<String, Integer>>();
        this.concurrentLeaderboard = new HashMap<String, ArrayList<Player>>();
    }

    // update joinable games and concurrent leaderboard
    public void updateLobby(Map<String, Game> activeGames) {
        joinableGames.clear();
        concurrentLeaderboard.clear();

        for (Map.Entry<String, Game> entry : activeGames.entrySet()) {
            Game game = entry.getValue();
            if (game.joinable) {
                // if game is joinable add it to the list of joinable games
                joinableGames.add(game);
            } else {
                // if game is not joinable add its leaderboard info to the concurrent
                // leaderboard
                concurrentLeaderboard.put(game.gameId, game.leaderboard);
            }
        }

    }

    public void updateAllTimeLeaderboard(ArrayList<Player> players) {
        for (Player player : players) {
            allTimeScores.put(player.uid, player.highScore);
            userIdToUsername.put(player.uid, player.userName);
        }

        // clear old entries, and reinsert with new entries
        leaderboardSortingQueue.clear();
        leaderboardSortingQueue.addAll(allTimeScores.entrySet());

        // only keep the top 20 scores
        while (leaderboardSortingQueue.size() > 20) {
            leaderboardSortingQueue.poll();
        }

        // add updated list to allTimeLeaderboard with the username as the key
        allTimeLeaderboard.clear();
        while (!leaderboardSortingQueue.isEmpty()) {
            Map.Entry<String, Integer> entry = leaderboardSortingQueue.poll();
            String username = userIdToUsername.get(entry.getKey());
            allTimeLeaderboard.add(new AbstractMap.SimpleEntry<>(username, entry.getValue()));
        }

        // sort this list by scores in descending order
        allTimeLeaderboard.sort((a, b) -> b.getValue().compareTo(a.getValue()));

    }

}
