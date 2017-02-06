package xyz.goldner;



import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Random;

/**
 * Created by frang on 30-Sep-16.
 */
public class ServerWorker implements Runnable {

    /* debug stuff */
    private static final boolean debug = true;




    private Connection connection;
    private static final String insertQuery = "insert into Mjerenje values (default,?,?,?)";



    protected Socket clientSocket = null;



    public ServerWorker(Socket clientSocket, Connection connection) {

        this.clientSocket = clientSocket;
        this.connection = connection;

    }

    @Override
    public void run() {

        double temperature = 0.0;



        try {


            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;
            String text = "";

            text = in.readLine();



            // this should work but it doesn't
            // it could work now cause the message has \r\n
            // kinda doesn't matter since the message is always an one liner
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

            /* send "kill" to kill the server
               waits for all the workers to finish first
             */

            if(text.equals("kill")){
                debugOutput("Trying to kill");

                Server.stop();

            }else if(text.contains(":")){ // only try to split if the ":" is actually in the string


                String[] array = text.split(":");
                if(array.length != 1){
                    debugOutput("ChipID "+ array[1] + " Temp " + array[0]);

                    temperature = Double.parseDouble(array[0]);
                }else{

                    return;

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


                /*


                try {
                    Statement statement = connection.createStatement();
                    statement.execute("insert into " + tableName + " values (" + array[1]+ " , " + array[0]+ " )");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                */



                Sensor sensor = new Sensor(Integer.parseInt(array[1]),connection);
                debugOutput("Status " + sensor.getStatus());
                //sensor.setLocation(1);
                debugOutput("New status " + sensor.getStatus());
                if(sensor.getStatus() >0){ // if room is set, write to database
                    int message = 0;

                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                    preparedStatement.setInt(1,sensor.getLocation());
                    java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
                    preparedStatement.setTimestamp(2,date);
                    preparedStatement.setDouble(3,temperature);

                    preparedStatement.execute();

                    String isHeatingOnQuery = "select Prostor.grijanjeUpaljeno from Soba join Prostor on Soba.prostorID = Prostor.prostorID join Senzor on Senzor.sobaID = Soba.sobaID where Senzor.senzorID = ?";

                    PreparedStatement ps = connection.prepareStatement(isHeatingOnQuery);
                    ps.setInt(1, Integer.parseInt(array[1]));

                    ResultSet rs = ps.executeQuery();

                    while(rs.next()){
                        message = rs.getInt(1);
                    }

                    out.println(message);


                }

                sensor = null;

                // writing to html file to check in browser debug use mostly ))
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }





    public void debugOutput(String text){
        if(debug == true){
            System.out.println(text);
        }
    }



}
