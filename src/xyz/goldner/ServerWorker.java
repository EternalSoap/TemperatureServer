package xyz.goldner;



import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;

/**
 * Created by frang on 30-Sep-16.
 */
public class ServerWorker implements Runnable {

    private boolean debug = true;


    /*--------------------------DB STUFF----------------------------------------------------------*/
    private String username = "fran";
    private String password = "FG5665DB";
    private String connectionString ="jdbc:mariadb://localhost:3306/fran";

    private String tableName = "testTemps";
    /*--------------------------------------------------------------------------------------------*/


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



            // this should work but it doesn't
            // it could work now cause the message has \r\n
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



            debugOutput(text);

            if(text.equals("kill")){
                debugOutput("Trying to kill");

                Server.stop();

            }else {


                String[] array = text.split(":");
                if(array.length != 1){
                    debugOutput("ChipID "+ array[1] + " Temp " + array[0]);
                    out.println("Ovo bi trebao bit temp " + array[0]);


                }

                // in case loading the driver fails
                /*


                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                debugOutput("Driver loaded");
                */


                try {

                    Connection connection = DriverManager.getConnection(connectionString,username,password);
                    Statement statement = connection.createStatement();
                    statement.execute("insert into "+tableName+ " values ('"+array[1]+"' , '"+array[0]+"' )");
                    statement.close();
                    connection.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }


                // writing to text file, will be replaced with db stuff soonish
                try {
                    PrintWriter writer = new PrintWriter("/var/www/html/java/temps.html", "UTF-8");
                    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                    Date dateobj = new Date();



                    writer.println(df.format(dateobj) + " " + array[0] + "C");
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }



            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    public void debugOutput(String text){
        if(debug == true){
            System.out.println(text);
        }
    }

}
