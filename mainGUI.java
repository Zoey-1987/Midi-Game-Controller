import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JToolBar;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;
import java.awt.event.MouseMotionAdapter;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class mainGUI extends JFrame {
	// A list of each number on a 61 keyed keyboard that is linked to a white key
	private int[] whiteKeys = {36, 38, 40, 41, 43, 45, 47, 48, 50, 52, 53, 55, 57, 59, 60, 62, 64, 65, 67, 69, 71, 72, 74, 76, 77, 79, 81, 83, 84, 86, 88, 89, 91, 93, 95, 96};
	// Gets the user directory to allow for images that don't have an absolute path
	String userDirectory = System.getProperty("user.dir");
	// Variables for moving the window
	int xCursor;
	int yCursor;

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainGUI frame = new mainGUI();
					frame.setVisible(true);
					MidiHandling.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public mainGUI() {
		setUndecorated(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(userDirectory + "\\src\\data\\SkongCope.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 550);
		mainPanel = new JPanel();
		mainPanel.setAlignmentX(0.0f);
		mainPanel.setBackground(Color.DARK_GRAY);
		mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

		setContentPane(mainPanel);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[] {5, 30, 30, 30, 73, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 0};
		gbl_mainPanel.rowHeights = new int[]{0, 350, 158, 0, 0};
		gbl_mainPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_mainPanel.rowWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		mainPanel.setLayout(gbl_mainPanel);

		JButton btnMini = new JButton("\u2014");

		/* 
		 * This section of code is dedicated to the minimize button of the GUI
		 */

		// Listeners to activate when the mouse hovers over or exits the minimize button
		btnMini.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnMini.setBackground(Color.GRAY);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnMini.setBackground(Color.DARK_GRAY);
			}
			// Minimize the GUI when clicked
			@Override
			public void mouseClicked(MouseEvent e) {
				setState(JFrame.ICONIFIED);
			}
		});

		// Gets the location of the cursor on the window
		JPanel pnlToolBar = new JPanel();
		pnlToolBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xCursor = e.getX();
				yCursor = e.getY();
			}
		});

		// Get the location of the mouse on the screen and subtracts the window location from it for accurate movement
		pnlToolBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x-xCursor, y-yCursor);
			}
		});

		pnlToolBar.setBackground(Color.DARK_GRAY);
		GridBagConstraints gbc_pnlToolBar = new GridBagConstraints();
		gbc_pnlToolBar.gridwidth = 17;
		gbc_pnlToolBar.insets = new Insets(0, 0, 5, 5);
		gbc_pnlToolBar.fill = GridBagConstraints.BOTH;
		gbc_pnlToolBar.gridx = 1;
		gbc_pnlToolBar.gridy = 0;
		mainPanel.add(pnlToolBar, gbc_pnlToolBar);

		// Various features of the minimize button

		btnMini.setFocusPainted(false);
		btnMini.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnMini.setBackground(Color.DARK_GRAY);
		btnMini.setForeground(Color.WHITE);
		GridBagConstraints gbc_btnMini = new GridBagConstraints();
		gbc_btnMini.insets = new Insets(0, 0, 5, 5);
		gbc_btnMini.gridx = 18;
		gbc_btnMini.gridy = 0;
		mainPanel.add(btnMini, gbc_btnMini);

		JButton btnMaxi = new JButton("\uD83D\uDDD6");

		/* 
		 * This section of code is dedicated to the maximize button of the GUI
		 */

		// Listeners to activate when the mouse hovers over or exits the maximize button
		btnMaxi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnMaxi.setBackground(Color.GRAY);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnMaxi.setBackground(Color.DARK_GRAY);
			}
			// Listens for mouse press on maximize button
			@Override
			public void mouseClicked(MouseEvent e) {
				if (btnMaxi.getText() == "🗖") {
					btnMaxi.setText("🗗");
					setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
				else {
					btnMaxi.setText("🗖");
					setExtendedState(JFrame.NORMAL);
				}
			}
		});

		// Various features of the maximize button

		btnMaxi.setFocusPainted(false);
		btnMaxi.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnMaxi.setBackground(Color.DARK_GRAY);
		btnMaxi.setForeground(Color.WHITE);
		GridBagConstraints gbc_btnMaxi = new GridBagConstraints();
		gbc_btnMaxi.insets = new Insets(0, 0, 5, 5);
		gbc_btnMaxi.gridx = 19;
		gbc_btnMaxi.gridy = 0;
		mainPanel.add(btnMaxi, gbc_btnMaxi);

		JButton btnExit = new JButton("\u2716");

		/* 
		 * This section of code is dedicated to the exit button of the GUI
		 */

		// Listeners to activate when the mouse hovers over or exits the exit button
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnExit.setBackground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnExit.setBackground(Color.DARK_GRAY);
			}
			// Listener for clicking the exit button
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				System.exit(0);
			}
		});

		// Various features of the exit button

		btnExit.setFocusPainted(false);
		btnExit.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnExit.setBackground(Color.DARK_GRAY);
		btnExit.setForeground(Color.WHITE);
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.insets = new Insets(0, 0, 5, 5);
		gbc_btnExit.gridx = 20;
		gbc_btnExit.gridy = 0;
		mainPanel.add(btnExit, gbc_btnExit);

		JPanel pnlMain = new JPanel();
		pnlMain.setBackground(Color.LIGHT_GRAY);
		pnlMain.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(0, 0, 0)));
		GridBagConstraints gbc_pnlMain = new GridBagConstraints();
		gbc_pnlMain.gridwidth = 17;
		gbc_pnlMain.insets = new Insets(0, 0, 5, 5);
		gbc_pnlMain.fill = GridBagConstraints.BOTH;
		gbc_pnlMain.gridx = 1;
		gbc_pnlMain.gridy = 1;
		mainPanel.add(pnlMain, gbc_pnlMain);

		JPanel pnlOptions = new JPanel();
		pnlOptions.setBackground(Color.LIGHT_GRAY);
		pnlOptions.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(0, 0, 0)));
		GridBagConstraints gbc_pnlOptions = new GridBagConstraints();
		gbc_pnlOptions.gridwidth = 3;
		gbc_pnlOptions.insets = new Insets(0, 0, 5, 5);
		gbc_pnlOptions.fill = GridBagConstraints.BOTH;
		gbc_pnlOptions.gridx = 18;
		gbc_pnlOptions.gridy = 1;
		mainPanel.add(pnlOptions, gbc_pnlOptions);

		// I need to add the code to simply draw rectangles here to test functionality + practicality
		// Inner class for custom drawing and key handling
		DrawingPanel pnlKeyboard = new DrawingPanel();
		pnlKeyboard.setBackground(Color.LIGHT_GRAY);
		pnlKeyboard.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(0, 0, 0)));
		GridBagConstraints gbc_pnlKeyboard = new GridBagConstraints();
		gbc_pnlKeyboard.gridwidth = 20;
		gbc_pnlKeyboard.insets = new Insets(0, 0, 5, 5);
		gbc_pnlKeyboard.gridx = 1;
		gbc_pnlKeyboard.gridy = 2;
		mainPanel.add(pnlKeyboard, gbc_pnlKeyboard);

		setVisible(true);

		// Requesting focus because otherwise it wont be able to accept key events (I think)
		pnlKeyboard.requestFocusInWindow();
		DrawingThread thread = new DrawingThread(pnlKeyboard);
		thread.start();
	}

	class DrawingPanel extends JPanel {
		private List<Rectangle> whiteRectangles = new ArrayList<>();  // Initialize the list of white rectangles
		private List<Rectangle> blackRectangles = new ArrayList<>();  // Initialize the list of black rectangles
		private Image keyboardImage;

		public DrawingPanel() {
			setPreferredSize(new Dimension(648, 120));
			setFocusable(true);
		}

		// Add a white rectangle to the list
		public void addWhiteRectangle(Rectangle rect) {
			whiteRectangles.add(rect);
		}

		// Add a black rectangle to the list
		public void addBlackRectangle(Rectangle rect) {
			blackRectangles.add(rect);
		}

		// Clear all rectangles (to update for new keys)
		public void clearRectangles() {
			whiteRectangles.clear();
			blackRectangles.clear();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			keyboardImage = new ImageIcon(userDirectory + "\\src\\data\\keyboard.png").getImage();
			g.drawImage(keyboardImage, 0, 0, getWidth(), getHeight(), this);

			// Draw all rectangles in the list
			for (Rectangle rect : whiteRectangles) {
				g.setColor(Color.LIGHT_GRAY);
				// Draw the rectangle

				g.fillRect(rect.x, rect.y, rect.width, rect.height);
			}
			for (Rectangle rect : blackRectangles) {
				g.setColor(Color.BLACK);
				// Draw the rectangle
				g.fillRect(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}


	class DrawingThread extends Thread {
		private DrawingPanel panel;
		private boolean hasLeft;
		private boolean hasRight;

		public DrawingThread(DrawingPanel panel) {
			this.panel = panel;  // Pass the DrawingPanel to the thread
		}

		public boolean getLeft() {
			return this.hasLeft;
		}

		public boolean getRight() {
			return this.hasRight;
		}

		public void setLeft(boolean left) {
			this.hasLeft = left;
		}

		public void setRight(boolean right) {
			this.hasRight = right;
		}

		public void run() {
			// A list of each number on a 61 keyed keyboard linked to a black key
			// This list is a double and uses decimels to essentially insert gaps into the rectangles drawing as black keys aren't ordered as simply as white
			// I'm sure this isn't the most efficient solution, but it works soooo
			double[] blackKeys = {37, 39, 39.5, 42, 44, 46, 46.5, 49, 51, 51.5, 54, 56, 58, 58.5, 61, 63, 63.5, 66, 68, 70, 70.5, 73, 75, 75.5, 78, 80, 82, 82.5, 85, 87, 87.5, 90, 92, 94};

			while (true) {  // Continuous loop to check for key presses
				List<Integer> activeKeys = MidiHandling.DisplayReceiver.getActiveKeys();

				// Clear existing rectangles
				panel.clearRectangles();

				for (Integer currentKey : activeKeys) { // For each currently pressed key
					int whiteKeyIndex = Arrays.binarySearch(whiteKeys, currentKey);
					int blackKeyIndex = Arrays.binarySearch(blackKeys, currentKey);
					int previousKey;
					int nextKey;
					if (whiteKeyIndex >= 0) {
						// If its the first key in the list it won't have a previous key
						if (currentKey == 36) {
							previousKey = -1;
						} else {
							previousKey = whiteKeys[whiteKeyIndex - 1];
						}
						// If its the last key in the list it won't have a next key
						if (currentKey == 96) {
							nextKey = -1;
						} else {
							nextKey = whiteKeys[whiteKeyIndex + 1];
						}
						// Draw a white key
						panel.addWhiteRectangle(new Rectangle(whiteKeyIndex * 18, 0, 18, 120));
						if (previousKey == currentKey - 2) {
							panel.addBlackRectangle(new Rectangle((whiteKeyIndex - 1) * 18 + 12, 0, 12, 69));
						}
						if (nextKey == currentKey + 2) {
							panel.addBlackRectangle(new Rectangle((whiteKeyIndex) * 18 + 12, 0, 12, 69));
						}
					} else if (blackKeyIndex >= 0) {
						// Draw a black key slightly offset
						panel.addBlackRectangle(new Rectangle(blackKeyIndex * 18 + 12, 0, 12, 73));
					}
				}
				panel.repaint();  // Repaint the panel to reflect new active keys

				try {
					Thread.sleep(100);  // Update every 100 milliseconds
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
