package dissonance_midihandling;

import java.util.List;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/* -------------------------------------------------------
 * 	This class uses the default receiver and overrides
 * 	the send method to keep other classes up to date
 * 	with what keys are currently pressed
 ------------------------------------------------------- */

public class MidiKeyReceiver implements Receiver {
    private final List<Integer> pressedKeys;
    private final MidiKeyHandler handler;

    public MidiKeyReceiver(List<Integer> pressedKeys, MidiKeyHandler handler) {
        this.pressedKeys = pressedKeys;
        this.handler = handler;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        byte[] data = message.getMessage();
        int key = data[1] & 0xFF;

        if (data[2] == 100) {
            if (!pressedKeys.contains(key)) pressedKeys.add(key);
            pressedKeys.sort(null);
        } else if (data[2] == 64) {
            pressedKeys.remove(Integer.valueOf(key));
        }
        handler.notifyListeners();
    }

    @Override
    public void close() {}
}
