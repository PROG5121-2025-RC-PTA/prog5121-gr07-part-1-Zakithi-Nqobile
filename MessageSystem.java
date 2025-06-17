import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MessageSystem {

    // Message class inside the main class
    static class Message {
        String recipient;
        String message;
        String flag;
        String id;
        String hash;

        public Message(String recipient, String message, String flag) {
            this.recipient = recipient;
            this.message = message;
            this.flag = flag;
            this.id = UUID.randomUUID().toString(); // Simple unique ID
            this.hash = generateHash();
        }

        private String generateHash() {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = digest.digest((recipient + message).getBytes());
                StringBuilder hexString = new StringBuilder();
                for (byte b : hashBytes) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        }

        @Override
        public String toString() {
            return "Recipient: " + recipient + "\nMessage: " + message + "\nID: " + id + "\nHash: " + hash + "\n";
        }
    }

    // Arrays
    static ArrayList<Message> sentMessages = new ArrayList<>();
    static ArrayList<Message> storedMessages = new ArrayList<>();
    static ArrayList<Message> disregardedMessages = new ArrayList<>();
    static ArrayList<String> messageIds = new ArrayList<>();
    static ArrayList<String> messageHashes = new ArrayList<>();

    public static void populateMessages() {
        addMessage(new Message("+27834557896", "Did you get the cake?", "sent"));
        addMessage(new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", "stored"));
        addMessage(new Message("+27834484567", "Yohoooo, I am at your gate.", "disregard"));
        addMessage(new Message("0838884567", "It is dinner time!", "sent"));
        addMessage(new Message("+27838884567", "Ok, I am leaving without you.", "stored"));
    }

    public static void addMessage(Message msg) {
        switch (msg.flag) {
            case "sent" -> sentMessages.add(msg);
            case "stored" -> storedMessages.add(msg);
            case "disregard" -> disregardedMessages.add(msg);
            default -> {
            }
        }
        messageIds.add(msg.id);
        messageHashes.add(msg.hash);
    }

    public static void displaySentRecipients() {
        System.out.println("\n--- Sent Messages ---");
        for (Message msg : sentMessages) {
            System.out.println("Recipient: " + msg.recipient + ", Message: " + msg.message);
        }
    }

    public static void displayLongestSentMessage() {
        Message longest = null;
        for (Message msg : sentMessages) {
            if (longest == null || msg.message.length() > longest.message.length()) {
                longest = msg;
            }
        }
        if (longest != null) {
            System.out.println("\nLongest Sent Message: " + longest.message);
        }
    }

    public static void searchById(String id) {
        for (Message msg : allMessages()) {
            if (msg.id.equals(id)) {
                System.out.println("\nMessage found by ID:\n" + msg);
                return;
            }
        }
        System.out.println("No message found with that ID.");
    }

    public static void searchByRecipient(String recipient) {
        System.out.println("\nMessages for: " + recipient);
        for (Message msg : allMessages()) {
            if (msg.recipient.equals(recipient)) {
                System.out.println("- " + msg.message);
            }
        }
    }

    public static void deleteByHash(String hash) {
        Iterator<Message> it = storedMessages.iterator();
        while (it.hasNext()) {
            Message msg = it.next();
            if (msg.hash.equals(hash)) {
                System.out.println("\nDeleted Message: " + msg.message);
                it.remove();
                return;
            }
        }
        System.out.println("No message found with that hash.");
    }

    public static void displayReport() {
        System.out.println("\n--- Sent Messages Report ---");
        for (Message msg : sentMessages) {
            System.out.println("Recipient: " + msg.recipient);
            System.out.println("Message: " + msg.message);
            System.out.println("Hash: " + msg.hash);
            System.out.println("------------------------");
        }
    }

    public static ArrayList<Message> allMessages() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);
        return all;
    }

    public static void main(String[] args) {
        populateMessages();

        displaySentRecipients();
        displayLongestSentMessage();

        // Search for a known ID (we'll use the first sent one for testing)
        if (!sentMessages.isEmpty()) {
            searchById(sentMessages.get(0).id);
        }

        searchByRecipient("+27838884567");

        // Delete by known hash (first stored message)
        if (!storedMessages.isEmpty()) {
            deleteByHash(storedMessages.get(0).hash);
        }

        displayReport();
    }
}
