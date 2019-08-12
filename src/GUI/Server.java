package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Server extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Port;
	private List<User> clients;
	private ServerSocket Server;
	private JTextField portField;
	private JTextField hostField;
	private GAbout AboutObj;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public Server() {
		this.Port = 6820;
		this.clients = new ArrayList<User>();

		final JFrame JFrame = new JFrame("Chat");
		JFrame.setTitle("Server");
		JFrame.getContentPane().setLayout(null);

		JLabel label = new JLabel("Server Configuration");
		label.setFont(new Font("Tahoma", Font.BOLD, 13));
		label.setBounds(10, 32, 150, 20);
		JFrame.getContentPane().add(label);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 53, 370, 2);
		JFrame.getContentPane().add(separator);

		JLabel label_1 = new JLabel("Set Source Address");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(10, 68, 126, 15);
		JFrame.getContentPane().add(label_1);

		JLabel label_2 = new JLabel("Set Source Port");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(10, 98, 126, 15);
		JFrame.getContentPane().add(label_2);

		portField = new JTextField();
		portField.setText("6820");
		portField.setFont(new Font("Calibri", Font.PLAIN, 12));
		portField.setColumns(10);
		portField.setBounds(130, 96, 100, 20);
		JFrame.getContentPane().add(portField);

		hostField = new JTextField();
		hostField.setText("localhost");
		hostField.setFont(new Font("Calibri", Font.PLAIN, 12));
		hostField.setColumns(10);
		hostField.setBounds(130, 66, 100, 20);
		JFrame.getContentPane().add(hostField);

		JLabel label_3 = new JLabel("");
		Image server = new ImageIcon(this.getClass().getResource("/server.png")).getImage();
		label_3.setIcon(new ImageIcon(server));
		label_3.setBounds(240, 53, 137, 137);
		JFrame.getContentPane().add(label_3);

		JButton button = new JButton("Establish Server");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					run();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		button.setFont(new Font("Calibri", Font.PLAIN, 13));
		button.setBounds(10, 128, 220, 23);
		JFrame.getContentPane().add(button);

		JButton button_1 = new JButton("Disconnect");
		button_1.setFont(new Font("Calibri", Font.PLAIN, 13));
		button_1.setBounds(10, 162, 220, 23);
		JFrame.getContentPane().add(button_1);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 394, 21);
		JFrame.getContentPane().add(menuBar);

		JMenu menu = new JMenu("Home");
		menuBar.add(menu);

		JMenuItem menuItem = new JMenuItem("About");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutObj = new GAbout();
				AboutObj.setVisible(true);
			}
		});
		menu.add(menuItem);

		JSeparator separator_1 = new JSeparator();
		menu.add(separator_1);

		JMenuItem menuItem_2 = new JMenuItem("Exit");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		menu.add(menuItem_2);
		JFrame.setSize(400, 240);
		JFrame.setResizable(false);
		JFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

		JFrame.setVisible(true);

	}	

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("unused")
		Server server = new Server();
	}

	public void run() throws IOException {
		Server = new ServerSocket(Port) {
			protected void finalize() throws IOException {
				this.close();
			}
		};
		System.out.println("Port 6280 is now open.");

		while (true) {
			// accepts a new client
			Socket client = Server.accept();

			// get nickname of newUser
			@SuppressWarnings("resource")
			String nickname = (new Scanner ( client.getInputStream() )).nextLine();
			nickname = nickname.replace(",", ""); //  ',' use for serialisation
			nickname = nickname.replace(" ", "_");
			System.out.println("New Client: \"" + nickname + "\"\n\t     Host:" + client.getInetAddress().getHostAddress());

			// create new User
			User newUser = new User(client, nickname);

			// add newUser message to list
			this.clients.add(newUser);

			// Welcome msg
			newUser.getOutStream().println("<b>Welcome</b> " + newUser.toString());

			// create a new thread for newUser incoming messages handling
			new Thread(new UserHandler(this, newUser)).start();
		}
	}
	// delete a user from the list
	public void removeUser(User user){
		this.clients.remove(user);
	}

	// send incoming msg to all Users
	public void broadcastMessages(String msg, User userSender) {
		for (User client : this.clients) {
			client.getOutStream().println(
					userSender.toString() + "<span>: " + msg+"</span>");
		}
	}

	// send list of clients to all Users
	public void broadcastAllUsers(){
		for (User client : this.clients) {
			client.getOutStream().println(this.clients);
		}
	}

	// send message to a User (String)
	public void sendMessageToUser(String msg, User userSender, String user){
		boolean find = false;
		for (User client : this.clients) {
			if (client.getNickname().equals(user) && client != userSender) {
				find = true;
				userSender.getOutStream().println(userSender.toString() + " -> " + client.toString() +": " + msg);
				client.getOutStream().println(
						"(<b>Private</b>)" + userSender.toString() + "<span>: " + msg+"</span>");
			}
		}
		if (!find) {
			userSender.getOutStream().println(userSender.toString() + " -> (<b>no one!</b>): " + msg);
		}
	}
}
class UserHandler implements Runnable {

	private Server server;
	private User user;

	public UserHandler(Server server, User user) {
		this.server = server;
		this.user = user;
		this.server.broadcastAllUsers();
	}

	public void run() {
		String message;

		// when there is a new message, broadcast to all
		Scanner sc = new Scanner(this.user.getInputStream());
		while (sc.hasNextLine()) {
			message = sc.nextLine();
			// Gestion des messages private
			if (message.charAt(0) == '@'){
				if(message.contains(" ")){
					System.out.println("private msg : " + message);
					int firstSpace = message.indexOf(" ");
					String userPrivate= message.substring(1, firstSpace);
					server.sendMessageToUser(
							message.substring(
									firstSpace+1, message.length()
									), user, userPrivate
							);
				}

				// Gestion du changement
			}else if (message.charAt(0) == '#'){
				user.changeColor(message);
				// update color for all other users
				this.server.broadcastAllUsers();
			}else{
				// update user list
				server.broadcastMessages(message, user);
			}
		}
		// end of Thread
		server.removeUser(user);
		this.server.broadcastAllUsers();
		sc.close();
	}
}
class User {
	private PrintStream streamOut;
	private InputStream streamIn;
	private String nickname;
	@SuppressWarnings("unused")
	private Socket client;
	String color;

	// constructor
	public User(Socket client, String name) throws IOException {
		this.streamOut = new PrintStream(client.getOutputStream());
		this.streamIn = client.getInputStream();
		this.client = client;
		this.nickname = name;
	}

	// change color user
	public void changeColor(String hexColor){
		// check if it's a valid hexColor
		Pattern colorPattern = Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})");
		Matcher m = colorPattern.matcher(hexColor);
		if (m.matches()){
			Color c = Color.decode(hexColor);
			// if the Color is too Bright don't change
			double luma = 0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue(); // per ITU-R BT.709
			if (luma > 160) {
				this.getOutStream().println("<b>Color Too Bright</b>");
				return;
			}
			this.color = hexColor;
			this.getOutStream().println("<b>Color changed successfully</b> " + this.toString());
			return;
		}
		this.getOutStream().println("<b>Failed to change color</b>");
	}

	// Getters
	public PrintStream getOutStream(){
		return this.streamOut;
	}

	public InputStream getInputStream(){
		return this.streamIn;
	}

	public String getNickname(){
		return this.nickname;
	}

	// Print user name
	public String toString(){

		return this.getNickname();
	}

}
