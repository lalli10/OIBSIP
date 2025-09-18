import java.util.*;

class Book {
    int id;
    String title;
    String author;
    boolean isIssued;

    Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    @Override
    public String toString() {
        return id + " | " + title + " | " + author + (isIssued ? " (Issued)" : " (Available)");
    }
}

public class DigitalLibraryManagement {
    private static Scanner sc = new Scanner(System.in);
    private static Map<Integer, Book> books = new HashMap<>();
    private static int bookCounter = 1;

    // Admin login credentials (demo)
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Digital Library Management System =====");
            System.out.println("1. Admin Login");
            System.out.println("2. User Access");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> adminLogin();
                case "2" -> userMenu();
                case "3" -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // ---------------- Admin ----------------
    private static void adminLogin() {
        System.out.print("Enter admin username: ");
        String user = sc.nextLine().trim();
        System.out.print("Enter admin password: ");
        String pass = sc.nextLine().trim();

        if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
            System.out.println("Admin login successful!");
            adminMenu();
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. View All Books");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> addBook();
                case "2" -> removeBook();
                case "3" -> viewBooks();
                case "4" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static void addBook() {
        System.out.print("Enter book title: ");
        String title = sc.nextLine();
        System.out.print("Enter book author: ");
        String author = sc.nextLine();

        Book book = new Book(bookCounter++, title, author);
        books.put(book.id, book);
        System.out.println("Book added successfully: " + book);
    }

    private static void removeBook() {
        System.out.print("Enter book ID to remove: ");
        String input = sc.nextLine().trim();
        try {
            int id = Integer.parseInt(input);
            if (books.containsKey(id)) {
                books.remove(id);
                System.out.println("Book removed successfully!");
            } else {
                System.out.println("Book not found!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }

    private static void viewBooks() {
        System.out.println("\n--- Library Books ---");
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            books.values().forEach(System.out::println);
        }
    }

    // ---------------- User ----------------
    private static void userMenu() {
        while (true) {
            System.out.println("\n===== User Menu =====");
            System.out.println("1. View Books");
            System.out.println("2. Search Book by Title");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> viewBooks();
                case "2" -> searchBook();
                case "3" -> issueBook();
                case "4" -> returnBook();
                case "5" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static void searchBook() {
        System.out.print("Enter book title to search: ");
        String title = sc.nextLine().toLowerCase();

        boolean found = false;
        for (Book book : books.values()) {
            if (book.title.toLowerCase().contains(title)) {
                System.out.println(book);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No book found with that title.");
        }
    }

    private static void issueBook() {
        System.out.print("Enter book ID to issue: ");
        String input = sc.nextLine().trim();
        try {
            int id = Integer.parseInt(input);
            Book book = books.get(id);
            if (book != null && !book.isIssued) {
                book.isIssued = true;
                System.out.println("Book issued successfully: " + book.title);
            } else {
                System.out.println("Book not available or already issued.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }

    private static void returnBook() {
        System.out.print("Enter book ID to return: ");
        String input = sc.nextLine().trim();
        try {
            int id = Integer.parseInt(input);
            Book book = books.get(id);
            if (book != null && book.isIssued) {
                book.isIssued = false;
                System.out.println("Book returned successfully: " + book.title);
            } else {
                System.out.println("Invalid book ID or book was not issued.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }
}
