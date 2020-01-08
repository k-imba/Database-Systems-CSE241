import java.util.*;
import java.io.*;
import java.sql.*;
import java.time.*;
import java.text.*;
public class Customer{
    private String firstname;
    private String lastname;
    private int DL_num;
    private int customer_id;
    private int vehicle_ID;
    private double vehicle_price;
    private String company_discount;
    private double discount_amount;
    private int num_hours;
    private double amount_balance;
    private double total_amount;
    private CreateConnection connect;
    private Location location;
    private ResultSet r;
    Scanner scan = new Scanner(System.in);
    public Customer(CreateConnection c){
        this.connect = c;
        location = new Location(c);
    }
    public void createReservation(){
        //int loc_index = 0;
        try{
            customerInfo();
            System.out.println("Please select the ID of the location you would like to rent a vehicle from: ");
            r = connect.executeQuery("select * from location");
            while(r.next()){
                System.out.println(r.getString("location_ID") + "     " + r.getString("city") + ", " + r.getString("state"));
            }

            while(!scan.hasNextInt()) {
                System.out.println("Sorry that is not a vaild ID, please try again:");
                scan.next();
            }
            int loc_ID = scan.nextInt();
            while(!location.locationExists(loc_ID)){
                System.out.println("this is not a valid location, please try again: ");
                while(!scan.hasNextInt()) {
                    System.out.println("Sorry that is not a vaild ID, please try again:");
                    scan.next();
                }
                loc_ID = scan.nextInt();
            }
            ResultSet available_veh = location.getVehicles(loc_ID);
            // ISSUE 110: following while loop truncates first row of data
            /*if(!available_veh.next()){
                System.out.println("this location does not exist, please try another location.");
                System.exit(0);
                while(!scan.hasNextInt()) {
                    System.out.println("Sorry that is not a valid ID, please try again:");
                    scan.next();
                }
                loc_ID = scan.nextInt();
                scan.nextLine();
                //available_veh = connect.executeQuery("select * from inventory where location_ID = " + loc_ID + "and available_status = 1");
                location.getVehicles(loc_ID);*/
            //}
            System.out.println("Please select the ID of the vehicle you would like: ");
            while(available_veh.next()){
                System.out.println(available_veh.getString("v_ID") + "   " + available_veh.getString("make") + "  " + available_veh.getString("v_model") + "  " + available_veh.getString("price_per_hour"));
                //vehicle_id = Integer.parseInt(available_veh.getString("v_ID"));
            }
            while(!scan.hasNextInt()) {
                System.out.println("Sorry that is not a vaild ID, please try again:");
                scan.next();
            }
            vehicle_ID = scan.nextInt();
            scan.nextLine();
            while(!location.vehicleExists(vehicle_ID)){
                System.out.println("Sorry, I didnt not recognize that vehicle, please try again: ");
                //System.out.println(available_veh.getString("v_ID") + "   " + available_veh.getString("make") + "  " + available_veh.getString("v_model") + "  " + available_veh.getString("price_per_hour"));
                while(!scan.hasNextInt()) {
                    System.out.println("Sorry that is not a vaild ID, please try again:");
                    scan.next();
                }
                vehicle_ID = scan.nextInt();
                scan.nextLine();
            }
            vehicle_price = location.priceOfVehicle(vehicle_ID);
            System.out.println("Enter the number of hours you would like to rent this vehicle for: "); 
            while(!scan.hasNextInt()){
                System.out.println("Sorry that isn't a valid number, please try again: ");
                scan.next();
            }
            num_hours = scan.nextInt();
            scan.nextLine();
            amount_balance = num_hours * vehicle_price;
            total_amount = amount_balance - (amount_balance * discount_amount);
            total_amount = Math.floor(total_amount * 100) / 100;
            int res_ID = IdGen();
            String date = LocalDate.now().toString();
            date = "\'" + date + "\'";
            //System.out.println();
            //System.out.println(res_ID + " " + vehicle_ID + " " + customer_id + " " + amount_balance + " " + total_amount);

            connect.executeUpdate("insert into reservation (r_ID, r_date, v_ID, c_ID, amount_balance, total_amount, status) values (" + res_ID + ", TO_DATE(" + date + ", 'YYYY-MM-DD'), " + vehicle_ID + ", " + customer_id + ", " + amount_balance + ", " + total_amount + ", " + 0 + ")");
            connect.executeUpdate("update inventory set available_status = 0 where v_ID = " + vehicle_ID);
            System.out.println("reservation succesful!");
        }
        catch(SQLException exc){
            System.out.println(exc.getMessage());
        }

    }
    public void customerInfo (){
        System.out.println("Please enter your first name");
        while(!scan.hasNextLine()){
            System.out.println("Sorry that isn't a valid First Name, please try again: ");
            scan.next();
        }
        firstname = scan.nextLine();
        firstname = "\'" + firstname + "\'";
        //System.out.println(firstname);
        System.out.println("Please enter your last name");
        while(!scan.hasNextLine()){
            System.out.println("Sorry that isn't a valid Last Name, please try again: ");
            scan.next();
        }
        lastname = scan.nextLine();
        lastname = "\'" + lastname + "\'";
        //System.out.println(lastname);
        System.out.println("Please enter your company's name");
        while(!scan.hasNextLine()){
            System.out.println("Sorry that isn't a valid company, please try again: ");
            scan.next();
        }
        company_discount = scan.nextLine();
        company_discount = "\'" + company_discount + "\'";
        double de = discountExist(company_discount);
        if(de == 0){
            System.out.println("This company does not recieve a discount");
            de = 1111111;
        }
        //System.out.println(company_discount);
        System.out.println("Please enter your Drivers License Number:");
        while(!scan.hasNextInt()){
            System.out.println("Sorry that isn't a valid DL Number, please try again: ");
            scan.next();
        }
        DL_num = scan.nextInt();
        scan.nextLine();
        //System.out.println(DL_num);
        
        customer_id = IdGen();
        //System.out.println(customer_id); 
        connect.executeUpdate("insert into customer (c_ID, c_firstname, c_lastname, d_ID, DL_num) values (" + customer_id + ", " + firstname + ", " + lastname + ", " + de + ", " + DL_num + ")");
        
    }
    public void vehicleReturn(){
        System.out.println("Welcome to Hurt's Returns! type the vehicle ID of the vehicle you wish to return: ");
        while(!scan.hasNextInt()){
            System.out.println("Sorry that isn't valid, please try again: ");
            scan.next();
        }
        int return_vehicleID = scan.nextInt();
        System.out.println("Please enter the number of miles you have driven");
        while(!scan.hasNextInt()){
            System.out.println("Sorry that isn't valid, please try again: ");
            scan.next();
        }
        int numMiles = scan.nextInt();
        scan.nextLine();
        int odometer_val = 0;
        try{
            ResultSet r = connect.executeQuery("select * from inventory where v_ID = " + return_vehicleID + " and available_status = 0");
            if(!r.next()){
                System.out.println("Sorry, this vehicle does not exist or is already with us. Thank you!");
            }
            else{
                connect.executeUpdate("update inventory set available_status = 1 where v_ID = " + return_vehicleID);
                connect.executeUpdate("update reservation set status = 1 where v_ID = " + return_vehicleID);
                ResultSet rs = connect.executeQuery("select * from inventory where v_ID = " + return_vehicleID);
                while(rs.next()){
                    odometer_val = Integer.parseInt(r.getString("odometer"));
                }
                odometer_val += numMiles;
                connect.executeUpdate("update inventory set odometer = " + odometer_val + " where v_ID = " + return_vehicleID);
                System.out.println("Your rental car has been successfully returned. Thank you for choosing Hurts Rent-A-Lemon!.");
            }
        }
        catch(SQLException exc){
            System.out.println(exc.getMessage());
        }

    }
    public double discountExist(String company_discount){
        double d = 0;
        try{
            ResultSet r = connect.executeQuery("select * from discount where d_company = " + company_discount);
            while(r.next()){
                //System.out.println(r.getString("d_amount"));
                discount_amount = Double.parseDouble(r.getString("d_amount"));
                d = Double.parseDouble(r.getString("d_ID"));
            }
        }
        catch(SQLException exc){
            System.out.println(exc.getMessage());
        }
        return d;
    
        
    }
    public int IdGen(){
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        return n;
    }
}