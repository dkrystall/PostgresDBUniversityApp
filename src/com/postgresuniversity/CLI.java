package com.postgresuniversity;

import java.sql.*;
import java.util.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CLI {
    static SQLServer server = new SQLServer();
    static Connection conn = server.connect();
    static Scanner scanner = new Scanner(System.in);
    public static void main (String args[] ) throws IOException{
        String command = "";
        Boolean running = true;
        while(running){
            System.out.println();
            System.out.println("What would you like to do?");
            System.out.println("1: Add Faculty Entity ");
            System.out.println("2: Remove Faculty Entity");
            System.out.println("3: Add Project Entity");
            System.out.println("4: Remove Project Entity");
            System.out.println("5: Display a Table");
            System.out.println("6: Display all Tables");
            command = scanner.nextLine();
            String currentInput = command;
            switch (currentInput){
                case "1" :
                    System.out.println("Adding Professor entity...");
                    System.out.println("Enter Professor's Social Security Number");
                    String pssn = scanner.nextLine();
                    int pssnInt = Integer.parseInt(pssn);
                    System.out.println("Enter Professor's Name");
                    String pname = scanner.nextLine();
                    System.out.println("Enter Professor's gender");
                    String gender = scanner.nextLine();
                    System.out.println("Enter Professor's rank");
                    String rank = scanner.nextLine();
                    int rankInt = Integer.parseInt(rank);
                    System.out.println("Enter Professor's Research Specialty");
                    String reaspe = scanner.nextLine();

                    try {
                        Statement statement = conn.createStatement();
                        statement.executeUpdate("INSERT INTO Professors " + "VALUES("+pssnInt+", '"+pname+"','"+gender+"',"+rankInt+",'"+reaspe+"')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "2" :
                    System.out.println("Begin removing faculty entity...");
                    System.out.println("Enter Professor's ssn");
                    String facultyID = scanner.nextLine();
                    delete("PROFESSORS", facultyID);
                    break;
                case "3" :
                    System.out.println("Adding Project entity...");
                    put("PROJECTS");
                    break;

                case "4" :
                    System.out.println("Removing Project entity...");
                    System.out.println("Enter Project Number");
                    String projectID = scanner.nextLine();
                    delete("PROJECTS", projectID);
                    break;

                case "5" :
                    System.out.println("Which table would you like to display?");
                    printTables();
                    String choice = scanner.nextLine();
                    Choice c = getTableNameFromNumberedList(choice);

                    try {
                        Statement statement = conn.createStatement();
                        ResultSet rs = statement.executeQuery(selectAllFromTable(c.choice));
                        System.out.println(c.choice.toUpperCase());
                        while(rs.next()){
                            for(int i = 1; i <= c.cols; i++){
                                System.out.print(rs.getString(i) +" ");
                            }
                            System.out.println();

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "6":
                    for(int i = 1; i <= 10; i++){
                        try {
                            Statement statement = conn.createStatement();
                            Choice thisChoice = getTableNameFromNumberedList(String.valueOf(i));
                            ResultSet rs = statement.executeQuery(selectAllFromTable(thisChoice.choice));
                            System.out.println(thisChoice.choice.toUpperCase());
                            while(rs.next()){
                                for(int j = 1; j <= thisChoice.cols; j++){
                                    System.out.print(rs.getString(j) +" ");
                                }
                                System.out.println();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        System.out.println();
                    }
                    break;
                default :
                    System.out.println("Invalid Input");
                    break;

            }

        }
    }

    public static HashSet<Integer> getProjectNumbers(){
        HashSet projectNumbers = new HashSet<Integer>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(selectAllFromTable("PROJECTS"));
            while(rs.next()){
                for(int i = 1; i <= 1; i++){
                    String numString = rs.getString(i).split(" ")[0]; //removing white space after number.
                    int projNum = Integer.valueOf(numString);
                    projectNumbers.add(projNum);
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectNumbers;
    };


    public static void put(String table) {
        Boolean checkingID = true;
        if (table.toUpperCase().equals("PROJECTS")) {
            System.out.println("Enter a Sponsor for your project");
            String sponsor = scanner.nextLine();
            sponsor = sponsor.substring(0,9); //value only holds 10 characters
            HashSet projects = getProjectNumbers();

            String projectNumber = "";
            while (checkingID) {
                System.out.println("Enter desired project ID:");
                int pronum = Integer.valueOf(scanner.nextLine());
                if (projects.contains(pronum)) {
                    System.out.println("That ID is already in use. Please enter another");
                } else {
                    projectNumber = String.valueOf(pronum);
                    checkingID = false;
                }
            }

            System.out.println("Enter Project Start Date");
            String start = scanner.nextLine();


            System.out.println("Enter Project End Date");
            String end = scanner.nextLine();

            System.out.println("Enter budget for project");
            int budget = scanner.nextInt();

            String sqlInsert = "INSERT INTO PROJECTS VALUES(" + projectNumber + ", '" + sponsor + "','" + start + "','" + end + "'," + budget + ")";
            try {
                Statement statement = conn.createStatement();
                statement.executeUpdate(sqlInsert);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void delete(String table, String id) {
        String idLabel;
        if (table.toUpperCase().equals("PROFESSORS")){
            idLabel = "pssn";
            try {
                Statement statement = conn.createStatement();
                statement.executeUpdate("DELETE FROM profwd WHERE "+idLabel+"='"+Integer.valueOf(id)+"'");
                statement.executeUpdate("DELETE FROM profruns WHERE "+idLabel+"='"+Integer.valueOf(id)+"'");
                statement.executeUpdate("DELETE FROM profworks WHERE "+idLabel+"='"+Integer.valueOf(id)+"'");
                statement.executeUpdate("DELETE FROM manages WHERE "+idLabel+"='"+Integer.valueOf(id)+"'");
                statement.executeUpdate("DELETE FROM "+table+" WHERE "+idLabel+"='"+Integer.valueOf(id)+"'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            idLabel = "pronum";
            try {
                Statement statement = conn.createStatement();
                statement.executeUpdate("DELETE FROM MANAGES WHERE "+idLabel+"='"+Integer.valueOf(id)+"'");
                statement.executeUpdate("DELETE FROM PROFWORKS WHERE "+idLabel+"='"+Integer.valueOf(id)+"'");
                statement.executeUpdate("DELETE FROM STUDWORKS WHERE "+idLabel+"='"+Integer.valueOf(id)+"'");
                statement.executeUpdate("DELETE FROM "+table+" WHERE "+idLabel+"='"+Integer.valueOf(id)+"'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static String selectAllFromTable(String table){
        return "Select * FROM "+ table;
    }

    public static void printTables(){
        System.out.println("1: Departments");
        System.out.println("2: Graduate Students");
        System.out.println("3: Project Mangers");
        System.out.println("4: Professors");
        System.out.println("5: Professor Running Department");
        System.out.println("6: Professor Working on Projects");
        System.out.println("7: Professors working in Department");
        System.out.println("8: Projects");
        System.out.println("9: Students in Departments");
        System.out.println("10: Students Working on Projects");
    }
    public static Choice getTableNameFromNumberedList(String choice){
        Choice c = new Choice();
        if (choice.equals("1")){
            c.choice = "departments";
            c.cols = 3;
        }
        if (choice.equals("2")){
            c.choice = "gradstudents";
            c.cols = 6;
        }
        if (choice.equals("3")){
            c.choice = "manages";
            c.cols = 3;
        }
        if (choice.equals("4")){
            c.choice = "professors";
            c.cols = 5;
        }
        if (choice.equals("5")){
            c.choice = "profruns";
            c.cols = 3;
        }
        if (choice.equals("6")){
            c.choice = "profwd";
            c.cols = 2;
        }
        if (choice.equals("7")){
            c.choice = "profworks";
            c.cols = 3;
        }
        if (choice.equals("8")){
            c.choice = "projects";
            c.cols = 5;
        }
        if (choice.equals("9")){
            c.choice = "studwd";
            c.cols = 2;
        }
        if (choice.equals("10")){
            c.choice = "studworks";
            c.cols = 3;
        }
        return c;
    }

    public static class Choice {
        int cols;
        String choice;
    }
}
/*
class SQLServer {
    private final String url = "jdbc:postgresql://cs1.calstatela.edu:5432/cs4222s18";
    private final String user = "cs4222s18";
    private final String password = "fHypPC8C"; //add this before running

    public SQLServer() {}

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
*/