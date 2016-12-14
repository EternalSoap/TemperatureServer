package xyz.goldner;

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
