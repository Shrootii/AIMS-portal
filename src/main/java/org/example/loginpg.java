package org.example;

import java.sql.* ;
import java.util.*;

public class loginpg extends facultyPortal {

    //Asking the user for username and password
    public static String[] askCreds(){

        //Asking user to login
        System.out.println("Enter your login credentials to continue!");
        System.out.println("Username: ");

        //taking username input
        Scanner sc = new Scanner(System.in);
        String name = sc.next();

        System.out.println("Password: ");
        String pwd = sc.next();

        //sc.close();
        return new String[]{ name, pwd };
    }


    //matching username and password in database
    public static int checkCreds(String name, String pwd, int type){

        int flag =0;
        Connection c = null;
        Statement stmt = null;
        try{

            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery( "SELECT * FROM auth;" );

            while ( rs.next() ) {
                String username = rs.getString("id_email");
                String password = rs.getString("id_password");
                int type1 = rs.getInt("id_type");

                //System.out.println( "un -" + username + "  pwd- " + password);

                if(username.equals(name)){
                    //System.out.println("Matched");
                    if(password.equals(pwd) && type1==type){
                        //System.out.println("login");
                        flag = 1;
                        break;
                    }
                    else if(type1!=type){
                        flag=2;
                    }
                    else{
                        flag=3;
                    }
                }
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        return flag;
    }

    public static void loop(int type){

        int res=0;
        String[] creds = askCreds();
        res = checkCreds(creds[0], creds[1], type);

        if(res == 0|| res==2) {
            System.out.println("***Please enter correct username.***");
            loop(type);
        }
        else if (res==3) {
            System.out.println("***Incorrect password.Try again.***");
            loop(type);
        }
        else{
            System.out.println("***You have successfully logged in.***");
            if (type==1){
                //student portal
                //autoupdate of cgpa
                studentPortal.cgpaUpdate(creds[0]);
                studentPortal.updateSem(creds[0]);
                studentPortal.portal(creds[0]);
            }
            else if(type==2){
                //faculty portal
                facultyPortal.portal(creds[0]);
            }
            else if(type==3){
                //admin portal
                adminPortal.portal(creds[0]);
            }
        }
    }


    public static void login(){

        System.out.println("WELCOME!");

        //Asking user -> student, faculty or admin?
        System.out.println("Please specify the user type. \n1.Student\n2.Faculty\n3.Admin");

        Scanner sc = new Scanner(System.in);
        int type = sc.nextInt();


        if(type!=1 && type!=2 && type!=3){
            System.out.println("*****INVALID INPUT. TRY AGAIN.*****\n");
            login();
        }
        else{
            loop(type);
        }

        return;

    }


    public static void main(String[] args){

        //setting up connection to database
        try (Connection datacon = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/aims", "postgres", "shruti2709")) {

            if (datacon != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }


        login();

        //sc.close();
        return;

    }
}
