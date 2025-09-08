package dissonance_midihandling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

public class MidiKeyHandler {
    private final List<Integer> pressedKeys = new CopyOnWriteArrayList<>();
    private Transmitter transmitter;
    
    public MidiKeyHandler() throws MidiUnavailableException {
        MidiKeyReceiver receiver = new MidiKeyReceiver(pressedKeys);
        transmitter = MidiSystem.getTransmitter();
        transmitter.setReceiver(receiver);
    }

    public List<Integer> getPressedKeys() {
        return new ArrayList<>(pressedKeys); // Return a copy to avoid thread issues
    }
}
