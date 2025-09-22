package dissonance_gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/* -------------------------------------------------------
 * 	This is essentially the new base class for images
 * 	within my GUI, as I want to be able to change the
 *  resolution of the program without multiple images
 ------------------------------------------------------- */

public class ScaledLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	private Image image;

    public ScaledLabel(ImageIcon icon) {
        this.image = icon.getImage();
    }

    public void setScaledIcon(ImageIcon icon) {
        this.image = icon.getImage();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

