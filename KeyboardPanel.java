package dissonance_gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import dissonance_midihandling.MidiKeyboardLayout;

/* -------------------------------------------------------
 * 	An extension of the panel class that handles anything
 * 	related to the midi keyboard visualiser on the GUI
 ------------------------------------------------------- */

public class KeyboardPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private List<Integer> pressed = new ArrayList<>();
	Color BrownOne = new Color(92,51,19);
	Color BrownTwo = new Color(112,71,28);
	Color BrownThree = new Color(197,134,59);
	
	Color BlackOne = new Color(37, 25, 9);
	Color BlackTwo = new Color(53, 33, 14);
	Color BlackThree = new Color(22, 15, 5);

    int sizeRatio;
    
    private final int defaultWhiteKeyWidth = 20;
    private final int defaultWhiteKeyHeight = 120;
    private final int defaultWhiteKeyGap = 2;
    
    private final int defaultBlackWidth = 14;
    private final int defaultBlackHeight = 62;
    private final int defaultBlackKeyGap = 8;

    public void setPressedKeys(List<Integer> pressedKeys) {
        this.pressed = pressedKeys;
        repaint();
    }

    /* --------------------------------------------------------
     *   This override is to help display the midi keys
     *   being pressed in real time. I will admit that some
     *   parts of this are quite inefficient but there are
     *   other features I need to implement, so I'll do my
     *   best to explain what's going on <3
     -------------------------------------------------------- */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        sizeRatio = getHeight() / 120; // The default art has white keys 120px tall
        
        int whiteKeyWidth = defaultWhiteKeyWidth * sizeRatio;
        int whiteKeyHeight = defaultWhiteKeyHeight * sizeRatio;
        int whiteKeyGap = defaultWhiteKeyGap * sizeRatio;
        
        int blackKeyWidth = defaultBlackWidth * sizeRatio;
        int blackKeyHeight = defaultBlackHeight * sizeRatio;
        int blackKeyGap = defaultBlackKeyGap * sizeRatio;
        
        // Because of the way keyboards / pianos are laid out I need to determine whether or not a white key is partially overlapped by a black key
        int previousKey = 0; 
        int nextKey = 0;
        

        for (Integer key : pressed) {
        	if (MidiKeyboardLayout.isWhite(key)) {
        		int whiteKeyIndex = MidiKeyboardLayout.whiteIndex(key);
        		paintWhiteKey(g2, whiteKeyIndex*(whiteKeyWidth+whiteKeyGap), whiteKeyWidth, whiteKeyHeight);
        		
        		// The first key in the index won't have a black key on the left as a guarantee
        		if(whiteKeyIndex == 0) {
        			previousKey = -1;
        		} else {
					previousKey = MidiKeyboardLayout.getWhiteKey(whiteKeyIndex-1);
				}
				// If its the last key in the list it won't have a next key
				if (whiteKeyIndex == MidiKeyboardLayout.getWhiteArrayLength()) {
					nextKey = -1;
				} else {
					nextKey = MidiKeyboardLayout.getWhiteKey(whiteKeyIndex+1);
				}
				// If the gap between two keys in the array is 2, then the two keys must have a black key in between them
				if (previousKey == key-2) {
					paintDefaultBlackKey(g2, MidiKeyboardLayout.blackIndex(key-1)*(blackKeyWidth+blackKeyGap) + (14*sizeRatio), blackKeyWidth, blackKeyHeight);
				}
				if (nextKey == key+2) {
					paintDefaultBlackKey(g2, MidiKeyboardLayout.blackIndex(key+1)*(blackKeyWidth+blackKeyGap) + (14*sizeRatio), blackKeyWidth, blackKeyHeight);
				}
        	}
        }
        
        // I put all the black keys in a separate for loop just so that they are always painted after, otherwise the white keys can overwrite them
        // This is super inefficient it works perfectly, so maybe I'll come back to it but I've got other stuff to do
        for (Integer key : pressed) {
        	if (MidiKeyboardLayout.isBlack(key)) {
        		paintBlackKey(g2, MidiKeyboardLayout.blackIndex(key)*(blackKeyWidth+blackKeyGap) + (14*sizeRatio), blackKeyWidth, blackKeyHeight);
        	}
        }
    }
    
    // I wanted to have the keys actually have some detail unlike my last program, so that means drawing a lot of rectangles so I just threw some functions together
    // I've removed as many magic numbers as possible but I didn't even know how to explain these doubles, it just reflects the art I've made so they have to be this size
    // This is the same for the other two paint functions <3
    public void paintWhiteKey(Graphics2D g, int x, int width, int height) {
    	int barOne = (int)Math.round(height * 0.016666666666);
    	int barTwo = (int)Math.round(height * 0.025);
    	int barThree = (int)Math.round(height * 0.916666666666);
    	int barFour = (int)Math.round(height * 0.025);
    	int barFive = (int)Math.round(height * 0.016666666666);
    	int totalBar = 0;

    	
    	g.setColor(BrownOne);
    	g.fillRect(x, 0, width, barOne);
    	totalBar += barOne;
    	g.setColor(BrownTwo);
    	g.fillRect(x, totalBar, width, barTwo);
    	totalBar += barTwo;
    	g.setColor(BrownThree);
    	g.fillRect(x, totalBar, width, barThree);
    	totalBar += barThree;
    	g.setColor(BrownOne);
    	g.fillRect(x, totalBar, width, barFour);
    	totalBar += barFour;
    	g.setColor(BrownTwo);
    	g.fillRect(x, totalBar, width, barFive);
    }
    
    
    public void paintBlackKey(Graphics2D g, int x, int width, int height) {
        int blackKeyBorder = 2 * sizeRatio;
    	int barOne = (int)Math.round(height * 0.90322580645 );
    	int barTwo = (int)Math.round(height * 0.03225806451);
    	int barThree = (int)Math.round(height * 0.04838709677);
    	int totalBar = 0;

    	
    	g.setColor(Color.BLACK);
    	g.fillRect(x, 0, width, height);

    	g.setColor(BlackTwo);
    	g.fillRect(x + blackKeyBorder, 0, width - 2*blackKeyBorder, barOne);
    	totalBar += barOne;
    	
    	g.setColor(BlackThree);
    	g.fillRect(x + blackKeyBorder, totalBar, width - 2*blackKeyBorder, barTwo);
    	totalBar += barTwo;
    	
    	g.setColor(BlackThree);
    	g.fillRect(x + blackKeyBorder, totalBar, width - 2*blackKeyBorder, barThree);
    	totalBar += barThree;
    }
    
    
    public void paintDefaultBlackKey(Graphics2D g, int x, int width, int height) {
        int blackKeyBorder = 2 * sizeRatio;
    	int barOne = (int)Math.round(height * 0.82258064516);
    	int barTwo = (int)Math.round(height * 0.03225806451 );
    	int barThree = (int)Math.round(height * 0.08064516129);
    	int totalBar = 0;

    	
    	g.setColor(Color.BLACK);
    	g.fillRect(x, 0, width, height);

    	g.setColor(new Color(69, 57, 34));
    	g.fillRect(x + blackKeyBorder, 0, width - 2*blackKeyBorder, barOne);
    	totalBar += barOne;
    	
    	g.setColor(new Color(82,65,42));
    	g.fillRect(x + blackKeyBorder, totalBar, width - 2*blackKeyBorder, barTwo);
    	totalBar += barTwo;
    	
    	g.setColor(new Color(53, 44, 25));
    	g.fillRect(x + blackKeyBorder, totalBar, width - 2*blackKeyBorder, barThree);
    	totalBar += barThree;
    }
}
