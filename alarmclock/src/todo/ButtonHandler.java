package todo;

import se.lth.cs.realtime.semaphore.*;
import done.*;

public class ButtonHandler implements Runnable {
	private SharedData data;
	private ClockInput input;
	private Semaphore signal;
	private int setTime;

	public ButtonHandler(ClockInput input, SharedData data, Semaphore signal) {
		this.data = data;
		this.input = input;
		this.signal = signal;
	}

	public void run() {
		int prevChoice = input.SHOW_TIME;
		int choice;

		while (true) {
			signal.take();
			choice = input.getChoice();

			if (prevChoice == input.SHOW_TIME && choice == input.SET_ALARM) {
				data.SetAlarm(input.getValue());
				setTime = -1;
			}

			if (choice == input.SHOW_TIME && prevChoice == input.SET_TIME) {
				setTime = input.getValue();
			}

			if (setTime > -1) {
				data.SetTime(setTime);
				setTime = -1;

			}
			prevChoice = choice;
		}

	}

}
