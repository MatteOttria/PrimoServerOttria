package primaprovaserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{
    private Socket socket;
    static private ArrayList usersList = new ArrayList<String>();
    static private ArrayList passList = new ArrayList<String>();
    static private ArrayList idList = new ArrayList<Integer>();
    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected.");
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferReader = new BufferedReader(streamReader);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("You are connect to the server. Comandi possibili: 1)calcola, 2) registrazione, 3) accesso, 4)stop");
            gestore(printWriter, bufferReader);
            socket.close();
            System.out.println("Client disconnected.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void gestore(PrintWriter printWriter, BufferedReader bufferedReader)throws Exception{
        while (true) {
            String str = bufferedReader.readLine();
            if (str.equals("calcola")) {
                massaCorporea(printWriter,bufferedReader);
            }else if(str.equals("stop")){
                printWriter.println("Disconnesso");
                return;
            }else if(str.equals("accesso")){
                accesso(printWriter, bufferedReader);
            }else if(str.equals("registrazione")){
                registrazione(printWriter, bufferedReader);
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
                printWriter.println("Uscito dal calcolo");
                return;
            }else{
                printWriter.println("Comando: " + str + "non riconosciuto.");
            }
        }
            System.out.println(bmi);
            return;
    }

    public static void accesso(PrintWriter printWriter, BufferedReader bufferedReader) throws Exception{
        boolean exit = false;
        printWriter.println("Comandi possibili: 1)nome 'userName', 2)stop");
        while (!exit) {
            String str = bufferedReader.readLine();
            String[] words = str.split(" ");
            if(words[0].equals("nome")){
                if(checkName(words[1].toUpperCase())){
                    if(cell(words[1].toUpperCase()) > -1){
                        int n = cell(words[1].toUpperCase());
                        printWriter.println("Inserire password");
                        String psw = bufferedReader.readLine();
                        if(cellpsw(n,psw)){
                            String name = (String) usersList.get(n);
                            printWriter.println("Bentornato " + name.toLowerCase() + ", ID: " + idList.get(n));
                        }
                    }
                }
                printWriter.println("Nome o password sbagliati");
            }else if(words[0].equals("stop")){
                printWriter.println("Uscito dal'accesso");
                return;
            }else{
                printWriter.println("Comando: " + str + "non riconosciuto.");
            }
        }
    }

    public static void registrazione(PrintWriter printWriter, BufferedReader bufferedReader) throws Exception{
        boolean exit = false;
        printWriter.println("Comandi possibili: 1)nome 'userName', 2)stop");
        while (!exit) {
            String str = bufferedReader.readLine();
            String[] words = str.split(" ");
            if(words[0].equals("nome")){
                if(checkName(words[1].toUpperCase())){
                    printWriter.println("Nome gia' in uso");
                }
                usersList.add(words[1].toUpperCase());
                printWriter.println("Inserire password");
                String psw = bufferedReader.readLine();
                passList.add(psw);
                idList.add(usersList.size());
                printWriter.println("Registrazione effettuata, ID: " + idList.get(usersList.size() - 1));
                return;
            }else if(words[0].equals("stop")){
                printWriter.println("Uscito dalla registrazione");
                return;
            }else{
                printWriter.println("Comando: " + str + "non riconosciuto.");
            }
        }

    }

    public static boolean checkName(String name){
        for(int i = 0; i < usersList.size(); i++){
            if(usersList.get(i).equals(name)){
                return true;
            }
        }
        return false;
    }

    public static int cell(String name){
        for(int i = 0; i < usersList.size(); i++){
            if(usersList.get(i).equals(name)){
                return i;
            }
        }
        return -1;
    }

    public static boolean cellpsw(int i, String psw){
        if (passList.get(i).equals(psw)) {
            return true;
        }
        return false;
    }
}
