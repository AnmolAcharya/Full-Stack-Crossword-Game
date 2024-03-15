package uta.cse3310;

public class GameClock{

	private int minute;
    	private int second;
    	public boolean timeExceed;

    	public GameClock() {
        	minute = 0;
        	second = 0;
       		timeExceed = false;
    	}

    	public void startTimer() {
        	try {
        	    while (minute < 5) {
        	        second++;
        	        if (second == 60) {
        	            minute++;
        	            second = 0;
        	        }
        	        Thread.sleep(1000); // Sleep for 1 second (1000 milliseconds)
        	    }
        	    timeExceed = true;
        	} catch (InterruptedException e) {
        	    e.printStackTrace();
        	}
    	}

    	public String getTime() {
	        return String.format("%02d:%02d", minute, second);
    	}
	

}


