package xyz.goldner;

public class Main {

    public static void main(String[] args) {

        Server server;
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
}
