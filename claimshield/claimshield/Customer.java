package claimshield
public class Customer {
    private String id, fullName, customerType, parentPolicyHolderId;

    public Customer(String id, String fullName, String customerType, String parentPolicyHolderId) {
        this.id = id; this.fullName = fullName; this.customerType = customerType; this.parentPolicyHolderId = parentPolicyHolderId;
    }
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getCustomerType() { return customerType; }
    public String getParentPolicyHolderId() { return parentPolicyHolderId; }

    @Override
    public String toString() {
        return "Customer [ID=" + id + ", Name=" + fullName + ", Type=" + customerType + ", Parent PH=" + parentPolicyHolderId + "]";
    }
}
