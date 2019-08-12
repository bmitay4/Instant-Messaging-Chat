package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.*;

import java.util.ArrayList;
import java.util.Arrays;


public class GMain extends Thread{

  final JTextPane ChatField = new JTextPane();
  final JTextPane UsersField = new JTextPane();
  final JTextField jtextInputChat = new JTextField();
  private String oldMsg = "";
  private Thread read;
  private String serverName;
  private int PORT;
  private String name;
  BufferedReader input;
  PrintWriter output;
  Socket server;
  GAbout AboutObj;

  public GMain() {
    this.serverName = "localhost";
    this.PORT = 6820;
    this.name = "nickname";

    String fontfamily = "Calibri, sans-serif";
    Font font = new Font(fontfamily, Font.PLAIN, 15);

    final JFrame JFrame = new JFrame("Chat");
    JFrame.setTitle("IBM Chat (Ariel University)");
    JFrame.getContentPane().setLayout(null);
    JFrame.setSize(575, 375);
    JFrame.setResizable(false);
    JFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

    ChatField.setBounds(25, 25, 490, 320);
    ChatField.setFont(font);
    ChatField.setMargin(new Insets(6, 6, 6, 6));
    ChatField.setEditable(false);
    JScrollPane scrollPaneChat = new JScrollPane(ChatField);
    scrollPaneChat.setBounds(10, 31, 432, 200);

    ChatField.setContentType("text/html");
    ChatField.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    UsersField.setBounds(520, 25, 156, 320);
    UsersField.setEditable(true);
    UsersField.setFont(font);
    UsersField.setMargin(new Insets(6, 6, 6, 6));
    UsersField.setEditable(false);
    JScrollPane scrollPaneUsers = new JScrollPane(UsersField);
    scrollPaneUsers.setBounds(452, 31, 107, 200);

    UsersField.setContentType("text/html");
    UsersField.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    //Sending message field
    jtextInputChat.setBounds(0, 350, 400, 50);
    jtextInputChat.setFont(font);
    jtextInputChat.setMargin(new Insets(6, 6, 6, 6));
    final JScrollPane jtextInputChatSP = new JScrollPane(jtextInputChat);
    jtextInputChatSP.setBounds(10, 239, 432, 50);

    final JButton jsbtn = new JButton("Send");
    jsbtn.setFont(new Font("Calibri", Font.BOLD, 14));
    jsbtn.setBounds(452, 239, 107, 22);

    final JButton jsbtndeco = new JButton("Disconnect");
    jsbtndeco.setFont(new Font("Calibri", Font.BOLD, 14));
    jsbtndeco.setBounds(452, 266, 107, 22);

    jtextInputChat.addKeyListener(new KeyAdapter() {
      //Sending message on Enter key
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          sendMessage();
        }

        // Get last message typed
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          String currentMessage = jtextInputChat.getText().trim();
          jtextInputChat.setText(oldMsg);
          oldMsg = currentMessage;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          String currentMessage = jtextInputChat.getText().trim();
          jtextInputChat.setText(oldMsg);
          oldMsg = currentMessage;
        }
      }
    });

    // Click on send button
    jsbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        sendMessage();
      }
    });

    // Connection view
    final JTextField UsernameField = new JTextField(this.name);
    final JTextField PortField = new JTextField(Integer.toString(this.PORT));
    PortField.setFont(new Font("Calibri", Font.PLAIN, 12));
    final JTextField HostField = new JTextField(this.serverName);
    HostField.setFont(new Font("Calibri", Font.PLAIN, 12));
    final JButton ConnectButton = new JButton("Login");

    // check if those field are not empty
    UsernameField.getDocument().addDocumentListener(new TextListener(UsernameField, PortField, HostField, ConnectButton));
    PortField.getDocument().addDocumentListener(new TextListener(UsernameField, PortField, HostField, ConnectButton));
    HostField.getDocument().addDocumentListener(new TextListener(UsernameField, PortField, HostField, ConnectButton));

    ConnectButton.setFont(new Font("Calibri", Font.BOLD, 15));
    HostField.setBounds(362, 244, 80, 20);
    UsernameField.setBounds(362, 294, 80, 20);
    PortField.setBounds(362, 269, 80, 20);
    ConnectButton.setBounds(452, 294, 107, 20);

    ChatField.setBackground(Color.LIGHT_GRAY);
    UsersField.setBackground(Color.LIGHT_GRAY);

    JFrame.getContentPane().add(ConnectButton);
    JFrame.getContentPane().add(scrollPaneChat);
    JFrame.getContentPane().add(scrollPaneUsers);
    JFrame.getContentPane().add(UsernameField);
    JFrame.getContentPane().add(PortField);
    JFrame.getContentPane().add(HostField);
    
    JLabel lblHost_1 = new JLabel("Host");
    lblHost_1.setFont(new Font("Calibri", Font.BOLD, 14));
    lblHost_1.setBounds(297, 247, 36, 14);
    JFrame.getContentPane().add(lblHost_1);
    
    JLabel lblHost = new JLabel("Port");
    lblHost.setFont(new Font("Calibri", Font.BOLD, 14));
    lblHost.setBounds(297, 272, 36, 14);
    JFrame.getContentPane().add(lblHost);
    
    JLabel lblPort = new JLabel("UserName");
    lblPort.setFont(new Font("Calibri", Font.BOLD, 14));
    lblPort.setBounds(297, 297, 67, 14);
    JFrame.getContentPane().add(lblPort);
    
    JLabel lblChat = new JLabel("Chat");
    lblChat.setFont(new Font("Calibri", Font.BOLD, 16));
    lblChat.setBounds(10, 11, 41, 14);
    JFrame.getContentPane().add(lblChat);
    
    JLabel lblUsersOnline = new JLabel("Users Online");
    lblUsersOnline.setFont(new Font("Calibri", Font.BOLD, 16));
    lblUsersOnline.setBounds(452, 11, 107, 14);
    JFrame.getContentPane().add(lblUsersOnline);
    
    JLabel lblLogin = new JLabel("");
	Image login = new ImageIcon(this.getClass().getResource("/login.png")).getImage();
	lblLogin.setIcon(new ImageIcon(login));
    lblLogin.setBounds(481, 242, 50, 50);
    JFrame.getContentPane().add(lblLogin);
    
    JLabel lblInstruction = new JLabel("Instructions");
    lblInstruction.setFont(new Font("Calibri", Font.BOLD, 16));
    lblInstruction.setBounds(10, 242, 90, 14);
    JFrame.getContentPane().add(lblInstruction);
    
    JLabel lblInstruction1 = new JLabel("Enter the IP address of the server and the");
    lblInstruction1.setFont(new Font("Calibri", Font.PLAIN, 12));
    lblInstruction1.setBounds(10, 260, 277, 14);
    JFrame.getContentPane().add(lblInstruction1);
    
    JLabel lblInstruction2 = new JLabel("port number. Then, select a nickname and click");
    lblInstruction2.setFont(new Font("Calibri", Font.PLAIN, 12));
    lblInstruction2.setBounds(10, 278, 277, 14);
    JFrame.getContentPane().add(lblInstruction2);
    
    JLabel lblInstruction3 = new JLabel("Login");
    lblInstruction3.setFont(new Font("Calibri", Font.PLAIN, 12));
    lblInstruction3.setBounds(10, 296, 277, 14);
    JFrame.getContentPane().add(lblInstruction3);
    
    JMenuBar menuBar = new JMenuBar();
    JFrame.setJMenuBar(menuBar);
    
    JMenu mnHome = new JMenu("Home");
    menuBar.add(mnHome);
    
    JMenuItem mntmAbout = new JMenuItem("About");
    mntmAbout.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent arg0) {
    		AboutObj = new GAbout();
    		AboutObj.setVisible(true);
    	}
    });
    mnHome.add(mntmAbout);
    
    JSeparator separator = new JSeparator();
    mnHome.add(separator);
    
    JMenuItem mntmExit = new JMenuItem("Exit");
    mntmExit.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		System.exit(1);
    	}
    });
    mnHome.add(mntmExit);
    JFrame.setVisible(true);

    // On connect
    ConnectButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        try {
          name = UsernameField.getText();
          String port = PortField.getText();
          serverName = HostField.getText();
          PORT = Integer.parseInt(port);

          appendToPane(ChatField, "<span>Connecting to " + serverName + " on port " + PORT + "...</span>");
          server = new Socket(serverName, PORT);

          appendToPane(ChatField, "<span>Connected to " +
              server.getRemoteSocketAddress()+"</span>");

          input = new BufferedReader(new InputStreamReader(server.getInputStream()));
          output = new PrintWriter(server.getOutputStream(), true);

          // send nickname to server
          output.println(name);

          // create new Read Thread
          read = new Read();
          read.start();
          JFrame.remove(UsernameField);
          JFrame.remove(PortField);
          JFrame.remove(HostField);
          JFrame.remove(ConnectButton);
          JFrame.remove(lblHost_1);
          JFrame.remove(lblHost);
          JFrame.remove(lblPort);
          JFrame.remove(lblInstruction1);
          JFrame.remove(lblInstruction2);
          JFrame.remove(lblInstruction3);
          JFrame.remove(lblInstruction);
          JFrame.remove(lblLogin);

          JFrame.getContentPane().add(jsbtn);
          JFrame.getContentPane().add(jtextInputChatSP);
          JFrame.getContentPane().add(jsbtndeco);
          JFrame.revalidate();
          JFrame.repaint();
          ChatField.setBackground(Color.WHITE);
          UsersField.setBackground(Color.WHITE);
        } catch (Exception ex) {
          appendToPane(ChatField, "<span>Could not connect to Server</span>");
          JOptionPane.showMessageDialog(JFrame, ex.getMessage());
        }
      }

    });

    // on disconnect
    jsbtndeco.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent ae) {
        JFrame.getContentPane().add(UsernameField);
        JFrame.getContentPane().add(PortField);
        JFrame.getContentPane().add(HostField);
        JFrame.getContentPane().add(ConnectButton);
        JFrame.getContentPane().add(lblHost_1);
        JFrame.getContentPane().add(lblHost);
        JFrame.getContentPane().add(lblPort);
        JFrame.getContentPane().add(lblInstruction1);
        JFrame.getContentPane().add(lblInstruction2);
        JFrame.getContentPane().add(lblInstruction3);
        JFrame.getContentPane().add(lblInstruction);
        JFrame.getContentPane().add(lblLogin);
        
        JFrame.remove(jsbtn);
        JFrame.remove(jtextInputChatSP);
        JFrame.remove(jsbtndeco);
        JFrame.revalidate();
        JFrame.repaint();
        read.interrupt();
        UsersField.setText(null);
        ChatField.setBackground(Color.LIGHT_GRAY);
        UsersField.setBackground(Color.LIGHT_GRAY);
        appendToPane(ChatField, "<span>Connection closed.</span>");
        output.close();
      }
    });

  }

  // check if if all field are not empty
  public class TextListener implements DocumentListener{
    JTextField jtf1;
    JTextField jtf2;
    JTextField jtf3;
    JButton jcbtn;

    public TextListener(JTextField jtf1, JTextField jtf2, JTextField jtf3, JButton jcbtn){
      this.jtf1 = jtf1;
      this.jtf2 = jtf2;
      this.jtf3 = jtf3;
      this.jcbtn = jcbtn;
    }

    public void changedUpdate(DocumentEvent e) {}

    public void removeUpdate(DocumentEvent e) {
      if(jtf1.getText().trim().equals("") ||
          jtf2.getText().trim().equals("") ||
          jtf3.getText().trim().equals("")
          ){
        jcbtn.setEnabled(false);
      }else{
        jcbtn.setEnabled(true);
      }
    }
    public void insertUpdate(DocumentEvent e) {
      if(jtf1.getText().trim().equals("") ||
          jtf2.getText().trim().equals("") ||
          jtf3.getText().trim().equals("")
          ){
        jcbtn.setEnabled(false);
      }else{
        jcbtn.setEnabled(true);
      }
    }

  }

  public void sendMessage() {
    try {
      String message = jtextInputChat.getText().trim();
      if (message.equals("")) {
        return;
      }
      this.oldMsg = message;
      output.println(message);
      jtextInputChat.requestFocus();
      jtextInputChat.setText(null);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage());
      System.exit(0);
    }
  }

  public static void main(String[] args) throws Exception {
    @SuppressWarnings("unused")
	GMain client = new GMain();
  }

  // read new incoming messages
  class Read extends Thread {
    public void run() {
      String message;
      while(!Thread.currentThread().isInterrupted()){
        try {
          message = input.readLine();
          if(message != null){
            if (message.charAt(0) == '[') {
              message = message.substring(1, message.length()-1);
              ArrayList<String> ListUser = new ArrayList<String>(
                  Arrays.asList(message.split(", "))
                  );
              UsersField.setText(null);
              for (String user : ListUser) {
                appendToPane(UsersField, "@" + user);
              }
            }else{
              appendToPane(ChatField, message);
            }
          }
        }
        catch (IOException ex) {
          System.err.println("Failed to parse incoming message");
        }
      }
    }
  }

  // send html to pane
  private void appendToPane(JTextPane tp, String msg){
    HTMLDocument doc = (HTMLDocument)tp.getDocument();
    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
    try {
      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
      tp.setCaretPosition(doc.getLength());
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}