import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.util.List;
import java.util.ArrayList;

public class ServerChat extends UnicastRemoteObject implements IServerChat {

   // PORT
   private static final int RMI_PORT = 2020;
   // URL
   private static final String RMI_HOST = "rmi://localhost/";

   // Registry
   private Registry registry;
   // Lista de salas
   private List<String> roomList;

   // Constructor
   public ServerChat(String host) throws RemoteException, AlreadyBoundException {
      roomList = new ArrayList<>();
      RegistryServer(host);
   }

   /*****************
    * Local Methods *
    *****************/

   public static void main(String[] args) {
      String host = "localhost";

      if (args.length == 1)
         host = args[0];

      try {
         IServerChat server = new ServerChat(host);

         // [Debug] Inicialização de duas salas
         server.createRoom("room_1");
         server.createRoom("room_2");
      } catch (Exception e) {
         System.out.println("> [System] Server failed: " + e);
      }
   }

   /**
    * Start the RMI Registry
    */
   public void RegistryServer(String hostName) throws RemoteException, AlreadyBoundException {
      System.out.println("> [System] Starting Server...");

      registry = LocateRegistry.createRegistry(RMI_PORT);
      registry.bind(RMI_HOST + "Servidor", this);

      System.out.println("> [System] Server Online!");
   }

   /******************
    * Remote Methods *
    ******************/

   @Override
   public List<String> getRooms() throws RemoteException {
      return roomList;
   }

   @Override
   public void createRoom(String roomName) throws RemoteException {
      String name = roomName.toLowerCase().replace(" ", "_");
      try {
         IRoomChat room = new RoomChat(name);
         registry.bind(RMI_HOST + name, room);
         roomList.add(name);
         System.out.println("> [Server] Room \'" + name + "\' created");
      } catch (Exception e) {
         System.out.println("> [Server] Failed to create room \'" + name + "\': " + e);
      }
   }
}
