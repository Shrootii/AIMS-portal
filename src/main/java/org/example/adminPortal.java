package org.example;

import java.sql.*;
import java.util.*;
import java.io.*;

public class adminPortal extends adminCC {


    public static boolean semUpdate(){

        Scanner sn = new Scanner(System.in);

        System.out.println("Enter the current year: ");
        int year = sn.nextInt();

        System.out.println("Enter the current semester(A/B): ");
        String sem = sn.next();

        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "UPDATE calendar SET current_year = ? AND current_sem=?";
            PreparedStatement stm = c.prepareStatement(str);
            stm.setInt(1,year);
            stm.setString(2,sem);

            stm.executeUpdate();


        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return true;
    }

    public static void grad(){

        Scanner sn = new Scanner(System.in);

        System.out.println("Enter the minimum core credits required: ");
        int core = sn.nextInt();
        System.out.println("Enter the minimum elective credits required: ");
        int elective = sn.nextInt();
        System.out.println("Enter the minimum BTP credits required: ");
        int btp = sn.nextInt();

        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "UPDATE graduation SET core_credits=?";
            PreparedStatement stm = c.prepareStatement(str);
            stm.setInt(1,core);
            stm.executeUpdate();
            String str1 = "UPDATE graduation SET elective_credits=?";
            PreparedStatement stm1 = c.prepareStatement(str1);
            stm1.setInt(1,elective);
            stm1.executeUpdate();
            String str2 = "UPDATE graduation SET btp_credits=?";
            PreparedStatement stm2 = c.prepareStatement(str2);
            stm2.setInt(1,core);
            stm2.executeUpdate();

            stm.close();
            stm1.close();
            stm2.close();


        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Updated.");
        return;

    }

    public static void genScript(){

        System.out.println("Enter the student id.");
        Scanner sc = new Scanner(System.in);
        String input = sc.next();

        try {
            File file = new File("transcript.txt");
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);

            Connection c = null;

            try{
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                        "postgres", "shruti2709");

                String str = "SELECT * FROM student_record WHERE id=?";
                PreparedStatement stm = c.prepareStatement(str);
                stm.setString(1,input);
                ResultSet rs = stm.executeQuery();

                String str1 = "SELECT * FROM all_enrollments WHERE student_id=?";
                PreparedStatement stm1 = c.prepareStatement(str1);
                stm1.setString(1,input);
                ResultSet rs1 = stm1.executeQuery();



                if(rs.next()){
                    pw.write("Transcript of " + rs.getString("name") + "\n");
                    pw.write("ID: " + rs.getString("id") + "\n");
                    pw.write("BATCH: " + rs.getString("batch") + "\n");
                    pw.write("BRANCH: " + rs.getString("branch") + "\n");

                    pw.write("\n\n");

                    pw.write("Code\tCredits\tGrade\tType\n\n");

                    while(rs1.next()){
                        String code = rs1.getString("course_code");
                        String type = rs1.getString("course_type");
                        String grade = rs1.getString("grade");
                        int creds = rs1.getInt("credits");

                        pw.write(code + "\t"+creds+"\t"+grade+"\t"+type+"\n");
                    }

                }

                rs1.close();
                rs.close();
                stm.close();
                stm1.close();



            } catch ( Exception e ) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                System.exit(0);
            }


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void portal(String username) {

        // setting up connection to database
        try (Connection datacon = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/aims", "postgres", "shruti2709")) {
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("\nWELCOME ADMIN!");

        System.out.println("Enter your choice: ");
        System.out.println("1. Update semester. ");
        System.out.println("2. Check grades. ");
        System.out.println("3. Generate transcript. ");
        System.out.println("4. Update profile. ");
        System.out.println("5. Set graduation credit requirements. ");
        if(username.equals("dean@admin"))System.out.println("6. Edit course catalog. ");
        System.out.println("7. Logout. ");



        //registerCourse();
        Scanner sc = new Scanner(System.in);
        int ans = sc.nextInt();

        switch(ans){

            //updating the semester in calendar
            case 1:
                semUpdate();
                portal(username);
                break;


            //Editing course catalog
            case 6:
                adminCC.editCatalog();
                portal(username);
                break;


            //checking grades of all students
            //grades according to a course
            case 2:

                System.out.println("Please enter the course code you want to view grades of.\n");
                String input = sc.next();
                System.out.println("Please enter the semester (format - 2020A/2020B)\n");
                String sem = sc.next();
                grades.viewGrades(input, 2,sem);
                portal(username);
                break;

            //Generate transcript
            case 3:
                genScript();
                portal(username);
                break;

            //Update profile
            case 4:
                editRecord.edit(username,3);
                portal(username);
                break;

            //Graduation credit requirements
            case 5:
                grad();
                portal(username);
                break;


            //logging out
            case 7:
                System.out.println("You have logged out.");
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
