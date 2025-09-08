package dissonance_midihandling;

import java.util.List;

public class KeyMonitor extends Thread {
    private final MidiKeyHandler handler;

    public KeyMonitor(MidiKeyHandler handler) {
        this.handler = handler;
    }

    public void run() {
        while (true) {
            List<Integer> keys = handler.getPressedKeys();
            if (!keys.isEmpty()) {
                System.out.println("Pressed keys: " + keys);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Key monitor interrupted.");
                break;
            }
        }
    }
}
