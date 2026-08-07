package claimshield
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ClaimManager mgr = new ClaimManager();
    private static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        mgr.loadData();
        while (true) {
            System.out.println("\n--- ClaimShield Admin System ---");
            System.out.println("1. Manage Customer Directory\n2. Manage Insurance Cards\n3. Process Claims\n4. Save and Exit");
            System.out.print("Select choice: ");
            String choice = sc.nextLine();
            if (choice.equals("1")) customerMenu();
            else if (choice.equals("2")) cardMenu();
            else if (choice.equals("3")) claimMenu();
            else if (choice.equals("4")) { mgr.saveData(); System.out.println("System synchronized. Exit clean."); break; }
        }
    }

    private static void customerMenu() {
        System.out.println("\n1. Add Customer\n2. View All Customers\n3. Remove Customer");
        String opt = sc.nextLine();
        if (opt.equals("1")) {
            System.out.print("Enter ID (c-7digits): "); String id = sc.nextLine();
            if (!id.matches("^c-\\d{7}$") || mgr.getCustomer(id) != null) { System.out.println("Invalid/Duplicate ID."); return; }
            System.out.print("Name: "); String name = sc.nextLine();
            System.out.print("Type (1: PolicyHolder, 2: Dependent): "); String type = sc.nextLine().equals("1") ? "PolicyHolder" : "Dependent";
            String parentId = null;
            if (type.equals("Dependent")) {
                System.out.print("Parent PolicyHolder ID: "); parentId = sc.nextLine();
                if (mgr.getCustomer(parentId) == null) { System.out.println("Parent profile does not exist."); return; }
            }
            mgr.addCustomer(new Customer(id, name, type, parentId));
            System.out.println("Customer saved successfully.");
        } else if (opt.equals("2")) {
            for (Customer c : mgr.getAllCustomers()) System.out.println(c);
        } else if (opt.equals("3")) {
            System.out.print("Enter ID to remove: "); Customer c = mgr.getCustomer(sc.nextLine());
            if (c != null) { mgr.removeCustomer(c); System.out.println("Removed profile."); }
        }
    }

    private static void cardMenu() {
        System.out.println("\n1. Register Card\n2. View Cards");
        if (sc.nextLine().equals("1")) {
            System.out.print("Card Number (10 digits): "); String num = sc.nextLine();
            if (!num.matches("^\\d{10}$") || mgr.getCard(num) != null) { System.out.println("Invalid/Duplicate Number."); return; }
            System.out.print("Holder ID: "); String hId = sc.nextLine();
            System.out.print("Policy Owner ID: "); String oId = sc.nextLine();
            System.out.print("Expiration (YYYY-MM-DD): "); LocalDateTime exp = LocalDate.parse(sc.nextLine()).atStartOfDay();
            mgr.addCard(new InsuranceCard(num, hId, oId, exp));
            System.out.println("Card bound.");
        } else { for (InsuranceCard c : mgr.getAllCards()) System.out.println(c); }
    }

    private static void claimMenu() {
        System.out.println("\n1. Create Claim\n2. View Claims\n3. Add Document\n4. Update Status");
        String opt = sc.nextLine();
        if (opt.equals("1")) {
            System.out.print("Claim ID (f-10digits): "); String id = sc.nextLine();
            if (!id.matches("^f-\\d{10}$") || mgr.getClaim(id) != null) { System.out.println("Invalid/Duplicate ID."); return; }
            System.out.print("Insured Customer ID: "); String custId = sc.nextLine();
            System.out.print("Card Number: "); String cardNum = sc.nextLine();
            InsuranceCard card = mgr.getCard(cardNum);
            if (card == null) { System.out.println("Unrecognized card."); return; }
            System.out.print("Amount: "); double amt = Double.parseDouble(sc.nextLine());
            if (amt <= 0) return;
            System.out.print("Exam Date (YYYY-MM-DD): "); LocalDateTime exam = LocalDate.parse(sc.nextLine()).atStartOfDay();
            System.out.print("Claim Date (YYYY-MM-DD): "); LocalDateTime claim = LocalDate.parse(sc.nextLine()).atStartOfDay();

            if (exam.isAfter(claim) || exam.isAfter(card.getExpirationDate())) {
                System.out.println("Business Rule Violation: Check date sequencing constraints."); return;
            }
            mgr.addClaim(new Claim(id, claim, custId, cardNum, exam, new ArrayList<>(), amt, "New"));
            System.out.println("Claim created.");
        } else if (opt.equals("2")) {
            for (Claim cl : mgr.getAllClaims()) System.out.println(cl);
        } else if (opt.equals("3")) {
            System.out.print("Claim ID: "); Claim cl = mgr.getClaim(sc.nextLine());
            if (cl == null) return;
            System.out.print("Doc name (e.g. Receipt): "); String name = sc.nextLine();
            cl.getDocuments().add(cl.getId() + "_" + cl.getCardNumber() + "_" + name + ".pdf");
            System.out.println("Document structural string parsed.");
        } else if (opt.equals("4")) {
            System.out.print("Claim ID: "); Claim cl = mgr.getClaim(sc.nextLine());
            if (cl == null) return;
            System.out.print("New Status (Processing/Done): "); String stat = sc.nextLine();
            if (cl.getStatus().equals("New") && (stat.equals("Processing") || stat.equals("Done"))) cl.setStatus(stat);
            else if (cl.getStatus().equals("Processing") && stat.equals("Done")) cl.setStatus(stat);
            else System.out.println("Forbidden: State pipeline movement is strictly forward.");
        }
    }
}
