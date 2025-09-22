package dissonance_midihandling;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/* -------------------------------------------------------
 * 	A thread to check if any keybinds line up with any
 * 	currently pressed keys and to use the Robot class
 *  to mimic the relevant keystrokes, this is essentially
 *  where the midi conversion all takes place
 ------------------------------------------------------- */

public class KeyMonitor extends Thread {
    private final MidiKeyHandler handler;
	private Robot robot;
	private String userDirectory = System.getProperty("user.dir");

    public KeyMonitor(MidiKeyHandler handler) {
        this.handler = handler;
    }
    
    
    public void run() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
    	LoadKeybinds load = new LoadKeybinds(userDirectory + "\\src\\Data\\config.txt");
		List<Keybind> keybinds = new ArrayList<>(load.readKeybinds());
        while (true) {
            List<Integer> keys = handler.getPressedKeys();
            checkBindings(keybinds, keys);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Key monitor interrupted.");
                break;
            }
        }
    }
    
    public void checkBindings(List<Keybind> keybinds, List<Integer> keys) {
    	for (Keybind k : keybinds) {
    		boolean shouldBeActive = isSubset(k.getMidiKeys(), keys);
    		Integer robotCode =  k.getRobotCode();
    		// The point in the should be active section is to prevent repeated presses on hold, this way it doesn't keep adding them as new key-presses
    		if (shouldBeActive && !k.isActive()) {
    			k.setActive(true);
    			if (robotCode >= 1000) {
    				robot.mousePress(robotCode);
    			}	 
    			else {
    				robot.keyPress(robotCode);
    			}
    		}
    		
    		else if (!shouldBeActive && k.isActive()) {
    			k.setActive(false);
    			if (robotCode >= 1000) {
    				robot.mouseRelease(robotCode);
    			}	 
    			else {
    				robot.keyRelease(robotCode);
    			}
    		}

    	}
    }
    
    private boolean isSubset(List<Integer> midikeys, List<Integer> keys) {
        int keybindIndex = 0; // Index for keybinds
        int pressedKeyIndex = 0; // Index for pressedKeys

        while (keybindIndex < midikeys.size() && pressedKeyIndex < keys.size()) {
            int midikey = midikeys.get(keybindIndex);
            int pressedKey = keys.get(pressedKeyIndex);

            if (midikey == pressedKey) {
            	keybindIndex++; 
            	pressedKeyIndex++; 
            } else if (pressedKey < midikey) {
            	pressedKeyIndex++; // Skip smaller pressed key - the list is sorted so smaller keys are irrelevant 
            } else {
                return false;
            }
        }
        // If all keys in the bind are found this will return true
        return keybindIndex == midikeys.size(); 
    }
    
}
