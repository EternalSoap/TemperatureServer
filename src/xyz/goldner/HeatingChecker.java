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

    private ArrayList<Integer> arrayListSpaceIDs  = new ArrayList<>();


    public HeatingChecker(){}

    @Override
    public void run() {

        Database database = new Database();
        Connection connection = database.getConnection();
        String getDistinctProstor = "select DISTINCT Prostor.prostorID from Prostor";
        String averageTemps = "select AVG(M.temperatura) from (SELECT Mjerenje.datumVrijeme, Mjerenje.temperatura from Mjerenje" +
                " join Soba  on Mjerenje.sobaID = Soba.sobaID and Soba.prostorID = ? ORDER by 1 DESC limit 10) M";

        try {
            PreparedStatement ps = connection.prepareStatement(getDistinctProstor);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                arrayListSpaceIDs.add(rs.getInt(1));
            }

            ps = connection.prepareStatement(averageTemps);

            for (int i :
                    arrayListSpaceIDs) {
                ps.setInt(1,i);

                rs = ps.executeQuery();
                while(rs.next()){



                }

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
