package lift;

public class Monitor {
	private LiftView lv;
	private int here; // this level
	private int next; // next level
	private int[] waitEntry; // awaiting entry
	private int[] waitExit;
	private int direction; // direction of the elevator
	private int load; // how many there are in the lift
	private int waitingPassengers;

	public Monitor(LiftView lv) {
		waitEntry = new int[7];
		waitExit = new int[7];
		here = 0;
		next = 0;
		load = 0;
		waitingPassengers = 0;
		direction = 1;
		this.lv = lv;
	}

	public void moveLift() {
		int hereTmp;
		int nextTmp;

		synchronized (this) {
			notifyAll();

			here = next;

			while (waitForPassengers()) {
				halt();
			}
			while (waitingPassengers == 0) {
				halt();
			}
			nextFloor();
			hereTmp = here;
			nextTmp = next;
		}
		lv.moveLift(hereTmp, nextTmp);

	}

	public synchronized void enterLift(int initFloor, int destFloor) {
		waitEntry[initFloor] += 1;
		waitingPassengers++;
		lv.drawLevel(initFloor, waitEntry[initFloor]);
		notifyAll();
		while (!(enterPlease(initFloor))) {
			halt();
		}
		load++;
		waitEntry[initFloor]--;
		lv.drawLevel(initFloor, waitEntry[initFloor]);
		lv.drawLift(initFloor, load);
		waitExit[destFloor]++;
		notifyAll();

		while (!exitPlease(destFloor)) {
			halt();
		}
		load--;
		waitingPassengers--;
		lv.drawLift(destFloor, load);
		waitExit[destFloor]--;
		notifyAll();

	}

	private boolean enterPlease(int floor) {
		return load < 4 && here == floor && here == next;
	}

	private boolean waitForPassengers() {

		return (waitEntry[here] > 0 && load < 4) || waitExit[here] > 0;
	}

	private boolean exitPlease(int floor) {
		return (floor == here && here == next);
	}

	private void nextFloor() {
		next = here + direction * 1;
		if (next == 7) {
			direction = -1;
			next = 5;
		}
		if (next == -1) {
			direction = 1;
			next = 1;
		}

	}

	private void halt() {
		try {
			wait();
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}

}
