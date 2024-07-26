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
	 * Establish baseline variables, currently set to use the systems default midi transmitter, create an arraylist for the currently toggled keys
	 * and a global variable for Robot so that it can press keys in one class and release them in another
	 * This should (hopefully) be changed at some point to allow the user to select any from a list of their avaliable devices when implemented into mainGUI
	 */
	private static final String transmitterName = "default";
	private static final String transmitterProperties = "javax.sound.midi.Transmitter";
	private static ArrayList<Integer> activeKeys = new ArrayList<>();
	private static ArrayList<Integer> keysToRemove = new ArrayList<>();
	private static Robot robot;

	// When this function is called it will get the transmitter and await input
	// It will then display both the note that is pressed and its status as integers
	public static void run() {

		// Get a transmitter and synthesizer from their device names
		// using system properties or defaults
		Transmitter transmitter = getTransmitter();

		// If either device is null then break out of the function and not attempt to receieve any input
		if (transmitter == null) {
			return;
		}


		/* 
		 * This section of code was greatly assisted by github user: ksnortum
		 * as their midi-examples repository essentially taught me how to set up a receiver and transmitter using java's midi class
		 * and their original code was used to assess how I would go through with this project, so big thanks to them! 
		 */
		try {
			// Program will attempt to get a midi receiver, and if successful
			Receiver receiver = MidiSystem.getReceiver();
			DisplayReceiver displayReceiver = new DisplayReceiver(receiver); 
			transmitter.setReceiver(displayReceiver); 
			System.out.println("Awaiting input...");
			keyPressThread k1 = new keyPressThread();
			k1.start();
		} catch (MidiUnavailableException e) {
			System.err.println("Error getting receiver from synthesizer");
			e.printStackTrace();
		}
	}

	/*
	 * This section of code is used at the beginning to get the transmitter device (The midi keyboard) as an input
	 * It will throw an exception if it is unable to locate a device, or if a device is found but won't connect
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
	 * A class to allow the midi keyboard to send data to the receiver allowing for the pc to read key inputs
	 * It does this by registering each keyboard press as an integer and using the robot class to simulate these key presses
	 * using their ASCII values. This class adds the key presses to an arraylist and a seperate thread handles the input of 
	 * all the keys currently pressed.
	 */
	private static class DisplayReceiver implements Receiver {
		// Establish a receiver
		private Receiver receiver;

		public DisplayReceiver(Receiver receiver) {
			this.receiver = receiver;
		}

		// The function responsible for sending the midi data in bytes to the rest of the program
		@Override
		public void send(MidiMessage message, long timeStamp) {
			byte[] bytes = message.getMessage();
			// If the action detected is a keypress add it to activeKeys
			if (bytes[2] == 100) {
				activeKeys.add(byteToInt(bytes[1]));
			}
			// If the action detected is a key being raised then it is added to keysToRemove
			else if (bytes[2] == 64) {
				keysToRemove.add(byteToInt(bytes[1]));
			}
			// If the action detected is neither an error message is output
			else {
				System.out.println("Error: Unfamiliar Keystroke Detected");
			}
		}

		// When the program is exited the receiver is closed to free up resources
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
						System.out.println(n);
					};
					try {
						Thread.sleep(50); // Adjust the repeat rate as necessary, 50 seems to be more than responsive enough
					} catch (InterruptedException e) {
						System.out.println("Sleep interrupted");
					}
					// If there are any keys to remove it will run the appropriate function
					if (!keysToRemove.isEmpty()) {
						removeKeys();
					}
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// Function to find each key in the remove keys array, erase it from the main array and clear itself when its done iterating
		public static void removeKeys() {
			for (Integer keyNumber : keysToRemove) {
				int index = activeKeys.indexOf(keyNumber);
				activeKeys.remove(index);
			}
			keysToRemove.clear();
		}
	}
}
