import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.*;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.border.BevelBorder;


public class mainGUI extends JFrame {
	// Gets the user directory to allow for images that don't have an absolute path
	String userDirectory = System.getProperty("user.dir");
	// Variables for moving the window6
	int xCursor;
	int yCursor;

	public static File configFile;

	static JTextArea txtRandomTextBox;
	static File configDirectory;

	static int selectedKey;

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;

	List<Integer> demoKeysList = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainGUI frame = new mainGUI();
					frame.setVisible(true);
					System.out.println("Key Pressed: " + KeyEvent.getKeyText(32));
					System.out.println("Mouse Pressed: " + MouseEvent.getModifiersExText(1024));
					MidiHandling.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/* ------------------------------------------------------------------------------
	 * The code to create the main JFrame that the rest of the GUI is contained on
	 ------------------------------------------------------------------------------ */

	// Main GUI
	public static mainGUI instance;
	public mainGUI() {
		instance = this;
		setUndecorated(true); // Removes default toolbar etc for custom design
		setIconImage(Toolkit.getDefaultToolkit().getImage(userDirectory + "\\src\\data\\images\\SkongCope.png")); // Sets the icon
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 550); // Set window size
		mainPanel = new JPanel();
		mainPanel.setAlignmentX(0.0f);
		mainPanel.setBackground(new Color(43, 24, 63));
		mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

		setContentPane(mainPanel);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[] {5, 30, 30, 30, 73, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 65, 45, 45, 0};
		gbl_mainPanel.rowHeights = new int[]{0, 350, 158, 0, 0};
		gbl_mainPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_mainPanel.rowWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		mainPanel.setLayout(gbl_mainPanel);


		/* ------------------------------------------------------------------------------
		 * The creation of all objects related to the toolbar
		 ------------------------------------------------------------------------------ */

		// Creation of the minimize button on the GUI

		JButton btnMini = new JButton("\u2014");
		btnMini.setFocusPainted(false);
		btnMini.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnMini.setBackground(new Color(43, 24, 63));
		btnMini.setForeground(Color.WHITE);
		GridBagConstraints gbc_btnMini = new GridBagConstraints();
		gbc_btnMini.insets = new Insets(0, 0, 5, 5);
		gbc_btnMini.gridx = 19;
		gbc_btnMini.gridy = 0;
		mainPanel.add(btnMini, gbc_btnMini);

		// Creation of the exit button on the GUI

		JButton btnExit = new JButton("\u2716");
		btnExit.setFocusPainted(false);
		btnExit.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnExit.setBackground(new Color(43, 24, 63));
		btnExit.setForeground(Color.WHITE);
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.insets = new Insets(0, 0, 5, 5);
		gbc_btnExit.gridx = 20;
		gbc_btnExit.gridy = 0;
		mainPanel.add(btnExit, gbc_btnExit);

		// Create the actual toolbar itself to give something to click on

		JPanel pnlToolBar = new JPanel();
		pnlToolBar.setBackground(new Color(43, 24, 63));
		GridBagConstraints gbc_pnlToolBar = new GridBagConstraints();
		gbc_pnlToolBar.gridwidth = 17;
		gbc_pnlToolBar.insets = new Insets(0, 0, 5, 5);
		gbc_pnlToolBar.fill = GridBagConstraints.BOTH;
		gbc_pnlToolBar.gridx = 1;
		gbc_pnlToolBar.gridy = 0;
		mainPanel.add(pnlToolBar, gbc_pnlToolBar);


		/* ------------------------------------------------------------------------------
		 * The code to handle the movement of the window when the toolbar is clicked
		 ------------------------------------------------------------------------------ */


		// Gets the location of the cursor on the window

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


		/* ------------------------------------------------------------------------------
		 * The code to handle the buttons contained in the custom toolbar
		 ------------------------------------------------------------------------------ */

		// Exit button

		// Listeners to activate when the mouse hovers over or exits the exit button
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnExit.setBackground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnExit.setBackground(new Color(43, 24, 63));
			}
			// Listener for clicking the exit button
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				System.exit(0);
			}
		});

		// Minimize button

		// Listeners to activate when the mouse hovers over or exits the minimize button
		btnMini.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnMini.setBackground(Color.GRAY);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnMini.setBackground(new Color(43, 24, 63));
			}
			// Minimize the GUI when clicked
			@Override
			public void mouseClicked(MouseEvent e) {
				setState(JFrame.ICONIFIED);
			}
		});


		/* ------------------------------------------------------------------------------
		 * The code to create all objects related to the left side panel
		 ------------------------------------------------------------------------------ */


		// Creation of the main left panel

		JPanel pnlMain = new JPanel();
		pnlMain.setBackground(Color.LIGHT_GRAY);
		pnlMain.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(0, 0, 0)));
		pnlMain.setLayout(null);
		GridBagConstraints gbc_pnlMain = new GridBagConstraints();
		gbc_pnlMain.gridwidth = 17;
		gbc_pnlMain.insets = new Insets(0, 0, 5, 5);
		gbc_pnlMain.fill = GridBagConstraints.BOTH;
		gbc_pnlMain.gridx = 1;
		gbc_pnlMain.gridy = 1;
		mainPanel.add(pnlMain, gbc_pnlMain);

		// Creation of the random text box that goes with it

		txtRandomTextBox = new JTextArea();
		txtRandomTextBox.setForeground(Color.BLACK);
		txtRandomTextBox.setOpaque(false);
		txtRandomTextBox.setBorder(null);
		txtRandomTextBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtRandomTextBox.setBounds(424, 64, 87, 121);
		pnlMain.add(txtRandomTextBox);
		txtRandomTextBox.setRows(1);

		JLabel lblTitle = new JLabel("");
		lblTitle.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\title.png"));
		lblTitle.setBounds(23, 11, 337, 44);
		pnlMain.add(lblTitle);

		// Creation of the label for picking a key

		JLabel lblPickInputNew = new JLabel("");
		lblPickInputNew.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\pickInputA.png"));
		lblPickInputNew.setBounds(23, 66, 251, 24);
		pnlMain.add(lblPickInputNew);

		// Creation of the label to bind midi inputs to the key

		JLabel lblBindKeyNew = new JLabel("");
		lblBindKeyNew.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\bindKeyA.png"));
		lblBindKeyNew.setBounds(23, 101, 251, 24);
		pnlMain.add(lblBindKeyNew);

		// Creation of the label to display the midi key binds

		JLabel lblDisplayBinding = new JLabel("");
		lblDisplayBinding.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\displayBindingA.png"));
		lblDisplayBinding.setBounds(23, 136, 304, 24);
		pnlMain.add(lblDisplayBinding);

		// Creation of the delete binding label

		JLabel lblDeleteBinding = new JLabel("");
		lblDeleteBinding.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\deleteBindingA.png"));
		lblDeleteBinding.setBounds(23, 171, 294, 24);
		pnlMain.add(lblDeleteBinding);

		// Creation of the selected input text field

		JTextField txtSelectedInput = new JTextField();
		txtSelectedInput.setForeground(Color.PINK);
		txtSelectedInput.setFont(new Font("Arial Black", Font.PLAIN, 28));
		txtSelectedInput.setBorder(null);
		txtSelectedInput.setOpaque(false);
		txtSelectedInput.setEditable(false);
		txtSelectedInput.setBounds(23, 277, 313, 44);
		pnlMain.add(txtSelectedInput);
		txtSelectedInput.setBackground(Color.WHITE);
		txtSelectedInput.setHorizontalAlignment(SwingConstants.CENTER);
		txtSelectedInput.setColumns(1);

		JLabel lblLeftPanelImage = new JLabel("");
		lblLeftPanelImage.setIcon(new ImageIcon(userDirectory + "\\src\\Data\\images\\panelImageLeft.png"));
		lblLeftPanelImage.setBounds(3, 3, 577, 344);
		pnlMain.add(lblLeftPanelImage);


		/* ------------------------------------------------------------------------------
		 * The code to handle the buttons contained in the left side panel
		 ------------------------------------------------------------------------------ */

		// Listener for pick input button

		lblPickInputNew.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				lblPickInputNew.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\\\pickInputB.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblPickInputNew.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\pickInputA.png"));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				captureInput(txtSelectedInput);
			}
		});

		// Listener for add bind button

		lblBindKeyNew.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				lblBindKeyNew.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\\\bindKeyB.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblBindKeyNew.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\\\bindKeyA.png"));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				captureMidiInput();
			}
		});

		// Listener for display binding button

		lblDisplayBinding.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				lblDisplayBinding.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\\\displayBindingB.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblDisplayBinding.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\\\displayBindingA.png"));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				Integer theKey = selectedKey;
				demoKeysList = getDemoKeys(theKey.toString());
				try {
					for (int item: demoKeysList) {}
				}
				catch(NullPointerException k) {
					JOptionPane.showMessageDialog(null, "Please provide an input using the buttons\nabove, and assure this key is bound", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				demoKeysList.clear();
			}
		});

		// Listener for delete binding button

		lblDeleteBinding.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				lblDeleteBinding.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\deleteBindingB.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblDeleteBinding.setIcon(new ImageIcon(userDirectory + "\\src\\data\\images\\deleteBindingA.png"));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				removeKeyBind();
			}
		});




		/* ------------------------------------------------------------------------------
		 * The code for the creation of all object related to the right hand panel
		 ------------------------------------------------------------------------------ */


		// Creation of the right side panel

		JPanel pnlOptions = new JPanel();
		pnlOptions.setBackground(Color.LIGHT_GRAY);
		pnlOptions.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(0, 0, 0)));
		pnlOptions.setLayout(null);
		GridBagConstraints gbc_pnlOptions = new GridBagConstraints();
		gbc_pnlOptions.gridwidth = 3;
		gbc_pnlOptions.insets = new Insets(0, 0, 5, 5);
		gbc_pnlOptions.fill = GridBagConstraints.BOTH;
		gbc_pnlOptions.gridx = 18;
		gbc_pnlOptions.gridy = 1;
		mainPanel.add(pnlOptions, gbc_pnlOptions);

		// Creation of the profile panel title label

		JLabel lblProfilesTitle = new JLabel("");
		lblProfilesTitle.setIcon(new ImageIcon(userDirectory + "\\src\\Data\\images\\Profiles.png"));
		lblProfilesTitle.setBounds(8, 12, 139, 21);
		lblProfilesTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		pnlOptions.add(lblProfilesTitle);

		// Creation of the help label

		JLabel lblHelp = new JLabel("");
		lblHelp.setBounds(0, 0, 0, 0);
		lblHelp.setToolTipText("Allows for multiple sets of keybinds at once");
		lblHelp.setHorizontalAlignment(SwingConstants.RIGHT);
		ImageIcon helpIcon = new ImageIcon(userDirectory + "\\src\\data\\images\\help.png");
		lblHelp.setIcon(helpIcon);
		pnlOptions.add(lblHelp);

		// Creation of the profiles combo box

		JComboBox<String> cbbProfiles = new JComboBox<>();
		cbbProfiles.setFont(new Font("Tahoma", Font.BOLD, 12));
		cbbProfiles.setBounds(13, 44, 127, 22);
		File directory = new File(userDirectory + "\\src\\data\\");
		updateDropBox(directory, cbbProfiles);
		pnlOptions.add(cbbProfiles);

		// Creation of the refresh button

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(13, 76, 127, 23);
		pnlOptions.add(btnRefresh);

		// Creation of the add new profile button

		JButton btnAddNewProfile = new JButton("Add New Profile");
		btnAddNewProfile.setBounds(13, 110, 127, 23);
		pnlOptions.add(btnAddNewProfile);

		// Creation of the remove profile button

		JButton btnRemoveProfile = new JButton("Delete Profile");
		btnRemoveProfile.setBounds(13, 144, 127, 23);
		pnlOptions.add(btnRemoveProfile);

		// Creation of that one random label I have and I'm not sure if I'll use it or not

		JLabel lblRightPanelImage = new JLabel("");
		lblRightPanelImage.setIcon(new ImageIcon(userDirectory + "\\src\\Data\\images\\panelImageRight.png"));
		lblRightPanelImage.setBounds(3, 3, 146, 344);
		pnlOptions.add(lblRightPanelImage);
		lblRightPanelImage.setFont(new Font("Comic Sans MS", Font.BOLD, 14));


		/* ------------------------------------------------------------------------------
		 * The code to handle the buttons contained in the right side panel
		 ------------------------------------------------------------------------------ */


		// Combo box item change

		cbbProfiles.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				configDirectory = new File(userDirectory + "\\src\\data\\" + cbbProfiles.getSelectedItem() + ".txt");
				setConfigFile(configDirectory);
				MidiHandling.updateKeyConfig();
			}
		});

		// Combo box property change

		cbbProfiles.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				configDirectory = new File(userDirectory + "\\src\\data\\" + cbbProfiles.getSelectedItem() + ".txt");
				setConfigFile(configDirectory);
				MidiHandling.updateKeyConfig();
			}
		});

		// Add new profile

		btnAddNewProfile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String name = JOptionPane.showInputDialog(btnAddNewProfile, "Enter profile name", null);
				File newFile = new File(directory + "\\" + name + ".txt");
				profileHandling(newFile, btnAddNewProfile, true);
				updateDropBox(directory, cbbProfiles);
			}
		});

		// Refresh combo box

		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updateDropBox(directory, cbbProfiles);
			}
		});

		// Remove profile

		btnRemoveProfile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				File newFile = new File(directory + "\\" + cbbProfiles.getSelectedItem() + ".txt");
				profileHandling(newFile, btnRemoveProfile, false);
				updateDropBox(directory, cbbProfiles);
			}
		});


		/* ------------------------------------------------------------------------------
		 * The code for the bottom panel displaying the midi keyboard
		 ------------------------------------------------------------------------------ */


		// Creation of the drawing panel created from a class lower in the program

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
		pnlKeyboard.requestFocusInWindow();
		// Create a thread to draw and remove the keys from the visual keyboard when necessary
		DrawingThread thread = new DrawingThread(pnlKeyboard);
		thread.start();
	}


	/* ------------------------------------------------------------------------------
	 * All the methods required for the buttons and labels in the left hand panel
	 ------------------------------------------------------------------------------ */


	// Method to create a dialog box to prompt the user to input midi keys for the new key binding

	public static void captureMidiInput() {
		// Create dialog box with preset size and title
		JDialog dialog = new JDialog(mainGUI.instance, "Select Midi Keys", false);
		dialog.setSize(500, 200);
		dialog.getContentPane().setLayout(null);

		// First label on the first line
		JLabel lblInstruction1 = new JLabel("Press and hold the desired keys on the Midi keyboard");
		lblInstruction1.setBounds(50, 20, 500, 30);
		dialog.getContentPane().add(lblInstruction1);

		// Second label second line
		JLabel lblInstruction2 = new JLabel("until the end of the countdown...");
		lblInstruction2.setBounds(50, 40, 500, 30);
		dialog.getContentPane().add(lblInstruction2);

		// Timer that will be modified by a thread to show the user how long to hold the keys for
		JLabel lblTimer = new JLabel("3");
		lblTimer.setBounds(250, 40, 500, 30);
		dialog.getContentPane().add(lblTimer);

		// Get parent frame location and size
		int frameX = mainGUI.instance.getX();
		int frameY = mainGUI.instance.getY();
		int frameWidth = mainGUI.instance.getWidth();
		int frameHeight = mainGUI.instance.getHeight();

		// Calculate center of the parent frame
		int dialogX = frameX + (frameWidth - dialog.getWidth()) / 2;
		int dialogY = frameY + (frameHeight - dialog.getHeight()) / 2;

		// Set the location of the dialog relative to the parent frame
		dialog.setLocation(dialogX, dialogY);
		dialog.setFocusable(true);
		dialog.setVisible(true);

		// Create new thread for the timer and pass in the label and dialog box
		timerThread myTimerThread = new timerThread(lblTimer, dialog);
		myTimerThread.start();
	}


	public static List<String> readFile(File myFile) {
		List<String> fileText = new ArrayList<String>();
		try {
			File myObj = myFile;
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				fileText.add(data);
			}
			myReader.close();
			return fileText;
		} 
		catch (FileNotFoundException e) {System.out.println("File not found");}
		return fileText;
	}

	public static void writeFile(File myFile, List<String> midiKeyBinds, List<String> keyCodes) {
		try {
			File myObj = myFile;
			FileWriter myWriter = new FileWriter(myObj);
			for (int i = 0; i < midiKeyBinds.size(); i++) {
				myWriter.write(midiKeyBinds.get(i) + "," + keyCodes.get(i) + "\n");
			}
			myWriter.close();
		} 
		catch (FileNotFoundException e) {System.out.println("File not found");} 
		catch (IOException e) {e.printStackTrace();}
	}

	public static void removeKeyBind() {
		Integer tempKeyCode = selectedKey;
		String[][] configArray = getKeyConfigArray();
		List<String> midiKeyBinds = new ArrayList<String>();
		List<String> keyCodes = new ArrayList<String>();
		int numberOfRows = configArray.length;

		// Check through each row in the array to see if any key binds exist that are incompatible with the new one
		for (int i = 0; i < numberOfRows; i++ ) {
			// If the current item of config has a compatible midi 
			if (!configArray[i][1].startsWith(tempKeyCode.toString())) {
				midiKeyBinds.add(configArray[i][0]);
				keyCodes.add(configArray[i][1]);
			}
		}

		writeFile(configFile, midiKeyBinds, keyCodes);
		getKeyConfigArray();
		MidiHandling.updateKeyConfig();

	}

	public static String[][] getKeyConfigArray() {
		// The program will try to open the associated file and will throw an error if it can't be found
		List<String> textFileContents = readFile(configFile);
		int numberOfLines = textFileContents.size();
		String[][] keyControls = new String[numberOfLines][2];
		// For each line in the text file
		for (int i = 0; i < numberOfLines; i++) {
			String regex = "[,]";
			String[] lineData = textFileContents.get(i).split(regex);
			keyControls[i][0] = lineData[0];
			keyControls[i][1] = lineData[1];
		}
		return keyControls;
	}

	public static void setConfigFile(File config) {
		configFile = config;
	}


	// Function to open popup and listen for key or mouse input
	public static void captureInput(JTextField txtSelectedInput) {
		// Create dialog box with preset size and title
		JDialog dialog = new JDialog(mainGUI.instance, "Press a Key or Mouse Button", true);
		dialog.setSize(300, 100);
		dialog.getContentPane().setLayout(null);

		JLabel lblInstruction = new JLabel("Press any key or mouse button...");
		lblInstruction.setBounds(50, 20, 200, 30);
		dialog.getContentPane().add(lblInstruction);

		// Add a KeyListener to capture key press
		dialog.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				selectedKey = e.getKeyCode();
				txtSelectedInput.setText(KeyEvent.getKeyText(selectedKey));

				dialog.dispose();
			}
		});

		// Add a MouseListener to capture mouse press and determine the code for each button
		dialog.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.toString().contains("Button1")) {
					txtSelectedInput.setText("Mouse_1");
					selectedKey = 1024;
				} 
				else if (e.toString().contains("Button2")) {
					txtSelectedInput.setText("Mouse_2");
					selectedKey = 2048;
				}
				else if (e.toString().contains("Button3")) {
					txtSelectedInput.setText("Mouse_3");
					selectedKey = 4096;
				}
				else {
					txtSelectedInput.setText("Unfamiliar Mouse Button");
				}
				dialog.dispose();
			}
		});

		// Get parent frame location and size (mainGUI.instance)
		int frameX = mainGUI.instance.getX();
		int frameY = mainGUI.instance.getY();
		int frameWidth = mainGUI.instance.getWidth();
		int frameHeight = mainGUI.instance.getHeight();

		// Calculate the center of the main frame
		int dialogX = frameX + (frameWidth - dialog.getWidth()) / 2;
		int dialogY = frameY + (frameHeight - dialog.getHeight()) / 2;

		// Set dialog location relative to the parent frame
		dialog.setLocation(dialogX, dialogY);

		// Make sure the dialog listens for key events
		dialog.setFocusable(true);
		dialog.setVisible(true);
	}

	public static List<Integer> getDemoKeys(String keyCodeSearch) {
		String[][] configArray = getKeyConfigArray();
		List<Integer> demoKeys = new ArrayList<Integer>();
		// For each item in the config file
		for (int i = 0; i < configArray.length; i++) {
			// If its key code matches to key code being searched
			if (configArray[i][1].equals(keyCodeSearch)) {
				String currentCode = configArray[i][0];
				// Break the key bind down into individual keys
				for (int j = 0; j < currentCode.length(); j += 2) {
					String pair = currentCode.substring(j, j + 2);
					demoKeys.add(Integer.valueOf(pair));
				}
				return demoKeys;
			}
		}
		return null;
	}

	// Thread for the timer displayed on the popup window
	private static class timerThread extends Thread {
		private JLabel lblTimer;
		private JDialog dialog;

		// Constructor to pass in the timer label
		public timerThread(JLabel lblTimer, JDialog dialog) {
			this.lblTimer = lblTimer;
			this.dialog = dialog;
		}

		// Perform a countdown when created
		public void run() {
			try {
				for (int i = 3; i > 0; i--) {
					lblTimer.setText(String.valueOf(i));
					Thread.sleep(1000); // Wait for 1 second
				}
				List<Integer> tempActiveKeys = MidiHandling.DisplayReceiver.getActiveKeys();
				tempActiveKeys.sort(null);
				Integer tempKeyCode = selectedKey;
				String midiKeyBind = "";
				for (int key: tempActiveKeys) {
					midiKeyBind = midiKeyBind + key;
				}
				String[][] configArray = getKeyConfigArray();
				List<String> midiKeyBinds = new ArrayList<String>();
				List<String> keyCodes = new ArrayList<String>();
				int numberOfRows = configArray.length;
				boolean overwritten = false;

				// Check through each row in the array to see if any key binds exist that are incompatible with the new one
				for (int i = 0; i < numberOfRows; i++ ) {

					// If the current item of config has a compatible midi 
					if (configArray[i][0].startsWith(tempActiveKeys.get(0).toString()) || configArray[i][1].startsWith(tempKeyCode.toString())) {
						midiKeyBinds.add(midiKeyBind);
						keyCodes.add(tempKeyCode.toString());
						overwritten = true;
					} else {
						midiKeyBinds.add(configArray[i][0]);
						keyCodes.add(configArray[i][1]);
					}

				}

				if (!overwritten) {
					midiKeyBinds.add(midiKeyBind);
					keyCodes.add(tempKeyCode.toString());
				}

				writeFile(configFile, midiKeyBinds, keyCodes);
				getKeyConfigArray();
				MidiHandling.updateKeyConfig();
				dialog.dispose();

			} catch (InterruptedException e) {
				// InterruptedException error would go here if I wanted to display it but I don't see the point here
			}	
		}
	}


	/* ------------------------------------------------------------------------------
	 * All the methods required for the right hand panel for profile based tasks
	 ------------------------------------------------------------------------------ */


	// Function responsible for creating and deleting profiles

	public static void profileHandling(File newProfile, JButton button, boolean create) {
		File profile = newProfile;
		try {
			if (create) {
				profile.createNewFile();
			}
			else {
				profile.delete();
			}
		} catch (IOException e) {
			JOptionPane.showInputDialog(button, "An error occured", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Function that updates the drop down menu with all avaliable directories
	public static void updateDropBox(File directory, JComboBox<String> cbbProfiles) {
		fileSearching configFilter = new fileSearching(".txt"); // Creates a filter for ending in .txt
		String[] configList = directory.list(configFilter); // Creates a list of all files in the given directory that match the filter
		if (configList != null) { 
			cbbProfiles.removeAllItems(); // Clear the combo box to prevent duplicates
			// Run through each item in the list and add it to the menu whilst cutting off the file extension at the end
			for (int i = 0; i < configList.length; i++) {
				String nameOnly = configList[i].substring(0, configList[i].length() - 4); //Removes .txt
				cbbProfiles.addItem(nameOnly);
			}
		}
	}


	/* ------------------------------------------------------------------------------
	 * All the methods required for the bottom panel / keyboard image
	 ------------------------------------------------------------------------------ */


	// The class to create the drawing panel to allow for the drawing of keys in real time in an efficient way

	class DrawingPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		private List<Rectangle> whiteRectangles = new ArrayList<>();  // Initialize the list of white rectangles
		private List<Rectangle> blackRectangles = new ArrayList<>();  // Initialize the list of black rectangles
		private List<Rectangle> redRectangles = new ArrayList<>(); // Initiialize the list of red rectangles
		private Image keyboardImage;

		public DrawingPanel() {
			setPreferredSize(new Dimension(720, 133));
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

		// Add a black rectangle to the list
		public void addRedRectangle(Rectangle rect) {
			redRectangles.add(rect);
		}

		// Clear all rectangles (to update for new keys)
		public void clearRectangles() {
			whiteRectangles.clear();
			blackRectangles.clear();
			redRectangles.clear();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			keyboardImage = new ImageIcon(userDirectory + "\\src\\data\\images\\keyboard.png").getImage();
			g.drawImage(keyboardImage, 0, 0, getWidth(), getHeight(), this);

			// Draw all rectangles in the list
			for (Rectangle rect : whiteRectangles) {
				g.setColor(Color.LIGHT_GRAY);
				// Draw the rectangle

				g.fillRect(rect.x, rect.y, rect.width, rect.height);
			}
			for (Rectangle rect : redRectangles) {
				g.setColor(Color.RED);
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

		public DrawingThread(DrawingPanel panel) {
			this.panel = panel;  // Pass the DrawingPanel to the thread
		}

		public void run() {
			// A list of each number on a 61 keyed keyboard linked to a white key
			int[] whiteKeys = {36, 38, 40, 41, 43, 45, 47, 48, 50, 52, 53, 55, 57, 59, 60, 62, 64, 65, 67, 69, 71, 72, 74, 76, 77, 79, 81, 83, 84, 86, 88, 89, 91, 93, 95, 96};
			// A list of each number on a 61 keyed keyboard linked to a black key
			// This list is a double and uses decimels to essentially insert gaps into the rectangles drawing as black keys aren't ordered as simply as white
			// I'm sure this isn't the most efficient solution, but it works soooo
			double[] blackKeys = {37, 39, 39.5, 42, 44, 46, 46.5, 49, 51, 51.5, 54, 56, 58, 58.5, 61, 63, 63.5, 66, 68, 70, 70.5, 73, 75, 75.5, 78, 80, 82, 82.5, 85, 87, 87.5, 90, 92, 94};

			List<Integer> activeKeys = MidiHandling.DisplayReceiver.getActiveKeys();
			Integer theKey = selectedKey;


			int whiteKeyIndex, blackKeyIndex, previousKey, nextKey, whiteKeyWidth, blackKeyWidth, whiteKeyHeight, blackKeyHeightSmall, blackKeyHeightLarge, blackWhiteGap;

			// I added all these just to make it really easy to change the sizes of rectangles in case I rescale the GUI again.
			// This is because I intend to develop this application to always be a set size and to not be full screened.
			whiteKeyWidth = 20;
			blackKeyWidth = 14;
			whiteKeyHeight = 133;
			blackKeyHeightSmall = 77;
			blackKeyHeightLarge = 81;
			blackWhiteGap = whiteKeyWidth - (blackKeyWidth / 2);


			while (true) {  // Continuous loop to check for key presses
				// Clear existing rectangles
				panel.clearRectangles();

				for (Integer currentKey : activeKeys) { // For each currently pressed key

					whiteKeyIndex = Arrays.binarySearch(whiteKeys, currentKey);
					blackKeyIndex = Arrays.binarySearch(blackKeys, currentKey);

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
						panel.addWhiteRectangle(new Rectangle(whiteKeyIndex * whiteKeyWidth, 0, whiteKeyWidth, whiteKeyHeight));
						if (previousKey == currentKey - 2) {
							panel.addBlackRectangle(new Rectangle((whiteKeyIndex - 1) * whiteKeyWidth + blackWhiteGap, 0, blackKeyWidth, blackKeyHeightSmall));
						}
						if (nextKey == currentKey + 2) {
							panel.addBlackRectangle(new Rectangle((whiteKeyIndex) * whiteKeyWidth + blackWhiteGap, 0, blackKeyWidth, blackKeyHeightSmall));
						}
					} else if (blackKeyIndex >= 0) {
						// Draw a black key slightly offset
						panel.addBlackRectangle(new Rectangle(blackKeyIndex * whiteKeyWidth + blackWhiteGap, 0, blackKeyWidth, blackKeyHeightLarge));
					}
				}

				if (demoKeysList != null) {
					for (Integer currentKey : demoKeysList) {
						whiteKeyIndex = Arrays.binarySearch(whiteKeys, currentKey);
						blackKeyIndex = Arrays.binarySearch(blackKeys, currentKey);

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
							panel.addRedRectangle(new Rectangle(whiteKeyIndex * whiteKeyWidth, 0, whiteKeyWidth, whiteKeyHeight));
							if (previousKey == currentKey - 2) {
								panel.addBlackRectangle(new Rectangle((whiteKeyIndex - 1) * whiteKeyWidth + blackWhiteGap, 0, blackKeyWidth, blackKeyHeightSmall));
							}
							if (nextKey == currentKey + 2) {
								panel.addBlackRectangle(new Rectangle((whiteKeyIndex) * whiteKeyWidth + blackWhiteGap, 0, blackKeyWidth, blackKeyHeightSmall));
							}
						} else if (blackKeyIndex >= 0) {
							// Draw a black key slightly offset
							panel.addRedRectangle(new Rectangle(blackKeyIndex * whiteKeyWidth + blackWhiteGap, 0, blackKeyWidth, blackKeyHeightLarge));
						}
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


	public static void insertText(String text) {
		txtRandomTextBox.insert(text + "\n", 0);
	}
}


// Class used to locate all files ending in the provided string
// Used to find all files ending in .txt as these are the config files
class fileSearching implements FilenameFilter {

	String searchText;

	// Constructor method to pass in the text to search for
	public fileSearching(String searchText) {
		this.searchText = searchText;
	}

	// Function to return the file name ending in the search text
	public boolean accept(File dir, String name) {
		return name.endsWith(searchText);
	}
}


