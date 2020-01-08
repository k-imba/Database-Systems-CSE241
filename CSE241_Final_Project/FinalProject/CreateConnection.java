import java.util.*;
import java.io.*;
import java.sql.*;
public class CreateConnection{
    private Connection con;
    private Statement s;
    String username;
    String password;
    public void makeconnection(){
        boolean check = true;
        while (check){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter username : ");
        username = scan.nextLine();
        System.out.println("Enter password: ");
        password = scan.nextLine();
            try{
                con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
                System.out.println("Connection Succesful!");
                // Data Fields
                check = false;
            }
            catch(SQLException exc){
                System.out.println("Error Connecting To Database, Please Try Again");
            }
        }
        //scan.close();
    }
    public ResultSet executeQuery(String q){
        ResultSet result = null;
        try{
            //con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
            s = con.createStatement();
            result = s.executeQuery(q);
        }
        catch(SQLException exc){
            System.out.println(exc.getMessage());
        }
        return result;
    }
    public void executeUpdate(String i){
        try{
            s = con.createStatement();
            s.executeUpdate(i);

        }
        catch(SQLException exc){
            System.out.println(exc.getMessage());
        }
    }
}