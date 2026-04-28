// Online Java Compiler
// Use this editor to write, compile and run your Java code online

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

// ----------- Account Class -----------
class Account implements Serializable {
    private String accountNumber;
    private String holderName;
    private double balance;

    public Account(String accountNumber, String holderName) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = 0.0;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

// ----------- Transaction Class -----------
class Transaction implements Serializable {
    private String type;
    private double amount;
    private LocalDateTime timestamp;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String toString() {
        return type + " | Amount: " + amount + " | Time: " + timestamp;
    }
}

// ----------- File Manager -----------
class FileManager {
    private static final String FILE_NAME = "accounts.dat";

    public static void save(Map<String, Account> accounts) {
        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static Map<String, Account> load() {
        try (ObjectInputStream ois =
                 new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Map<String, Account>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}

// ----------- Bank Class -----------
class Bank {
    private Map<String, Account> accounts;

    public Bank() {
        accounts = FileManager.load();
    }

    public void createAccount(String accNo, String name) {
        if (!accounts.containsKey(accNo)) {
            accounts.put(accNo, new Account(accNo, name));
            System.out.println("Account created!");
        } else {
            System.out.println("Account already exists!");
        }
    }

    public Account getAccount(String accNo) {
        return accounts.get(accNo);
    }

    public void deposit(String accNo, double amount) {
        Account acc = getAccount(accNo);
        if (acc != null) {
            acc.deposit(amount);
            System.out.println("Deposit successful!");
        } else {
            System.out.println("Account not found!");
        }
    }

    public void withdraw(String accNo, double amount) {
        Account acc = getAccount(accNo);
        if (acc != null && acc.withdraw(amount)) {
            System.out.println("Withdrawal successful!");
        } else {
            System.out.println("Failed! Check balance/account.");
        }
    }

    public void transfer(String from, String to, double amount) {
        Account sender = getAccount(from);
        Account receiver = getAccount(to);

        if (sender != null && receiver != null && sender.withdraw(amount)) {
            receiver.deposit(amount);
            System.out.println("Transfer successful!");
        } else {
            System.out.println("Transfer failed!");
        }
    }

    public void displayAccounts() {
        for (Account acc : accounts.values()) {
            System.out.println(acc.getAccountNumber() + " | " +
                               acc.getHolderName() + " | Balance: " +
                               acc.getBalance());
        }
    }

    public void saveData() {
        FileManager.save(accounts);
    }
}

// ----------- Main Class -----------
public class BankY {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("\n--- BankY Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. View Accounts");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Account Number: ");
                    String accNo = sc.next();
                    System.out.print("Name: ");
                    String name = sc.next();
                    bank.createAccount(accNo, name);
                    break;

                case 2:
                    System.out.print("Account Number: ");
                    accNo = sc.next();
                    System.out.print("Amount: ");
                    double dep = sc.nextDouble();
                    bank.deposit(accNo, dep);
                    break;

                case 3:
                    System.out.print("Account Number: ");
                    accNo = sc.next();
                    System.out.print("Amount: ");
                    double wd = sc.nextDouble();
                    bank.withdraw(accNo, wd);
                    break;

                case 4:
                    System.out.print("From Account: ");
                    String from = sc.next();
                    System.out.print("To Account: ");
                    String to = sc.next();
                    System.out.print("Amount: ");
                    double amt = sc.nextDouble();
                    bank.transfer(from, to, amt);
                    break;

                case 5:
                    bank.displayAccounts();
                    break;

                case 6:
                    bank.saveData();
                    System.out.println("Data saved. Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}