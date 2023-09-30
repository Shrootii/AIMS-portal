package org.example;

import java.io.*;
import java.sql.*;
import java.util.*;


public class importcsv{

    public static void copycsv(){

        System.out.println("Enter the course code you want to upload grades of.");
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

            while(rs.next()){
                String ccode = rs.getString("course_code");
                if(ccode.equals(input)){
                    flag=1;
                    break;
                }
            }

            if(flag==0) System.out.println("*****Course not offered.*****");
            else{

                // System.out.println("Please enter the path where you want to download csv file.");
                // sc.nextLine();
                // String path = sc.nextLine();
                // path = path + "grades.csv";
                String filename = "grades.csv";

                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filename));

                //download csv file for editing
                String str1 = "SELECT * FROM current_enrollments WHERE course_code=?";
                PreparedStatement stm1 = c.prepareStatement(str1);
                stm1.setString(1, input);
                ResultSet rs1 = stm1.executeQuery();

                // write header line containing column names
                //fileWriter.write("student_id,course_code,course_type,credits,grade");
                fileWriter.write("student_id,course_code,grade");

                while (rs1.next()) {
                    String id = rs1.getString("student_id");
                    String code = rs1.getString("course_code");
                    //String type = rs1.getString("course_type");
                    //int creds = rs1.getInt("credits");
                    String grade = rs1.getString("grade");

                    String line = String.format("%s,%s,%s",
                            id, code, grade);
                    // String line = String.format("%s,%s,%s,%d,%s",
                    //         id, code, type, creds, grade);

                    fileWriter.newLine();
                    fileWriter.write(line);
                }

                fileWriter.close();
                System.out.println("CSV file has been downloaded.");
            }

            rs.close();
            stm.close();

        }catch(Exception e ){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

    }




    public static void uploadcsv() {

        System.out.println("Enter the path of csv file to be uploaded");
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();

        int batchSize = 20;

        Connection c=null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            c.setAutoCommit(false);

            String sql = "UPDATE all_enrollments SET grade = ? WHERE student_id=? AND course_code=?";
            PreparedStatement statement = c.prepareStatement(sql);

            String sql1 = "UPDATE current_enrollments SET grade = ? WHERE student_id=? AND course_code=?";
            PreparedStatement statement1 = c.prepareStatement(sql1);

            BufferedReader lineReader = new BufferedReader(new FileReader(path));
            String lineText = null;

            int count = 0;

            lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String id = data[0];
                String grade = data[2];
                String code = data[1];

                statement.setString(1, grade);
                statement.setString(2, id);
                statement.setString(3, code);

                statement1.setString(1, grade);
                statement1.setString(2, id);
                statement1.setString(3, code);

                statement.addBatch();
                statement1.addBatch();

                if (count % batchSize == 0) {
                    statement.executeBatch();
                    statement1.executeBatch();
                }
            }

            lineReader.close();

            // execute the remaining queries
            statement.executeBatch();
            statement1.executeBatch();

            statement.close();
            statement1.close();
            c.commit();
            c.close();


            System.out.println("Grades uploaded.");


        }catch(Exception e ){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        return;

    }
}
