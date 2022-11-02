package primaprovaserver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Handler;

public class ClientHandler extends Thread{
    private static Socket socket;
    PrintWriter printWriter;
    static private String serverName;
    static private ArrayList<String> usersList = new ArrayList<String>();
    static private ArrayList<String> passList = new ArrayList<String>();
    static private ArrayList<Integer> idList = new ArrayList<Integer>();
    static private ArrayList<ClientHandler> handlerList = new ArrayList<ClientHandler>();
    public ClientHandler(Socket socket, String serverName, ArrayList<ClientHandler> handlerList){
        this.socket = socket;
        this.serverName = serverName;
        this.handlerList = handlerList;
    }

    public Socket getSocket(){
        return this.socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected.");
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferReader = new BufferedReader(streamReader);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("You are connect to the server. Comandi possibili: 1)calcola, 2) registrazione, 3) accesso, 4)data, 5) server, 6)stop, 7)esplodi");
            gestore(printWriter, bufferReader);
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
                stop(printWriter, bufferedReader);
                return;
            }else if(str.equals("accesso")){
                accesso(printWriter, bufferedReader);
            }else if(str.equals("registrazione")){
                registrazione(printWriter, bufferedReader);
            }else if(str.equals("data")){
                data(printWriter, bufferedReader);
            }else if(str.equals("server")){
                printWriter.println("Nome server: " + serverName);
            }else if(str.equals("esplodi")){
                clientsCloser(printWriter, bufferedReader);
                return;
            }else{
                printWriter.println("Comando: " + str + " non riconosciuto.");
            }
        }
    }

    public static void stop(PrintWriter printWriter, BufferedReader bufferedReader) throws Exception{
        printWriter.println("Disconnesso");
        System.out.println("Client disconnected.");
        socket.close();
    }


    public static void clientsCloser(PrintWriter printWriter, BufferedReader bufferedReader) throws Exception{
        for (ClientHandler  client: handlerList) {
            System.out.println(client.getSocket());
            client.printWriter.println("Client disconnected.");
        }
        return; 
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
                printWriter.println("Comando: " + str + " non riconosciuto.");
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
                printWriter.println("Comando: " + str + " non riconosciuto.");
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
                printWriter.println("Comando: " + str + " non riconosciuto.");
            }
        }

    }

    public static void data(PrintWriter printWriter, BufferedReader bufferedReader) throws Exception{
        boolean exit = false;
        printWriter.println("Comandi possibili: 1)giorno, 2)ora, 3)stop");
        while (!exit) {
            String str = bufferedReader.readLine();
            if(str.equals("giorno")){
                LocalDate date = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");;
                printWriter.println("Giorno: " + date.format(formatter));
            }else if(str.equals("ora")){
                LocalTime time = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                printWriter.println(time.format(formatter));
            }else{
                printWriter.println("Comando: " + str + " non riconosciuto.");
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
