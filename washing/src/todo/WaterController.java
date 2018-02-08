package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;

public class WaterController extends PeriodicThread {
	private AbstractWashingMachine machine;
	private int currentAction = WaterEvent.WATER_IDLE;
	private double targetLevel = 0d;
	private WashingProgram washingProgram;
	private RTEvent currentEvent;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed));
		this.machine = mach;
	}

	public void perform() {
		currentEvent = mailbox.tryFetch();
		if ((currentEvent != null) && (currentEvent instanceof WaterEvent)
				&& (currentEvent.getSource() instanceof WashingProgram)) {

			switch (((WaterEvent) currentEvent).getMode()) {

			case WaterEvent.WATER_IDLE:
				currentAction = WaterEvent.WATER_IDLE;
				machine.setDrain(false);
				machine.setFill(false);
				break;
			case WaterEvent.WATER_FILL:
				currentAction = WaterEvent.WATER_FILL;
				targetLevel = ((WaterEvent) currentEvent).getLevel();
				washingProgram = ((WashingProgram) currentEvent.getSource());
				if (machine.isLocked()) {
					machine.setFill(true);
					machine.setDrain(false);
				}
				break;
			case WaterEvent.WATER_DRAIN:
				currentAction = WaterEvent.WATER_DRAIN;
				targetLevel = ((WaterEvent) currentEvent).getLevel();
				machine.setFill(false);
				machine.setDrain(true);
				washingProgram = ((WashingProgram) currentEvent.getSource());
				break;
			}
		}
		if (washingProgram != null
				&& ((currentAction == WaterEvent.WATER_DRAIN && machine.getWaterLevel() <= targetLevel)
						|| (currentAction == WaterEvent.WATER_FILL && machine.getWaterLevel() >= targetLevel))) {
			// System.out.println("ACK");
			washingProgram.putEvent(new AckEvent(this));
			washingProgram = null;
		}

	}
}
