package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;

public class SpinController extends PeriodicThread {
	private AbstractWashingMachine machine;
	private int currentAction = SpinEvent.SPIN_OFF;
	private RTEvent currentEvent;
	private int spinDirection = AbstractWashingMachine.SPIN_RIGHT;
	private long rotDirTime = 60 * 1000;

	private long next;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed));
		this.machine = mach;
		rotDirTime = (long) (rotDirTime / speed);
	}

	public void perform() {
		currentEvent = mailbox.tryFetch();

		if (currentEvent != null && currentEvent instanceof SpinEvent
				&& currentEvent.getSource() instanceof WashingProgram) {
			currentAction = ((SpinEvent) currentEvent).getMode();

			switch (((SpinEvent) currentEvent).getMode()) {
			case SpinEvent.SPIN_FAST:
				break;
			case SpinEvent.SPIN_OFF:
				machine.setSpin(SpinEvent.SPIN_OFF);
				break;
			case SpinEvent.SPIN_SLOW:
				machine.setSpin(spinDirection);
				next = System.currentTimeMillis() + rotDirTime;

				break;

			}

		}

		if (currentAction == SpinEvent.SPIN_SLOW && machine.isLocked() && (System.currentTimeMillis() >= next)) {
			switch (spinDirection) {
			case AbstractWashingMachine.SPIN_LEFT:
				spinDirection = AbstractWashingMachine.SPIN_RIGHT;
				break;
			case AbstractWashingMachine.SPIN_RIGHT:
				spinDirection = AbstractWashingMachine.SPIN_LEFT;
			}
			machine.setSpin(spinDirection);
			next = (long) (System.currentTimeMillis() + rotDirTime);
		} else if (currentAction == SpinEvent.SPIN_FAST && machine.getWaterLevel() == 0 && machine.isLocked()) {
			machine.setSpin(SpinEvent.SPIN_FAST);
		
		} else if (currentAction == SpinEvent.SPIN_FAST) {
			machine.setSpin(SpinEvent.SPIN_OFF);
		}

	}
}
