package xyz.goldner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by frang on 30-Sep-16.
 */
public class Server implements Runnable {

    protected int serverPort = 8081;
    protected ServerSocket serverSocket = null;
    protected boolean isRunning = false;
    protected Thread serverThread = null;

    public Server()
    {
        this.isRunning = true;
    }

    public Server(int port)
    {
        this.serverPort = port;
        this.isRunning = true;
    }

    @Override
    public void run() {

        synchronized(this)
        {
            this.serverThread = Thread.currentThread();
        }
        createServerSocket();
        while(isRunning())
        {
            Socket clientSocket = null;
            try
            {
                clientSocket = this.serverSocket.accept();
            }catch (IOException e){e.printStackTrace();}

            new Thread(new ServerWorker(clientSocket)).start();

        }

    }

    public synchronized void stop()
    {
        this.isRunning = false;
        try
        {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
