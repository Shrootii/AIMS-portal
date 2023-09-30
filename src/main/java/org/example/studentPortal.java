package org.example;

import java.sql.*;
import java.util.*;

public class studentPortal extends editRecord {

    public static boolean cgpaUpdate(String username){

        double cg = grades.calculateCg(username);

        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "UPDATE student_record SET cgpa = ? WHERE id = ?";
            PreparedStatement stm = c.prepareStatement(str);
            stm.setDouble(1,cg);
            stm.setString(2,username);

            stm.executeUpdate();

            //System.out.println("code\tcredits\tgrades");

            stm.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        //System.out.println("CGPA updated");

        return true;

    }

    public static boolean updateSem(String username){


        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String st = "SELECT batch FROM student_record WHERE id=?";
            PreparedStatement stmt = c.prepareStatement(st);
            stmt.setString(1,username);

            ResultSet rs = stmt.executeQuery();
            int batch = 0;
            if(rs.next()) batch = rs.getInt("batch");

            String stg = "SELECT * FROM calendar";
            PreparedStatement stmtt = c.prepareStatement(stg);

            ResultSet rs1 = stmtt.executeQuery();
            int c_batch = 0;
            String sem ="";
            if(rs1.next()){
                c_batch = rs1.getInt("current_year");
                sem = rs1.getString("current_sem");
            }

            int total_sem =0;
            total_sem = c_batch - batch;
            total_sem = total_sem * 2;
            if(sem.equals("A")) total_sem+=1;
            else total_sem+=2;


            String str = "UPDATE student_record SET current_sem = ? WHERE id = ?";
            PreparedStatement stm = c.prepareStatement(str);
            stm.setDouble(1,total_sem);
            stm.setString(2,username);

            stm.executeUpdate();

            //System.out.println("code\tcredits\tgrades");
            stmt.close();
            stmtt.close();
            stm.close();
            rs.close();
            rs1.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        //System.out.println("CGPA updated");

        return true;
    }

    public static boolean gradcheck(String username){

        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String st = "SELECT * FROM graduation";
            PreparedStatement stmt = c.prepareStatement(st);
            ResultSet rs = stmt.executeQuery();
            int core=0,btp=0,elec =0;

            if(rs.next()){
                core = rs.getInt("core_credits");
                btp = rs.getInt("btp_credits");
                elec = rs.getInt("elective_credits");
            }


            String stg = "SELECT * FROM all_enrollments WHERE student_id=?";
            PreparedStatement stmtt = c.prepareStatement(stg);
            stmtt.setString(1,username);
            ResultSet rs1 = stmtt.executeQuery();
            int ccore = 0;
            int celec=0;
            int cbtp=0;
            while(rs1.next()){
                String type = rs1.getString("course_type");
                String grade = rs1.getString("grade");
                if(type.equals("elective") && grade!="F" && grade!=null){
                    celec+= rs1.getInt("credits");
                }
                else if(type.equals("btp") && grade!="F" && grade!=null){
                    cbtp+= rs1.getInt("credits");
                }
                else if(type.equals("program core") && grade!="F" && grade!=null){
                    ccore+= rs1.getInt("credits");
                }
            }

            if(cbtp<btp){
                System.out.println("You do not have sufficient BTP credits.");
            }
            else if(ccore<core){
                System.out.println("You do not have sufficient Program core credits.");
            }
            else if(celec<elec){
                System.out.println("You do not have sufficient elective credits.");
            }
            else System.out.println("You can graduate.");

            stmt.close();
            stmtt.close();
            rs.close();
            rs1.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return true;

    }


    public static void portal(String username) {

        // setting up connection to database
        try (Connection datacon = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/aims", "postgres", "shruti2709")) {

            // if (datacon != null) {
            //     System.out.println("Connected to the database!");
            // } else {
            //     System.out.println("Failed to make connection!");
            // }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }



        System.out.println("\nWELCOME STUDENT!");

        System.out.println(
                "\nEnter your choice.\n\n1. Register for a course.\n2. Degister a course.\n3. Check grades of all courses.\n4. Compute CGPA.\n5. Edit personal details.\n6. Graduation check\n7. Logout.");


        //registerCourse();
        Scanner sc = new Scanner(System.in);
        int ans = sc.nextInt();

        switch(ans){

            //registering for a course
            case 1:
                registerCourse.register(username);
                portal(username);
                break;

            //degistering a course
            case 2:
                deregisterCourse.deregister(username);
                portal(username);
                break;

            //checking grades of all courses
            case 3:
                System.out.println("Please enter the semester (format - 2020A/2020B)\n");
                String sem = sc.next();
                System.out.println(
                        "\nFollowing are the grades of all courses completed.\n");
                grades.viewGrades(username,1,sem);
                portal(username);
                break;

            //computing current cgpa
            case 4:

                double cg = grades.calculateCg(username);
                System.out.println("Current CGPA: " + cg);
                portal(username);
                break;

            //Editing personal details such as contact, address etc.
            case 5:
                editRecord.edit(username,1);
                portal(username);
                break;

            //graduation check
            case 6:
                gradcheck(username);
                portal(username);
                break;

            //logging out
            case 7:
                System.out.println("You have logged out.\n");
                break;

            default:
                System.out.println("*****INVALID INPUT. TRY AGAIN.*****");
                portal(username);
                break;
        }

        //sc.close();
        return;

    }

}
