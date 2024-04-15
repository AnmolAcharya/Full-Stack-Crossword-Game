package uta.cse3310;

public class GameClock {
	private	long endTime;

	public GameClock(long end) {

		this.endTime = end;
	}

	public void startTimer(Runnable endGame) {
		new Thread(() -> {
            try {
                // Convert minutes to milliseconds and sleep
                Thread.sleep(endTime * 60 * 1000);
                // After the sleep duration ends, call the end game method
                endGame.run();
            } catch (InterruptedException e) {
                System.out.println("Game timer was interrupted.");
            }
        }).start();
	}

}


