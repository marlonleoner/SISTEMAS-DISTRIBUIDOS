import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author marlonleoner
 */
public interface IUserChat extends Remote {

   // Recebe as mensagens
   public void deliverMsg(String senderName, String msg) throws RemoteException;
}
