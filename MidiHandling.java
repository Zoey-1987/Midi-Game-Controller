import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MidiHandling {
	
	/*
	 * Establish baseline variables, currently set to use the systems default midi synthesizer and transmitter
	 * This should (hopefully) be changed at some point to allow the user to select any from a list of their avaliable devices when implemented into main
	 */
	private static final String transmitterName = "default";
	private static final String transmitterProperties = "javax.sound.midi.Transmitter";
	private static Thread keyRepeatThread;
	private static List<Integer> activeKeys = new CopyOnWriteArrayList<>();
	private static Robot robot;
	// When this function is called it will get the transmitter and synthesizer and await input from the transmitter
	// It will then display both the note that is pressed and its status as integers
	public static void run() {
		
		// Get a transmitter and synthesizer from their device names
		// using system properties or defaults
		Transmitter transmitter = getTransmitter();
		
		// If either device is null then break out of the function and not attempt to receieve any input
		if (transmitter == null) {
			return;
		}
		
		
		// You get your receiver from the synthesizer, then set it in
		// your transmitter.  Optionally, you can create an implementation
		// of Receiver to display the messages before they're sent.
		try {
			Receiver receiver = MidiSystem.getReceiver();
			DisplayReceiver displayReceiver = new DisplayReceiver(receiver); // optional
			transmitter.setReceiver(displayReceiver); // or just "receiver"
			
			// You should be able to play on your musical keyboard (transmitter)
			// and hear sounds through your PC synthesizer (receiver)
			System.out.println("Awaiting input...");
			keyPressThread k1 = new keyPressThread();
			k1.start();
		} catch (MidiUnavailableException e) {
			System.err.println("Error getting receiver from synthesizer");
			e.printStackTrace();
		}
	}

	/**
	 * @return a specific transmitter object by setting the system property, otherwise the default
	 */
	private static Transmitter getTransmitter() {
		if (! transmitterName.isEmpty() && ! "default".equalsIgnoreCase(transmitterName)) {
			System.setProperty(transmitterProperties, transmitterName);
		}
		
		try {
			return MidiSystem.getTransmitter();
		} catch (MidiUnavailableException e) {
			System.err.println("------------------------------------------------------------\nError getting transmitter, ensure device is connected\n------------------------------------------------------------");
			e.printStackTrace();
			return null;
		}
	}

	/*
	 *  Segment of code that helps display all of the midi data received
	 */
	private static class DisplayReceiver implements Receiver {
		private Receiver receiver;
		
		public DisplayReceiver(Receiver receiver) {
			this.receiver = receiver;
		}

		@Override
		public void send(MidiMessage message, long timeStamp) {
			byte[] bytes = message.getMessage();
			if (bytes[2] ==100) {
				activeKeys.add(byteToInt(bytes[1]));
			}
			else {
				robot.keyRelease(byteToInt(bytes[1]) + 28);
				activeKeys.remove(activeKeys.indexOf(byteToInt(bytes[1])));
			}
		}

		@Override
		public void close() {
			receiver.close();
		}




	
		// Convert the byte values to integers to make it easier to develop and understand
		private int byteToInt(byte b) {
			return b & 0xff;
		}
		
	}
	
	/*
	 * A thread that will display to the user what keys are currently being pressed down
	 * This will be replaced with active key presses using the robot class
	 */
	
	private static class keyPressThread extends Thread {

		public void run() {
			System.out.println("Starting thread...");
			while (true) {
				try {
					robot = new Robot();
					for (Integer n : activeKeys) {
						System.out.println(n+28);
						robot.keyPress(n + 28);
					};
					try {
						Thread.sleep(50); // Adjust the repeat rate as necessary
					} catch (InterruptedException e) {
						System.out.println("Sleep interrupted");
					}
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
