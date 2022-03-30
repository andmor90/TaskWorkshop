package pl.coderslab;


import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {

    public static void main(String[] args) {


        switch (showMenu()) {
            case "add":
                addOption();
                break;
            case "remove":
                removeOption();
                break;
            case "list":
                readCsv();
                break;
            case "exit":
                break;

        }
    }

    public static String showMenu() {
        String[] menu = {"Please select an option:", "add", "remove", "list", "exit"};
        for (int i = 0; i < menu.length; i++) {
            if (i == 0) {
                System.out.println(ConsoleColors.BLUE + menu[0] + ConsoleColors.RESET);
            } else {
                System.out.println(menu[i]);
            }
        }
        Scanner scan = new Scanner(System.in);
        String selectionUser = scan.nextLine();
        while (!Arrays.asList(menu).contains(selectionUser)) {
            System.out.println("Niepoprawny wybór. Proszę wybrać ponownie.");
            selectionUser = scan.nextLine();
        }
        return selectionUser;
    }

    public static String[][] readCsv() {
        Path path = Paths.get("tasks.csv");
        int countLines = 0;
        try {
            countLines = (int) Files.lines(path).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] tasks = new String[countLines][3];
        try {
            Scanner scan = new Scanner(path);
            while (scan.hasNextLine()) {
                for (int i = 0; i < tasks.length; i++) {
                    String[] split = scan.nextLine().split(",");
                    for (int j = 0; j < tasks[i].length; j++) {
                        String split2 = split[j];
                        tasks[i][j] = split2;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        for (int i = 0; i < tasks.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tasks[i].length; j++) {
                System.out.print(tasks[i][j] + ", ");
            }
            System.out.println();
        }
        return tasks;
    }

    public static String[][] addOption() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please add task description");
        String taskDescription = scan.nextLine();
        System.out.println("Please add task due date (ex format 2022-03-30)");
        String taskDate = scan.nextLine();
        System.out.println("Is your task is important: true/false");
        String isImportant = scan.nextLine();

        String[][] taskTable = readCsv();
        String[][] arrayCopy = new String[taskTable.length + 1][3];
        for (int i = 0; i < taskTable.length; i++) {
            for (int j = 0; j < taskTable[i].length; j++) {
                arrayCopy[i][j] = taskTable[i][j];
            }
        }
        arrayCopy[arrayCopy.length - 1][0] = taskDescription;
        arrayCopy[arrayCopy.length - 1][1] = taskDate;
        arrayCopy[arrayCopy.length - 1][2] = isImportant;

        File file = new File("tasks.csv");
        try (FileWriter fw = new FileWriter(file)) {
            for (int i = 0; i < arrayCopy.length; i++) {
                for (int j = 0; j < arrayCopy[i].length; j++) {
                    if (j < 2) {
                        fw.append(arrayCopy[i][j] + ", ");
                    } else {
                        fw.append(arrayCopy[i][j]);
                    }
                }
                fw.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayCopy;
    }

    public static void removeOption() {
        String[][] table = readCsv();

        Scanner scan = new Scanner(System.in);
        System.out.println("Please select number to remove.");
        int value = scan.nextInt();

        while (value >= table.length || value < 0) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            value = scan.nextInt();
        }

        String[][] remove = ArrayUtils.remove(table, value);
        for (int i = 0; i < remove.length; i++) {
            for (int j = 0; j < remove[i].length; j++) {
                System.out.print(remove[i][j]);

            }
            System.out.println();
        }

        File file2 = new File("tasks.csv");
        try (FileWriter fw = new FileWriter(file2)) {
            for (int i = 0; i < remove.length; i++) {
                for (int j = 0; j < remove[i].length; j++) {
                    if (j < 2) {
                        fw.append(remove[i][j] + ", ");
                    } else {
                        fw.append(remove[i][j]);
                    }
                }
                fw.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Value was successfully deleted");
    }
}

