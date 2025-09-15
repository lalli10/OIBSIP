import java.sql.*;
import java.util.Scanner;

public class OnlineReservationSystem {
    static Connection con;
    static Scanner sc = new Scanner(System.in);
    static int loggedInUserId = -1;

    public static void main(String[] args) {
        try {
            connect();
            createTables();
            mainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Database Connection =====
    static void connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:reservation.db");
    }

    static void createTables() throws SQLException {
        Statement stmt = con.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL)");
        stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                "pnr INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "name TEXT," +
                "age INTEGER," +
                "train_no INTEGER," +
                "train_name TEXT," +
                "class_type TEXT," +
                "date_of_journey TEXT," +
                "from_place TEXT," +
                "to_place TEXT," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id))");
    }

    // ===== Main Menu =====
    static void mainMenu() throws SQLException {
        while (true) {
            System.out.println("\n1. Login\n2. Sign Up\nEnter choice (or 'q' to quit): ");
            String choice = sc.nextLine().trim();
            if (choice.equalsIgnoreCase("q")) {
                System.out.println("Exiting...");
                break;
            }
            switch (choice) {
                case "1" -> login();
                case "2" -> signUp();
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ===== User Signup =====
    static void signUp() throws SQLException {
        while (true) {
            System.out.print("Choose a username (or 'q' to quit): ");
            String username = sc.nextLine().trim();
            if (username.equalsIgnoreCase("q")) return;

            System.out.print("Choose a password (or 'q' to quit): ");
            String password = sc.nextLine().trim();
            if (password.equalsIgnoreCase("q")) return;

            PreparedStatement ps = con.prepareStatement("INSERT INTO users(username, password) VALUES(?, ?)");
            try {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
                System.out.println("User registered successfully!");
                return;
            } catch (SQLException e) {
                System.out.println("Username already exists. Try again.");
            }
        }
    }

    // ===== User Login =====
    static void login() throws SQLException {
        while (true) {
            System.out.print("Enter username (or 'q' to quit): ");
            String username = sc.nextLine().trim();
            if (username.equalsIgnoreCase("q")) return;

            System.out.print("Enter password (or 'q' to quit): ");
            String password = sc.nextLine().trim();
            if (password.equalsIgnoreCase("q")) return;

            PreparedStatement ps = con.prepareStatement(
                    "SELECT user_id FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                loggedInUserId = rs.getInt("user_id");
                System.out.println("Login successful!");
                userMenu();
                return;
            } else {
                System.out.println("Invalid credentials! Try again.");
            }
        }
    }

    // ===== User Menu =====
    static void userMenu() throws SQLException {
        int choice = 0;
        do {
            System.out.println("\n1. Book Ticket\n2. Cancel Ticket\n3. View My Tickets\n4. View Database Tables\n5. Logout");
            System.out.print("Enter choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input!");
                continue;
            }
            switch (choice) {
                case 1 -> bookTicket();
                case 2 -> cancelTicket();
                case 3 -> viewTickets();
                case 4 -> viewDatabaseTables();
                case 5 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 5);

    }

    // ===== Book Ticket =====
    static void bookTicket() throws SQLException {
        System.out.print("Enter passenger name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("Enter train number: ");
        int trainNo = Integer.parseInt(sc.nextLine());
        System.out.print("Enter train name: ");
        String trainName = sc.nextLine();
        System.out.print("Enter class type: ");
        String classType = sc.nextLine();
        System.out.print("Enter date of journey: ");
        String date = sc.nextLine();
        System.out.print("Enter from place: ");
        String from = sc.nextLine();
        System.out.print("Enter to place: ");
        String to = sc.nextLine();

        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO reservations(user_id, name, age, train_no, train_name, class_type, date_of_journey, from_place, to_place) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, loggedInUserId);
        ps.setString(2, name);
        ps.setInt(3, age);
        ps.setInt(4, trainNo);
        ps.setString(5, trainName);
        ps.setString(6, classType);
        ps.setString(7, date);
        ps.setString(8, from);
        ps.setString(9, to);
        ps.executeUpdate();

        System.out.println("Ticket booked successfully!");
    }

    // ===== Cancel Ticket =====
    static void cancelTicket() throws SQLException {
        System.out.print("Enter PNR to cancel (or 'q' to quit): ");
        String input = sc.nextLine().trim();
        if (input.equalsIgnoreCase("q")) return;

        int pnr;
        try {
            pnr = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid PNR!");
            return;
        }

        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM reservations WHERE pnr=? AND user_id=?");
        ps.setInt(1, pnr);
        ps.setInt(2, loggedInUserId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Reservation found: " + rs.getString("name") + " | Train: " + rs.getString("train_name"));
            System.out.print("Confirm cancel (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                PreparedStatement del = con.prepareStatement("DELETE FROM reservations WHERE pnr=?");
                del.setInt(1, pnr);
                del.executeUpdate();
                System.out.println("Ticket cancelled.");
            }
        } else {
            System.out.println("No reservation found with given PNR.");
        }
    }

    // ===== View My Tickets =====
    static void viewTickets() throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM reservations WHERE user_id=?");
        ps.setInt(1, loggedInUserId);
        ResultSet rs = ps.executeQuery();
        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println("PNR: " + rs.getInt("pnr") +
                    " | Name: " + rs.getString("name") +
                    " | Train: " + rs.getString("train_name") +
                    " | Date: " + rs.getString("date_of_journey") +
                    " | From: " + rs.getString("from_place") +
                    " | To: " + rs.getString("to_place"));
        }
        if (!found) System.out.println("No tickets booked.");
    }

    // ===== View Database Tables (Admin/debugging) =====
    static void viewDatabaseTables() throws SQLException {
        System.out.println("\n=== Users Table ===");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        boolean empty = true;
        while (rs.next()) {
            empty = false;
            System.out.println("User ID: " + rs.getInt("user_id") +
                    " | Username: " + rs.getString("username") +
                    " | Password: " + rs.getString("password"));
        }
        if (empty) System.out.println("No users found.");

        System.out.println("\n=== Reservations Table ===");
        rs = stmt.executeQuery("SELECT * FROM reservations");
        empty = true;
        while (rs.next()) {
            empty = false;
            System.out.println("PNR: " + rs.getInt("pnr") +
                    " | User ID: " + rs.getInt("user_id") +
                    " | Name: " + rs.getString("name") +
                    " | Train: " + rs.getString("train_name") +
                    " | Date: " + rs.getString("date_of_journey") +
                    " | From: " + rs.getString("from_place") +
                    " | To: " + rs.getString("to_place") +
                    " | Class: " + rs.getString("class_type"));
        }
        if (empty) System.out.println("No reservations found.");
    }
}
