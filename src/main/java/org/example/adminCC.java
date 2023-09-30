package org.example;

import java.sql.*;
import java.util.*;

public class adminCC {

    //register
    public static void registerC(){

        Scanner sc = new Scanner (System.in);

        System.out.println("Enter the following course details.");
        System.out.println("1. Course name: ");
        String cname = sc.nextLine();
        System.out.println("2. Course code: ");
        String ccode = sc.next();
        System.out.println("3. Credits");
        int ccredits = sc.nextInt();
        System.out.println("4. LTP structure: ");
        String cltp = sc.next();
        System.out.println("PreRequisite required (enter none if no required): ");
        String cprereq = sc.next();


        Connection c=null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "SELECT * FROM course_catalog";
            PreparedStatement stm = c.prepareStatement(str);

            ResultSet rs = stm.executeQuery();

            int flag=0;

            while ( rs.next() ) {


                String code = rs.getString("course_code");

                if(ccode.equals(code)){
                    flag=1;
                    break;
                }

            }

            if(flag==1){
                System.out.println("*****Course already exists.*****\n");
            }
            else{
                if(cprereq.equals("none")){
                    String data = "INSERT into course_catalog(course_name,course_code,credits,ltp)" + "VALUES(?,?,?,?)";

                    PreparedStatement insert = c.prepareStatement(data);
                    insert.setString(1,cname);
                    insert.setString(2,ccode);
                    insert.setInt(3,ccredits);
                    insert.setString(4,cltp);
                    insert.executeUpdate();
                    insert.close();
                }
                else{
                    String data = "INSERT into course_catalog(course_name,course_code,credits,ltp,prereq)" + "VALUES(?,?,?,?,?)";

                    PreparedStatement insert = c.prepareStatement(data);
                    insert.setString(1,cname);
                    insert.setString(2,ccode);
                    insert.setInt(3,ccredits);
                    insert.setString(4,cltp);
                    insert.setString(5,cprereq);
                    insert.executeUpdate();
                    insert.close();
                }

                System.out.println("Course successfully registered.");

                //deleting the course from all enrollments and current enrollments
            }
            rs.close();
            stm.close();

        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        return;
    }


    //deregister
    public static void deregisterC(){
        System.out.println(
                "\nFollowing is the course catalog.\n");

        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "SELECT * FROM course_catalog";
            PreparedStatement stm = c.prepareStatement(str);
            ResultSet rs = stm.executeQuery();

            System.out.println("name\tcode\tcredits\tLTP\tprerequisite");

            while ( rs.next() ) {
                String code = rs.getString("course_code");
                int creds = rs.getInt("credits");
                String name = rs.getString("course_name");
                String ltp = rs.getString("ltp");
                String prereq = rs.getString("prereq");

                System.out.println(name+"\t"+code+"\t"+creds+"\t"+ltp+"\t"+prereq);
            }

            System.out.println("\n");

            System.out.println("Enter the course code you want to deregister.");
            Scanner sc = new Scanner(System.in);
            String input = sc.next();

            int flag=0;

            while ( rs.next() ) {


                String code = rs.getString("course_code");

                if(input.equals(code)){
                    flag=1;
                    break;
                }

            }

            if(flag==0){
                System.out.println("*****Course not registered.*****\n");
            }
            else{
                String sql = "DELETE FROM course_catalog course_code =?";
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1,input);
                stmt.executeUpdate();
                stmt.close();

                System.out.println("Successfully Unenrolled " + input+".");
            }

            rs.close();
            stm.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }


        return;

    }

    //Update course
    public static void updateC(){

        System.out.println(
                "\nFollowing is the course catalog.\n");

        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "SELECT * FROM course_catalog";
            PreparedStatement stm = c.prepareStatement(str);
            ResultSet rs = stm.executeQuery();

            System.out.println("name\tcode\tcredits\tLTP\tprerequisite");

            while ( rs.next() ) {
                String code = rs.getString("course_code");
                int creds = rs.getInt("credits");
                String name = rs.getString("course_name");
                String ltp = rs.getString("ltp");
                String prereq = rs.getString("prereq");

                System.out.println(name+"\t"+code+"\t"+creds+"\t"+ltp+"\t"+prereq);
            }

            System.out.println("\n");

            System.out.println("Enter the course code you want to update.");
            Scanner sc = new Scanner(System.in);
            String input = sc.next();

            int flag=0;

            while ( rs.next() ) {


                String code = rs.getString("course_code");

                if(code.equals(input)){
                    flag=1;
                    break;
                }

            }

            if(flag==0){
                System.out.println("*****Course not registered.*****\n");
            }
            else{

                System.out.println("Enter the field you want to update.");
                System.out.println("1.Course name\n2.Course code\n3.Credits\n4.LTP structure\n5.Prerequisite");

                int res = sc.nextInt();

                if(res==1){

                    System.out.println("Enter new name.");
                    sc.nextLine();
                    String name = sc.nextLine();

                    String sql = "UPDATE course_catalog SET course_name = ? WHERE course_code =?";
                    PreparedStatement stmt = c.prepareStatement(sql);
                    stmt.setString(1,name);
                    stmt.setString(2,input);
                    stmt.executeUpdate();
                    stmt.close();
                }
                else if(res==2){

                    System.out.println("Enter new code.");
                    String code = sc.next();

                    String sql = "UPDATE course_catalog SET course_code = ? WHERE course_code =?";
                    PreparedStatement stmt = c.prepareStatement(sql);
                    stmt.setString(1,code);
                    stmt.setString(2,input);
                    stmt.executeUpdate();
                    stmt.close();
                }
                else if(res==3){

                    System.out.println("Enter new credits.");
                    int credits = sc.nextInt();

                    String sql = "UPDATE course_catalog SET credits = ? WHERE course_code =?";
                    PreparedStatement stmt = c.prepareStatement(sql);
                    stmt.setInt(1,credits);
                    stmt.setString(2,input);
                    stmt.executeUpdate();
                    stmt.close();
                }
                else if(res==4){

                    System.out.println("Enter new LTP structure.");
                    String ltp = sc.next();

                    String sql = "UPDATE course_catalog SET ltp = ? WHERE course_code =?";
                    PreparedStatement stmt = c.prepareStatement(sql);
                    stmt.setString(1,ltp);
                    stmt.setString(2,input);
                    stmt.executeUpdate();
                    stmt.close();
                }
                else if(res==5){

                    System.out.println("Enter new prerequisite.");
                    String name = sc.next();

                    String sql = "UPDATE course_catalog SET prereq = ? WHERE course_code =?";
                    PreparedStatement stmt = c.prepareStatement(sql);
                    stmt.setString(1,name);
                    stmt.setString(2,input);
                    stmt.executeUpdate();
                    stmt.close();
                }



                System.out.println("Successfully Updated " + input+".");
            }

            rs.close();
            stm.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }


        return;

    }

    public static void editCatalog(){

        System.out.println("Choose an option.");
        System.out.println("1. Register a course.");
        System.out.println("2. Deregister a course.");
        System.out.println("3. Update a course details.");

        Scanner sc = new Scanner (System.in);

        int ans = sc.nextInt();

        if(ans==1){
            registerC();
        }
        else if(ans==2){
            deregisterC();
        }
        else if(ans==3){
            updateC();
        }
        else{
            System.out.println("*****INVALID INPUT.*****");
        }

        return;

    }

}

