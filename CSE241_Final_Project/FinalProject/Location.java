import java.util.*;
import java.io.*;
import java.sql.*;
public class Location{
    CreateConnection c;
    public Location(CreateConnection c){
        this.c = c;
    }
    public ResultSet getVehicles(int loc_ID){
       // try{
            ResultSet r;
            r = c.executeQuery("select * from inventory where location_ID = " + loc_ID + "and available_status = 1");
            return r;
        //}
        //catch(SQLException exc){
            ///System.out.println(exc.getMessage());
       // }
    }
    public boolean vehicleExists(int v_id){
        try{
            ResultSet r = c.executeQuery("select * from inventory where v_ID = " + v_id);
            while(r.next()){
                return true;
            }
        }
        catch(SQLException exc){
            System.out.println(exc.getMessage());
        }
        return false;
    }
    public double priceOfVehicle(int v_ID){
        try{
            ResultSet r = c.executeQuery("select * from inventory where v_ID = " + v_ID + "and available_status = 1 ");
            while(r.next()){
                return Double.parseDouble(r.getString("price_per_hour"));
            }
        }
        catch(SQLException exc){
            System.out.println(exc.getMessage());
        }
        return 0.0;
    }
    public boolean locationExists(int loc_ID){
        try{
            ResultSet r = c.executeQuery("select * from location where location_ID = " + loc_ID);
            while(r.next()){
                return true;
            }
        }
        catch(SQLException exc){
            System.out.println(exc.getMessage());
        }
        return false;
    }
}