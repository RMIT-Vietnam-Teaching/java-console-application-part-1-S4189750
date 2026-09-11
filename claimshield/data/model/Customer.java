package claimshield.model;

public class Customer extends User {
    private final String customerId;
    private InsuranceCard insuranceCard;
    private double totalClaimAmount;
    private final String customerType;

    public Customer(String userId, String username, String password, String fullName, String email,
                    String status, String customerId, String customerType, double totalClaimAmount) {
        super(userId, username, password, fullName, email, "CUSTOMER", status);
        this.customerId = customerId;
        this.customerType = customerType;
        this.totalClaimAmount = totalClaimAmount;
    }

    public String getCustomerId() { return customerId; }
    public String getCustomerType() { return customerType; }
    public double getTotalClaimAmount() { return totalClaimAmount; }
    public void addApprovedAmount(double amt) { this.totalClaimAmount += amt; }
    public InsuranceCard getInsuranceCard() { return insuranceCard; }
    public void setInsuranceCard(InsuranceCard card) { this.insuranceCard = card; }

    public String getMembershipTier() {
        if (totalClaimAmount >= 25000) return "Platinum";
        if (totalClaimAmount >= 10000) return "Gold";
        if (totalClaimAmount >= 5000) return "Silver";
        return "Standard";
    }

    public double getEffectiveCoPayRate() {
        double baseCoPay = 0.30;
        switch (getMembershipTier()) {
            case "Platinum": return baseCoPay * (1 - 0.15);
            case "Gold": return baseCoPay * (1 - 0.10);
            case "Silver": return baseCoPay * (1 - 0.05);
            default: return baseCoPay;
        }
    }

    @Override
    public void displayDashboard() {
        System.out.println("\n*** CUSTOMER MEMBER PORTAL ***");
        System.out.println("1. Track Coverage Status & Membership Tier\n2. View Personal Claims Ledger\n3. Logout");
    }
}

