package lift;

public class PersonThread extends Thread {
	private Monitor mon;
	private int initFloor;
	private int destFloor;

	public PersonThread(Monitor mon) {
		this.mon = mon;
		initFloor = (int) (Math.random() * 7);
		destFloor = destFloor();
	}

	@Override
	public void run() {
		while (true) {
			mon.enterLift(initFloor, destFloor);

			try {
				Thread.sleep(1000 * ((int) (Math.random() * 46.0)));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initFloor = (int) (Math.random() * 7);
			destFloor = destFloor();

		}
	}

	private int destFloor() {
		int floor = (int) (Math.random() * 7);

		while (floor == initFloor) {
			floor = (int) (Math.random() * 7);
		}

		return floor;

	}

}
