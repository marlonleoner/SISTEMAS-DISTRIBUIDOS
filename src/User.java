import java.rmi.Naming;
import java.rmi.RemoteException;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

/**
 *
 * @author marlonleoner
 */
public class User extends JFrame implements ActionListener {

   private static final long serialVersionUID = 1L;
   // Host URL
   final static String  HOST_URL  = "rmi://localhost/";
   // Host Port
   final static Integer HOST_PORT = 2020;
   // Server
   private IServerChat server;
   // Sala
   private IRoomChat room;
   // Usuário
   private IUserChat user;
   // Nome do Usuário
   private String username;
   // Lista com as salas
   private List<String> roomList;

   // Panel para escolher sala
   private JPanel chatPanel;

   private JPanel     inputPanel;
   private JTextField textField;
   private String     name, message;
   private Font       meiryoFont;
   private Border     blankBorder;

   private JList<String>            list;
   private DefaultListModel<String> listModel;

   protected JTextArea textArea, userArea;
   protected JFrame    frame;
   protected JButton   sendButton;

   public User() throws Exception {
      meiryoFont  = new Font("Meiryo", Font.PLAIN, 14);
      blankBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

      server   = (IServerChat) Naming.lookup(HOST_URL + "Servidor");
      roomList = server.getRooms();

      user     = new UserChat(this);
      username = "Marlon";

      room = (IRoomChat) Naming.lookup(HOST_URL + roomList.get(0));

      frame = new JFrame("[Chat] T2 - Sistemas Distribuidos");

      //-----------------------------------------
      /*
      * intercept close method, inform server we are leaving
      * then let the system exit.
      */
      frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent windowEvent) {
         if(user != null){
            try {
               room.leaveRoom(username);
            }
            catch (Exception e) {
               e.printStackTrace();
            }
         }
         System.exit(0);
      }
      });

      chatPanel = new JPanel(new BorderLayout());

      JPanel infosPanel    = getInfosPanel();
      JPanel messagesPanel = getChatPanel();
      JPanel usersPanel    = getUsersPanel();
      JPanel inputPanel    = getInputMessage();

      JPanel outerPanel = new JPanel(new BorderLayout());

      outerPanel.add(messagesPanel, BorderLayout.CENTER);
      outerPanel.add(usersPanel,    BorderLayout.EAST);

      chatPanel.setBorder(blankBorder);
      chatPanel.add(infosPanel, BorderLayout.NORTH);
      chatPanel.add(outerPanel, BorderLayout.CENTER);
      chatPanel.add(inputPanel, BorderLayout.SOUTH);

      frame.add(chatPanel);
      frame.pack();
      frame.setAlwaysOnTop(true);
      frame.setLocation(150, 150);
      textField.requestFocus();

      frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      frame.setVisible(true);

      room.joinRoom(username, user);
   }

	/**
	 * Main method to start client GUI app.
	 * @param args
	 */
	public static void main(String args[]){
		//set the look and feel to 'Nimbus'
		try{
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
				if("Nimbus".equals(info.getName())){
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
         }

         new User();
		}
		catch(Exception e) {
         System.out.println("> [UserGUI] Error: " + e);
         e.printStackTrace();
      }
   }

	/**
	 * Method to set up the JPanel to display the infos
	 * @return
	 */
	public JPanel getInfosPanel() {
      //* Panel com informações de usuário e da sala *//
      JPanel labelPanel = new JPanel(new BorderLayout());

      JLabel roomLabel = new JLabel();
		roomLabel.setFont(new Font("Meiryo", Font.PLAIN, 28));
      String roomName = "";
      try {
         roomName += room.getRoomName();
      }
      catch(Exception e) {
         roomName += "Room";
         System.out.println("> [UserGUI] Error to get Room Name: " + e);
      }
      roomLabel.setText(roomName);

      JLabel userLabel = new JLabel();
      userLabel.setText(" [ " + username + " ]");

      labelPanel.add(userLabel, BorderLayout.NORTH);
      labelPanel.add(roomLabel, BorderLayout.SOUTH);

      // Panel //
      JPanel buttonsPanel = new JPanel(new BorderLayout());


		return labelPanel;
	}

	/**
	 * Method to set up the JPanel to display the chat text
	 * @return
	 */
	public JPanel getChatPanel() {
      textArea = new JTextArea();
		textArea.setMargin(new Insets(5, 10, 10, 10));
      textArea.setFont(meiryoFont);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
      textArea.setEditable(false);

      JScrollPane scrollPane = new JScrollPane(textArea);

      JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.add(scrollPane, BorderLayout.CENTER);
      textPanel.setFont(new Font("Meiryo", Font.PLAIN, 12));

		return textPanel;
	}

	/**
	 * Method to build the panel with input field
	 * @return inputPanel
	 */
	public JPanel getInputMessage(){
      textField = new JTextField();
      textField.setFont(meiryoFont);
      // Send on enter then clear to prepare for next message
      textField.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               message = textField.getText();
               if(!message.isEmpty()) {
                  textField.setText("");
                  room.sendMsg(username, message);
               }
            }
            catch(Exception ex) {
               System.out.println("> [UserGUI] Error to sendo message: " + ex);
            }
         }
      });

      sendButton = new JButton("Send");
      sendButton.setPreferredSize(new Dimension(150, 40));
		sendButton.addActionListener(this);

      inputPanel = new JPanel(new BorderLayout());
      inputPanel.add(sendButton, BorderLayout.EAST);
      inputPanel.add(textField,  BorderLayout.CENTER);

		return inputPanel;
	}

	/**
	 * Method to build the panel displaying currently connected users
	 * with a call to the button panel building method
	 * @return
	 */
	public JPanel getUsersPanel() {
		JPanel userPanel = new JPanel(new BorderLayout());

		String[] noClientsYet = {"No other users"};

      listModel = new DefaultListModel<String>();

      for(int i = 0; i < roomList.size(); i++)
         listModel.addElement(roomList.get(i));

      list = new JList<String>(listModel);
      list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      list.setVisibleRowCount(8);
      list.setFont(meiryoFont);

      JScrollPane listScrollPane = new JScrollPane(list);
      userPanel.add(listScrollPane, BorderLayout.CENTER);

      userPanel.setPreferredSize(new Dimension(150, chatPanel.getHeight()));

		return userPanel;
	}

	/**
	 * Action handling on the buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e){
		try {
			if(e.getSource() == sendButton){
            message = textField.getText();
            if(!message.isEmpty()) {
               textField.setText("");
               room.sendMsg(username, message);
            }
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
   }

   /* ==================================== */
   public void sendMessage(String sender, String message) {
      textArea.append("[" + sender + "]: " + message + '\n');
      System.out.println("> [UserGUI] " + sender + " sending message: " + message);
   }
}














// Chat no terminal
// public class User {

//    // Host URL
//    final static String  HOST_URL  = "rmi://localhost/";
//    // Host Port
//    final static Integer HOST_PORT = 2020;
//    // Server
//    private IServerChat server;
//    // Usuário
//    private IUserChat user;
//    // Lista com as salas
//    private List<String> roomList;

//    private Scanner in;

//    public User() {
//       roomList = new ArrayList<>();
//       user     = null;

//       in = new Scanner(System.in);
//    }

//    private String getName() {
//       System.out.print("Digite seu nome: ");
//       return in.nextLine();
//    }

//    private void run() {
//       try {
//          String username = getName();
//          this.user       = new UserChat(username);

//          IServerChat server = (IServerChat) Naming.lookup(HOST_URL + "Servidor");
//          roomList = server.getRooms();

//          System.out.println("Listas de Salas");
//          for(int i = 0; i < roomList.size(); i++) {
//             System.out.println((i+1) + ") " + roomList.get(i));
//          }

//          int nRoom = 0;
//          do {
//             System.out.println("Selecione o número da sala: ");
//             nRoom = Integer.parseInt(in.nextLine());

//          }while(nRoom < 0 || nRoom > roomList.size());

//          IRoomChat room = (IRoomChat) Naming.lookup(HOST_URL + "room_" + roomList.get(nRoom - 1));
//          room.joinRoom(username, user);
//          while (true) {

//          }
//       }
//       catch (Exception e) {
//          System.out.println("> [UserGUI] UserGUI failed: " + e);
//       }
//    }
//    public static void main(String[] args) {
//       User u = new User();
//       u.run();
//    }
// }
