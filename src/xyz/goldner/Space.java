package xyz.goldner;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by fran on 30.01.17..
 */
public class Space {

    private int spaceID;
    private float averageTemp; // average temp of all the rooms in the space
    private int currentDesiredTemp; // user selected temperature for the space (schedule or manual)

    private static final int morning = 6; // 6am
    private static final int night = 0; // 00.00
    private static final int defaultTemp = 20;

    public Space (int spaceID){
        this.spaceID = spaceID;
        this.getAverageTemp();
        // if the manual temp is not set (value is 0) get it from a valid schedule for the space
        // value is 20 by default if not set manually or by using a schedule
        this.currentDesiredTemp = (this.getManualTemp()!=0?this.getManualTemp():(this.getValidScheduleTemps() !=0?this.getValidScheduleTemps():defaultTemp));

    }


    public boolean isHeatingRequired (){
        return currentDesiredTemp > averageTemp;
    }

    public int getSpaceID() {return this.spaceID;}


    private void getAverageTemp(){

        Database database = new Database();
        Connection connection = database.getConnection();
        String getAverageTempQuery = "select AVG(T.temperatura) from (select DISTINCT Soba.sobaID,Mjerenje.temperatura from Soba join Mjerenje on Soba.sobaID = Mjerenje.sobaID where Soba.prostorID = ? group by 1) T";

        try {
            PreparedStatement ps = connection.prepareStatement(getAverageTempQuery);
            ps.setInt(1,this.spaceID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                this.averageTemp = rs.getFloat(1);

            }

            System.out.println(this.averageTemp);

            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /*
        gets the manually set desired temp (in case it's set)
     */
    private int getManualTemp(){
        int desiredTemp = 0;

        Database database = new Database();
        Connection connection = database.getConnection();
        String getManualTempQuery = "select Prostor.rucnaTemperatura from Prostor where Prostor.prostorID = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(getManualTempQuery);
            ps.setInt(1,this.spaceID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                desiredTemp = rs.getInt(1);
            }

            database.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return desiredTemp;
    }

    /*
        gets the current desired temp (depending on time) from a valid schedule for this space
     */
    private int getValidScheduleTemps (){
        int desiredTemp =0;

        Database database = new Database();
        Connection connection = database.getConnection();
        String getValidScheduleTempsQuery = "select Raspored.tempDan,Raspored.tempNoc from Raspored join Prostor_Raspored on Raspored.rasporedID = Prostor_Raspored.rasporedID join Prostor on Prostor.prostorID = Prostor_Raspored.prostorID where Prostor.prostorID = ? and (Raspored.datumOd <= ? and (Raspored.datumDo > ? or Raspored.datumDo IS NULL ))";


        try {
            PreparedStatement ps = connection.prepareStatement(getValidScheduleTempsQuery);

            ps.setInt(1,this.spaceID);
            ps.setTimestamp(2,Timestamp.valueOf(LocalDate.now().atStartOfDay()));
            ps.setTimestamp(3,Timestamp.valueOf(LocalDate.now().atStartOfDay()));

            ResultSet rs = ps.executeQuery();

            if(!rs.isBeforeFirst()){ // empty set, return 0 and use the default temperature (20)
                return 0;
            }

            while(rs.next()){
                if(!isNight(LocalDateTime.now())){ // get day temps from schedule

                    desiredTemp = rs.getInt(1);

                }else{ // try to get night temp, if it's not set (sql returns 0) use day temp instead

                    desiredTemp = (rs.getInt(2)!=0?rs.getInt(2):rs.getInt(1));

                }
            }

            database.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return desiredTemp;
    }

    private boolean isNight (LocalDateTime localDateTime){

        // night heating begins at 00.00, morning begins at 6am as defined in the "night" and "morning" constants

        if(localDateTime.getHour() >= night && localDateTime.getHour() <= morning){
            return true;
        }
        return false;

    }



}
