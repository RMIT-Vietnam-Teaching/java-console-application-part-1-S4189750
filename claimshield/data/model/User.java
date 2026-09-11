package claimshield.model;

public abstract class User {
    private String userId, username, password, fullName, email, role, status;

    public User(String userId, String username, String password, String fullName, String email, String role, String status) {
        this.userId = userId; this.username = username; this.password = password;
        this.fullName = fullName; this.email = email; this.role = role; this.status = status;
    }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public abstract void displayDashboard();
}

