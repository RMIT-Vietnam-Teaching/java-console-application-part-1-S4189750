package claimshield.model;

public class Admin extends User {
    public Admin(String userId, String username, String password, String fullName, String email, String status) {
        super(userId, username, password, fullName, email, "ADMIN", status);
    }
    @Override
    public void displayDashboard() {
        System.out.println("\n*** ADMINISTRATOR CONTROL PANEL ***");
        System.out.println("1. System Accounts CRUD\n2. View Audit Logs\n3. Execute Financial Analytics\n4. Logout");
    }
}

