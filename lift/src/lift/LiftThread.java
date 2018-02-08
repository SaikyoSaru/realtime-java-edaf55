package lift;

public class LiftThread extends Thread {
	private Monitor monitor;

	public LiftThread(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {
		while (true) {
			monitor.moveLift();

		}
	}

}
