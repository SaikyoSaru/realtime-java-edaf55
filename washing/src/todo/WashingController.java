package todo;

import done.*;
import se.lth.cs.realtime.RTThread;

public class WashingController implements ButtonListener {
	// TODO: add suitable attributes
	private AbstractWashingMachine theMachine;
	private double theSpeed;
	private int buttonChoice;
	private int oldChoice;
	private TemperatureController temperatureController;
	private WaterController waterController;
	private SpinController spinController;
	private RTThread washingProgram;

	public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		this.theMachine = theMachine;
		this.theSpeed = theSpeed;
		buttonChoice = 0;
		oldChoice = 0;

		temperatureController = new TemperatureController(theMachine, theSpeed);
		waterController = new WaterController(this.theMachine, theSpeed);
		spinController = new SpinController(this.theMachine, theSpeed);

		temperatureController.start();
		waterController.start();
		spinController.start();

	}

	public void processButton(int theButton) {
		buttonChoice = theButton;
		//System.out.println("choice:  " + buttonChoice);
		
		switch (buttonChoice) {
		case 0:
			washingProgram = new WashingProgram0(theMachine, theSpeed, temperatureController, waterController,
					spinController);
			washingProgram.start();
			break;
		case 1:
			if (oldChoice == 0) {
				washingProgram = new WashingProgram1(theMachine, theSpeed, temperatureController, waterController,
						spinController);
				washingProgram.start();
				
			}
			break;
		case 2:
			if (oldChoice == 0) {
				washingProgram = new WashingProgram2(theMachine, theSpeed, temperatureController, waterController,
						spinController);
				washingProgram.start();
				
			}
			break;
		case 3:
			if (oldChoice == 0) {
				washingProgram = new WashingProgram3(theMachine, theSpeed, temperatureController, waterController,
						spinController);
				washingProgram.start();
				
			}
			break;
		}
		theMachine.setLock(true);
		oldChoice = buttonChoice;
	}
}
