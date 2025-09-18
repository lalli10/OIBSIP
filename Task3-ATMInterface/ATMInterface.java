import java.util.*;

class Transaction {
    String type;
    double amount;
    double balance;

    Transaction(String type, double amount, double balance) {
        this.type = type;
        this.amount = amount;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return type + " of " + amount + " | Balance after: " + balance;
    }
}

class BankAccount {
    private double balance;
    private List<Transaction> history = new ArrayList<>();

    BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            history.add(new Transaction("Deposit", amount, balance));
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            history.add(new Transaction("Withdraw", amount, balance));
            System.out.println("Withdrew: " + amount);
        } else {
            System.out.println("Insufficient balance or invalid amount!");
        }
    }

    public void transfer(BankAccount receiver, double amount) {
        if (amount > 0 && amount <= balance) {
            this.withdraw(amount);
            receiver.deposit(amount);
            history.add(new Transaction("Transfer", amount, balance));
            System.out.println("Transferred: " + amount);
        } else {
            System.out.println("Transfer failed! Insufficient balance.");
        }
    }

    public void showHistory() {
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : history) {
                System.out.println(t);
            }
        }
    }
}

public class ATMInterface {
    private static final String USER_ID = "user123";
    private static final String USER_PIN = "1234";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Login
        System.out.print("Enter User ID: ");
        String userId = sc.nextLine();
        System.out.print("Enter PIN: ");
        String userPin = sc.nextLine();

        if (!userId.equals(USER_ID) || !userPin.equals(USER_PIN)) {
            System.out.println("Invalid credentials! Exiting...");
            return;
        }

        BankAccount account = new BankAccount(1000); // start with 1000 balance
        BankAccount receiver = new BankAccount(500); // for transfer demo

        while (true) {
            System.out.println("\n===== ATM Menu =====");
            System.out.println("1. Transactions History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> account.showHistory();
                case 2 -> {
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmt = sc.nextDouble();
                    account.withdraw(withdrawAmt);
                }
                case 3 -> {
                    System.out.print("Enter amount to deposit: ");
                    double depositAmt = sc.nextDouble();
                    account.deposit(depositAmt);
                }
                case 4 -> {
                    System.out.print("Enter amount to transfer: ");
                    double transferAmt = sc.nextDouble();
                    account.transfer(receiver, transferAmt);
                }
                case 5 -> {
                    System.out.println("Thank you for using ATM. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
