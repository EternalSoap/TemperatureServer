package xyz.goldner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Server started on port 8080");
        Server server = new Server();
        new Thread(server).start();


    }
}
