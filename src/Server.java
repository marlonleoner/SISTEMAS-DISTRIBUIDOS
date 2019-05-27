import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Server {

   private static final String HOST_URL = "rmi://localhost/Servidor";

   public static void main(String[] args) {
      System.out.println("> [System] Starting Server...");
      try {
         Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
         IServerChat server = new ServerChat();
         Naming.bind(HOST_URL, server);
         System.out.println("> [System] Server Online!");

         // Servidor inicia com duas salas criadas - Testes
         server.createRoom("room_1");
         server.createRoom("room_2");
      }catch (Exception e) {
         System.out.println("> [System] Server failed: " + e);
      }
   }
}
