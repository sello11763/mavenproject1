/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package assignment.mavenproject1;

import java.util.Random;
import java.util.Scanner;

class Registration {

    // Variables to store the user's details
    String firstName;
    String lastName;
    String username;
    String password;
    String cellPhoneNumber;

    // Constructor - stores the user's details when a Registration object is created
    public Registration(String firstName, String lastName, String username,
                        String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    // Checks that the username contains an underscore AND is 5 characters or less
    public boolean checkUserName() {
        boolean hasUnderscore = username.contains("_");
        boolean shortEnough = username.length() <= 5;

        if (hasUnderscore == true && shortEnough == true) {
            return true;
        } else {
            return false;
        }
    }

    // Checks the password meets all rules using regex
    // (?=.*[A-Z])        - must have at least one capital letter
    // (?=.*[0-9])        - must have at least one number
    // (?=.*[^a-zA-Z0-9]) - must have at least one special character
    // .{8,}              - must be at least 8 characters long
    public boolean checkPasswordComplexity() {
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";

        if (password.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

    // Checks the cell number starts with +27 followed by exactly 9 digits
    // Regex reference: standard SA international phone number pattern ^\+27[0-9]{9}$
    public boolean checkCellPhoneNumber() {
        String regex = "^\\+27[0-9]{9}$";

        if (cellPhoneNumber.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

    // Registers the user - checks all three fields and returns the correct message
    public String registerUser() {
        if (checkUserName() == false) {
            return "Username is not correctly formatted; please ensure that your username "
                 + "contains an underscore and is no more than five characters in length.";
        }

        if (checkPasswordComplexity() == false) {
            return "Password is not correctly formatted; please ensure that the password "
                 + "contains at least eight characters, a capital letter, a number, "
                 + "and a special character.";
        }

        if (checkCellPhoneNumber() == false) {
            return "Cell phone number is incorrectly formatted or does not contain "
                 + "international code; please correct the number and try again.";
        }

        return "Registration successful! Welcome, " + firstName + " " + lastName + ".";
    }
}
class Login {

    // We store the registered user so we can compare credentials at login
    Registration registeredUser;
    String enteredUsername;
    String enteredPassword;

    // Constructor - takes the registered user and the login attempt details
    public Login(Registration registeredUser, String enteredUsername, String enteredPassword) {
        this.registeredUser = registeredUser;
        this.enteredUsername = enteredUsername;
        this.enteredPassword = enteredPassword;
    }

    // Checks if the entered username and password match what was registered
    public boolean loginUser() {
        boolean usernameMatch = enteredUsername.equals(registeredUser.username);
        boolean passwordMatch = enteredPassword.equals(registeredUser.password);

        if (usernameMatch == true && passwordMatch == true) {
            return true;
        } else {
            return false;
        }
    }

    // Returns a welcome message on success or an error message on failure
    public String returnLoginStatus() {
        if (loginUser() == true) {
            return "Welcome " + registeredUser.firstName + ", "
                 + registeredUser.lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}


class Message {

    // Variables to store this message's details
    String messageID;
    String recipient;
    String messageText;
    String messageHash;
    int messageNumber;

    // Static variables are shared across ALL Message objects
    // This means every message uses the same counter and the same array
    static int totalMessagesSent = 0;
    static String[] sentMessages = new String[100];

    // Constructor - called when we create a new message
    // It automatically generates the ID and hash
    public Message(String recipient, String messageText) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageNumber = totalMessagesSent + 1;
        this.messageHash = createMessageHash();
    }

    // Builds a random 10-digit ID by adding one random digit at a time in a loop
    private String generateMessageID() {
        Random random = new Random();
        String id = "";

        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10);
            id = id + digit;
        }

        return id;
    }

    // Checks that the message ID is not longer than 10 characters
    public boolean checkMessageID() {
        if (messageID.length() <= 10) {
            return true;
        } else {
            return false;
        }
    }

    // Checks the recipient number is correctly formatted with a +27 international code
    // Regex reference: standard SA international phone number pattern ^\+27[0-9]{9}$
    public String checkRecipientCell() {
        String regex = "^\\+27[0-9]{9}$";

        if (recipient.matches(regex)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain "
                 + "an international code. Please correct the number and try again.";
        }
    }

    // Builds the message hash from:
    // - First two digits of the message ID
    // - The message number
    // - The first and last words of the message
    // Example: 00:1:HITONIGHT
    public String createMessageHash() {
        // Get the first two characters of the message ID
        String firstTwoDigits = messageID.substring(0, 2);

        // Split the message text into an array of words
        String[] words = messageText.split(" ");

        // Get the first and last word from the array
        String firstWord = words[0];
        String lastWord  = words[words.length - 1];

        // Join everything together and convert to uppercase
        String hash = firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // Checks the message is 250 characters or less
    // Returns how many characters over the limit if it fails
    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int over = messageText.length() - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }
    }

    // Lets the user choose to send, disregard, or store the message
    public String SentMessage(int choice) {
        if (choice == 1) {
            // Store the message in the array at the current index, then increment
            sentMessages[totalMessagesSent] = printMessages();
            totalMessagesSent = totalMessagesSent + 1;
            return "Message successfully sent.";

        } else if (choice == 2) {
            return "Press 0 to delete the message.";

        } else if (choice == 3) {
            storeMessage();
            return "Message successfully stored.";

        } else {
            return "Invalid option selected.";
        }
    }

    // Returns all the details of this single message as one string
    public String printMessages() {
        return "Message ID: "   + messageID   + "\n"
             + "Message Hash: " + messageHash + "\n"
             + "Recipient: "    + recipient   + "\n"
             + "Message: "      + messageText;
    }

    // Returns the total number of messages sent so far
    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    // Loops through the sentMessages array and prints each one
    // We only loop up to totalMessagesSent to skip the empty slots
    public static void printAllSentMessages() {
        if (totalMessagesSent == 0) {
            System.out.println("No messages have been sent yet.");
        } else {
            System.out.println("\n--- All Sent Messages ---");

            for (int i = 0; i < totalMessagesSent; i++) {
                System.out.println("\nMessage " + (i + 1) + ":");
                System.out.println(sentMessages[i]);
                System.out.println("-------------------------");
            }
        }
    }

    // Saves the message to a JSON file so it can be sent later
    // Research: FileWriter is used to write text to a file
    // true passed to FileWriter means we append instead of overwriting
    public void storeMessage() {
        try {
            String json = "{"
                + "\"messageID\": \""   + messageID   + "\", "
                + "\"messageHash\": \"" + messageHash + "\", "
                + "\"recipient\": \""   + recipient   + "\", "
                + "\"message\": \""     + messageText + "\""
                + "}";

            java.io.FileWriter writer = new java.io.FileWriter("stored_messages.json", true);
            writer.write(json + "\n");
            writer.close();

        } catch (Exception e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
}
public class Mavenproject1 {

    public static void main(String[] args) {
      


    

        Scanner scanner = new Scanner(System.in);

        // ------------------------------------------------
        // STEP 1: REGISTRATION
        // ------------------------------------------------
        System.out.println("============================================");
        System.out.println("       Welcome to ChatApp Registration      ");
        System.out.println("============================================");

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter a username (max 5 characters, must contain '_'): ");
        String username = scanner.nextLine();

        System.out.print("Enter a password (min 8 chars, 1 capital, 1 number, 1 special character): ");
        String password = scanner.nextLine();

        System.out.print("Enter your SA cell number (example: +27838968976): ");
        String cellNumber = scanner.nextLine();

        // Create a Registration object and try to register
        Registration reg = new Registration(firstName, lastName, username, password, cellNumber);
        String registrationResult = reg.registerUser();

        System.out.println("\n--- Registration Result ---");
        System.out.println(registrationResult);

        // Stop the program if registration failed
        if (registrationResult.startsWith("Registration successful") == false) {
            System.out.println("Please restart and try again.");
            scanner.close();
            return;
        }

        // ------------------------------------------------
        // STEP 2: LOGIN
        // ------------------------------------------------
        System.out.println("\n============================================");
        System.out.println("                  Login                     ");
        System.out.println("============================================");

        System.out.print("Enter your username to log in: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter your password to log in: ");
        String loginPassword = scanner.nextLine();

        // Create a Login object and check the credentials
        Login login = new Login(reg, loginUsername, loginPassword);

        System.out.println("\n--- Login Result ---");
        System.out.println(login.returnLoginStatus());

        // Stop the program if login failed
        if (login.loginUser() == false) {
            System.out.println("Please restart and try again.");
            scanner.close();
            return;
        }

        // ------------------------------------------------
        // STEP 3: MESSAGING
        // Only reached if registration and login both passed
        // ------------------------------------------------
        System.out.println("\nWelcome to QuickChat.");

        // Ask how many messages the user wants to send
        System.out.print("\nHow many messages would you like to send? ");
        int maxMessages = Integer.parseInt(scanner.nextLine());

        // Keep showing the menu until the user chooses Quit
        boolean running = true;

        while (running == true) {

            System.out.println("\n============================================");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            int menuChoice = Integer.parseInt(scanner.nextLine());

            if (menuChoice == 1) {

                // Check if the user has used up all their messages
                if (Message.totalMessagesSent >= maxMessages) {
                    System.out.println("You have reached your message limit of " + maxMessages + ".");

                } else {

                    // Collect the message details from the user
                    System.out.print("Enter recipient cell number (e.g. +27718693002): ");
                    String recipientNumber = scanner.nextLine();

                    System.out.print("Enter your message: ");
                    String messageText = scanner.nextLine();

                    // Create a new Message object
                    Message msg = new Message(recipientNumber, messageText);

                    // Check the recipient number
                    String cellCheck = msg.checkRecipientCell();
                    System.out.println(cellCheck);

                    // Check the message length
                    String lengthCheck = msg.checkMessageLength();
                    System.out.println(lengthCheck);

                    // Only continue if both checks passed
                    if (cellCheck.equals("Cell phone number successfully captured.")
                            && lengthCheck.equals("Message ready to send.")) {

                        // Show the full message details
                        System.out.println("\n--- Message Details ---");
                        System.out.println("Message ID: "   + msg.messageID);
                        System.out.println("Message Hash: " + msg.messageHash);
                        System.out.println("Recipient: "    + msg.recipient);
                        System.out.println("Message: "      + msg.messageText);

                        // Ask what to do with the message
                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Choose an option: ");
                        int sendChoice = Integer.parseInt(scanner.nextLine());

                        // Call SentMessage and print the result
                        System.out.println(msg.SentMessage(sendChoice));
                    }
                }

            } else if (menuChoice == 2) {
                // Show all sent messages by calling the static method
                Message.printAllSentMessages();

            } else if (menuChoice == 3) {
                // Quit the application and show the total messages sent
                running = false;
                System.out.println("\nTotal messages sent: " + Message.totalMessagesSent);
                System.out.println("Goodbye!");

            } else {
                System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }

        scanner.close();
    }
        
    }  
