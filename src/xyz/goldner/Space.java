package xyz.goldner;

/**
 * Created by fran on 30.01.17..
 */
public class Space {

    private int spaceID;
    private double averageTemp; // average temp of all the rooms in the space
    private int currentDesiredTemp; // user selected temperature for the space (schedule or manual)

    public Space (int spaceID){
        this.spaceID = spaceID;
    }


    private void getAverageTemp(){



    }


    /*
        gets the manually set desired temp (in case it's set)
     */
    private int getManualTemp(){
        int desiredTemp = 0;

        return desiredTemp;
    }

    /*
        gets the current desired temp (depending on time) from a valid schedule for this space
     */
    private int getValidScheduleTemp (){
        int desiredTemp =0;

        return desiredTemp;
    }

}
