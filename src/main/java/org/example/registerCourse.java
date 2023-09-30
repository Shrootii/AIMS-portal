package org.example;

import java.sql.*;
import java.util.*;
//import aims.studentPortal;

public class registerCourse {

    public static boolean viewCatalog(){

        System.out.println(
                "\nFollowing are the course offerings this semester.\n");

        Connection c = null;
        Statement stmt = null;

        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery( "SELECT * FROM course_offerings;" );

            System.out.println("course\tcode\tcredits\tLTP\tpre-requisite\tmin CG\ttype");

            while ( rs.next() ) {
                String name = rs.getString("course_name");
                String code = rs.getString("course_code");
                int creds = rs.getInt("credits");
                String ltp = rs.getString("ltp");
                String prereq = rs.getString("prereq");
                int cg = rs.getInt("min_cg");
                String type = rs.getString("course_type");

                System.out.println(name+"\t"+code+"\t"+creds+"\t"+ltp+"\t"+prereq+"\t"+cg+"\t"+type);
            }

            System.out.println("\n");
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        return true;
    }

    public static void register(String username) {

        viewCatalog();

        //taking course input
        System.out.println("Please enter the course code you want to register for.\n");
        Scanner sc = new Scanner(System.in);
        String input = sc.next();

        //System.out.println(input);

        Connection c = null;
        Statement stmt = null;

        //checking if correct input code is taken
        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims",
                    "postgres", "shruti2709");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery( "SELECT * FROM course_offerings;" );

            int flag=0;

            while ( rs.next() ) {


                String code = rs.getString("course_code");

                if(input.equals(code)){
                    flag=1;
                    break;
                }

            }

            if(flag==0){
                System.out.println("*****Course not offered. Try again.*****\n");
                register(username);
            }
            else{

                //saving all course information in various variables

                int creds = rs.getInt("credits");

                String prereq = rs.getString("prereq");
                int cg = rs.getInt("min_cg");
                String type = rs.getString("course_type");


                //fetching data from student record
                //checking prereq from all enrollments

                PreparedStatement statement = c.prepareStatement("SELECT * FROM all_enrollments WHERE student_id = ? AND course_code = ?");
                statement.setString(1, username);
                statement.setString(2, input);

                ResultSet sr = statement.executeQuery();
                boolean isRegistered = sr.next();

                if(isRegistered) System.out.println("You are already registered for this course.");
                else{
                    statement.setString(2, prereq);
                    ResultSet result = statement.executeQuery();

                    boolean prerequisite = result.next();

                    if(!prerequisite){
                        //prerequisite not enrolled
                        System.out.println(
                                "You have not cleared the required pre-requisites for this course.");
                    }
                    else{

                        int credits = result.getInt("credits");
                        String grade = result.getString("grade");

                        //System.out.println(grade + " credits " + credits);

                        //to check if prerequisite passed or not
                        if(grade==null || grade.equals("F")){
                            System.out.println("You have not cleared the required prerequisites for this course.");
                        }
                        else{
                            //if prerequisite is cleared, we check the max credit limit.
                            //calculating current credits in a semester.
                            //calculating previous credits earned. (only courses passed)

                            String str = "SELECT * FROM all_enrollments WHERE student_id=?";
                            PreparedStatement stm = c.prepareStatement(str);
                            stm.setString(1,username);

                            ResultSet prev = stm.executeQuery();
                            double prev_credits = 0;
                            double current_credits=0;

                            while(prev.next()){

                                String course_grade = prev.getString("grade");

                                if(course_grade==null){
                                    current_credits += prev.getInt("credits");
                                }
                                else if(course_grade.equals("F")) continue;
                                else{
                                    prev_credits+=prev.getInt("credits");
                                }

                                System.out.println(course_grade);
                            }

                            //System.out.println("prev  " + prev_credits);
                            //System.out.println("current " + current_credits);

                            //get the current semester from student record
                            String query = "SELECT current_sem,cgpa FROM student_record WHERE id=?";
                            PreparedStatement state = c.prepareStatement(query);
                            state.setString(1,username);

                            ResultSet sem = state.executeQuery();
                            int semester =1;
                            float cgpa = 10;

                            if(sem.next()) {
                                semester = sem.getInt("current_sem");
                                cgpa = sem.getFloat("cgpa");
                            }

                            //System.out.println(semester);
                            //System.out.println("cgpa " + cgpa);

                            //checking the credit limit condition
                            if(semester!=1){
                                prev_credits = prev_credits/(semester-1);
                                prev_credits = prev_credits * 1.25;
                            }
                            else prev_credits = 24;
                            //max of 24 credits in semester 1

                            //System.out.println(prev_credits);

                            if(current_credits + creds > prev_credits)
                                System.out.println("You have exceeded the maximum credit limit for this semester.");

                            else if(cgpa<cg)
                                System.out.println("You do not fulfil the minimum cgpa criteria.");
                            else {

                                //adding the course in current enrollments + all enrollments
                                String stg = "SELECT * FROM calendar";
                                PreparedStatement stmtt = c.prepareStatement(stg);

                                ResultSet rs1 = stmtt.executeQuery();
                                int c_batch = 0;
                                String semr ="";
                                if(rs1.next()){
                                    c_batch = rs1.getInt("current_year");
                                    semr = rs1.getString("current_sem");
                                }

                                String semester1 = Integer.toString(c_batch);
                                semester1 = semester1 + semr;

                                String data = "INSERT into all_enrollments(student_id,course_code,course_type,credits,sem)" + "VALUES(?,?,?,?,?)";
                                PreparedStatement insert = c.prepareStatement(data);
                                insert.setString(1,username);
                                insert.setString(2,input);
                                insert.setString(3,type);
                                insert.setInt(4,creds);
                                insert.setString(5,semester1);

                                insert.executeUpdate();

                                String en = "INSERT into current_enrollments(student_id,course_code,course_type,credits)" + "VALUES(?,?,?,?)";
                                PreparedStatement inser = c.prepareStatement(en);
                                inser.setString(1,username);
                                inser.setString(2,input);
                                inser.setString(3,type);
                                inser.setInt(4,creds);
                                inser.executeUpdate();


                                System.out.println("Successfully enrolled in " + input +".\n");

                                insert.close();
                                inser.close();

                            }

                            state.close();
                            sem.close();
                            stm.close();
                            prev.close();

                        }

                    }
                }

                sr.close();
                statement.close();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        return;
    }

}
