import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.util.List;
import java.util.ArrayList;

public class ServerChat extends UnicastRemoteObject implements IServerChat {

   private static final String RMI_HOST = "rmi://localhost/";

   // Registry
   private Registry registry;
   // Lista de salas
   private List<String> roomList;

   // Constructor
   public ServerChat(Registry registry) throws RemoteException, AlreadyBoundException {
      roomList = new ArrayList<>();
      this.registry = registry;
   }

   /*****************
    * Local Methods *
    *****************/

   public void removeRoom(String room) throws RemoteException, NotBoundException {
      roomList.remove(room);
      registry.unbind(RMI_HOST + room);
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
      if(roomName != null) {
         try {
            IRoomChat room = new RoomChat(roomName);
            registry.bind(RMI_HOST + roomName, room);
            roomList.add(roomName);
            System.out.println("> [Server] Room \'" + roomName + "\' created");
         }
         catch (Exception e) {
            System.out.println("> [Server] Failed to create room \'" + roomName + "\': " + e);
            e.printStackTrace();
         }
      }
   }
}
