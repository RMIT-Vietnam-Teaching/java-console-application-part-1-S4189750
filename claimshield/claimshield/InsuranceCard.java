import java.time.LocalDateTime;

public class InsuranceCard {
    private String cardNumber;
    private String cardHolderId;
    private String policyOwnerId;
    private LocalDateTime expirationDate;

    public InsuranceCard(String cardNumber, String cardHolderId, String policyOwnerId, LocalDateTime expirationDate) {
        this.cardNumber = cardNumber;
        this.cardHolderId = cardHolderId;
        this.policyOwnerId = policyOwnerId;
        this.expirationDate = expirationDate;
    }

    public String getCardNumber() { return cardNumber; }
    public String getCardHolderId() { return cardHolderId; }
    public String getPolicyOwnerId() { return policyOwnerId; }
    public LocalDateTime getExpirationDate() { return expirationDate; }

    @Override
    public String toString() {
        return "Card [No=" + cardNumber + ", Holder=" + cardHolderId + ", Owner=" + policyOwnerId + ", Exp=" + expirationDate + "]";
    }
}