package todo;

import done.AbstractWashingMachine;

public class WashingProgram0 extends WashingProgram {

	
	protected WashingProgram0(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void wash() throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("Abort");
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0));
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));
		
		myMachine.setLock(false);
	}

}
