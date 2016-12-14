package xyz.goldner;

/**
 * Created by fran on 08.12.16..
 */
public class Sensor {

    int chipID;
    String location;
    boolean isRegistered;


    // add is registered check, auto register if false (db stuff)
    public Sensor(int chipID){
        this.chipID = chipID;


    }

    public int getChipID(){
        return this.chipID;
    }

    public String getLocation(){
        return this.location;
    }

    public void setChipID(int chipID){
        this.chipID = chipID;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public boolean getIsRegistered(){
        return this.isRegistered;
    }





}
