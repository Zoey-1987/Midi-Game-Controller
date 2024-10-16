import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class MidiHandling {

	/*
	 * Establish baseline variables, currently set to use the systems default midi transmitter, create an arraylist for the currently toggled keys
	 * and a global variable for Robot so that it can press keys in one class and release them in another
	 * This should (hopefully) be changed at some point to allow the user to select any from a list of their avaliable devices when implemented into mainGUI
	 */
	private static final String transmitterName = "default";
	private static final String transmitterProperties = "javax.sound.midi.Transmitter";
	private static List<Integer> pressedMidiKeys = new CopyOnWriteArrayList<>();
	private static List<Integer> keysToRemove = new CopyOnWriteArrayList<>();
	private static List<Integer> pressedKeys = new CopyOnWriteArrayList<>();
	private static Robot robot;
	private static Set<String> triggeredChords = new HashSet<>();

	// Create an array for the items to be read in from the text document
	public static String[][] keyControls = mainGUI.getKeyConfigArray();

	// When this function is called it will get the transmitter and await input
	// It will then display both the note that is pressed and its status as integers
	public static void run() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			JOptionPane.showMessageDialog(null, "Can't locate a midi device, ensure it is properly connected", "Device Unavaliable", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			return null;
		}
	}

	/*
	 * A class to allow the midi keyboard to send data to the receiver allowing for the pc to read key inputs
	 * It does this by registering each keyboard press as an integer and using the robot class to simulate these key presses
	 * using their ASCII values. This class adds the key presses to an arraylist and a seperate thread handles the input of 
	 * all the keys currently pressed.
	 */
	public static class DisplayReceiver implements Receiver {
		// Establish a receiver
		private Receiver receiver;

		public DisplayReceiver(Receiver receiver) {
			this.receiver = receiver;
		}

		// The function responsible for sending the midi data in bytes to the rest of the program
		@Override
		public void send(MidiMessage message, long timeStamp) {
			byte[] bytes = message.getMessage();
			int key = byteToInt(bytes[1]);
			// If the action detected is a keypress add it to pressedMidiKeys
			if (bytes[2] == 100) {
				pressedMidiKeys.add(key);
			}
			// If the action detected is a key being raised then it is added to keysToRemove
			else if (bytes[2] == 64) {
				keysToRemove.add(key);
			}
			// If the action detected is neither an error message is output
			else {
				System.out.println("Error: Unfamiliar Keystroke Detected");
			}
		}

		public static List<Integer> getActiveKeys() {
			return pressedMidiKeys;
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
			// This thread needs to permanently loop for as long as the program is running to give real time updates to key strokes
			while (true) {
				// For each key in the pressedMidiKeys list, it will mimic key presses for those items
				for (Integer n : pressedMidiKeys) {
					mimicKeyPresses();
				};
				try {
					Thread.sleep(50); // Adjust the repeat rate as necessary, 50 seems to be more than responsive enough
				} 
				catch (InterruptedException e) {
					System.out.println("Sleep interrupted");
				}
				// If there are any keys to remove it will run the appropriate function
				if (!keysToRemove.isEmpty()) {
					removeKeysFromArrays();
				}
			}
		}


		// Function to find each key in the remove keys array, erase it from the main array and clear itself when its done iterating
		private static void removeKeysFromArrays() {
			// For each key that is being removed
			for (Integer keyNumber : keysToRemove) {
				// Go through each key in the config
				for (int i = 0; i < keyControls.length; i++) {
					String currentCode = keyControls[i][0]; // currentCode is the current config line being looked at
					if (containsKey(currentCode, keyNumber) && triggeredChords.contains(currentCode)) {
						int keyCode = Integer.valueOf(keyControls[i][1]);
						keyHandling(keyCode, false);
						triggeredChords.remove(currentCode);
						break;
					}
				}
				pressedMidiKeys.remove(keyNumber); // Remove the key from pressedMidiKeys
				pressedKeys.remove(keyNumber); // Remove the key from pressedKeys
				keysToRemove.remove(keyNumber); // Remove the key from keysToRemove
			}
		}		


		// The function responsible for mimicking the appropriate key presses
		private static void mimicKeyPresses() {
			for (Integer currentKey : pressedMidiKeys) { // For each currently pressed key
				if (!pressedKeys.contains(currentKey)) {
					for (int i = 0; i < keyControls.length; i++) { // Go through each item in the config

						String currentCode = keyControls[i][0];

						// Check if the activeKey can be part of a chord
						if (currentCode.contains(currentKey.toString()) && isCompleteChord(currentCode, currentKey) && !triggeredChords.contains(currentCode)) {
							int keyCode = Integer.valueOf(keyControls[i][1]);
							keyHandling(keyCode, true);
							pressedKeys.add(currentKey);
							triggeredChords.add(currentCode);
							break;
						}
					}
				}
			}
		}


		// Used to check if the keybind in config contains a key by checking it in pairs, as each key is 2 digits long
		// This helps negate the chances of a false key, such as if a config had 5678 and you're testing for key 67, it would be a false positive
		private static boolean containsKey(String currentCode, Integer keyNumber) {
			for (int i = 0; i < currentCode.length(); i += 2) {
				String pair = currentCode.substring(i, i + 2);
				if (pair.contains(keyNumber.toString())) {
					return true;
				}
			}
			return false;
		}


		// Function to check if a chord is fully played and handle its triggering
		private static boolean isCompleteChord(String chord, Integer startKey) {

			// Convert the code received from the config into its separate keys, and then checking if all of those keys are currently pressed
			List<Integer> chordKeys = new ArrayList<>();
			// Because each key on a standard 61 key keyboard is a 2 digit number, the code can be broken up into it
			for (int i = 0; i < chord.length(); i += 2) {
				// Each of these keys are then added to a list of chord keys
				chordKeys.add(Integer.parseInt(chord.substring(i, i + 2)));
			}

			// Returns true or false depending on if all chord keys are currently active
			return pressedMidiKeys.containsAll(chordKeys);
		}


		// Function to handle key / mouse presses and releases
		private static void keyHandling(int keyCode, boolean isPress) {
			// If the code is above 1000 it is likely a mouse press (I havent found any key presses remotely this high yet)
			// After determining mouse or key it will use isPress to determine to either raise or lower the key
			if (keyCode >= 1000) {
				if (isPress) {
					robot.mousePress(keyCode);
					mainGUI.insertText(getMouseNumber(keyCode) + " Pressed");
					
				} else {
					robot.mouseRelease(keyCode);
					mainGUI.insertText(getMouseNumber(keyCode) + " Released");
				}
			} 
			else {
				if (isPress) {
					robot.keyPress(keyCode);
					mainGUI.insertText(KeyEvent.getKeyText(keyCode) + "Pressed");
				} else {
					robot.keyRelease(keyCode);
					mainGUI.insertText(KeyEvent.getKeyText(keyCode) + "Released");
				}
			}
		}
	}
	
	// A function to check what mouse number a code represents
	
	private static String getMouseNumber(int keyCode) {
		switch(keyCode) {
		case 1024:
			return("Mouse_1");
		case 2048:
			return ("Mouse_2");
		case 4096:
			return("Mouse_3");
		}
		// In case the code isn't one of these three, just return null to handle any errors
		return null;
	}
	
	public static void updateKeyConfig() {
		keyControls = mainGUI.getKeyConfigArray();
	}
}
