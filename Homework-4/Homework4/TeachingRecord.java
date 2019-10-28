import java.util.*;
import java.io.*;
import java.sql.*;
class TeachingRecord {
  public static void main (String[] arg)throws SQLException, IOException, java.lang.ClassNotFoundException {
        boolean check = true;
        while (check){
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter username : ");
            String username = scan.nextLine();
            System.out.println("Enter password: ");
            String password = scan.nextLine();
            try{
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
                System.out.println("Connection Succesful!");
                System.out.print("Enter Instructor Name: ");
                String input = scan.nextLine();
                Statement s = con.createStatement();
                String q;
                String q1;
                String q2;
                ResultSet result;
                int ID = 0;
                boolean isNum = false;
                q = "select * from instructor where name LIKE '%" + input + "%'";
                //con.setString(1, input);
                result = s.executeQuery(q);
                while (!result.next()) {
                    System.out.println ("Empty result. Please try again:");
                    String newIn = scan.nextLine();
                    q1 = "select * from instructor where name LIKE '%" + newIn + "%'";
                    result = s.executeQuery(q1);
                }
                do {
                    System.out.println (result.getString("ID") + " " + result.getString("name"));
                } while (result.next());
                System.out.println ("Enter Instructor ID number: ");
                while (!isNum) {
                    try{
                        ID = scan.nextInt();
                        scan.nextLine();
                        isNum = true;
                    }
                    catch(InputMismatchException ex){
                        System.out.print("please enter a number: ");
                        scan.nextLine();
                    }
                }
                while(ID < 0 || ID > 99999){
                    System.out.println("Sorry that is an invalid ID: ");
                    ID = scan.nextInt();
                }
                q2 = "select * from instructor natural join teaches natural join course where ID LIKE '%" + ID + "%'";
                result = s.executeQuery(q2);
                if(!result.next()){
                    System.out.println("Sorry this professor does not teach any courses~");
                    return;
                }
                System.out.printf("Department \tCNO\t Title      \t Section \t Semester \t Year\n");
                do{
                    System.out.printf(result.getString("dept_name") + "\t" + result.getString("course_ID") + "\t" + result.getString("title") + "\t" + result.getString("sec_ID") + "\t" + result.getString("semester") + "\t" + result.getString("year") + "\n");
                }while (result.next());
                //System.out.printf(String.format("5%- 5s%- 5s%- 5s\n", result.getString("course_ID"), result.getString("title"), result.getString("sec_ID")));
                s.close();
                con.close();
                check = false;
            }
            catch(SQLException exc){
                System.out.println("Error Connecting To Database, Please Try Again");
            }
        }
    }
}