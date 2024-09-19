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


public class mainGUI extends JFrame {
	// A list of each number on a 61 keyed keyboard that is linked to a white key
	private int[] whiteKeys = {36, 38, 40, 41, 43, 45, 47, 48, 50, 52, 53, 55, 57, 59, 60, 62, 64, 65, 67, 69, 71, 72, 74, 76, 77, 79, 81, 83, 84, 86, 88, 89, 91, 93, 95, 96};
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
	
	public mainGUI() {
		setUndecorated(true); // Removes default toolbar etc for custom design
		setIconImage(Toolkit.getDefaultToolkit().getImage(userDirectory + "\\src\\data\\SkongCope.png")); // Sets the icon
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 550); // Set window size
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
		
		
		/* ------------------------------------------------------------------------------
		 * The creation of all objects related to the toolbar
		 ------------------------------------------------------------------------------ */

		// Creation of the minimize button on the GUI
		
		JButton btnMini = new JButton("\u2014");
		btnMini.setFocusPainted(false);
		btnMini.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnMini.setBackground(Color.DARK_GRAY);
		btnMini.setForeground(Color.WHITE);
		GridBagConstraints gbc_btnMini = new GridBagConstraints();
		gbc_btnMini.insets = new Insets(0, 0, 5, 5);
		gbc_btnMini.gridx = 18;
		gbc_btnMini.gridy = 0;
		mainPanel.add(btnMini, gbc_btnMini);
		
		// Creation of the maximize button on the GUI
		
		JButton btnMaxi = new JButton("\uD83D\uDDD6");
		btnMaxi.setFocusPainted(false);
		btnMaxi.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnMaxi.setBackground(Color.DARK_GRAY);
		btnMaxi.setForeground(Color.WHITE);
		GridBagConstraints gbc_btnMaxi = new GridBagConstraints();
		gbc_btnMaxi.insets = new Insets(0, 0, 5, 5);
		gbc_btnMaxi.gridx = 19;
		gbc_btnMaxi.gridy = 0;
		mainPanel.add(btnMaxi, gbc_btnMaxi);
		
		// Creation of the exit button on the GUI
		
		JButton btnExit = new JButton("\u2716");
		btnExit.setFocusPainted(false);
		btnExit.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnExit.setBackground(Color.DARK_GRAY);
		btnExit.setForeground(Color.WHITE);
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.insets = new Insets(0, 0, 5, 5);
		gbc_btnExit.gridx = 20;
		gbc_btnExit.gridy = 0;
		mainPanel.add(btnExit, gbc_btnExit);
		
		// Create the actual toolbar itself to give something to click on
		
		JPanel pnlToolBar = new JPanel();
		pnlToolBar.setBackground(Color.DARK_GRAY);
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

		
		// Minimize button
		
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

		// Maximize button

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

		// Exit button

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
		
		
		/* ------------------------------------------------------------------------------
		 * The code to create all objects related to the left side panel
		 ------------------------------------------------------------------------------ */
		
		
		// Creation of the main left panel
		
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
		SpringLayout sl_pnlMain = new SpringLayout();
		pnlMain.setLayout(sl_pnlMain);
		
		// Creation of that one random label I have and I'm not sure if I'll use it or not
		
		JLabel lblRandomTitle = new JLabel("Input");
		sl_pnlMain.putConstraint(SpringLayout.NORTH, lblRandomTitle, 10, SpringLayout.NORTH, pnlMain);
		sl_pnlMain.putConstraint(SpringLayout.EAST, lblRandomTitle, -107, SpringLayout.EAST, pnlMain);
		lblRandomTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		pnlMain.add(lblRandomTitle);
		
		// Creation of the random text box that goes with it
		
		txtRandomTextBox = new JTextArea();
		sl_pnlMain.putConstraint(SpringLayout.NORTH, txtRandomTextBox, 42, SpringLayout.NORTH, pnlMain);
		sl_pnlMain.putConstraint(SpringLayout.SOUTH, txtRandomTextBox, -10, SpringLayout.SOUTH, pnlMain);
		sl_pnlMain.putConstraint(SpringLayout.WEST, txtRandomTextBox, 389, SpringLayout.WEST, pnlMain);
		sl_pnlMain.putConstraint(SpringLayout.EAST, txtRandomTextBox, -10, SpringLayout.EAST, pnlMain);
		pnlMain.add(txtRandomTextBox);
		
		// Creation of the select input button
		
		JButton btnSelectRegularInput = new JButton("Select Input");
		sl_pnlMain.putConstraint(SpringLayout.NORTH, btnSelectRegularInput, 1, SpringLayout.NORTH, txtRandomTextBox);
		sl_pnlMain.putConstraint(SpringLayout.EAST, btnSelectRegularInput, -154, SpringLayout.WEST, txtRandomTextBox);
		pnlMain.add(btnSelectRegularInput);
		
		// Creation of the key bindings title label
		
		JLabel lblKeyBindingsTitle = new JLabel("Key Bindings");
		sl_pnlMain.putConstraint(SpringLayout.NORTH, lblKeyBindingsTitle, 0, SpringLayout.NORTH, lblRandomTitle);
		sl_pnlMain.putConstraint(SpringLayout.WEST, lblKeyBindingsTitle, 81, SpringLayout.WEST, pnlMain);
		lblKeyBindingsTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		pnlMain.add(lblKeyBindingsTitle);
		
		// Creation of the select key label
		
		JLabel lblSelectKey = new JLabel("Input:");
		sl_pnlMain.putConstraint(SpringLayout.WEST, btnSelectRegularInput, 53, SpringLayout.EAST, lblSelectKey);
		sl_pnlMain.putConstraint(SpringLayout.NORTH, lblSelectKey, 0, SpringLayout.NORTH, txtRandomTextBox);
		sl_pnlMain.putConstraint(SpringLayout.WEST, lblSelectKey, 32, SpringLayout.WEST, pnlMain);
		lblSelectKey.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		pnlMain.add(lblSelectKey);
		
		// Creation of the selected key label
		
		JLabel lblSelectedKey = new JLabel("Selected Key:");
		sl_pnlMain.putConstraint(SpringLayout.NORTH, lblSelectedKey, 8, SpringLayout.SOUTH, lblSelectKey);
		sl_pnlMain.putConstraint(SpringLayout.WEST, lblSelectedKey, 10, SpringLayout.WEST, pnlMain);
		lblSelectedKey.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		pnlMain.add(lblSelectedKey);

		// Creation of the add bindings label

		JLabel lblAddMidiKeys = new JLabel("Add Binding:");
		sl_pnlMain.putConstraint(SpringLayout.NORTH, lblAddMidiKeys, 6, SpringLayout.SOUTH, lblSelectedKey);
		sl_pnlMain.putConstraint(SpringLayout.WEST, lblAddMidiKeys, 0, SpringLayout.WEST, lblSelectedKey);
		lblAddMidiKeys.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		pnlMain.add(lblAddMidiKeys);
		
		// Creation of the selected input text field
		
		JTextField txtSelectedInput = new JTextField();
		txtSelectedInput.setEditable(false);
		txtSelectedInput.setBackground(Color.WHITE);
		sl_pnlMain.putConstraint(SpringLayout.NORTH, txtSelectedInput, 6, SpringLayout.SOUTH, btnSelectRegularInput);
		sl_pnlMain.putConstraint(SpringLayout.WEST, txtSelectedInput, 0, SpringLayout.WEST, btnSelectRegularInput);
		sl_pnlMain.putConstraint(SpringLayout.EAST, txtSelectedInput, 0, SpringLayout.EAST, btnSelectRegularInput);
		txtSelectedInput.setHorizontalAlignment(SwingConstants.CENTER);
		pnlMain.add(txtSelectedInput);
		txtSelectedInput.setColumns(1);
		
		// Creation of the selected midi input button
		
		JButton btnSelectMidiInput = new JButton("Select Input");
		sl_pnlMain.putConstraint(SpringLayout.NORTH, btnSelectMidiInput, 6, SpringLayout.SOUTH, txtSelectedInput);
		sl_pnlMain.putConstraint(SpringLayout.WEST, btnSelectMidiInput, 0, SpringLayout.WEST, btnSelectRegularInput);
		sl_pnlMain.putConstraint(SpringLayout.EAST, btnSelectMidiInput, 0, SpringLayout.EAST, btnSelectRegularInput);
		pnlMain.add(btnSelectMidiInput);
		
		
		/* ------------------------------------------------------------------------------
		 * The code to handle the buttons contained in the left side panel
		 ------------------------------------------------------------------------------ */

		// Select Input Button
		
		btnSelectRegularInput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				captureInput(txtSelectedInput);
			}
		});
		
		// Select Midi input button
		
		btnSelectMidiInput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				captureMidiInput();
			}
		});
		
		
		/* ------------------------------------------------------------------------------
		 * The code for the creation of all object related to the right hand panel
		 ------------------------------------------------------------------------------ */
		
		
		// Creation of the right side panel
		
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
		GridBagLayout gbl_pnlOptions = new GridBagLayout();
		gbl_pnlOptions.columnWidths = new int[]{67, 9, 0};
		gbl_pnlOptions.rowHeights = new int[]{21, 0, 0, 0, 0, 0};
		gbl_pnlOptions.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_pnlOptions.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlOptions.setLayout(gbl_pnlOptions);
		
		// Creation of the profile panel title label

		JLabel lblProfilesTitle = new JLabel("Profiles");
		lblProfilesTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		GridBagConstraints gbc_lblProfilesTitle = new GridBagConstraints();
		gbc_lblProfilesTitle.anchor = GridBagConstraints.NORTH;
		gbc_lblProfilesTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblProfilesTitle.insets = new Insets(10, 20, 5, 5);
		gbc_lblProfilesTitle.gridx = 0;
		gbc_lblProfilesTitle.gridy = 0;
		pnlOptions.add(lblProfilesTitle, gbc_lblProfilesTitle);
		
		// Creation of the help label

		JLabel lblHelp = new JLabel("");
		lblHelp.setToolTipText("Allows for multiple sets of keybinds at once");
		lblHelp.setHorizontalAlignment(SwingConstants.RIGHT);
		ImageIcon helpIcon = new ImageIcon(userDirectory + "\\src\\data\\help.png");
		lblHelp.setIcon(helpIcon);
		GridBagConstraints gbc_lblHelp = new GridBagConstraints();
		gbc_lblHelp.insets = new Insets(5, 0, 5, 10);
		gbc_lblHelp.anchor = GridBagConstraints.WEST;
		gbc_lblHelp.gridx = 1;
		gbc_lblHelp.gridy = 0;
		pnlOptions.add(lblHelp, gbc_lblHelp);
		
		// Creation of the profiles combo box

		JComboBox<String> cbbProfiles = new JComboBox<>();
		GridBagConstraints gbc_cbbProfiles = new GridBagConstraints();
		gbc_cbbProfiles.gridwidth = 2;
		gbc_cbbProfiles.insets = new Insets(5, 10, 5, 10);
		gbc_cbbProfiles.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbbProfiles.gridx = 0;
		gbc_cbbProfiles.gridy = 1;
		File directory = new File(userDirectory + "\\src\\data\\");
		updateDropBox(directory, cbbProfiles);
		pnlOptions.add(cbbProfiles, gbc_cbbProfiles);
		
		// Creation of the refresh button
		
		JButton btnRefresh = new JButton("Refresh");
		GridBagConstraints gbc_btnRefresh = new GridBagConstraints();
		gbc_btnRefresh.gridwidth = 2;
		gbc_btnRefresh.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRefresh.anchor = GridBagConstraints.WEST;
		gbc_btnRefresh.insets = new Insets(5, 10, 5, 10);
		gbc_btnRefresh.gridx = 0;
		gbc_btnRefresh.gridy = 2;
		pnlOptions.add(btnRefresh, gbc_btnRefresh);
		
		// Creation of the add new profile button
		
		JButton btnAddNewProfile = new JButton("Add New Profile");
		GridBagConstraints gbc_btnAddNewProfile = new GridBagConstraints();
		gbc_btnAddNewProfile.gridwidth = 2;
		gbc_btnAddNewProfile.insets = new Insets(5, 10, 5, 10);
		gbc_btnAddNewProfile.gridx = 0;
		gbc_btnAddNewProfile.gridy = 3;
		pnlOptions.add(btnAddNewProfile, gbc_btnAddNewProfile);
		
		// Creation of the remove profile button
		
		JButton btnRemoveProfile = new JButton("Delete Profile");
		GridBagConstraints gbc_btnRemoveProfile = new GridBagConstraints();
		gbc_btnRemoveProfile.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveProfile.gridwidth = 2;
		gbc_btnRemoveProfile.insets = new Insets(5, 10, 5, 10);
		gbc_btnRemoveProfile.gridx = 0;
		gbc_btnRemoveProfile.gridy = 4;
		pnlOptions.add(btnRemoveProfile, gbc_btnRemoveProfile);
		
		
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
		JDialog dialog = new JDialog((JFrame) null, "Select Midi Keys", false);
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
        // I used a timer rather than on press as this way you can have a key bind that uses both hands on the midi keyboard (If you really wanted)
        JLabel lblTimer = new JLabel("3");
        lblTimer.setBounds(250, 40, 500, 30);
        dialog.getContentPane().add(lblTimer);
  
        // Center the dialog on the screen
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Dimension dialogSize = dialog.getSize();
        int x = (screenSize.width - dialogSize.width) / 2;
        int y = (screenSize.height - dialogSize.height) / 2;
        dialog.setLocation(x, y);
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
			MidiHandling.updateKeyConfig();
			myWriter.close();
		} 
		catch (FileNotFoundException e) {System.out.println("File not found");} 
		catch (IOException e) {e.printStackTrace();}
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
        JDialog dialog = new JDialog((JFrame) null, "Press a Key or Mouse Button", true);
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
            	System.out.println(selectedKey);
            	
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
            	System.out.println(selectedKey);
            	dialog.dispose();
            }
        });

        // Center the dialog on the screen
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Dimension dialogSize = dialog.getSize();
        int x = (screenSize.width - dialogSize.width) / 2;
        int y = (screenSize.height - dialogSize.height) / 2;
        dialog.setLocation(x, y);

        // Make sure the dialog listens for key events
        dialog.setFocusable(true);
        dialog.setVisible(true);
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
				System.out.println(tempActiveKeys.size());
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
						System.out.println("Incompatible");
						midiKeyBinds.add(midiKeyBind);
						keyCodes.add(tempKeyCode.toString());
						overwritten = true;
					} else {
						System.out.println("Compatible");
						midiKeyBinds.add(configArray[i][0]);
						keyCodes.add(configArray[i][1]);
					}
					
				}
				
				if (!overwritten) {
					midiKeyBinds.add(midiKeyBind);
					keyCodes.add(tempKeyCode.toString());
				}
				
				for (int i = 0; i < midiKeyBinds.size(); i++ ) {
					System.out.print("Line number " + i + ": ");
					System.out.print(midiKeyBinds.get(i) + ", ");
					System.out.println(keyCodes.get(i));
					
				}
				System.out.println(configFile);
				writeFile(configFile, midiKeyBinds, keyCodes);
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
					int previousKey, nextKey, whiteKeyWidth, blackKeyWidth, whiteKeyHeight, blackKeyHeightSmall, blackKeyHeightLarge, blackWhiteGap;

					// I added all these just to make it really easy to change the sizes of rectangles in case I rescale the GUI again.
					// This is because I intend to develop this application to always be a set size and to not be full screened.
					whiteKeyWidth = 20;
					blackKeyWidth = 14;
					whiteKeyHeight = 133;
					blackKeyHeightSmall = 77;
					blackKeyHeightLarge = 81;
					blackWhiteGap = whiteKeyWidth - (blackKeyWidth / 2);
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


