package xyz.goldner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by fran on 30.01.17..
 *
 * Check the temps in all the rooms for all the spaces and determines if the heating should be on or off
 *
 * Runs every 5 minutes
 */
public class HeatingChecker implements Runnable{

    
    private ArrayList<Space> arrayListSpace = new ArrayList<>();



    public HeatingChecker(){}

    @Override
    public void run() {


        Database database = new Database();
        Connection connection = database.getConnection();
        String getDistinctProstor = "select DISTINCT Prostor.prostorID from Prostor";
        String updateSpaceHeatingOn = "update Prostor set grijanjeUpaljeno = 1 where prostorID = ?";
        String updateSpaceHeatingOff = "update Prostor set grijanjeUpaljeno = 0 where prostorID = ?";


        try {
            PreparedStatement ps = connection.prepareStatement(getDistinctProstor);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                arrayListSpace.add(new Space(rs.getInt(1)));
            }

            for (Space s :
                    arrayListSpace) {
                if(s.isHeatingRequired()){
                    ps = connection.prepareStatement(updateSpaceHeatingOn);
                    ps.setInt(1,s.getSpaceID());
                    ps.execute();
                }else{
                    ps = connection.prepareStatement(updateSpaceHeatingOff);
                    ps.setInt(1,s.getSpaceID());
                    ps.execute();
                }
            }
            

            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
