package uta.cse3310;

public class GameClock{
	private	long startTime;
	private	long endTime;
    public boolean timeExceed;

	public GameClock(long end) {
        this.timeExceed = false;
		this.endTime = end;
	}

	public void startTimer() {
    	startTime = System.currentTimeMillis();
	}

	// Use this to get the time on a active game and check if it is over 5 minutes
	public String getTime() {
		long elapsedTime = System.currentTimeMillis() - startTime;
		long elapsedSeconds = elapsedTime / 1000;
		long second = elapsedSeconds % 60;
		long minute = elapsedSeconds / 60;
		if(minute >= endTime) {
			timeExceed = true;
		}
		return minute + ": " + second;
	}
}


