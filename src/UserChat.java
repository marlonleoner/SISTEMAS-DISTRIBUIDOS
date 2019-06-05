import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserChat extends UnicastRemoteObject implements IUserChat {

   // PORT
   private static final int RMI_PORT = 2020;
   // URL
   private static final String RMI_HOST = "rmi://localhost/";

   private UserGUI userGUI;

   public UserChat(UserGUI userGUI) throws RemoteException {
      this.userGUI = userGUI;
   }

   public void deliverMsg(String senderName, String msg) throws RemoteException {
      userGUI.receiveMessage(senderName, msg);
   }
}
