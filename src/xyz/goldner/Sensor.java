package xyz.goldner;

import java.sql.*;

/**
 * Created by fran on 08.12.16..
 */
public class Sensor {

    private static final String getDataFromDBQuery = "Select * from Senzor where senzorID = ?";
    private static final String addSensorQuery = "Insert into Senzor values (?,default,?)";
    private static final String updateSensorDataQuery = "Update Senzor set sobaID = ?, status = ? where senzorID = ?";

    int chipID;
    int location;
    Connection dbConnection;

    /*
    status marks the current status of the sensor
    -1 - unknown
    0 - registered (in the database) but not added to any of the rooms
    1 - registered and added to a room
     */
    int status = -1;


    /** METHODS **/



    public Sensor(int chipID, Connection connection){

        this.chipID = chipID;
        this.dbConnection = connection;

        getDataFromDB();

    }

    public int getChipID(){
        return this.chipID;
    }

    public int getLocation(){
        return this.location;
    }

    public void setLocation(int location){
        this.location = location;
        if(location > 0){
            setStatus(1);
        }else{
            setStatus(0);
        }
        updateDB();
    }

    public int getStatus(){return this.status;}

    public void setStatus(int status){
        this.status = status;
        updateDB();
    }


    private void getDataFromDB(){

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(getDataFromDBQuery);
            preparedStatement.setInt(1,this.chipID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()) { // empty set

                addSensor();

            }

            while(resultSet.next()){ // not so empty so get all the relevant data and store it in the obj

                setLocation(resultSet.getInt("sobaID"));
                setStatus(resultSet.getInt("status"));

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void addSensor() {

        try {
            PreparedStatement insertStatement = dbConnection.prepareStatement(addSensorQuery);
            insertStatement.setInt(1,this.chipID);
            insertStatement.setInt(2,0);
            insertStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void updateDB(){
        try {
            PreparedStatement updateStatement = dbConnection.prepareStatement(updateSensorDataQuery);
            updateStatement.setInt(1,this.location);
            updateStatement.setInt(2,this.status);
            updateStatement.setInt(3,this.chipID);
            updateStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
