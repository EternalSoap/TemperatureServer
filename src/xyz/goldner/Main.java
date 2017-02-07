package xyz.goldner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {


    public static Server server;

    public static void main(String[] args) {


        if(args.length !=0)
        {
            server = new Server(Integer.parseInt(args[0]));
            System.out.println("Server started on port "+args[0]);

        }
        else
        {
            server = new Server();
            System.out.println("Server started on default port");

        }
        new Thread(server).start();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        HeatingChecker heatingChecker = new HeatingChecker();


        //checks if the heating should be turned on every 5 minutes
        executorService.scheduleAtFixedRate(heatingChecker,1,5, TimeUnit.MINUTES);


    }

    public static void kill(){

        if(server.isRunning() == false){
            try {
                server.serverThread.join();
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        System.exit(0);



    }

}
