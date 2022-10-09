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
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println("You are connect to the server.");
                    String str;
                    if ((str = bufferReader.readLine()) != null){
                        printWriter.println(str);
                        gestore(str, printWriter);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void gestore(String str, PrintWriter printWriter) throws Exception{
        double altezza = 0;
        double peso = 0;
        double bmi = 0;
        while (true) {
            String[] words = str.split("\\W+");
            if(words[1] == "altezza"){
                altezza = Double.parseDouble(words[2]);
                printWriter.println(altezza);
            }else if (words[1] == "peso") {
                peso = Double.parseDouble(words[2]);
                printWriter.println(peso);
            }else if (words[1] == "calcola"){
                bmi = peso/(altezza * altezza);
                printWriter.println(bmi);
            }
            return;
        }
    }
}
