package primaprovaserver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class App 
{
    public static void main(String [] args) throws Exception
    {
        try(ServerSocket serverSocket = new ServerSocket(5001)){
            System.out.println("Server opened.");
            while(true){
                try(Socket socket = serverSocket.accept()){
                    System.out.println("Client connected.");
                    InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferReader = new BufferedReader(streamReader);
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.println("You are connect to the server."); 
                    printWriter.flush();
                    String str;
                    str = bufferReader.readLine();
                    System.out.println(str);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
