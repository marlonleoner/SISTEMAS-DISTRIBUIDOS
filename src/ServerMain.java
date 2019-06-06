
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.UIManager;

public class ServerMain {

   private static final int    RMI_PORT = 2020;
   private static final String RMI_HOST = "rmi://localhost/";

   private Registry registry;

   private ServerGUI GUI;

   private ServerChat server;
   private IRoomChat  room;

   public static void main(String[] args) {
      try {
         for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }

         ServerMain main = new ServerMain();
      } catch (Exception e) {
         System.out.println("> [System] Server failed: " + e);
      }
   }

   public ServerMain() throws RemoteException, AlreadyBoundException {
      RegistryServer();

      GUI = new ServerGUI(this);
      GUI.attListRooms(server.getRooms());
      GUI.setVisible(true);

      new Thread(new Runnable() {
         @Override public void run() {
            while (true) {
               try {
                  GUI.attListRooms(server.getRooms());
                  Thread.sleep(500);
               }
               catch (RemoteException re) {
                  System.out.println("> [UserMain] RemoteException: " + re);
                  re.printStackTrace();
               }
               catch (InterruptedException ie) {
                  System.out.println("> [UserMain] InterruptedException: " + ie);
                  ie.printStackTrace();
               }
            }
         }
      }).start();
   }

   private void RegistryServer() throws RemoteException, AlreadyBoundException {
      System.out.println("> [System] Starting Server...");

      registry = LocateRegistry.createRegistry(RMI_PORT);

      server = new ServerChat(registry);
      registry.bind(RMI_HOST + "Servidor", server);

      System.out.println("> [System] Server Online!");
   }


   public void closeRoom(String roomName) throws RemoteException, NotBoundException {
      try {
         room = (IRoomChat) registry.lookup(RMI_HOST + roomName);
      }
      catch(NotBoundException e) {
         System.out.println("> [Server] NotBoundException: " + e);
         e.printStackTrace();
      }

      room.closeRoom();
      server.removeRoom(roomName);
      GUI.attListRooms(server.getRooms());
   }
}
