import java.util.Scanner;

public class ChatAppRegistration {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Chat App User Registration ===");

        // Name
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        // Surname
        System.out.print("Enter your surname: ");
        String surname = scanner.nextLine();

        // Email
        System.out.print("Enter your email address: ");
        String email = scanner.nextLine();

        // Username
        String username;
        while (true) {
            System.out.print("Enter a username (must contain '_'): ");
            username = scanner.nextLine();
            if (username.contains("_")) {
                break;
            } else {
                System.out.println("Invalid username. It must contain an underscore (_).");
            }
        }

        // Password
        String password;
        while (true) {
            System.out.print("Enter a password (exactly 7 characters): ");
            password = scanner.nextLine();
            if (password.length() == 7) {
                break;
            } else {
                System.out.println("Invalid password. It must be exactly 7 characters long.");
            }
        }

        // Phone number
        String phoneNumber;
        while (true) {
            System.out.print("Enter your phone number (must start with +27): ");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.startsWith("+27")) {
                break;
            } else {
                System.out.println("Invalid phone number. It must start with +27.");
            }
        }

        // Registration success
        System.out.println("\n✅ Registration successful!");
        System.out.println("Welcome, " + name + " " + surname + "!");
        System.out.println("Your username: " + username);
        System.out.println("Your email: " + email);
        System.out.println("Your phone number: " + phoneNumber);
    }
}
