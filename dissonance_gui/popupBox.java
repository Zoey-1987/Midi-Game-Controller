package dissonance_gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Window.Type;

public class popupBox extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	Color toolbarGreen = new Color(104, 100, 49);
	private JPanel pnlToolBar;
	int xCursor;
	int yCursor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					popupBox frame = new popupBox();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public popupBox() {
		setType(Type.POPUP);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setBounds(0, 0, 450, 300);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		FlowLayout flowLayout = (FlowLayout) contentPane.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));

		setContentPane(contentPane);
		setupToolbar(contentPane);
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
		int baseX = 0, baseY = 0, baseW = 450, baseH = 26;
		pnlToolBar.setBounds(baseX, baseY, baseW, baseH);
		pnlToolBar.setPreferredSize(new Dimension(baseW, baseH));
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

}
