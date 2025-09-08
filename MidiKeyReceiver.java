package dissonance_midihandling;

import java.util.List;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class MidiKeyReceiver implements Receiver {
    private final List<Integer> pressedKeys;

    public MidiKeyReceiver(List<Integer> pressedKeys) {
        this.pressedKeys = pressedKeys;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        byte[] data = message.getMessage();
        int key = data[1] & 0xFF;

        if (data[2] == 100) {
            if (!pressedKeys.contains(key)) pressedKeys.add(key);
        } else if (data[2] == 64) {
            pressedKeys.remove(Integer.valueOf(key));
        }
    }

    @Override
    public void close() {}
}
