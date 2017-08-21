package model;

public class StatisticsSummarize {

	private int granted;
	private int failed;
	private int wait;
	private int total;

	
	public StatisticsSummarize(int granted, int failed, int wait) {
		this.granted = granted;
		this.failed = failed;
		this.wait = wait;
		this.total = granted + failed + wait;
	}


	public int getGranted() {
		return granted;
	}

	public int getFailed() {
		return failed;
	}

	
	public int getWait() {
		return wait;
	}

	public int getTotal() {
		return total;
	}
}
