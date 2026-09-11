package claimshield.model;
import java.time.LocalDateTime;
import java.util.List;

public class Claim {
    private final String claimId, insuredPersonId, cardNumber;
    private final LocalDateTime claimDate, examDate;
    private final List<String> documents;
    private final double claimAmount;
    private String status, processedByOfficerId;

    public Claim(String claimId, LocalDateTime claimDate, String insuredPersonId, String cardNumber,
                 LocalDateTime examDate, List<String> documents, double claimAmount, String status, String officerId) {
        this.claimId = claimId; this.claimDate = claimDate; this.insuredPersonId = insuredPersonId; this.cardNumber = cardNumber;
        this.examDate = examDate; this.documents = documents; this.claimAmount = claimAmount; this.status = status; this.processedByOfficerId = officerId;
    }
    public String getClaimId() { return claimId; }
    public String getInsuredPersonId() { return insuredPersonId; }
    public String getCardNumber() { return cardNumber; }
    public LocalDateTime getClaimDate() { return claimDate; }
    public LocalDateTime getExamDate() { return examDate; }
    public List<String> getDocuments() { return documents; }
    public double getClaimAmount() { return claimAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getProcessedByOfficerId() { return processedByOfficerId; }
    public void setProcessedByOfficerId(String id) { this.processedByOfficerId = id; }
}

