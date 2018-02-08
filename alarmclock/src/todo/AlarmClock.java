package todo;

import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;

/**
 * Main class of alarm-clock application. Constructor providing access to IO.
 * Method start corresponding to main, with closing down done in terminate.
 */
public class AlarmClock {
	private ClockInput input;
	private ClockOutput output;
	private Semaphore signal;
	// Declare thread objects here..
	private Thread timer;
	private Thread buttons;
	private SharedData data;

	/**
	 * Create main application and bind attributes to device drivers.
	 * 
	 * @param i
	 *            The input from simulator/emulator/hardware.
	 * @param o
	 *            Dito for output.
	 */
	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		signal = input.getSemaphoreInstance();
	}

	/**
	 * Tell threads to terminate and wait until they are dead.
	 */
	public void terminate() {
		// Do something more clever here...
		output.console("AlarmClock exit.");
	}

	/**
	 * Create thread objects, and start threads
	 */
	public void start() {
		data = new SharedData(output, input); // handles all shared data
		timer = new Thread(new TimeHandler(data)); 
		buttons = new Thread(new ButtonHandler(input, data, signal));

		timer.start();
		buttons.start();
	}

}
