package claimshield.model;
import java.time.LocalDateTime;

public class InsuranceCard {
    private final String cardNumber, cardHolderId, policyOwnerId;
    private final LocalDateTime expirationDate;

    public InsuranceCard(String cardNumber, String cardHolderId, String policyOwnerId, LocalDateTime expirationDate) {
        this.cardNumber = cardNumber; this.cardHolderId = cardHolderId; this.policyOwnerId = policyOwnerId; this.expirationDate = expirationDate;
    }
    public String getCardNumber() { return cardNumber; }
    public String getCardHolderId() { return cardHolderId; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    @Override
    public String toString() {
        return "Card #" + cardNumber + " [Holder: " + cardHolderId + " | Exp: " + expirationDate.toLocalDate() + "]";
    }
}
