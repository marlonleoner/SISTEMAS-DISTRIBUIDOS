import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.List;
import java.util.ArrayList;

public interface IServerChat extends Remote {

    // Retorna todas as salas do servidor
    public List<String> getRooms() throws RemoteException;

    // Criação de uma sala
    public void createRoom(String roomName) throws RemoteException;
}
