import java.util.Scanner;

public class BankingApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankingSystem bankingSystem = new BankingSystem();
    
        int choice;
        do {
            try {
                System.out.println("1. Create a new account");
                System.out.println("2. Display all accounts");
                System.out.println("3. Update an account");
                System.out.println("4. Delete an account");
                System.out.println("5. Deposit an amount into your account");
                System.out.println("6. Withdraw an amount from your account");
                System.out.println("7. Search for an account");
                System.out.println("8. Exit");
                System.out.println("Enter your choice:");
    
                choice = scanner.nextInt();
    
                switch (choice) {
                    case 1:
                        bankingSystem.createAccount();
                        break;
                    case 2:
                        bankingSystem.displayAllAccounts();
                        break;
                    case 3:
                        bankingSystem.updateAccount();
                        break;
                    case 4:
                        bankingSystem.deleteAccount();
                        break;
                    case 5:
                        bankingSystem.depositAmount();
                        break;
                    case 6:
                        bankingSystem.withdrawAmount();
                        break;
                    case 7:
                        bankingSystem.searchAccount();
                        break;
                    case 8:
                        System.out.println("Exiting the application. Goodbye!");
                        break;
                    default:
                        throw new Exception("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Invalid choice. Please enter a valid option.");
                // Consume the newline character left in the buffer
                scanner.nextLine();
                choice = 0; // Reset choice to continue the loop
            }
    
        } while (choice != 8);
    
        scanner.close();
    }
    
}

