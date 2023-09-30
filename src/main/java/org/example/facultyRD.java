package org.example;

import java.sql.*;
import java.util.*;

public class facultyRD {

    //remove course from current offerings
    public static void deregisterOfferings(){

        registerCourse.viewCatalog();

        System.out.println("Enter the course code you want to deregister.\n");
        Scanner sc = new Scanner(System.in);
        String input = sc.next();

        Connection c=null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "SELECT * FROM course_offerings";
            PreparedStatement stm = c.prepareStatement(str);

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
                System.out.println("*****Course not currently offered. Try again.*****\n");
                deregisterOfferings();
            }
            else{
                String sql = "DELETE FROM course_offerings WHERE course_code =?";
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1,input);

                stmt.executeUpdate();

                stmt.close();

                System.out.println("Successfully deregistered course " + input+".");
                registerCourse.viewCatalog();

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

    public static void registerOffering(){

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the following details to register a course.\n");
        System.out.println("1.Course name ");
        String cname = sc.nextLine();
        System.out.println("2.Course code ");
        String ccode = sc.next();
        System.out.println("3.Credits ");
        int ccredits = sc.nextInt();
        System.out.println("4.LTP structure ");
        String cltp = sc.next();
        System.out.println("5.PreRequisite required (enter none if no required) ");
        String cprereq = sc.next();
        System.out.println("6.Minimum CG required ");
        int cmin = sc.nextInt();
        System.out.println("7.Course type(program core/elective) ");
        sc.nextLine();
        String ctype = sc.nextLine();


        Connection c=null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "SELECT * FROM course_offerings";
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
                System.out.println("*****Course already offered.*****\n");
            }
            else{
                //check course catalog to see if offered course exists
                String sql = "SELECT * FROM course_catalog WHERE course_code=?";
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1,ccode);

                ResultSet rs1 = stmt.executeQuery();

                if(!rs1.next()){
                    System.out.println("*****Course not present in course catalog.*****");
                }
                else{

                    if(cprereq.equals("none")){
                        String data = "INSERT into course_offerings(course_name,course_code,credits,ltp,min_cg,course_type)" + "VALUES(?,?,?,?,?,?)";

                        PreparedStatement insert = c.prepareStatement(data);
                        insert.setString(1,cname);
                        insert.setString(2,ccode);
                        insert.setInt(3,ccredits);
                        insert.setString(4,cltp);
                        insert.setInt(5,cmin);
                        insert.setString(6,ctype);
                        insert.executeUpdate();
                        insert.close();
                    }
                    else{
                        String data = "INSERT into course_offerings(course_name,course_code,credits,ltp,min_cg,course_type,prereq)" + "VALUES(?,?,?,?,?,?,?)";

                        PreparedStatement insert = c.prepareStatement(data);
                        insert.setString(1,cname);
                        insert.setString(2,ccode);
                        insert.setInt(3,ccredits);
                        insert.setString(4,cltp);
                        insert.setInt(5,cmin);
                        insert.setString(6,ctype);
                        insert.setString(7,cprereq);
                        insert.executeUpdate();
                        insert.close();
                    }

                    System.out.println("Course successfully registered.");

                }

                rs1.close();
                stmt.close();

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

}
