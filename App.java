package dissonance_midihandling;

import javax.sound.midi.MidiUnavailableException;

public class App {
    public static void main(String[] args) {
        try {
            MidiKeyHandler handler = new MidiKeyHandler();
            KeyMonitor monitor = new KeyMonitor(handler);
            monitor.start();
        } catch (MidiUnavailableException e) {
            System.out.println("Could not initialize MIDI devices.");
        }
    }
}
