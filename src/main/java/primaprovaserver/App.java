package primaprovaserver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class App 
{
    public static void main(String [] args) throws Exception
    {
        try(ServerSocket serverSocket = new ServerSocket(1723)){
            System.out.println("Server opened.");
            while(true){
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected.");
                    InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferReader = new BufferedReader(streamReader);
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println("You are connect to the server.");
                    gestore(printWriter, bufferReader);
                }   
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void gestore(PrintWriter printWriter, BufferedReader bufferedReader) throws Exception{
        double altezza = 0;
        double peso = 0;
        double bmi = 0;
        while (bmi == 0) {
            String str = bufferedReader.readLine();
            String[] words = str.split(" ");
            if(words[0].equals("altezza")){
                altezza = Double.parseDouble(words[1]);
                printWriter.println("altezza inserita");
            }else if (words[0].equals("peso")) {
                peso = Double.parseDouble(words[1]);
                printWriter.println("peso inserito");
            }else if (words[0].equals("calcola")){
                bmi = peso/(altezza * altezza);
                printWriter.println("calcolo eseguito:" + bmi);
            }else if(words[0].equals("stop")){
                printWriter.println("server chiuso");
                return;
            }
        }
        System.out.println(bmi);
        return;
    }
}
