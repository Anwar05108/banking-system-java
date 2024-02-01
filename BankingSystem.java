import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.util.Random;
import java.util.Scanner;

public class BankingSystem {

    private static final String FILE_NAME = "accounts.txt";
    private static final String DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final Double MINIMUM_OPENING_BALANCE = 1000.00;
    private static final Double MINIMUM_BALANCE = 500.00;
    private static final String[] HEADER = { "Account Type", "Account Holder Name", "Account Holder's Id",
            "Account Number", "Account Opening Date", "Account Balance" };

    public void createAccount() throws Exception {

        try {
            Scanner scanner = new Scanner(System.in);

            String accountType = getAccountType(scanner);

            System.out.println("Enter account holder name:");
            String accountHolderName = scanner.nextLine();
            System.out.println("Enter account holder's id:");
            String accountHolderId = scanner.nextLine();

            Date accountOpeningDate = new Date(System.currentTimeMillis());
            System.out.println("Enter account balance:");
            Double accountBalance = scanner.nextDouble();

            if (accountBalance < MINIMUM_OPENING_BALANCE) {
                throw new Exception("Error: Minimum opening balance is " + MINIMUM_OPENING_BALANCE);
            }

            String accountNumber = accountType.substring(0, 2) + (int) ((Math.random()) * 1000000);

            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(accountType + DELIMITER + accountHolderName + DELIMITER + accountHolderId + DELIMITER
                    + accountNumber + DELIMITER + accountOpeningDate + DELIMITER + accountBalance + NEW_LINE_SEPARATOR);
            bufferedWriter.close();
            fileWriter.close();

            System.out.println("Account created successfully! Account number is " + accountNumber + "name is "
                    + accountHolderName + " and balance is " + accountBalance + ".");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private String getAccountType(Scanner scanner) throws Exception {

        System.out.println("Enter account type:");

        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.println("3. Salary Account");
        System.out.println("4. Fixed Deposit Account");
        System.out.println("Enter your choice:");
        String accountType = "";

        String accountTypeInput = scanner.nextLine();

        switch (accountTypeInput) {
            case "1":
                accountType = "Savings Account";
                break;
            case "2":
                accountType = "Current Account";
                break;
            case "3":
                accountType = "Salary Account";
                break;
            case "4":
                accountType = "Fixed Deposit Account";
                break;

            default:
                throw new Exception("Error: Invalid account type");
        }
        return accountType;
    }

    public void displayAllAccounts() {
        try {
            FileReader fileReader = new FileReader(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            System.out.printf("%-25s %-25s %-25s %-25s %-25s %-25s\n", "Type", "Name", "ID", "Number",
                    "Account Opening Date", "Balance");
            System.out.println("-".repeat(150));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] accountDetails = line.split(DELIMITER);

                String type = accountDetails[0];
                String name = accountDetails[1];
                String id = accountDetails[2];
                String number = accountDetails[3];
                String openingDate = accountDetails[4];
                String balance = accountDetails[5];

                System.out.printf("%-25s %-25s %-25s %-25s %-25s %-25s\n", type, name, id, number, openingDate,
                        balance);
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading accounts file: " + e.getMessage());
        }

    }

    public void updateAccount() {
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter account number:");
            String accountNumber = scanner.nextLine();
            System.out.println("Enter id:");
            String id = scanner.nextLine();
            String accountDetails[] = findAccount(accountNumber, id);
            if (accountDetails == null) {
                throw new Exception("Account not found");
            }
            // which field to update?
            System.out.println("Which field do you want to update?");
            System.out.println("1. Account holder name");
            System.out.println("2. Account holder's id");
            System.out.println("Enter your choice:");
            String choice = scanner.nextLine();

            String updatedLine = "";
            switch (choice) {
                case "1":
                    System.out.println("Enter new account holder name:");
                    String accountHolderName = scanner.nextLine();
                    accountDetails[1] = accountHolderName;
                    updatedLine = String.join(DELIMITER, accountDetails);
                    break;
                case "2":
                    System.out.println("Enter new account holder's id:");
                    String accountHolderId = scanner.nextLine();
                    accountDetails[2] = accountHolderId;
                    updatedLine = String.join(DELIMITER, accountDetails);
                    break;
                
                default:
                    throw new Exception("Error: Invalid choice");
            }

            updateFile(accountNumber, updatedLine);


        }
        catch(Exception e){
            System.out.println("Error updating account: " + e.getMessage());
        }
    }

    private void updateFile(String accountNumber, String updatedLine) throws Exception{
        File file = new File(FILE_NAME);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

        String line;
        long currentPosition = 0;
        while ((line = randomAccessFile.readLine()) != null) {
            String accountDetails[] = line.split(DELIMITER);
            String number = accountDetails[3];
            if (number.equals(accountNumber)) {
                randomAccessFile.seek(currentPosition);

                randomAccessFile.writeBytes(updatedLine);
                break;
            }

            currentPosition = randomAccessFile.getFilePointer();
        }

        randomAccessFile.close();

    }

    public void deleteAccount() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter account number:");
            String accountNumber = scanner.nextLine();

            FileReader fileReader = new FileReader(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = "";
            String updatedFileContent = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] accountDetails = line.split(DELIMITER);

                String number = accountDetails[3];

                if (number.equals(accountNumber)) {
                    continue;
                }

                updatedFileContent += line + NEW_LINE_SEPARATOR;
            }

            bufferedReader.close();
            fileReader.close();

            FileWriter fileWriter = new FileWriter(FILE_NAME);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(updatedFileContent);
            bufferedWriter.close();
            fileWriter.close();

            System.out.println("Account deleted successfully!");

        } catch (Exception e) {
            System.out.println("Error deleting account: " + e.getMessage());
        }
    }

    public void depositAmount() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter account number:");
            String accountNumber = scanner.nextLine();

            FileReader fileReader = new FileReader(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = "";
            String updatedFileContent = "";
            boolean accountFound = false;
            Double newBalance = 0.0;

            while ((line = bufferedReader.readLine()) != null) {
                String[] accountDetails = line.split(DELIMITER);

                String type = accountDetails[0];
                String name = accountDetails[1];
                String id = accountDetails[2];
                String number = accountDetails[3];
                String openingDate = accountDetails[4];
                String balance = accountDetails[5];

                if (number.equals(accountNumber)) {
                    accountFound = true;
                    System.out.println("Enter amount to deposit:");
                    Double amount = scanner.nextDouble();
                    newBalance = Double.parseDouble(balance) + amount;

                    updatedFileContent += type + DELIMITER + name + DELIMITER + id + DELIMITER + number + DELIMITER
                            + openingDate + DELIMITER + newBalance + NEW_LINE_SEPARATOR;
                } else {
                    updatedFileContent += line + NEW_LINE_SEPARATOR;
                }
            }

            bufferedReader.close();
            fileReader.close();

            if (!accountFound) {
                throw new Exception(" Account not found");
            }

            FileWriter fileWriter = new FileWriter(FILE_NAME);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(updatedFileContent);
            bufferedWriter.close();
            fileWriter.close();

            System.out
                    .println("Amount deposited successfully! New balance is " + newBalance + "\n" + updatedFileContent);
        } catch (Exception e) {
            System.out.println("Error depositing amount: " + e.getMessage());
        }
    }

    public void withdrawAmount() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter account number:");
            String accountNumber = scanner.nextLine();
            System.out.println("Enter id:");
            String id = scanner.nextLine();
            String accountDetails[] = findAccount(accountNumber, id);
            if (accountDetails == null) {
                throw new Exception("Account not found");
            }

            System.out.println("Enter amount to withdraw:");
            Double amount = scanner.nextDouble();
            // check if after deducting amount, balance is less than minimum balance
            Double balance = Double.parseDouble(accountDetails[5]);
            if (balance - amount < MINIMUM_BALANCE) {
                throw new Exception("Error: Minimum balance should be " + MINIMUM_BALANCE);
            }

            Double newBalance = balance - amount;

            updateFile(accountNumber, newBalance);

        } catch (Exception e) {
            System.out.println("Error withdrawing amount: " + e.getMessage());
        }

    }

    private void updateFile(String userAccountNumber, Double newBalance) throws Exception {
        File file = new File(FILE_NAME);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

        String line;
        long currentPosition = 0;
        while ((line = randomAccessFile.readLine()) != null) {
            String accountDetails[] = line.split(DELIMITER);
            String number = accountDetails[3];
            if (number.equals(userAccountNumber)) {
                
                accountDetails[5] = String.valueOf(newBalance);
                

                randomAccessFile.seek(currentPosition);
                String updatedLine = String.join(DELIMITER, accountDetails);
                randomAccessFile.writeBytes(updatedLine);
                break;
            }

            currentPosition = randomAccessFile.getFilePointer();
        }

        randomAccessFile.close();
    }

    private String[] findAccount(String userAccountNumber, String userId) throws Exception {
        FileReader fileReader = new FileReader(FILE_NAME);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            String[] accountDetails = line.split(DELIMITER);
            String number = accountDetails[3];
            String id = accountDetails[2];
            if (number.equals(userAccountNumber) && id.equals(userId)) {
                return accountDetails;
            }
        }
        return null;
    }

    public void searchAccount() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter account number:");
            String accountNumber = scanner.nextLine();
            String accountDetails[] = findAccount(accountNumber);
            if (accountDetails == null) {
                throw new Exception("Account not found");
            }

            String type = accountDetails[0];
            String name = accountDetails[1];
            String id = accountDetails[2];
            String number = accountDetails[3];
            String openingDate = accountDetails[4];
            String balance = accountDetails[5];

            System.out.println("-".repeat(150));
            System.out.printf("%-25s %-25s %-25s %-25s %-25s %-25s\n", type, name, id, number, openingDate,
                    balance);
            System.out.println("-".repeat(150));

        } catch (Exception e) {
            System.out.println("Error searching account: " + e.getMessage());
        }
    }

    private String[] findAccount(String accountNumber) throws Exception {
        FileReader fileReader = new FileReader(FILE_NAME);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            String[] accountDetails = line.split(DELIMITER);
            String number = accountDetails[3];
            if (number.equals(accountNumber)) {
                return accountDetails;
            }
        }
        return null;
    }
}
