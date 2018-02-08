package todo;

import done.AbstractWashingMachine;

public class WashingProgram1 extends WashingProgram {
	private double speed;

	protected WashingProgram1(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
		// TODO Auto-generated constructor stub
		this.speed = speed;
	}

	@Override
	protected void wash() throws InterruptedException {
		System.out.println("Color Wash");
		//myMachine.setLock(true); // move to controller

		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
		mailbox.doFetch();
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.5));
		mailbox.doFetch();
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0));
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 60));
		mailbox.doFetch();
		// System.out.println("hot enough!: " + myMachine.getTemperature());
		sleep((long)(30*60*1000/speed));
		
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));
		
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
		mailbox.doFetch();

		for (int i = 0; i < 5; i++) {
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.5));
			mailbox.doFetch();
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0));
			sleep((long)(2*60*1000/speed));
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
			mailbox.doFetch();
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0));

		}

		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));

		sleep((long)(5*60*1000/speed));
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));

		myMachine.setLock(false);
		System.out.println("Wash complete!");

	}

}
