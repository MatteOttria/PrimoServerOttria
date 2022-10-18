package primaprovaserver;
import java.net.*;

public class App 
{
    public static void main(String [] args) throws Exception
    {
        try(ServerSocket serverSocket = new ServerSocket(1723)){
            System.out.println("Server opened.");
            while(true){
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }   
        }catch (Exception e){
            System.out.println(e);
        }
    }
}

