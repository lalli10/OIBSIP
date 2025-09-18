import java.util.*;

class User {
    String username;
    String password;
    String name;

    User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }
}

public class OnlineExam {
    private static User user = new User("student", "1234", "Default User");
    private static boolean isLoggedIn = false;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            if (!isLoggedIn) {
                System.out.println("\n===== Online Examination System =====");
                System.out.println("1. Login");
                System.out.println("2. Exit");
                System.out.print("Enter choice: ");
                String choice = sc.nextLine().trim();

                if (choice.equals("1")) {
                    login();
                } else if (choice.equals("2")) {
                    System.out.println("Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid choice!");
                }
            } else {
                showMenu();
            }
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String pass = sc.nextLine().trim();

        if (uname.equals(user.username) && pass.equals(user.password)) {
            isLoggedIn = true;
            System.out.println("\nWelcome " + user.name + "! You are logged in.");
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private static void showMenu() {
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Update Profile & Password");
        System.out.println("2. Take Exam");
        System.out.println("3. Logout");
        System.out.print("Enter choice: ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1" -> updateProfile();
            case "2" -> takeExam();
            case "3" -> {
                isLoggedIn = false;
                System.out.println("Logged out successfully.");
            }
            default -> System.out.println("Invalid choice!");
        }
    }

    private static void updateProfile() {
        System.out.print("Enter new name: ");
        user.name = sc.nextLine().trim();
        System.out.print("Enter new password: ");
        user.password = sc.nextLine().trim();
        System.out.println("Profile updated successfully!");
    }

    private static void takeExam() {
        String[][] questions = {
                {"Java is a ___ language?", "1. Procedural", "2. Object-Oriented", "3. Functional", "4. Scripting", "2"},
                {"Which keyword is used to inherit a class in Java?", "1. implements", "2. this", "3. extends", "4. super", "3"},
                {"Which of these is not a Java feature?", "1. Platform Independent", "2. Pointers", "3. Robust", "4. Secure", "2"},
                {"Which package contains the Scanner class?", "1. java.io", "2. java.util", "3. java.net", "4. java.lang", "2"}
        };

        int score = 0;
        int timeLimit = 30; // seconds
        long startTime = System.currentTimeMillis();

        List<String> userAnswers = new ArrayList<>();

        for (int i = 0; i < questions.length; i++) {
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            if (elapsed >= timeLimit) {
                System.out.println("\n Time is up! Auto-submitting...");
                break;
            }

            System.out.println("\nQ" + (i + 1) + ": " + questions[i][0]);
            for (int j = 1; j <= 4; j++) {
                System.out.println(questions[i][j]);
            }
            System.out.print("Your answer (1-4): ");

            String ans = sc.nextLine().trim();
            userAnswers.add(ans);

            if (ans.equals(questions[i][5])) score++;
        }

        // Show results
        System.out.println("\n===== Exam Finished! =====");
        System.out.println("You scored " + score + "/" + questions.length);

        // Show correct answers
        System.out.println("\nCorrect Answers:");
        for (int i = 0; i < questions.length; i++) {
            System.out.println("Q" + (i + 1) + ": " + questions[i][Integer.parseInt(questions[i][5])]);
        }
    }
}
