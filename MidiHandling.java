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
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class MidiHandling {

	/*
	 * Establish baseline variables, currently set to use the systems default midi transmitter, create an arraylist for the currently toggled keys
	 * and a global variable for Robot so that it can press keys in one class and release them in another
	 * This should (hopefully) be changed at some point to allow the user to select any from a list of their avaliable devices when implemented into mainGUI
	 */
	private static final String transmitterName = "default";
	private static final String transmitterProperties = "javax.sound.midi.Transmitter";
	private static List<Integer> activeKeys = new CopyOnWriteArrayList<>();
	private static List<Integer> keysToRemove = new CopyOnWriteArrayList<>();
	private static List<Integer> pressedKeys = new CopyOnWriteArrayList<>();
	private static Robot robot;

	// Create an array for the items to be read in from the text document
	public static String[][] keyControls = new String[9][2];

	// When this function is called it will get the transmitter and await input
	// It will then display both the note that is pressed and its status as integers
	public static void run() {
		keyPressThread.getKeyConfig();
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
			// If the action detected is a keypress add it to activeKeys
			if (bytes[2] == 100 && !activeKeys.contains(key)) {
				System.out.println("Key number: " + key);
				activeKeys.add(key);
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
			return activeKeys;
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
				for (Integer n : activeKeys) {
					pressKeys();
				};
				try {
					Thread.sleep(50); // Adjust the repeat rate as necessary, 50 seems to be more than responsive enough
				} 
				catch (InterruptedException e) {
					System.out.println("Sleep interrupted");
				}
				// If there are any keys to remove it will run the appropriate function
				if (!keysToRemove.isEmpty()) {
					removeKeys();
				}
			}
		}

		// Function to find each key in the remove keys array, erase it from the main array and clear itself when its done iterating
		public static void removeKeys() {
			for (Integer keyNumber : keysToRemove) {
				for (int i = 0; i < keyControls.length; i++) {
					String currentCode = keyControls[i][0];
					if (currentCode.startsWith(keyNumber.toString())) {
						int keyCode = Integer.valueOf(keyControls[i][1]);
						keyHandling(keyCode, false);
						break;
					}
				}
				activeKeys.remove(keyNumber); // Remove the key from activeKeys
				pressedKeys.remove(keyNumber); // Remove the key from pressedKeys
				keysToRemove.remove(keyNumber); // Remove the key from keysToRemove
			}
		}

		// The function responsible for mimicking the appropriate key presses
		public static void pressKeys() {
			for (Integer currentKey : activeKeys) { // For each currently pressed key
				if (!pressedKeys.contains(currentKey)) {
					for (int i = 0; i < 9; i++) { // Go through each item in the config
						boolean keyTrigger = false;
						String currentCode = keyControls[i][0];
						// Check if the current activeKey can be found at the start of one of the config numbers
						if (currentCode.startsWith(currentKey.toString())) {
							// If one of the config options does start with it, check if its a chord by seeing the length of the string
							if (currentCode.length() > 2) {
								// If the code is a chord then it will run the function to check for chords
								if (isCompleteChord(currentCode, currentKey)) {
									// When this code runs a full chord has been played, and therefore the program can execute the appropriate input
									keyTrigger = true;
								}
							} else {
								keyTrigger = true;
							}
						}
						// If a key was triggered and validated then the keyHandling function will activate it (This works for mouse events too!)
						if (keyTrigger) {
							int keyCode = Integer.valueOf(keyControls[i][1]);
							keyHandling(keyCode, true);
							pressedKeys.add(currentKey);
						}
					}
				}
			}

		}


		// Function to handle key / mouse presses and releases
		public static void keyHandling(int keyCode, boolean isPress) {
			// If the code is above 1000 it is likely a mouse press (I havent found any key presses remotely this high yet)
			// After determining mouse or key it will use isPress to determine to either raise or lower the key
			if (keyCode >= 1000) {
				if (isPress) {
					robot.mousePress(keyCode);			
				} else {
					robot.mouseRelease(keyCode);
				}
			} 
			else {
				if (isPress) {
					robot.keyPress(keyCode);
				} else {
					robot.keyRelease(keyCode);
				}
			}
		}


		// Function to determine if the detected potential chord is complete to validate the key press
		private static boolean isCompleteChord(String chord, Integer startKey) {
			String startKeyStr = startKey.toString();
			String remainingChord = chord.substring(startKeyStr.length());

			// Convert remaining part of the chord to individual keys contained in a new arraylist
			List<Integer> remainingKeys = new ArrayList<>();
			for (int i = 0; i < remainingChord.length(); i += 2) {
				remainingKeys.add(Integer.parseInt(remainingChord.substring(i, i + 2)));
			}

			// Check if all remaining keys are currently active
			return activeKeys.containsAll(remainingKeys);
		}

		// A function responsible for reading the config file holding keymaps
		public static Integer[][] getKeyConfig() {
			// The program will try to open the associated file and will throw an error if it can't be found
			try {
				String userDirectory = System.getProperty("user.dir");
				File myObj = new File(userDirectory + "\\src\\data\\config.txt");
				Scanner myReader = new Scanner(myObj);
				int lineNumber = 0; // Keep track of the line number to determine what row of the array it's added to
				while (myReader.hasNextLine()) {
					// Reads each line of data, splits it based on the location of the comma
					// Saves the first value in the first column and the second value in the second column
					String data = myReader.nextLine();
					String regex = "[,]";
					String[] lineData = data.split(regex);
					keyControls[lineNumber][0] = lineData[0];
					keyControls[lineNumber][1] = lineData[1];
					// Increments the line number to move onto the next segment of the array
					lineNumber++;
				}
				myReader.close();
			} 
			catch (FileNotFoundException e) {
				System.err.println("------------------------------------------------------------\nFile not found\n------------------------------------------------------------");
				e.printStackTrace();
			}


			return null;

		}


	}
}
