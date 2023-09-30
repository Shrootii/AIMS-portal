package org.example;

import java.sql.*;
import java.util.*;

public class facultyPortal extends editRecord {

    public static void portal(String username) {

        // setting up connection to database
        try (Connection datacon = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/aims", "postgres", "shruti2709")) {

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("\nWELCOME FACULTY!");

        System.out.println(
                "\nEnter your choice.\n\n1. Register a course.\n2. Degister a course.\n3. Check grades.\n4. Upload grades.\n5. Edit personal details.\n6. Logout.");


        //registerCourse();
        Scanner sc = new Scanner(System.in);
        int ans = sc.nextInt();

        switch(ans){

            //registering a course
            case 1:
                facultyRD.registerOffering();
                portal(username);
                break;

            //degistering a course
            case 2:
                facultyRD.deregisterOfferings();
                portal(username);
                break;


            //checking grades of all students
            //grades according to a course
            case 3:

                System.out.println("Please enter the course code you want to view grades of.\n");
                String input = sc.next();
                System.out.println("Please enter the semester (format - 2020A/2020B)\n");
                String sem = sc.next();
                grades.viewGrades(input, 2,sem);
                portal(username);
                break;

            //Upload grades
            case 4:

                importcsv.copycsv();
                importcsv.uploadcsv();
                portal(username);
                break;

            //Editing personal details such as contact, address etc.
            case 5:
                editRecord.edit(username,2);
                portal(username);
                break;

            //logging out
            case 6:
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
