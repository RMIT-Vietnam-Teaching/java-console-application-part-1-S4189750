package claimshield;

import claimshield.model.*;
import claimshield.respiratory.SystemManager;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    private static final SystemManager sys = new SystemManager();
    private static final Scanner sc = new Scanner(System.in);
    private static User activeUser = null;

    public static void main(String[] args) {
        sys.loadAllData();
        printWelcomeBanner();

        while (true) {
            if (activeUser == null) {
                System.out.println("\n=======================================");
                System.out.println("   CLAIMSHIELD SECURE ACCESS PORTAL    ");
                System.out.println("=======================================");
                System.out.print("Enter Username: ");
                String user = sc.nextLine().trim();
                System.out.print("Enter Password: ");
                String pass = sc.nextLine().trim();

                User attempt = sys.getUserByUsername(user);

                if (attempt != null && attempt.getPassword().equals(pass)) {
                    if (attempt.getStatus().equalsIgnoreCase("ACTIVE")) {
                        activeUser = attempt;
                        sys.logAction(activeUser.getUserId(), "LOGIN_SUCCESS", activeUser.getUserId());
                        System.out.println("\n>>> Access Granted. Welcome, " + activeUser.getFullName() + " [" + activeUser.getRole() + "]");
                    } else {
                        System.out.println(">>> Access Denied: This account is marked INACTIVE.");
                    }
                } else {
                    System.out.println(">>> Access Denied: Invalid username or password tokens.");
                }
            } else {
                activeUser.displayDashboard();
                System.out.print("Select operational action: ");
                String action = sc.nextLine().trim();
                processDashboardAction(action);
            }
        }
    }

    private static void printWelcomeBanner() {
        System.out.println("=======================================");
        System.out.println("COSC3110/3111 HEALTH INSURANCE SYSTEM");
        System.out.println("         Student ID: s4189750          ");
        System.out.println("     Student Name: Nguyen Ho Viet Bach    ");
        System.out.println("=======================================");
    }

    private static void processDashboardAction(String choice) {
        if (activeUser instanceof Admin) {
            handleAdminWorkflow(choice);
        } else if (activeUser instanceof ClaimsOfficer) {
            handleOfficerWorkflow(choice);
        } else if (activeUser instanceof Customer) {
            handleCustomerWorkflow(choice);
        }
    }

    private static void handleAdminWorkflow(String choice) {
        switch (choice) {
            case "1":
                System.out.print("Enter target Account User ID to soft-delete (e.g., u-005): ");
                String target = sc.nextLine().trim();
                if (sys.getUserById(target) != null) {
                    sys.softDeleteUser(target, activeUser.getUserId());
                    System.out.println(">>> Command executed. Account status switched to INACTIVE.");
                } else {
                    System.out.println(">>> User record not found.");
                }
                break;
            case "2":
                System.out.println("\n--- RUNTIME AUDIT TRAIL LOG RECORD LEDGER ---");
                if (sys.getAuditLogs().isEmpty()) {
                    System.out.println("[Empty Ledger System]");
                } else {
                    for (String log : sys.getAuditLogs()) System.out.println(log);
                }
                break;
            case "3":
                System.out.println("\nProcessing analytical calculations over database nodes...");
                sys.runTimeframeReport(LocalDateTime.now().minusMonths(1), LocalDateTime.now().plusMonths(1));
                sys.runOfficerReport();
                sys.runTierSummaryReport();
                break;
            case "4":
                logout();
                break;
            default:
                System.out.println(">>> Invalid selection.");
        }
    }

    private static void handleOfficerWorkflow(String choice) {
        switch (choice) {
            case "1":
                System.out.println("\n--- ACTIVE INSURANCE CLAIMS REPOSITORY ---");
                for (Claim c : sys.getAllClaims()) {
                    System.out.println("Claim ID: " + c.getClaimId() + " | Amount: $" + c.getClaimAmount() + " | Status: " + c.getStatus());
                }
                break;
            case "2":
                System.out.print("Enter target Claim ID to advance (f-10digits): ");
                String cId = sc.nextLine().trim();
                System.out.print("Enter target State transition (PROCESSING / DONE): ");
                String st = sc.nextLine().trim().toUpperCase();
                try {
                    sys.updateClaimStatus(cId, st, activeUser.getUserId());
                    System.out.println(">>> State sequence updated and saved successfully.");
                } catch (Exception e) {
                    System.out.println("\n[CRITICAL VIOLATION TERMINATED]");
                    System.out.println("Exception Intercepted: " + e.getMessage());
                }
                break;
            case "3":
                System.out.println("\n--- INSURANCE COVERAGE CARDS DIRECTORY ---");
                for (InsuranceCard card : sys.getAllCards()) System.out.println(card);
                break;
            case "4":
                logout();
                break;
            default:
                System.out.println(">>> Invalid selection.");
        }
    }

    private static void handleCustomerWorkflow(String choice) {
        Customer cust = (Customer) activeUser;
        switch (choice) {
            case "1":
                System.out.println("\n--- COVERAGE METRICS & MEMBERSHIP PROFILE ---");
                System.out.println("Account Owner Name   : " + cust.getFullName());
                System.out.println("Customer Account ID  : " + cust.getCustomerId());
                System.out.println("Total Approved Claims: $" + cust.getTotalClaimAmount());
                System.out.println("Determined Level Tier: " + cust.getMembershipTier());
                System.out.printf("Effective Co-Pay Rate: %.1f%%\n", (cust.getEffectiveCoPayRate() * 100));
                break;
            case "2":
                System.out.println("\n--- PERSONAL HISTORICAL CLAIMS LEDGER ---");
                boolean hasClaims = false;
                for (Claim cl : sys.getAllClaims()) {
                    if (cl.getInsuredPersonId().equals(cust.getCustomerId())) {
                        hasClaims = true;
                        System.out.println("Claim ID: " + cl.getClaimId() + " | Amount: $" + cl.getClaimAmount() + " | Status: " + cl.getStatus());
                    }
                }
                if (!hasClaims) System.out.println("No transactional requests found for your profile.");
                break;
            case "3":
                logout();
                break;
            default:
                System.out.println(">>> Invalid selection.");
        }
    }

    private static void logout() {
        sys.logAction(activeUser.getUserId(), "LOGOUT_CLEAN", activeUser.getUserId());
        System.out.println(">>> Session flushed securely.");
        activeUser = null;
    }
}


