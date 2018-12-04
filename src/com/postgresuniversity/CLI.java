package com.postgresuniversity;

import java.io.InputStreamReader;
import java.io.Console;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.util.Scanner;

public class CLI {
    static SQLServer server = new SQLServer();
    static Connection conn = server.connect();
    public static void main (String args[] ) throws IOException{
        String command = "";
        String selectedCommandInput = "";
        Scanner scanner = new Scanner(System.in);
        String[] parts = command.split(" ");
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("1: Add Faculty Entity ");
        System.out.println("2: Remove Faculty Entity");
        System.out.println("3: Add Project Entity");
        System.out.println("4: Remove Project Entity");
        System.out.println("5: Display a Table");
        System.out.println("6: Display all Tables");
        command = scanner.nextLine();
        if (command == null) {
            System.err.println("No console.");
            System.exit(1);
        }
        Boolean running = true;
        while(running){
            command = scanner.nextLine();
            String currentInput = command;
            switch (currentInput){
                case "1" :
                    System.out.println("Adding Professor entity...");
                    System.out.println("Enter Professor's pssn");
                    String pssn = scanner.nextLine();
                    int pssnInt = Integer.parseInt(pssn);
                    System.out.println("Enter Professor's pname");
                    String pname = scanner.nextLine();
                    System.out.println("Enter Professor's gender");
                    String gender = scanner.nextLine();
                    System.out.println("Enter Professor's rank");
                    String rank = scanner.nextLine();
                    int rankInt = Integer.parseInt(rank);
                    System.out.println("Enter Professor's reaspe");
                    String reaspe = scanner.nextLine();
                    String facultyToAdd = command;

                    try {
                        Statement statement = conn.createStatement();
                        statement.executeUpdate("INSERT INTO Professors " + "VALUES("+pssnInt+", '"+pname+"','"+gender+"',"+rankInt+",'"+reaspe+"')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "2" :
                    System.out.println("Begin removing faculty entity...");
                    System.out.println("Enter Professor's pssn");
                    String facultyID = scanner.nextLine();
                    //String facultyToDelete = command;
                    delete("PROFESSORS", facultyID);
                    break;
                case "3" :
                    System.out.println("Adding Project entity...");
                    String projectToAdd = command;
                    //delete(projectToAdd);
                    break;

                case "4" :
                    System.out.println("Removing Project entity...");
                    String projectToDelete = command;
                    //delete(projectToDelete);
                    break;

                case "5" :
                    System.out.println("Which table would you like to display?");
                    printTables();
                    String choice = scanner.nextLine();
                    //String parsedChoice = "";
                    //int rows = 0;
                    Choice c = getTableNameFromNumberedList(choice);

                    try {
                        Statement statement = conn.createStatement();
                        ResultSet rs = statement.executeQuery(selectAllFromTable(c.choice));
                        System.out.println(c.choice.toUpperCase());
                        while(rs.next()){
                            for(int i = 1; i <= c.rows; i++){
                                System.out.print(rs.getString(i) +" ");
                            }
                            System.out.println();

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "6":
                    //ArrayList choices = new ArrayList<Choice>();
                    //choices.add(getTableNameFromNumberedList("1"));
                    for(int i = 1; i <= 10; i++){
                        try {
                            Statement statement = conn.createStatement();
                            Choice thisChoice = getTableNameFromNumberedList(String.valueOf(i));
                            ResultSet rs = statement.executeQuery(selectAllFromTable(thisChoice.choice));
                            System.out.println(thisChoice.choice.toUpperCase());
                            while(rs.next()){
                                for(int j = 1; j <= thisChoice.rows; j++){
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

    public static void put(String data) {

    }
    public static void delete(String table, String id) {
        String idLabel;
        if (table.toUpperCase().equals("PROFESSORS")){
            idLabel = "pssn";
        } else {
            idLabel = "pronum";
        }
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM "+table+" WHERE "+idLabel+"='"+Integer.valueOf(id)+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String selectAllFromTable(String table){
        return "Select * FROM "+ table;
    }
    public static void printTables(){
        System.out.println("1: Departments");
        System.out.println("2: Gradstudents");
        System.out.println("3: managers");
        System.out.println("4: professors");
        System.out.println("5: profruns");
        System.out.println("6: profwd");
        System.out.println("7: profworks");
        System.out.println("8: projects");
        System.out.println("9: studwd");
        System.out.println("10: studworks");
    }
    public static Choice getTableNameFromNumberedList(String choice){
        Choice c = new Choice();
        if (choice.equals("1")){
            c.choice = "departments";
            c.rows = 3;
        }
        if (choice.equals("2")){
            c.choice = "gradstudents";
            c.rows = 6;
        }
        if (choice.equals("3")){
            c.choice = "manages";
            c.rows = 3;
        }
        if (choice.equals("4")){
            c.choice = "professors";
            c.rows = 5;
        }
        if (choice.equals("5")){
            c.choice = "profruns";
            c.rows = 3;
        }
        if (choice.equals("6")){
            c.choice = "profwd";
            c.rows = 2;
        }
        if (choice.equals("7")){
            c.choice = "profworks";
            c.rows = 3;
        }
        if (choice.equals("8")){
            c.choice = "projects";
            c.rows = 5;
        }
        if (choice.equals("9")){
            c.choice = "studwd";
            c.rows = 2;
        }
        if (choice.equals("10")){
            c.choice = "studworks";
            c.rows = 3;
        }
        return c;
    }
    public static class Choice {
        int rows;
        String choice;
    }
}