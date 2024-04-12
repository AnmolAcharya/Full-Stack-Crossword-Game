package uta.cse3310;

public class GameClock{
	private	long startTime;
    public boolean timeExceed;

	public GameClock() {
        this.timeExceed = false;
	}

	public void startTimer() {
    	startTime = System.currentTimeMillis();
	}

	// Use this to get the time on a active game
	public String getTime() {
		long elapsedTime = System.currentTimeMillis() - startTime;
		long elapsedSeconds = elapsedTime / 1000;
		long second = elapsedSeconds % 60;
		long minute = elapsedSeconds / 60;
		if(minute >= 5) {
			timeExceed = true;
		}
		return minute + ": " + second;
	}
}


