package xyz.goldner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by frang on 30-Sep-16.
 */
public class Server implements Runnable {

    protected int serverPort = 8081;
    protected static ServerSocket serverSocket = null;
    protected static boolean isRunning = false;
    protected Thread serverThread = null;
    protected static ArrayList<Thread> threadPool = new ArrayList<>();
    protected static Connection connection;
    protected static Database db;

    /** DEBUG STUFF **/
    private static final boolean truncateData = true;
    private static final String[] truncateQuery = {"Truncate Mjerenje", "Truncate Mjerenje_old", "Truncate testTemps"};




    public Server()
    {
        this.isRunning = true;

        connection = dbConnect();

    }

    public Server(int port)
    {
        this.serverPort = port;
        this.isRunning = true;
        connection = dbConnect();
    }

    @Override
    public void run() {

        synchronized(this)
        {
            this.serverThread = Thread.currentThread();
        }

        createServerSocket();
        while(isRunning() == true)
        {
            Socket clientSocket = null;
            try
            {
                clientSocket = this.serverSocket.accept();
            }catch (IOException e){e.printStackTrace();}



            Thread newConnection = new Thread(new ServerWorker(clientSocket,connection));
            threadPool.add(newConnection);
            newConnection.start();



        }

    }
    /*
    stops the server and clears some of the bigger tables so it's more manageable
     */

    public static synchronized void stop()
    {

        isRunning = false;
        if(truncateData == true) {
            try {
                Statement trucStatement = connection.createStatement();
                for (String query: truncateQuery
                     ) {
                    trucStatement.execute(query);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        dbDisconnect();



        Main.kill();
    }


    private void createServerSocket() {

        try{
            this.serverSocket = new ServerSocket(this.serverPort);
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public boolean isRunning() {
        return this.isRunning;
    }

    private Connection dbConnect(){

        db = new Database();
        return db.getConnection();

    }

    private static void dbDisconnect(){

        db.disconnect();

    }

}
