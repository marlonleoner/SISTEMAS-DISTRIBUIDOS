
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class UserMain {

   // PORT
   private static final int RMI_PORT = 2020;
   // URL
   private static final String RMI_HOST = "rmi://localhost/";

   private UserGUI GUI;

   private Registry registry;

   private IServerChat server;

   private IRoomChat room;

   private UserChat user;

   private String username;

   public static void main(String args[]) {
      try {
         // Set the look and feel to 'Nimbus'
         for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
         UserMain main = new UserMain();
      } catch (Exception e) {
         System.out.println("> [UserGUI] Error: " + e);
      }
   }

   public UserMain() throws RemoteException, NotBoundException {
      registry = LocateRegistry.getRegistry(RMI_PORT);
      server   = (IServerChat) registry.lookup(RMI_HOST + "Servidor");

      GUI  = new UserGUI(this);
      user = new UserChat(GUI);

      attRoomsList();
   }

   public void setName(String name) {
      username = name;
   }

   public void createRoom(String roomName) throws RemoteException, NotBoundException {
      server.createRoom(roomName);
   }

   public void joinRoom(String roomName) throws RemoteException, NotBoundException {
      room = (IRoomChat) registry.lookup(RMI_HOST + roomName);
      room.joinRoom(username, user);
   }

   public void leaveRoom() throws RemoteException{
      if(room != null) {
         room.leaveRoom(username);
         room = null;
      }
   }

   public void sendMessage(String message) throws RemoteException {
      if(room != null)
         room.sendMsg(username, message);
   }

   public void attRoomsList() throws RemoteException {
      List<String> roomsList = server.getRooms();
      GUI.attRoomsList(roomsList);
   }
}