import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumberGuessingGame {

    private static final int MAX_HINTS = 2;
    private JFrame frame;
    private int upperLimit;
    private int randomNumber;
    private int attempts;
    private int hintRequests;
    private JLabel attemptsLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberGuessingGame game = new NumberGuessingGame();
            game.showDifficultyDialog();
        });
    }

    private void showDifficultyDialog() {
        String[] options = {"Easy (1-50)", "Medium (1-100)", "Hard (1-200)"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose a difficulty level:",
                "Difficulty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );

        switch (choice) {
            case 0:
                upperLimit = 50;
                break;
            case 1:
                upperLimit = 100;
                break;
            case 2:
                upperLimit = 200;
                break;
            default:
                upperLimit = 100; 
        }

        startGame();
    }

    private void startGame() {
        frame = new JFrame("Number Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setBackground(new Color(255, 255, 255));

        attempts = 10;
        hintRequests = 0;
        randomNumber = (int) (Math.random() * upperLimit) + 1;

        JLabel welcomeLabel = new JLabel("Welcome to the Number Guessing Game!");
        attemptsLabel = new JLabel("Attempts left: " + attempts);
        showGuessDialog();

        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        frame.add(welcomeLabel);
        frame.add(attemptsLabel);

        frame.pack();
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    private void showGuessDialog() {
        JPanel panel = new JPanel(new GridLayout(5, 3));
        JLabel label = new JLabel("Enter your guess:");
        JTextField inputField = new JTextField();
        JButton submitButton = new JButton("Submit");
        inputField.setColumns(20);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processGuess(inputField.getText());
            }
        });

        panel.setBackground(new Color(255, 255, 255));
        panel.add(label);
        panel.add(inputField);
        panel.add(submitButton);

        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.add(attemptsLabel);
        frame.revalidate();
        frame.repaint();
    }

    private void processGuess(String guess) {
        int userGuess;
        try {
            userGuess = Integer.parseInt(guess);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        attempts--;

        if (userGuess == randomNumber) {
            int score = calculateScore(attempts);
            showResultDialog("Congratulations! You guessed the number in " + (10 - attempts) + " attempts. Your score: " + score);
        } else if (userGuess < randomNumber) {
            if (randomNumber - userGuess == 1) {
                JOptionPane.showMessageDialog(frame, "Almost there! Try again.", "Close Guess", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Too low! Try again.", "Incorrect Guess", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            if (userGuess - randomNumber == 1) {
                JOptionPane.showMessageDialog(frame, "Almost there! Try again.", "Close Guess", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Too high! Try again.", "Incorrect Guess", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (attempts == 2 && hintRequests < MAX_HINTS) {
            showHintDialog();
        } else if (attempts > 0) {
            showGuessDialog();
        } else {
            int score = calculateScore(attempts);
            showResultDialog("Sorry! You've run out of attempts. The correct number was " + randomNumber + ". Your score: " + score);
        }
        attemptsLabel.setText("Attempts left: " + attempts);
    }

    private void showHintDialog() {
        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Do you want a hint?",
                "Hint",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            provideLogicalHint(randomNumber);
            hintRequests++;
        }

        showGuessDialog();
    }

    private void provideLogicalHint(int number) {
        StringBuilder hintMessage = new StringBuilder("Logical Hint:\n");
        if (isPrime(number)) {
            hintMessage.append("The number is a prime number.");
        } else {
            hintMessage.append("The number is not a prime number.\n");
            if (number % 2 == 0) {
                hintMessage.append("The number is even.\n");
            } else {
                hintMessage.append("The number is odd.\n");
            }
            if (number % 10 == 0) {
                hintMessage.append("The number is divisible by 10.");
            }
        }

        JOptionPane.showMessageDialog(frame, hintMessage.toString(), "Hint", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showResultDialog(String message) {
        int score = calculateScore(attempts);
        String resultMessage = message + "\nYour score: " + score;

        int choice = JOptionPane.showOptionDialog(
                frame,
                resultMessage,
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Play Again", "Exit"},
                "Play Again"
        );

        if (choice == 0) {
            frame.dispose();
            showDifficultyDialog();
        } else {
            System.exit(0);
        }
    }

    private int calculateScore(int attempts) {
        int baseScore = 100;
        int deduction = Math.max(10 - attempts, 0) * 10;
        return Math.max(baseScore - deduction, 0);
    }

    private boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}