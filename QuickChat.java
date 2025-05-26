import java.io.*;
import java.util.*;
import javax.swing.*;

public class QuickChat {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Message> sentMessages = new ArrayList<>();

        System.out.print("Please log in (type 'login'): ");
        while (!sc.nextLine().equalsIgnoreCase("login")) {
            System.out.print("Invalid. Try again: ");
        }

        System.out.println("Welcome to QuickChat");

        System.out.print("Enter number of messages you want to send: ");
        int numMessages = Integer.parseInt(sc.nextLine());

        int messageCount = 0;

        while (true) {
            System.out.println("\nMenu:\n1. Send Message\n2. Show Recently Sent Messages\n3. Quit");
            String option = sc.nextLine().trim();

            switch (option) {
                case "1" -> {
                    if (messageCount >= numMessages) {
                        System.out.println("Message limit reached.");
                        break;
                    }

                    System.out.print("Enter recipient number (format: +1234567890): ");
                    String recipient = sc.nextLine().trim();

                    System.out.print("Enter your message: ");
                    String msgText = sc.nextLine().trim();

                    if (msgText.length() > 250) {
                        System.out.printf("Message exceeds 250 characters by %d. Please reduce the size.%n", msgText.length() - 250);
                        break;
                    }

                    Message message = new Message(messageCount, recipient, msgText);

                    if (!message.checkRecipientCell()) {
                        System.out.println("Cell phone number is incorrectly formatted. Must begin with '+' and contain 10–15 digits.");
                        break;
                    }

                    if (msgText.length() > 50) {
                        System.out.println("Warning: Message is over 50 characters.");
                    } else {
                        System.out.println("Message is under 50 characters — ready to send.");
                    }

                    System.out.print("Choose an action (send / store / discard): ");
                    String action = sc.nextLine().trim().toLowerCase();

                    switch (action) {
                        case "send" -> {
                            sentMessages.add(message);
                            messageCount++;
                            JOptionPane.showMessageDialog(null,
                                    "MessageID: " + message.getMessageID() +
                                            "\nMessage Hash: " + message.getMessageHash() +
                                            "\nRecipient: " + message.getRecipient() +
                                            "\nMessage: " + message.getMessage());
                            System.out.println("Message successfully sent.");
                        }

                        case "store" -> {
                            message.storeMessageToFile();
                            System.out.println("Message successfully stored.");
                        }

                        case "discard" -> {
                            System.out.println("Message discarded.");
                        }

                        default -> System.out.println("Invalid action.");
                    }
                }

                case "2" -> {
                    if (sentMessages.isEmpty()) {
                        System.out.println("No messages sent yet.");
                    } else {
                        System.out.println("Recently sent messages:");
                        for (Message m : sentMessages) {
                            System.out.printf("ID: %s, Recipient: %s, Text: %s%n",
                                    m.getMessageID(), m.getRecipient(), m.getMessage());
                        }
                    }
                }

                case "3" -> {
                    System.out.printf("Total messages sent: %d%n", sentMessages.size());
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }
}

class Message {
    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String message;
    private final String messageHash;

    public Message(int messageNumber, String recipient, String message) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = generateMessageHash();
    }

    private String generateMessageID() {
        StringBuilder id = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            id.append(r.nextInt(10));
        }
        return id.toString();
    }

    public boolean checkRecipientCell() {
        return recipient != null && recipient.matches("^\\+\\d{10,15}$");
    }

    public void storeMessageToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter("Chatgpt.json", true))) {
            out.println("{");
            out.println("  \"messageID\": \"" + messageID + "\",");
            out.println("  \"messageHash\": \"" + messageHash + "\",");
            out.println("  \"recipient\": \"" + recipient + "\",");
            out.println("  \"message\": \"" + message + "\"");
            out.println("}");
        } catch (IOException e) {
            System.out.println("Failed to store message: " + e.getMessage());
        }
    }

    private String generateMessageHash() {
        String[] words = message.trim().split("\\s+");
        String first = words.length > 0 ? words[0] : "";
        String last = words.length > 1 ? words[words.length - 1] : first;
        return messageID.substring(0, 2) + ":" + messageNumber + ":" + first.toUpperCase() + last.toUpperCase();
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }
}
