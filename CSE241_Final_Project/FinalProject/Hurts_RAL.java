import java.util.*;
import java.io.*;
import java.sql.*;
class Hurts_RAL {
    public static void main (String[] arg)throws SQLException, IOException, java.lang.ClassNotFoundException {
        CreateConnection c = new CreateConnection();
        c.makeconnection();
        System.out.println("Welcome to Hurts Rents-A-Lemon");
        int evc = verification("Are you an employee or customer? Type 1 for customer or type 0 for employee");
        if(evc == 1){
            System.out.println("You are a customer! You will now be redirected to the customer portal");
            Customer customer = new Customer(c);
            int rvd = verification("Press 1 to make a reservation, press 0 to return a vehicle:");
            if (rvd == 1){
                customer.createReservation();
            }
            else if(rvd == 0){
                customer.vehicleReturn();
            }
        }
        else if(evc == 0){
            System.out.println("You are an employee! You will no be redirected to the employee portal");
            Employee employee = new Employee(c);
            int ec = verification("press 1 to view reservations, press 0 to enter a new vehicle");
            if(ec == 1){
                employee.viewCurrentRes();
            }
            else if(ec == 0){
                employee.insertVehicle();
            }
        }
        
    }
    public static int verification (String input){
        Scanner scan = new Scanner(System.in);
        System.out.println(input);
        while(!scan.hasNextInt()) {
            System.out.println("Sorry that is not a vaild integer, please try again:");
            scan.next();
        }
        int option = scan.nextInt();
        while(option > 1 || option < 0){
            System.out.println("Sorry I dont recognize that number, please try again:");
            while(!scan.hasNextInt()) {
                System.out.println("Sorry that is not a vaild integer, please try again:");
                scan.next();
            }
            option = scan.nextInt();
            scan.nextLine();
        }
        return option; 
  }
}