import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class UserMain {

   private static final int    RMI_PORT = 2020;
   private String              RMI_HOST;

   private Registry registry;

   private UserGUI GUI;

   private String username;

   private IServerChat server;
   private IRoomChat   room;
   private UserChat    user;

   public static void main(String args[]) {
      try {
         // Set the look and feel to 'Nimbus'
         for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }

         if(args.length != 1) {
            System.out.println("Error! Try: java UserMain <ip>");
            System.exit(-1);
         }

         UserMain main = new UserMain(args[0]);
      } catch (Exception e) {
         System.out.println("> [UserGUI] Error: " + e);
      }
   }

   public UserMain(String host) throws RemoteException, NotBoundException {
      RMI_HOST = "rmi://" + host + "/";
      registry = LocateRegistry.getRegistry(RMI_PORT);
      server   = (IServerChat) registry.lookup(RMI_HOST + "Servidor");

      GUI  = new UserGUI(this);
      user = new UserChat(GUI);

      new Thread(new Runnable() {
         @Override public void run() {
            while (true) {
               try {
                  List<String> rooms = server.getRooms();
                  GUI.attRoomsList(rooms);
                  if(room != null) {
                     if(!rooms.contains(room.getRoomName()))
                        GUI.removeUser();
                  }

                  Thread.sleep(500);
               }
               catch (RemoteException re) {
                  System.out.println("> [UserMain] RemoteException: " + re);
                  re.printStackTrace();
               }
               catch (InterruptedException ie) {
                  System.out.println("> {UserMain] InterruptedException: " + ie);
                  ie.printStackTrace();
               }
            }
         }
      }).start();
   }

   public void setName(String name) {
      username = name;
   }

   public boolean createRoom(String roomName) throws RemoteException, NotBoundException {
      if(roomName == null)
         return true;

      List<String> rooms = server.getRooms();
      String name = roomName.toLowerCase().replace(" ", "_");
      if(!rooms.contains(name)) {
         server.createRoom(name);
         return true;
      }
      return false;
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

   public void removeUser() {
      room = null;
   }
}
