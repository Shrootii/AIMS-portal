package org.example;

import java.sql.*;
import java.util.*;

public class deregisterCourse {

    public static boolean viewEnrollments(String username){

        //the current student enrollments will be displayed
        System.out.println(
                "\nFollowing are the current course enrollments.\n");

        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "SELECT * FROM current_enrollments WHERE student_id=?";
            PreparedStatement stm = c.prepareStatement(str);
            stm.setString(1,username);

            ResultSet rs = stm.executeQuery();

            System.out.println("code\tcredits\ttype");

            while ( rs.next() ) {
                String code = rs.getString("course_code");
                int creds = rs.getInt("credits");
                String type = rs.getString("course_type");

                System.out.println(code+"\t"+creds+"\t"+type);
            }

            System.out.println("\n");
            rs.close();
            stm.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        return true;
    }

    public static void deregister(String username){

        viewEnrollments(username);

        System.out.println("Enter the course code you want to deregister.");
        Scanner sc = new Scanner(System.in);
        String input = sc.next();

        Connection c = null;

        //checking if correct input code is taken
        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "SELECT * FROM current_enrollments WHERE student_id=?";
            PreparedStatement stm = c.prepareStatement(str);
            stm.setString(1,username);

            ResultSet rs = stm.executeQuery();

            int flag=0;

            while ( rs.next() ) {


                String code = rs.getString("course_code");

                if(input.equals(code)){
                    flag=1;
                    break;
                }

            }

            if(flag==0){
                System.out.println("*****Course not currently enrolled. Try again.*****\n");
                deregister(username);
            }
            else{
                String sql = "DELETE FROM all_enrollments WHERE student_id = ? AND course_code =?";
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1,username);
                stmt.setString(2,input);

                stmt.executeUpdate();

                stmt.close();

                String sql1 = "DELETE FROM current_enrollments WHERE student_id = ? AND course_code =?";
                PreparedStatement stmt1 = c.prepareStatement(sql1);
                stmt1.setString(1,username);
                stmt1.setString(2,input);

                stmt1.executeUpdate();

                stmt1.close();

                System.out.println("Successfully Unenrolled " + input+".");

            }

            rs.close();
            stm.close();

            //deleting the course from all enrollments and current enrollments
        }catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        return;

    }

}
