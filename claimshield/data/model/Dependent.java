package claimshield.model;

public class Dependent extends Customer {
    private final String parentPolicyHolderId;

    public Dependent(String userId, String username, String password, String fullName, String email, String status, String customerId, String parentId, double totalClaimAmount) {
        super(userId, username, password, fullName, email, status, customerId, "Dependent", totalClaimAmount);
        this.parentPolicyHolderId = parentId;
    }
    public String getParentPolicyHolderId() { return parentPolicyHolderId; }
}
