package dissonance_midihandling;

/* -------------------------------------------------------
 * 	A class to store all the black and white keys on a
 * 	keyboard along with methods to retrieve information
 * 	
 * 	This should work for different sized keyboards,
 * 	however only the keys within this range would be
 * 	avaliable. I'd like to look into changing this but
 *  I don't have different keyboards to test with ;-;
 ------------------------------------------------------- */

public class MidiKeyboardLayout {
	
	private static final int[] whiteKeys = {
			36, 38, 40, 41, 43, 45, 47, 
			48, 50, 52, 53, 55, 57, 59, 
			60, 62, 64, 65, 67, 69, 71, 
			72, 74, 76, 77, 79, 81, 83, 
			84, 86, 88, 89, 91, 93, 95, 96
	};
	
	private static final double[] blackKeys = {
			37, 39, 39.5, 42, 44, 46, 46.5, 
			49, 51, 51.5, 54, 56, 58, 58.5, 
			61, 63, 63.5, 66, 68, 70, 70.5, 
			73, 75, 75.5, 78, 80, 82, 82.5, 
			85, 87, 87.5, 90, 92, 94};
	
	public static int getWhiteArrayLength() {
		return whiteKeys.length;
	}
	
	public static int getBlackArrayLength() {
		return whiteKeys.length;
	}
	
	public static int getWhiteKey(int index) {
		return whiteKeys[index];
	}
	
	public static double getBlackKey(int index) {
		return blackKeys[index];
	}

	public static boolean isWhite(int midiKey) {
		for (int key : whiteKeys) {
			if (key == midiKey) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isBlack(int midiKey) {
		for (double key : blackKeys) {
			if (key == midiKey) {
				return true;
			}
		}
		return false;
	}
	
	public static int whiteIndex(int midiKey) {
		for (int i = 0; i < whiteKeys.length; i++) {
			if (whiteKeys[i] == midiKey) {
				return i;
			}
		}
		return -1;
	}
	
	public static int blackIndex(int midiKey) {
		for (int i = 0; i < blackKeys.length; i++) {
			if (blackKeys[i] == midiKey) {
				return i;
			}
		}
		return -1;
	}

}
