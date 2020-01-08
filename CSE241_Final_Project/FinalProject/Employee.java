import java.util.*;
import java.io.*;
import java.sql.*;
public class Employee{
    CreateConnection connect;
    Scanner scan = new Scanner(System.in);
    public Employee(CreateConnection c){
        this.connect = c;
    }
    public void viewCurrentRes(){
        System.out.println("Here are a list of current reservations for your reference:");
        try{
            ResultSet r = connect.executeQuery("select * from reservation");
            System.out.printf("%-21s%-31s%-21s%-21s%-21s%-21s\n", "RESERVATION ID", "RESERVATION DATE", "VEHICLE ID", "CUSTOMER ID", "AMOUNT PRE-DISCOUNT", "FINAL AMOUNT PAID", "RETURN STATUS(1 = RETURNED, 0 = NOT RETURNED)");
            while(r.next()){
                System.out.printf("%-21s%-31s%-21s%-21s%-21s%-21s\n", r.getString("r_ID"), r.getString("r_date"), r.getString("v_ID"), r.getString("c_ID"), r.getString("amount_balance"), r.getString("total_amount"), r.getString("status"));
            }
        }
        catch(SQLException exc){
            System.out.println(exc.getMessage());
        }
    }
    public void insertVehicle(){
        String make;
        Location loc = new Location(connect);
        System.out.println("Please enter the vehicles location ID:");
        while(!scan.hasNextInt()){
            System.out.println("Sorry that isn't a valid number, please try again: ");
            scan.next();
        }
        int loc_ID = scan.nextInt();
        while(!loc.locationExists(loc_ID)){
            System.out.print("Enter a valid location ID:");
            while(!scan.hasNextInt()){
                System.out.println("Sorry that isn't a valid number, please try again: ");
                scan.next();
            }
            loc_ID = scan.nextInt();
        }
        scan.nextLine();
        System.out.println("Please enter vehicle make");
        while(!scan.hasNextLine()){
            System.out.println("Sorry that isn't a valid word, please try again: ");
            scan.next();
        }
        make = scan.nextLine();
        make = "\'" + make + "\'";
        System.out.println("Please enter vehicle model");
        while(!scan.hasNextLine()){
            System.out.println("Sorry that isn't a valid word, please try again: ");
            scan.next();
        }
        String model = scan.nextLine();
        model = "\'" + model + "\'";
        System.out.println("Please enter the mileage of the car:");
        while(!scan.hasNextInt()){
            System.out.println("Sorry that isn't a valid number, please try again: ");
            scan.next();
        }
        int odometer = scan.nextInt();
        scan.nextLine();
        System.out.println("Please enter vehicle license plate number");
        while(!scan.hasNextLine()){
            System.out.println("Sorry that isn't a valid LP number, please try again: ");
            scan.next();
        }
        String lpm = scan.nextLine();
        lpm = "\'" + lpm + "\'";
        System.out.println("Please enter the price per hour you would like to charge:");
        while(!scan.hasNextDouble()){
            System.out.println("Sorry that isn't a valid price, please try again: ");
            scan.next();
        }
        double price = scan.nextDouble();
        int veh_id = IdGen();
        connect.executeUpdate("insert into inventory (v_ID, location_ID, make, v_model, odometer, license_plate, available_status, price_per_hour) values (" 
            + veh_id + ", " + loc_ID + ", " + make + ", " + model + ", " + odometer + ", " + lpm + ", 1, " + price + ")");
        System.out.println("Vehicle was succesfully inserted");
        
    }
    public int IdGen(){
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        return n;
    }
}