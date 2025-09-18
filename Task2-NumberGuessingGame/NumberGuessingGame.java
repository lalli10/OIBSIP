import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int totalScore = 0;
        boolean playAgain = true;

        System.out.println(" Welcome to the Number Guessing Game!");
        System.out.println("Rules: Guess the number between 1 and 100.");
        System.out.println("You have 10 attempts per round. Points are higher if you guess earlier.");

        while (playAgain) {
            int numberToGuess = random.nextInt(100) + 1;
            int attempts = 0;
            boolean guessed = false;

            System.out.println("\n New Round Started! Try to guess the number!");

            while (attempts < 10 && !guessed) {
                System.out.print("Enter your guess (1-100): ");
                int guess = scanner.nextInt();
                attempts++;

                if (guess == numberToGuess) {
                    int points = (11 - attempts) * 10; // earlier guess = more points
                    totalScore += points;
                    System.out.println(" Correct! You guessed in " + attempts + " attempts.");
                    System.out.println(" You earned " + points + " points this round!");
                    guessed = true;
                } else if (guess < numberToGuess) {
                    System.out.println(" Too low! Try again.");
                } else {
                    System.out.println(" Too high! Try again.");
                }
            }

            if (!guessed) {
                System.out.println(" Out of attempts! The number was: " + numberToGuess);
            }

            System.out.println(" Total Score: " + totalScore);

            // Ask user if they want another round
            System.out.print("\nDo you want to play another round? (y/n): ");
            char choice = scanner.next().toLowerCase().charAt(0);
            playAgain = (choice == 'y');
        }

        System.out.println("\n Game Over! Final Score: " + totalScore);
        scanner.close();
    }
}
