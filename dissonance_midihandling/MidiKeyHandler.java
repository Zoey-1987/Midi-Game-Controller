package dissonance_midihandling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

/* -------------------------------------------------------
 * 	Class that waits for a key change to be detected
 * 	using the listener methods, and turns the pressed
 * 	keys into the more usable format of an ArrayList
 ------------------------------------------------------- */

public class MidiKeyHandler {
	
	public interface MidiKeyListener {
	    void keysChanged(List<Integer> pressedKeys);
	}
	
    private final List<Integer> pressedKeys = new CopyOnWriteArrayList<>();
    private final List<MidiKeyListener> listeners = new ArrayList<>();
    
    public MidiKeyHandler() throws MidiUnavailableException {
        MidiKeyReceiver receiver = new MidiKeyReceiver(pressedKeys, this);
        Transmitter transmitter = MidiSystem.getTransmitter();
        transmitter.setReceiver(receiver);
    }

    public List<Integer> getPressedKeys() {
        return new ArrayList<>(pressedKeys);
    }
    
    public void addListener(MidiKeyListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        List<Integer> snapshot = getPressedKeys();
        for (MidiKeyListener l : listeners) {
            l.keysChanged(snapshot);
        }
    }
    
    public void keyStateChanged() {
    	notifyListeners();
    }
}
