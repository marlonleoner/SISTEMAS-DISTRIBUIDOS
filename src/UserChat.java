import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author marlonleoner
 */
public class UserChat  extends UnicastRemoteObject implements IUserChat {

   private User userGUI;

   public UserChat(User userGUI) throws RemoteException {
      this.userGUI = userGUI;
   }

   public void deliverMsg(String senderName, String msg) throws RemoteException {
      userGUI.sendMessage(senderName, msg);
   }
}
