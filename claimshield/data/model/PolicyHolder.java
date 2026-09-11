package claimshield.model;
import java.util.ArrayList;
import java.util.List;

public class PolicyHolder extends Customer {
    private final List<Dependent> dependents = new ArrayList<>();

    public PolicyHolder(String userId, String username, String password, String fullName, String email, String status, String customerId, double totalClaimAmount) {
        super(userId, username, password, fullName, email, status, customerId, "PolicyHolder", totalClaimAmount);
    }
    public List<Dependent> getDependents() { return dependents; }
}
