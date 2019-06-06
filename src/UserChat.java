import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserChat extends UnicastRemoteObject implements IUserChat {

   private UserGUI GUI;

   public UserChat(UserGUI userGUI) throws RemoteException {
      this.GUI = userGUI;
   }

   public void deliverMsg(String senderName, String msg) throws RemoteException {
      GUI.receiveMessage(senderName, msg);
   }
}
