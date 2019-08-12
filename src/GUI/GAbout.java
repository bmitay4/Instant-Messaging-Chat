package GUI;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSeparator;

public class GAbout extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GAbout frame = new GAbout();
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
	public GAbout() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAbout = new JLabel("About");
		lblAbout.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAbout.setBounds(10, 11, 71, 20);
		contentPane.add(lblAbout);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 32, 414, 2);
		contentPane.add(separator);
		
		JLabel label_1 = new JLabel("");
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
			}
		});
		Image back = new ImageIcon(this.getClass().getResource("/back.png")).getImage();
		label_1.setIcon(new ImageIcon(back));
		label_1.setBounds(401, 11, 23, 20);
		contentPane.add(label_1);
		
		JLabel lblNewLabel = new JLabel("This chat was built as part of the course of communication and computing");
		lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 42, 414, 20);
		contentPane.add(lblNewLabel);
		
		JLabel lblAtArielUniversity = new JLabel("at Ariel University, 2018 by IBM.");
		lblAtArielUniversity.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblAtArielUniversity.setBounds(10, 58, 385, 20);
		contentPane.add(lblAtArielUniversity);
		
		JLabel lblAllRights = new JLabel("\u00A9 All rights reserved");
		lblAllRights.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblAllRights.setBounds(10, 231, 385, 20);
		contentPane.add(lblAllRights);
		
		JLabel lblThisChatAllows = new JLabel("This chat allows a conversation between multiple participants at once,\r\n");
		lblThisChatAllows.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblThisChatAllows.setBounds(10, 89, 414, 20);
		contentPane.add(lblThisChatAllows);
		
		JLabel lblOnceAsWell = new JLabel("as well as a private conversation between two participants.");
		lblOnceAsWell.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblOnceAsWell.setBounds(10, 104, 385, 20);
		contentPane.add(lblOnceAsWell);
		
		JLabel lblNoteIGive = new JLabel("Note, I give huge credit to Drakirus that I've learned a lot from his ");
		lblNoteIGive.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblNoteIGive.setBounds(10, 131, 385, 20);
		contentPane.add(lblNoteIGive);
		
		JLabel lblProjectsYouCan = new JLabel("projects.\r\n You can learn from his GitHub https://github.com/Drakirus");
		lblProjectsYouCan.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblProjectsYouCan.setBounds(10, 149, 385, 20);
		contentPane.add(lblProjectsYouCan);
		
		JLabel lblAnotherCadreI = new JLabel("Another Cadre I give the director channels YouTube \"einstein06\" and");
		lblAnotherCadreI.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblAnotherCadreI.setBounds(10, 175, 385, 20);
		contentPane.add(lblAnotherCadreI);
		
		JLabel lblcodeLabsI = new JLabel("\"Code Labs\". I was helped by their guides in carrying out this project.");
		lblcodeLabsI.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblcodeLabsI.setBounds(10, 192, 385, 20);
		contentPane.add(lblcodeLabsI);
	}
}
