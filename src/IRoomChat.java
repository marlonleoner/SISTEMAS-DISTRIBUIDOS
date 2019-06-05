import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRoomChat extends Remote {

   // Juntar-se à sala
   public void joinRoom(String userName, IUserChat user) throws RemoteException;

   // Envia mensagem para a sala
   public void sendMsg(String userName, String message) throws RemoteException;

   // Sair da sala
   public void leaveRoom(String usrName) throws RemoteException;

   // Fecha a sala
   public void closeRoom() throws RemoteException;

   // Nome da sala
   public String getRoomName() throws RemoteException;
}
