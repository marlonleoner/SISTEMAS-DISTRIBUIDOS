import java.util.List;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserGUI extends JFrame implements ActionListener {

   private UserMain main;

   // Variables
   private String message;
   private String username;
   private String roomname;

   public UserGUI(UserMain main) {
      roomname = "Choose a Room";
      username = getUserName();
      initComponents();

      this.main = main;
      main.setName(username);
   }

   public void initComponents() {
      meiryoFont = new Font("Meiryo", Font.PLAIN, 14);
      blankBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

      frame = new JFrame("[Chat] T2 - Sistemas Distribuidos");

      /*
       * intercept close method, inform server we are leaving then let the system
       * exit.
       */
      frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent windowEvent) {
            try {
               main.leaveRoom();
            }
            catch (Exception e) {
               e.printStackTrace();
            }

            System.exit(0);
         }
      });

      chatPanel = new JPanel(new BorderLayout());

      infosPanel    = getInfosPanel();
      messagesPanel = getChatPanel();
      inputPanel    = getInputMessage();
      roomsPanel    = getRoomsPanel();

      JPanel outerPanel = new JPanel(new BorderLayout());

      outerPanel.add(infosPanel,    BorderLayout.NORTH);
      outerPanel.add(messagesPanel, BorderLayout.CENTER);
      outerPanel.add(inputPanel,    BorderLayout.SOUTH);

      chatPanel.setBorder(blankBorder);
      chatPanel.add(outerPanel, BorderLayout.CENTER);
      chatPanel.add(roomsPanel, BorderLayout.EAST);

      frame.add(chatPanel);
      frame.pack();
      frame.setAlwaysOnTop(true);
      frame.setLocation(150, 150);
      inputTextMsg.requestFocus();

      frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      frame.setVisible(true);
   }

   /**
    * Method to set up the JPanel to display the infos, like username and roomname
    *
    * @return
    */
   public JPanel getInfosPanel() {
      // * Panel com informações de usuário e da sala *//
      JPanel labelPanel = new JPanel(new BorderLayout());

      roomLabel = new JLabel();
      roomLabel.setFont(new Font("Meiryo", Font.PLAIN, 28));
      roomLabel.setText(roomname);

      JLabel userLabel = new JLabel();
      userLabel.setText(" [ " + username + " ]");

      labelPanel.add(userLabel, BorderLayout.NORTH);
      labelPanel.add(roomLabel, BorderLayout.SOUTH);

      return labelPanel;
   }

   /**
    * Method to set up the JPanel to display the chat text
    *
    * @return
    */
   public JPanel getChatPanel() {
      messagesArea = new JTextArea();
      messagesArea.setMargin(new Insets(5, 10, 10, 10));
      messagesArea.setFont(meiryoFont);
      messagesArea.setLineWrap(true);
      messagesArea.setWrapStyleWord(true);
      messagesArea.setEditable(false);

      JScrollPane scrollPane = new JScrollPane(messagesArea);

      JPanel textPanel = new JPanel(new BorderLayout());
      textPanel.add(scrollPane, BorderLayout.CENTER);
      textPanel.setFont(new Font("Meiryo", Font.PLAIN, 12));

      return textPanel;
   }

   /**
    * Method to build the panel with input field
    *
    * @return inputPanel
    */
   public JPanel getInputMessage() {
      inputTextMsg = new JTextField();
      inputTextMsg.setFont(meiryoFont);
      inputTextMsg.setEnabled(false);
      // Send on enter then clear to prepare for next message
      inputTextMsg.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               message = inputTextMsg.getText();
               if (!message.isEmpty()) {
                  inputTextMsg.setText("");
                  main.sendMessage(message);
               }
            } catch (Exception ex) {
               System.out.println("> [UserGUI] Error to sendo message: " + ex);
            }
         }
      });

      sendButton = new JButton("Send");
      sendButton.setPreferredSize(new Dimension(150, 40));
      sendButton.addActionListener(this);
      sendButton.setEnabled(false);

      JPanel inputPanel = new JPanel(new BorderLayout());
      inputPanel.add(sendButton, BorderLayout.EAST);
      inputPanel.add(inputTextMsg, BorderLayout.CENTER);

      return inputPanel;
   }

   /**
    * Method to build the panel displaying currently connected users with a call to
    * the button panel building method
    *
    * @return
    */
   public JPanel getRoomsPanel() {
      JPanel roomsPanel = new JPanel(new BorderLayout());

      String[] noClientsYet = { "No other users" };

      listModel = new DefaultListModel<String>();

      for (int i = 0; i < 5; i++)
         listModel.addElement("Sala" + i);

      list = new JList<String>(listModel);
      list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      list.setVisibleRowCount(8);
      list.setFont(meiryoFont);
      list.addListSelectionListener(new ListSelectionListener(){
         @Override
         public void valueChanged(ListSelectionEvent e) {
            actionRoomButton.setEnabled(true);
         }
      });

      JScrollPane listScrollPane = new JScrollPane(list);

      // Buttons Panel //
      JPanel buttonsPanel = new JPanel(new BorderLayout());

      createRoomButton = new JButton("Create");
      createRoomButton.setPreferredSize(new Dimension(150, 29));
      createRoomButton.addActionListener(this);

      actionRoomButton = new JButton("Join");
      actionRoomButton.setPreferredSize(new Dimension(150, 30));
      actionRoomButton.addActionListener(this);
      actionRoomButton.setEnabled(false);

      buttonsPanel.add(createRoomButton, BorderLayout.NORTH);
      buttonsPanel.add(actionRoomButton, BorderLayout.CENTER);

      roomsPanel.add(buttonsPanel,   BorderLayout.NORTH);
      roomsPanel.add(listScrollPane, BorderLayout.CENTER);
      roomsPanel.setPreferredSize(new Dimension(150, chatPanel.getHeight()));

      return roomsPanel;
   }

   /**
    * Action handling on the buttons
    */
   @Override
   public void actionPerformed(ActionEvent e) {
      try {
         JButton button = (JButton) e.getSource();
         if(button == sendButton) {
            message = inputTextMsg.getText();
            if (!message.isEmpty()) {
               inputTextMsg.setText("");
               // room.sendMsg(username, message);
            }
         }
         else if(button == actionRoomButton) {
            Boolean isJoinButton = actionRoomButton.getText().equals("Join");
            if(isJoinButton) {
               actionRoomButton.setText("Exit");
               roomname = list.getSelectedValue();
               main.joinRoom(roomname);
            }
            else {
               actionRoomButton.setText("Join");
               roomname = "Choose a Room";
               list.clearSelection();
               main.leaveRoom();
            }
            list.setEnabled(!isJoinButton);
            actionRoomButton.setEnabled(isJoinButton);
            inputTextMsg.setEnabled(isJoinButton);
            sendButton.setEnabled(isJoinButton);
            roomLabel.setText(roomname);
            inputTextMsg.setText("");
         }
         else if(button == createRoomButton) {
            main.createRoom(getNewRoomName());
         }
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   /**
    *
    * @return String Username
    */
   private String getUserName() {
      return JOptionPane.showInputDialog(
         frame,
         "Choose an user name:",
         "New User",
         JOptionPane.PLAIN_MESSAGE
      );
   }

   /**
    *
    * @return String Username
    */
   private String getNewRoomName() {
      return JOptionPane.showInputDialog(
         frame,
         "Choose a room name:",
         "New Room",
         JOptionPane.PLAIN_MESSAGE
      );
   }

   /***********
    * Methods *
    ***********/

   /**
    *
    * @param sender
    * @param message
    */
   public void receiveMessage(String sender, String message) {
      messagesArea.append("[" + sender + "]: " + message + '\n');
      System.out.println("> [UserGUI] " + sender + " sending message: " + message);
   }

   public void attRoomsList(List<String> roomsList) {
      DefaultListModel rooms = new DefaultListModel();

      for (int i = 0; i < roomsList.size(); i++) {
         String roomName = roomsList.get(i);
         if(!roomName.equals("servidor")) {
            rooms.addElement(roomName);
         }
      }

      list.setModel(rooms);
   }

   /*************************
    * Variables declaration *
    *************************/
   // Panel para escolher sala
   private JPanel chatPanel;

   // Chat Infos
   private JPanel infosPanel;
   private JLabel roomLabel;

   // Rooms Infos
   private JPanel roomsPanel;
   private JButton createRoomButton, actionRoomButton;
   private JList<String> list;
   private DefaultListModel<String> listModel;

   // Chat Messages
   private JPanel messagesPanel;
   protected JTextArea messagesArea;

   // Messages Input
   private JPanel     inputPanel;
   private JTextField inputTextMsg;
   private JButton    sendButton;

   // Main Frame
   private JFrame frame;
   private Font   meiryoFont;
   private Border blankBorder;
}
