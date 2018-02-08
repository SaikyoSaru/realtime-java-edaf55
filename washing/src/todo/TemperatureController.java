package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;

public class TemperatureController extends PeriodicThread {
	private AbstractWashingMachine machine;
	private int currentMode = TemperatureEvent.TEMP_IDLE;
	private RTEvent currentEvent;
	private WashingProgram washingProgram;
	private double targetTemp;
	private double minWaterLevel = 0.2;
	private double tempDiff = 1;

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed));
		this.machine = mach;
	}

	public void perform() {
		currentEvent = mailbox.tryFetch();

		if (currentEvent != null && (currentEvent instanceof TemperatureEvent)
				&& (currentEvent.getSource() instanceof WashingProgram)) {
			currentMode = ((TemperatureEvent) currentEvent).getMode();
			switch (((TemperatureEvent) currentEvent).getMode()) {

			case TemperatureEvent.TEMP_IDLE:
				machine.setHeating(false);
				break;
			case TemperatureEvent.TEMP_SET:
				targetTemp = ((TemperatureEvent) currentEvent).getTemperature();
				washingProgram = (WashingProgram) currentEvent.getSource();
				break;
			}

		}
		if (machine.getWaterLevel() > minWaterLevel && currentMode == TemperatureEvent.TEMP_SET) {
			if (machine.getTemperature() < targetTemp - tempDiff) {
				machine.setHeating(true);
			} else if (machine.getTemperature() >= targetTemp) {
				machine.setHeating(false);
				if (washingProgram != null) {
					washingProgram.putEvent(new AckEvent(this));
					washingProgram = null;
				}
			}

		} else {
			machine.setHeating(false);
		}
	}
}
