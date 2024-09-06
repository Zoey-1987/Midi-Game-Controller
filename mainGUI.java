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
import java.util.List;

public class mainGUI extends JFrame {
	
    private List<Rectangle> rectangles = new ArrayList<>();
    private int rectWidth = 18;
    private int rectHeight = 120;
    private int x = 0;
    private int y = 0;
    private Image keyboardImage;

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
		String userDirectory = System.getProperty("user.dir");
		setIconImage(Toolkit.getDefaultToolkit().getImage(userDirectory + "\\src\\data\\ZoeyCopeNoBG.png"));
		keyboardImage = new ImageIcon(userDirectory + "\\src\\data\\keyboard.png").getImage();
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
    }
	
	class DrawingPanel extends JPanel {
		public DrawingPanel() {
			setPreferredSize(new Dimension(648,120));
			// A key listener temporarily set to just detect a space press so I can determine the size of the gaps between keys
			// When the space key is pressed it will draw over the next key on the keyboard
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						// Add a new rectangle to the list
						rectangles.add(new Rectangle(x, y, rectWidth, rectHeight));
						// Update coordinates for the next rectangle
						x += rectWidth + 0;
						if (x + rectWidth > getWidth()) {
							x = 0;
							y += rectHeight + 0;
						}
						repaint();  // Repaint the panel to draw the new rectangle
					}
				}
			});
			setFocusable(true);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(keyboardImage, 0, 0, getWidth(), getHeight(), this);

            // Draw all rectangles in the list
            g.setColor(Color.BLUE);
            for (Rectangle rect : rectangles) {
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
        }

	}
}
