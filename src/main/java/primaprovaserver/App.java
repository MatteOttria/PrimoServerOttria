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
                    printWriter.println("You are connect to the server. Comandi possibili: 1)calcola");
                    gestore(printWriter, bufferReader);
                }   
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void gestore(PrintWriter printWriter, BufferedReader bufferedReader)throws Exception{
        while (true) {
            String str = bufferedReader.readLine();
            if (str.equals("calcola")) {
                massaCorporea(printWriter,bufferedReader);
            }else{
                printWriter.println("Comando: " + str + " non riconosciuto.");
            }
        }
    }

    public static void massaCorporea(PrintWriter printWriter, BufferedReader bufferedReader) throws Exception{
        double altezza = 0;
        double peso = 0;
        double bmi = 0;
        printWriter.println("Comandi possibili: 1)altezza 'var', 2)peso 'var', 3)calcola, 4)stop");
        while (bmi == 0) {
            String str = bufferedReader.readLine();
            String[] words = str.split(" ");
            if(words[0].equals("altezza")){
                altezza = Double.parseDouble(words[1]);
                printWriter.println("Altezza inserita");
            }else if (words[0].equals("peso")) {
                peso = Double.parseDouble(words[1]);
                printWriter.println("Peso inserito");
            }else if (words[0].equals("calcola")){
                bmi = peso/(altezza * altezza);
                bmi = Math.round(bmi * 1000.0)/1000.0;
                printWriter.println("Calcolo eseguito:" + bmi);
            }else if(words[0].equals("stop")){
                return;
            }else{
                printWriter.println("Comando: " + str + "non riconosciuto.");
            }
        }
            System.out.println(bmi);
            return;
    }
}
