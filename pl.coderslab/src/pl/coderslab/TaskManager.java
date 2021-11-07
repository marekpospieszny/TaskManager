package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {

    public static Path filePath = Paths.get("./tasks.csv");
    public static String[][] tasks = downloadDataFromFile();
    public static Scanner scan = new Scanner(System.in);


    public static void showMenu() {
        System.out.println(ConsoleColors.BLUE + "\n" + "Please select an option:");
        String[] menu = {"add", "remove", "list", "exit"};
        for (String eachLine : menu) {
            System.out.println(ConsoleColors.RESET + eachLine);
        }
    }


    public static void addTask() {
        System.out.println("Please add task description");
        String taskDescription = scan.nextLine();
        String taskDueDate = "";
        while (true) {
            try {
                if (taskDueDate.length() == 10) {
                    String[] dateParts = taskDueDate.split("-");
                    if (Integer.parseInt(dateParts[0]) >= 2020 &&
                            Integer.parseInt(dateParts[1]) > 0 && Integer.parseInt(dateParts[1]) <= 12 &&
                            Integer.parseInt(dateParts[2]) > 0 && Integer.parseInt(dateParts[2]) <= 31) {
                        if (Integer.parseInt(dateParts[1]) == 2 && Integer.parseInt(dateParts[2]) > 28) {
                            System.out.println(ConsoleColors.RED + "Incorrect month and/or day value provided.");
                            System.out.println(ConsoleColors.RESET + "Please add task due date: YYYY-MM-DD");
                            taskDueDate = scan.nextLine();
                        } else {
                            break;
                        }
                    } else {
                        System.out.println(ConsoleColors.RED + "Incorrect year, month or day value provided.");
                        System.out.println(ConsoleColors.RESET + "Please add task due date: YYYY-MM-DD");
                        taskDueDate = scan.nextLine();
                    }
                } else {
                    System.out.println("Please add task due date: YYYY-MM-DD");
                    taskDueDate = scan.nextLine();
                }
            } catch (ArrayIndexOutOfBoundsException e1) {
                System.out.println(ConsoleColors.RED + "Incorrect date format provided.");
                System.out.println(ConsoleColors.RESET + "Please add task due date: YYYY-MM-DD");
                taskDueDate = scan.nextLine();
            } catch (NumberFormatException e2) {
                System.out.println(ConsoleColors.RED + "Incorrect date format provided.");
                System.out.println(ConsoleColors.RESET + "Please add task due date: YYYY-MM-DD");
                taskDueDate = scan.nextLine();
            }
        }
        String taskImportant = "";
        while (true) {
            if ("true".equals(taskImportant) || "false".equals(taskImportant)) {
                break;
            } else {
                System.out.println("Is your task important: true/false");
                taskImportant = scan.nextLine();
            }
        }

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];

        tasks[tasks.length - 1][0] = taskDescription;
        tasks[tasks.length - 1][1] = taskDueDate;
        tasks[tasks.length - 1][2] = taskImportant;
    }


    public static String getInt() {
        while (!scan.hasNextInt()) {
            scan.nextLine();
            System.out.println("Please select number.");
        }
        return scan.nextLine();
    }


    public static void removeTask() {
        String anotherLineToRemove = "yes";
        while ("yes".equals(anotherLineToRemove)) {
            try {
                System.out.println("Please select number to remove.");
                int indexToRemove = Integer.parseInt(getInt());
                tasks = ArrayUtils.remove(tasks, indexToRemove);
                System.out.println("Value was successfully deleted. Do you want to remove another number: yes/no ?");
                anotherLineToRemove = scan.nextLine();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Incorrect value provided. Please give number within range of the list.");
            } catch (NumberFormatException ex) {
                System.out.println("Incorrect value provided. Please give number within range of the list.");
            }
        }
    }


    public static void exitProgram() {
        System.out.println("exit");
        try (PrintWriter writer = new PrintWriter("./tasks.csv")) {
//            writer.print("");
//            writer.close();
            for (String[] row : tasks) {
//                Files.writeString(filePath, StringUtils.join(row, ",") + "\n", StandardOpenOption.APPEND);
                writer.print(StringUtils.join(row, ",") + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(ConsoleColors.RED + "Bye, bye.");
    }


    public static void showList() {
        System.out.println("list");
        for (int i = 0; i < tasks.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tasks[i].length; j++) {
                System.out.print(tasks[i][j] + " ");
            }
            System.out.println();
        }
    }


//  Below method is not used in program. It's provided here just in case user wants to read the file:
    public static void readingTasksFile() {
        try {
            for (String line : Files.readAllLines(filePath)) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Provided file location does not exist.");
        }
    }


    public static String[][] downloadDataFromFile() {
        int linesCount = 0;
        try {
            for (String line : Files.readAllLines(filePath)) {
                linesCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] tasks = new String[linesCount][];
        int i = 0;
        try {
            for (String row : Files.readAllLines(filePath)) {
                tasks[i] = row.split(",");
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tasks;
    }


    public static void main(String[] args) {

        showMenu();
        String input = scan.nextLine();

        while (!"exit".equals(input)) {

            switch (input) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask();
                    break;
                case "list":
                    showList();
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }

            showMenu();
            input = scan.nextLine();
        }

        exitProgram();
    }
}