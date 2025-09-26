package dissonance_gui;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import dissonance_gui.BackgroundPanel;
import dissonance_gui.RotatableLabel;
import dissonance_midihandling.KeyMonitor;
import dissonance_midihandling.MidiKeyHandler;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComponent;

import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;


public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	int xCursor;
	int yCursor;
	Color toolbarGreen = new Color(104, 100, 49);
	Color debugRed = new Color(255,0,0,60);
	private String userDirectory = System.getProperty("user.dir");

	private boolean debug = true;
	private UIFactory factory;

	private BackgroundPanel backgroundImagePanel;
	private JPanel frontPanel;
	private JPanel pnlToolBar;

	private String configFileName = (userDirectory + "\\src\\data\\1.txt");
	
	public String getConfigFileName() {
		return configFileName;
	}

	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	private boolean isRunning = false;
	KeyMonitor monitor;


	private List<JComponent> allComponents = new ArrayList<>();


	public void resizeItems() {
		for (JComponent comp : allComponents) {
			int componentX = comp.getX();
			int componentY = comp.getY();
			int componentWidth = comp.getWidth();
			int componentHeight = comp.getHeight();
			comp.setBounds((int)scaleX(componentX), (int)scaleY(componentY), (int)scaleX(componentWidth), (int)scaleY(componentHeight));
		}
	}

	private double xRatio, yRatio;
	private int xResolution = 1280;
	private int yResolution = 720;

	/** Update ratios based on current window size */
	private void updateScaling() {
		xRatio = (double) getWidth() / xResolution;
		yRatio = (double) getHeight() / yResolution;
		resizeItems();
		xResolution = getWidth();
		yResolution = getHeight();
		scaleToolbar(pnlToolBar, toolbarWidth);
		revalidate();
		repaint();
	}

	/** Scale a coordinate or size based on current window size */
	private double scaleX(int originalX) {
		return (originalX * xRatio);
	}

	private double scaleY(int originalY) {
		return (originalY * yRatio);
	}

	private int toolbarX = 0, toolbarY = 0 , toolbarWidth = xResolution, toolbarHeight = 26;


	private void scaleToolbar(JPanel toolbar, int originalWidth) {
		toolbar.setBounds((int)scaleX(toolbarX), (int)scaleY(toolbarY), (int)scaleX(originalWidth), toolbarHeight);
		toolbarWidth = toolbar.getWidth();
	}

	/* -------------------------------
	 * Run the GUI.
	 ------------------------------- */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI frame = new MainGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/* -------------------------------
	 * Create the frame.
	 ------------------------------- */
	public MainGUI() {

		setupFrame();
		factory = new UIFactory();
		setupToolbar(frontPanel);
		setupControlButtons();
		updateScaling();
		addAnalogDisplays();
		setupDial();
		addLevers();
		addGears();
		setupEventButtons();
		setupKeyboardPanel();
	}

	private void setupFrame() {
		Image icon = new ImageIcon(userDirectory + "\\src\\data\\GUI\\MainGUI.png").getImage();
		backgroundImagePanel = new BackgroundPanel(icon);
		frontPanel = new JPanel();
		allComponents.add(frontPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setBounds(0, 0, xResolution, yResolution);
		setLocationRelativeTo(null);
		setContentPane(backgroundImagePanel);
		backgroundImagePanel.setLayout(new BorderLayout(0, 0));

		frontPanel.setBorder(null);
		backgroundImagePanel.add(frontPanel, BorderLayout.CENTER);
		frontPanel.setPreferredSize(null);
		frontPanel.setBounds(new Rectangle(0, 0, xResolution, yResolution));

		frontPanel.setLayout(null);
	}


	private void setupToolbar(JPanel parentPanel) {
		/* --------------------------------------------
		 * 	Main Toolbar
	     -------------------------------------------- */
		pnlToolBar = new JPanel();
		pnlToolBar.setBackground(toolbarGreen);
		pnlToolBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		FlowLayout fl_pnlToolBar = (FlowLayout) pnlToolBar.getLayout();
		fl_pnlToolBar.setVgap(0);
		fl_pnlToolBar.setHgap(0);
		pnlToolBar.setBorder(null);
		int baseX = 0, baseY = 0, baseW = 1280, baseH = 26;
		pnlToolBar.setBounds((int)scaleX(baseX), (int)scaleY(baseY), (int)scaleX(baseW), (int)scaleY(baseH));

		parentPanel.add(pnlToolBar);


		pnlToolBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xCursor = e.getX();
				yCursor = e.getY();
			}
		});

		pnlToolBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - xCursor, y - yCursor);
			}
		});
	}


	private void setupControlButtons() {
		int btnWindowCommandX = 50, btnWindowCommandY = 26;

		/* --------------------------------------------
		 * 	Minimize Button
	     -------------------------------------------- */

		JButton btnMinimize = factory.createButton(1280, 0, btnWindowCommandX, btnWindowCommandY, "\u2014", toolbarGreen, false);
		pnlToolBar.add(btnMinimize);


		btnMinimize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) { 
				btnMinimize.setBackground(Color.GRAY); 
			}
			@Override
			public void mouseExited(MouseEvent e) { 
				btnMinimize.setBackground(toolbarGreen); 
			}
			@Override
			public void mouseClicked(MouseEvent e) { 
				setState(JFrame.ICONIFIED); 
			}
		});


		/* --------------------------------------------
		 * 	Maximize Button
	     -------------------------------------------- */

		JButton btnMaximize = factory.createButton(1280, 0, btnWindowCommandX, btnWindowCommandY, "\u25A1", toolbarGreen, false);
		pnlToolBar.add(btnMaximize);


		btnMaximize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) { 
				btnMaximize.setBackground(Color.GRAY); 
			}
			@Override
			public void mouseExited(MouseEvent e) { 
				btnMaximize.setBackground(toolbarGreen); 
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
					setExtendedState(JFrame.NORMAL);
				} else {
					setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
				updateScaling();

			}
		});


		/* --------------------------------------------
		 * 	Exit Button
	     -------------------------------------------- */

		JButton btnExit = factory.createButton(1280, 0, btnWindowCommandX, btnWindowCommandY, "\u2716", toolbarGreen, false);
		pnlToolBar.add(btnExit);


		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) { 
				btnExit.setBackground(Color.RED); 
			}
			@Override
			public void mouseExited(MouseEvent e) { 
				btnExit.setBackground(toolbarGreen); 
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				System.exit(0);
			}
		});

	}


	/* --------------------------------------------
	 * 	Factory for creating components
     -------------------------------------------- */

	private class UIFactory {

		private <T extends JComponent> T setupComponent(T component, int x, int y, int width, int height, boolean resizable, Color bg) {
			component.setBounds(x, y, width, height);

			if (debug) {
				component.setBorder(new LineBorder(Color.RED, 1, false));
				//component.setBackground(debugRed);
				//component.setOpaque(true);
			}

			if (resizable) {
				allComponents.add(component);
			}

			if (bg!=null) {
				component.setOpaque(true);
				component.setBackground(bg);
			}

			return component;
		}

		public JButton createButton(int x, int y, int width, int height, String text, Color bg, boolean resizable) {
			JButton button = new JButton(text);
			button.setBorder(null);
			button.setFocusPainted(false);
			button.setPreferredSize(new Dimension(width, height));
			return setupComponent(button, x, y, width, height, resizable, bg);
		}

		public KeyboardPanel createKeyboardPanel(int x, int y, int width, int height, Color bg, ImageIcon icon, boolean resizable) {
			KeyboardPanel keyboardPanel = new KeyboardPanel();
			keyboardPanel.setPreferredSize(new Dimension(width, height));
			keyboardPanel.setOpaque(false);
			return setupComponent(keyboardPanel, x, y, width, height, resizable, bg);
		}

		public ScaledLabel createScaledLabel(int x, int y, int width, int height, ImageIcon icon, boolean resizable, Color bg) {
			ScaledLabel scaledLabel = new ScaledLabel(icon);
			return setupComponent(scaledLabel, x, y, width, height, resizable, bg);
		}

		public RotatableLabel createRotatableLabel(int x, int y, int width, int height, ImageIcon icon, boolean resizable, int angleDegrees, Color bg) {
			RotatableLabel rotatableLabel = new RotatableLabel(icon, angleDegrees);
			return setupComponent(rotatableLabel, x, y, width, height, resizable, bg);
		}
	}















	/* --------------------------------------------
	 * 	Code for the analog letters
     -------------------------------------------- */

	private static final int analogX = 406, analogY = 462, analogWidth = 54, analogHeight = 74, analogGap = 6;
	private static final int NUM_LETTERS = 27, NUM_FLIP_FRAMES = 5;
	private static final int NUM_FRAMES = 4;

	private static final int leverX = 723, leverY = 490, leverWidth = 6, leverHeight = 42, leverGap = 22;

	private static final int gearX = 820, gearY = 490, gearWidth = 6, gearHeight = 30, gearGap = 18;

	private ScaledLabel[] analogDisplays = new ScaledLabel[5], lblGears = new ScaledLabel[4];
	private RotatableLabel[] levers = new RotatableLabel[3];
	private Random random = new Random();
	private ImageIcon[] letterIcons = loadSpriteSheet(userDirectory + "\\src\\data\\GUI\\letters.png", NUM_LETTERS, analogWidth, analogHeight);
	private ImageIcon[] flipIcons   = loadSpriteSheet(userDirectory + "\\src\\data\\GUI\\flip.png", NUM_FLIP_FRAMES, analogWidth, analogHeight);
	private ImageIcon[] gearIcons   = loadSpriteSheet(userDirectory + "\\src\\data\\GUI\\gears.png", NUM_FRAMES, gearWidth, gearHeight);
	private ImageIcon iconLever = new ImageIcon(userDirectory + "\\src\\data\\GUI\\lever.png");

	RotatableLabel lblDial;
	ScaledLabel btnStart;
	ScaledLabel btnStop;

	/** Load a horizontal sprite sheet into icons */
	private ImageIcon[] loadSpriteSheet(String path, int count, int w, int h) {
		try {
			BufferedImage sheet = ImageIO.read(new File(path));
			ImageIcon[] icons = new ImageIcon[count];
			for (int i = 0; i < count; i++) {
				BufferedImage sub = sheet.getSubimage(i * w, 0, w, h);
				icons[i] = new ImageIcon(sub);
			}
			return icons;
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageIcon[0];
		}
	}

	private void addAnalogDisplays() {
		for (int i=0;i<analogDisplays.length;i++) {
			analogDisplays[i] = factory.createScaledLabel(
					(analogX) + (analogWidth + analogGap)*i,
					(analogY),
					(analogWidth),
					(analogHeight),
					letterIcons[0], 
					true,
					null);
			frontPanel.add(analogDisplays[i]);
		}
		animateLetter(analogDisplays[0], 'H');
		animateLetter(analogDisplays[1], 'E');
		animateLetter(analogDisplays[2], 'L');
		animateLetter(analogDisplays[3], 'L');
		animateLetter(analogDisplays[4], 'O');
	}

	
	private void addLevers() {
		for (int i = 0; i < levers.length; i++) {
			levers[i] = factory.createRotatableLabel(
					(leverX) + (leverWidth + leverGap)*i,
					(leverY),
					(leverWidth),
					(leverHeight),
					iconLever, 
					true,
					0,
					null);
			frontPanel.add(levers[i]);

			final int index = i;
			levers[index].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (!isRunning) {
						if (!levers[index].isOn()) {
							disableAllLevers();
							levers[index].rotateTo(180, 0);
							lblDial.rotateTo(90*index,((int)Math.abs(Math.toDegrees(lblDial.getAngle())-(90*index)) * 500/90));
							//lblDial.rotateTo(90*index, ((int)Math.abs(Math.toDegrees(lblDial.getAngle())-90*index) * 500));
							levers[index].toggle();
							setConfigFileName(userDirectory + "\\src\\data\\" + (index+1) + ".txt");
						} 
						updateScaling();
					} 
					else {
						JOptionPane.showMessageDialog(null, "Hello!");
					}
				}
			});
		}
		levers[0].toggle();
		levers[0].rotateTo(180, 0);
	}


	private void disableAllLevers() {
		for (RotatableLabel lever : levers) {
			if (lever.isOn()) {
				lever.rotateTo(0, 0);
				lever.toggle();
			}
		}
	}


	private void addGears() {
		for (int i = 0; i < lblGears.length; i++) {
			lblGears[i] = factory.createScaledLabel(
					(gearX) + (gearWidth + gearGap)*i,
					(gearY),
					(gearWidth),
					(gearHeight),
					gearIcons[0],
					true,
					null);
			frontPanel.add(lblGears[i]);
			animateLabel(lblGears[i], gearIcons, null, 0, Integer.MAX_VALUE, 50);
		}
	}


	private void setupDial() {
		lblDial = factory.createRotatableLabel(322, 
				226, 
				42, 
				42, 
				new ImageIcon(userDirectory + "\\src\\data\\GUI\\pointer.png"), 
				true, 
				0,
				null);
		frontPanel.add(lblDial);
	}

	private int startButtonX = 821, stopButtonX = 869;
	private int eventButtonY = 443;
	private int eventButtonSize = 28;
	private ImageIcon startButtonImage = new ImageIcon(userDirectory + "\\src\\data\\GUI\\start.png");
	private ImageIcon stopButtonImage = new ImageIcon(userDirectory + "\\src\\data\\GUI\\stop.png");

	KeyboardPanel keyboardPanel;

	private void setupKeyboardPanel() {
		keyboardPanel = factory.createKeyboardPanel(253, 566, 768, 120, null, null, true);
		frontPanel.add(keyboardPanel);
	}

	private void setupEventButtons() {

		Color bgButton = new Color(124,73,37);
		Color bgButtonDark = bgButton.darker();
		Color bgButtonLight = bgButton.brighter();

		btnStart = factory.createScaledLabel(startButtonX, eventButtonY, eventButtonSize, 
				eventButtonSize, startButtonImage, true, bgButton);
		btnStop = factory.createScaledLabel(stopButtonX, eventButtonY, eventButtonSize, 
				eventButtonSize, stopButtonImage, true, bgButtonDark);


		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!isRunning) {
					btnStart.setBackground(bgButtonLight);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!isRunning) {
					btnStart.setBackground(bgButton);
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				MidiKeyHandler handler = null;
				if (!isRunning) {
					try {
						handler = new MidiKeyHandler();
						monitor = new KeyMonitor(handler, configFileName);
						monitor.start();
						isRunning = true;
						btnStart.setBackground(bgButtonDark);
						btnStop.setBackground(bgButton);
					} catch (MidiUnavailableException e1) {
						e1.printStackTrace();
					}
					handler.addListener(pressed -> {
						keyboardPanel.setPressedKeys(pressed);
						keyboardPanel.repaint();
					});
				}
			}
		});


		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (isRunning) {
					btnStop.setBackground(bgButtonLight);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (isRunning) {
					btnStop.setBackground(bgButton);
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (isRunning) {
					monitor.interrupt();
					isRunning = false;
					btnStart.setBackground(bgButton);
					btnStop.setBackground(bgButtonDark);
				}
			}
		});
		frontPanel.add(btnStart);
		frontPanel.add(btnStop);
	}

	private void animateLabel(ScaledLabel label, ImageIcon[] animationFrames, ImageIcon finalFrame, int delayBeforeStart, int animationDuration, int frameDuration) {
		Timer startTimer = new Timer(delayBeforeStart, e -> {
			Timer flipTimer = new Timer(frameDuration,null);
			final long endTime = System.currentTimeMillis() + animationDuration;

			flipTimer.addActionListener(ev -> {
				if (System.currentTimeMillis() >= endTime) {
					flipTimer.stop();
					if (finalFrame != null) {
						label.setScaledIcon(finalFrame);
					}
				} else {
					int idx = random.nextInt(animationFrames.length);
					label.setScaledIcon(animationFrames[idx]);
				}
			});

			flipTimer.start();
			((Timer) e.getSource()).stop();
		});
		startTimer.setRepeats(false);
		startTimer.start();
	}

	/** Animate one letter slot */
	private void animateLetter(ScaledLabel label, char finalChar) {
		int finalIndex;
		if (finalChar == ' ') {
			finalIndex = 26;
		} else {
			finalIndex = finalChar - 'A';
		}
		ImageIcon finalFrame = letterIcons[finalIndex];

		int delayBeforeStart = 50 + random.nextInt(450); // 0–500ms before starting
		int flipDuration = 500 + random.nextInt(500);    // 500–1000ms animation duration
		int frameDuration = 50; // 50ms per frame

		animateLabel(label, flipIcons, finalFrame, delayBeforeStart, flipDuration, frameDuration);

	}

}
