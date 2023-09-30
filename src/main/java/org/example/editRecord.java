package org.example;

import java.sql.*;
import java.util.*;

public class editRecord {

    public static boolean edit(String username, int type){

        System.out.println("Enter the field you would like to edit.");
        System.out.println("1.Name\n2.Address\n3.Contact\n");

        Scanner sc = new Scanner (System.in);
        int input = sc.nextInt();

        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");



            if(input == 1){

                System.out.println("Enter new name.");
                sc.nextLine();
                String name = sc.nextLine();
                String str="";

                if (type==1)str = "UPDATE student_record SET name = ? WHERE id = ?";
                else if(type==2) str = "UPDATE faculty_record SET name = ? WHERE id = ?" ;
                else if(type==3) str = "UPDATE admin_record SET name = ? WHERE id = ?";
                PreparedStatement stm = c.prepareStatement(str);
                stm.setString(1,name);
                stm.setString(2,username);

                stm.executeUpdate();
                System.out.println(name);

                System.out.println("Name updated.");
                //stm.close();
            }
            else if(input == 2){

                System.out.println("Enter new address.");
                sc.nextLine();
                String adrs = sc.nextLine();
                String str="";

                if (type==1) str = "UPDATE student_record SET address = ? WHERE id = ?";
                else if(type==2) str = "UPDATE faculty_record SET address = ? WHERE id = ?";
                else if(type==3) str = "UPDATE admin_record SET address = ? WHERE id = ?";
                PreparedStatement stm = c.prepareStatement(str);
                stm.setString(1,adrs);
                stm.setString(2,username);

                stm.executeUpdate();

                System.out.println("Address updated.");
                stm.close();
            }
            else if(input == 3){

                System.out.println("Enter new contact.");
                String num = sc.next();
                String str = "";

                if(type==1) str = "UPDATE student_record SET contact = ? WHERE id = ?";
                else if(type==2) str = "UPDATE faculty_record SET contact = ? WHERE id = ?";
                else if(type==3) str = "UPDATE admin_record SET contact = ? WHERE id = ?";
                PreparedStatement stm = c.prepareStatement(str);
                stm.setString(1,num);
                stm.setString(2,username);

                stm.executeUpdate();

                System.out.println("Contact number updated.");
                stm.close();
            }
            else {
                System.out.println("*****INVALID INPUT.*****");
            }


            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        return true;

    }

}
