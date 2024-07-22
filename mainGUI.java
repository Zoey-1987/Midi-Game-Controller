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
import javax.swing.border.MatteBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class mainGUI extends JFrame {

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
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\benco\\Downloads\\Solksingcope.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 550);
		mainPanel = new JPanel();
		mainPanel.setAlignmentY(0.0f);
		mainPanel.setAlignmentX(0.0f);
		mainPanel.setBackground(Color.DARK_GRAY);
		mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

		setContentPane(mainPanel);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[] {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 87, 30, 30, 30};
		gbl_mainPanel.rowHeights = new int[]{0, 0};
		gbl_mainPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0};
		gbl_mainPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
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
		
		// Various features of the minimize button
		
		btnMini.setFocusPainted(false);
		btnMini.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnMini.setBackground(Color.DARK_GRAY);
		btnMini.setForeground(Color.WHITE);
		GridBagConstraints gbc_btnMini = new GridBagConstraints();
		gbc_btnMini.insets = new Insets(0, 0, 0, 5);
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
		gbc_btnMaxi.insets = new Insets(0, 0, 0, 5);
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
			}
		});
		
		// Various features of the exit button
		
		btnExit.setFocusPainted(false);
		btnExit.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnExit.setBackground(Color.DARK_GRAY);
		btnExit.setForeground(Color.WHITE);
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.gridx = 20;
		gbc_btnExit.gridy = 0;
		mainPanel.add(btnExit, gbc_btnExit);
	}

}
