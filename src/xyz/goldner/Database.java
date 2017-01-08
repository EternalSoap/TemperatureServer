package xyz.goldner;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by fran on 14.12.16..
 */
public class Database {

    /*--------------------------DB STUFF----------------------------------------------------------*/
    private String username = "fran";
    private String password = "FG5665DB";
    private String connectionString ="jdbc:mariadb://localhost:3306/fran";


    /*--------------------------------------------------------------------------------------------*/

    private Connection connection = null;

    public Database() {

        try {
            this.connection = DriverManager.getConnection(connectionString,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public Connection getConnection(){

            return this.connection;

        }


    public void disconnect() {

        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }





}
