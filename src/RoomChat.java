import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class RoomChat extends UnicastRemoteObject implements IRoomChat {

   // Nome da Sala
   private String name;
   // Lista de usuários
   private Map<String, IUserChat> userList;

   public RoomChat(String name) throws RemoteException {
      this.name = name;
      userList  = new HashMap<>();
   }

   @Override
   public void joinRoom(String userName, IUserChat user) throws RemoteException {
      String sender  = "Server";
      String message = userName + " joined at room \'" + name + "\'";
      System.out.println("> [" + sender + "] " + message);

      userList.put(userName, user);
      sendMsg(sender, message);
      // for (String key : userList.keySet()) {
      //    userList.get(key).deliverMsg(sender, message);
      // }
   }

   @Override
   public void sendMsg(String userName, String message) throws RemoteException {
      System.out.println("> [" + userName + "] " + message  );

      for (String key : userList.keySet()) {
         userList.get(key).deliverMsg(userName, message);
      }
   }

   @Override
   public void leaveRoom(String usrName) throws RemoteException {
      String sender  = "Server";
      String message = usrName + " left the room \'" + name + "\'";

      if(userList.containsKey(usrName)) {
         sendMsg(sender, message);
         userList.remove(usrName);
      }
   }

   @Override
   public void closeRoom() throws RemoteException {
      sendMsg("Room", "room closed by server");
      userList.clear();
   }

   @Override
   public String getRoomName() throws RemoteException {
      return name;
   }
}
