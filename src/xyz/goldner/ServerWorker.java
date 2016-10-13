package xyz.goldner;



import java.io.*;
import java.net.Socket;

/**
 * Created by frang on 30-Sep-16.
 */
public class ServerWorker implements Runnable {

    protected Socket clientSocket = null;
    //protected int packetSize = (2*1024);


    public ServerWorker(Socket clientSocket) {

        this.clientSocket = clientSocket;

    }

    @Override
    public void run() {

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;
            String text = "";

            text = in.readLine();

            //ovo bi trebalo radit, al jedino radi ako tog nema
            /*
            while ((line = in.readLine()) != null) {
                text +=line;
                System.out.println("Linija "+line);
                if (line.isEmpty() || line == "\r\n") {
                    System.out.println("U ifu");
                    break;
                }
            }
            */


            String[] array = text.split(":");
            System.out.println("ChipID "+ array[0]);

            out.println("Ovo bi trebao bit test " + array[1]);





            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }







    }
}
