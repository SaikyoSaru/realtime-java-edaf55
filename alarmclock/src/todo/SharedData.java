package todo;

import done.*;
import se.lth.cs.realtime.semaphore.*;

public class SharedData {
	private int time, alarmTime, aCounter;
	boolean alarmOn, alarma;
	private ClockOutput clockOut;
	private ClockInput clockInput;
	private MutexSem sem;

	public SharedData(ClockOutput clock, ClockInput input) {
		this.clockOut = clock;
		this.clockInput = input;
		sem = new MutexSem();
		time = alarmTime = aCounter = 0;
	}

	public void SetAlarm(int newTime) {
		sem.take();
		alarmTime = newTime;
		sem.give();
	}

	public void SetTime(int newTime) {
		sem.take();
		time = newTime;
		sem.give();
	}
/*
 * Alarm ticker
 * */
	private void alarm() {
		
		if (clockInput.getAlarmFlag() && time == alarmTime) {
			aCounter = 20;
		}
		if (aCounter > 0) {
			aCounter -= 1;
			clockOut.doAlarm();
		}
		if (!clockInput.getAlarmFlag()) {
			alarmOff();
		}
		

	}
/*
 * Resets alarm counter
 * */
	private void alarmOff() {
		sem.take();
		aCounter = 0;
		sem.give();
	}
/**
 * increments time by 1s each time its called upon, also handles the format.
 */
	public void timeTick() {
		sem.take();
		time++;

		int tmp = time ;
		int seconds = tmp % 100;
		int minutes = tmp % 10000;
		minutes /= 100;
		int hours = tmp / 10000;

		if (seconds == 60) {
			minutes += 1;
			tmp += 40;
		}
		if (minutes == 60) {
			hours += 1;
			tmp += 4000;
		}
		if (hours == 24) {
			tmp = 0;
		}
		time = tmp;
		clockOut.showTime(time);
		alarm();

		sem.give();

	}
}
