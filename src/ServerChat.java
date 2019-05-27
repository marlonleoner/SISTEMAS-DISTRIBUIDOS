import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author MarlonLeoner
 */
public class ServerChat extends UnicastRemoteObject implements IServerChat {

   private static final String HOST_URL = "rmi://localhost/";

   // Lista de salas
   private List<String> roomList;

   public ServerChat() throws RemoteException {
      roomList = new ArrayList<>();
   }

   @Override
   public List<String> getRooms() throws RemoteException {
      return roomList;
   }

   @Override
   public void createRoom(String roomName) throws RemoteException {
      try {
         String    ROOM_URL = HOST_URL + roomName.toLowerCase();
         IRoomChat room     = new RoomChat(roomName.toLowerCase());
         Naming.bind(ROOM_URL, room);
         this.roomList.add(roomName);
         System.out.println("> [Server] Room \'" + roomName + "\' created");
      }
      catch(Exception e) {
         System.out.println("> [Server] Failed to create room \'"+ roomName + "\': " + e);
      }
   }
}
