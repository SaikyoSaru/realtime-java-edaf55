package todo;


public class TimeHandler implements Runnable {
	private long t, t0;
	private SharedData data;

	public TimeHandler(SharedData data) {
		this.data = data;
	}

	public void run() {
		t0 = System.currentTimeMillis(); // reference time
		t = 0;
		while (true) {

			t += 1000;
			long t1 = System.currentTimeMillis();
			long sleep = t -(t1-t0);
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				data.timeTick(); // ticks up time by 1s 
		}
	}


}
