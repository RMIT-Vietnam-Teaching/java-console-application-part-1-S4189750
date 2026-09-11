package claimshield.model;

public class ClaimsOfficer extends User {
    public ClaimsOfficer(String userId, String username, String password, String fullName, String email, String status) {
        super(userId, username, password, fullName, email, "OFFICER", status);
    }
    @Override
    public void displayDashboard() {
        System.out.println("\n*** CLAIMS OFFICER WORKSPACE ***");
        System.out.println("1. Query Active Claims\n2. Advance Claim Status\n3. View Cards Directory\n4. Logout");
    }
}
