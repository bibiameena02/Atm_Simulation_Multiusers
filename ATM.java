import java.io.*;
import java.util.*;

public class ATM {
    static String fileName = "accounts.txt";
    static Map<String, String> passwords = new HashMap<>();
    static Map<String, Double> balances = new HashMap<>();

    public static void main(String[] args) throws IOException {
        loadAccounts();
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (!passwords.containsKey(username) || !passwords.get(username).equals(password)) {
            System.out.println("Invalid username or password!");
            sc.close();
            return;
        }

        System.out.println("Login successful! Welcome " + username);
        boolean running = true;

        while (running) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.next(); // consume invalid input
                continue;
            }
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Current Balance: " + balances.get(username));
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    if (!sc.hasNextDouble()) {
                        System.out.println("Invalid amount.");
                        sc.next();
                        break;
                    }
                    double dep = sc.nextDouble();
                    if (dep <= 0) {
                        System.out.println("Amount must be positive.");
                        break;
                    }
                    balances.put(username, balances.get(username) + dep);
                    saveAccounts();
                    System.out.println("Deposit successful.");
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    if (!sc.hasNextDouble()) {
                        System.out.println("Invalid amount.");
                        sc.next();
                        break;
                    }
                    double with = sc.nextDouble();
                    if (with <= 0) {
                        System.out.println("Amount must be positive.");
                        break;
                    }
                    if (with <= balances.get(username)) {
                        balances.put(username, balances.get(username) - with);
                        saveAccounts();
                        System.out.println("Withdrawal successful.");
                    } else {
                        System.out.println("Insufficient funds.");
                    }
                    break;
                case 4:
                    running = false;
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }

    static void loadAccounts() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("accounts.txt file not found.");
            System.exit(1);
        }
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (line.isEmpty()) continue;  // skip empty lines

            String[] parts = line.split("\\s+"); // split on any whitespace
            if(parts.length < 3) {
                System.out.println("Skipping invalid line in accounts.txt: " + line);
                continue;
            }
            try {
                passwords.put(parts[0], parts[1]);
                balances.put(parts[0], Double.parseDouble(parts[2]));
            } catch (NumberFormatException e) {
                System.out.println("Invalid balance number in line: " + line);
            }
        }
        fileScanner.close();
    }

    static void saveAccounts() throws IOException {
        FileWriter fw = new FileWriter(fileName);
        for (String user : passwords.keySet()) {
            fw.write(user + " " + passwords.get(user) + " " + balances.get(user) + "\n");
        }
        fw.close();
    }
}
