package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {
    static String[] options = {"add", "remove", "list", "exit"};
    static Path path = Paths.get("tasks.csv");
    static String[][] tasks = null;

    public static void main(String[] args) throws ParseException {

        tasks = readData(path);
        vievOptions(options);
        selectOption();
    }

    public static void vievOptions(String[] options) {
        System.out.println(ConsoleColors.BLUE + "Please select an option: ");
        for (String option : options) {
            System.out.println(ConsoleColors.WHITE + option);
        }
    }

    public static String[][] readData(Path path) {
        String[] tasksLine = null;
        String line = "";
        String[][] tasks = null;
        try {
            Scanner scanner = new Scanner(path);
            int linesNumber = (int) Files.lines(path).count();
            tasks = new String[linesNumber][3];

            for (int i = 0; i < linesNumber; i++) {
                line = scanner.nextLine();
                System.out.println(line);
                tasksLine = line.split(",");
                for (int j = 0; j < 3; j++) {
                    tasks[i][j] = tasksLine[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static void selectOption() throws ParseException {

        Scanner scanner = new Scanner(System.in);
        String input;

                do {
                    input = scanner.nextLine();
                    switch (input) {
                        case "add":
                            addTask();
                            break;
                        case "remove":
                            removeTask();
                            break;
                        case "list":
                            list();
                            break;
                        case "exit":
                            exit();
                            break;
                        default:
                            System.out.println("Please select a correct option.");
                            input = scanner.nextLine();
                    }
                    System.out.println(ConsoleColors.BLUE + "Please select an option: ");
                } while (!input.equals("exit"));
    }

    public static void addTask() throws ParseException {

        Scanner scanner = new Scanner(System.in);
        String regex = "\\d{4}-\\d{2}-\\d{2}";

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];

        String input = "";
        System.out.println("Please add task description");
        input = scanner.nextLine();
        tasks[tasks.length - 1][0] = input;

        do {
            System.out.println("Please add task due date (yyyy-MM-dd)");
            input = scanner.nextLine();
        }
        while (!input.matches(regex));
        tasks[tasks.length - 1][1] = input;

        do {
            System.out.println("Is your task important: true/false");
            input = scanner.nextLine();
        } while (!input.equals("true") && !input.equals("false"));
        tasks[tasks.length - 1][2] = input;
    }

    public static void removeTask() {

        Scanner scanner = new Scanner(System.in);
        String input = "";
        do {
            System.out.println("Please select number to remove");
            input = scanner.nextLine();

        } while (!NumberUtils.isParsable(input) || Integer.parseInt(input) < 0 || Integer.parseInt(input) >= tasks.length);
        tasks = ArrayUtils.remove(tasks, Integer.parseInt(input));
    }

    public static void list() {
        int i = 0;
        for(String[] line : tasks) {
            System.out.println(i + " : " + Arrays.toString(line));
            i++;
        }
    }

    public static void exit() {

        try (PrintWriter printWriter = new PrintWriter(String.valueOf(path))) {
            int i = 0;
            for(String[] line : tasks) {
                printWriter.println(Arrays.toString(line));
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ConsoleColors.RED + "Bye, bye.");
    }
}