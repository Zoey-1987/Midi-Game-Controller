package dissonance_gui;

import java.awt.*;
import java.awt.event.*;


import javax.swing.*;

/* -------------------------------------------------------
 * 	A few labels in the GUI need to be rotatable such as
 * 	the dial, but this can also be used to flip things
 *  vertically, such as the levers hence the "isOn"
 ------------------------------------------------------- */

public class RotatableLabel extends ScaledLabel {

	private static final long serialVersionUID = 1L;
	private double angle;
	private Timer animationTimer;
	private boolean isOn = false;

	public RotatableLabel(ImageIcon Image, double angleDegrees) {
		super(Image);
		this.angle = Math.toRadians(angleDegrees);
	}
	
	
	public boolean isOn() {
		return isOn;
	}
	
	
	public void toggle() {
		isOn = !isOn;
	}
	

	public double getAngle() {
		return angle;
	}


	public void rotateTo(double targetAngleDegrees, int durationMS) {
		if (animationTimer != null && animationTimer.isRunning()) {
			animationTimer.stop();
		}
		
		if (durationMS <= 0) {
			this.angle = Math.toRadians(targetAngleDegrees);
			repaint();
			return;
		}
		
		double startAngle = this.angle;
		double endAngle = Math.toRadians(targetAngleDegrees);
		int frames = 60;
		int delay = durationMS / frames;
		
		final int[] step = {0};
		animationTimer = new Timer(delay, null);
		
		animationTimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				step[0]++;
				double progress = (double) step[0] / frames;
				
				angle = startAngle + (endAngle - startAngle) * progress;
				repaint();
				
				if (step[0] >= frames) {
					animationTimer.stop();
				}
			}
		});
		
		
		animationTimer.start();
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int w = getWidth();
		int h = getHeight();
		
		g2.rotate(angle, w / 2.0, h / 2.0);
		
		super.paintComponent(g2);
		g2.dispose();
	}

}
