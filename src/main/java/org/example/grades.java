package org.example;

import java.sql.*;

public class grades {

    public static boolean viewGrades(String username, int type,String sem){

        //displays the grades of all courses completed
        //username = student id when student
        //username - course code  when faculty


        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "";

            if(type==1) str = "SELECT * FROM all_enrollments WHERE student_id=? AND sem=?";
            else if(type==2) {
                str = "SELECT * FROM all_enrollments WHERE course_code=? AND sem=?";
            }
            PreparedStatement stm = c.prepareStatement(str);
            stm.setString(1,username);
            stm.setString(2,sem);


            ResultSet rs = stm.executeQuery();



            System.out.println("student id\t\t\t\t\tcode\tcredits\tgrades");

            while ( rs.next()) {

                String sname = rs.getString("student_id");
                String code = rs.getString("course_code");
                int creds = rs.getInt("credits");
                String grade = rs.getString("grade");

                if(grade!=null)System.out.println(sname+ "\t"+code+"\t"+creds+"\t"+grade);
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


    public static double calculateCg(String username){

        int points =0;
        double cgpa =0;
        int total_creds = 0;
        //calculates the current CGPA of student

        Connection c = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            String str = "SELECT * FROM all_enrollments WHERE student_id=?";
            PreparedStatement stm = c.prepareStatement(str);
            stm.setString(1,username);

            ResultSet rs = stm.executeQuery();

            //System.out.println("code\tcredits\tgrades");

            while ( rs.next()) {
                //String code = rs.getString("course_code");
                int creds = rs.getInt("credits");
                String grade = rs.getString("grade");

                if(grade==null || grade == "F"){
                    //System.out.println(code+"\t"+creds+"\t"+grade);
                    continue;
                }
                else if(grade.equals("A")){
                    points += 10 * creds;
                    total_creds += creds;
                }
                else if(grade.equals("A-")){
                    points += 9 * creds;
                    total_creds += creds;
                }
                else if(grade.equals("B")){
                    points += 8 * creds;
                    total_creds += creds;
                }
                else if(grade.equals("B-")){
                    points += 7 * creds;
                    total_creds += creds;
                }
                else if(grade.equals("C")){
                    points += 6 * creds;
                    total_creds += creds;
                }
                else if(grade.equals("C-")){
                    points += 5 * creds;
                    total_creds += creds;
                }
                else if(grade.equals("D")){
                    points += 4 * creds;
                    total_creds += creds;
                }
            }

            if(total_creds!=0)cgpa = points/total_creds;
            //System.out.println(cgpa + " "+ total_creds+ " "+points);


            //System.out.println("Current CGPA: " + cgpa);
            rs.close();
            stm.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        return cgpa;


    }

}
