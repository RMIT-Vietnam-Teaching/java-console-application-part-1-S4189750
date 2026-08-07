import java.time.LocalDateTime;
import java.util.ArrayList;

public class Claim {
    private String id, insuredPersonId, cardNumber, status;
    private LocalDateTime claimDate, examDate;
    private ArrayList<String> documents;
    private double claimAmount;

    public Claim(String id, LocalDateTime claimDate, String insuredPersonId, String cardNumber,
                 LocalDateTime examDate, ArrayList<String> documents, double claimAmount, String status) {
        this.id = id; this.claimDate = claimDate; this.insuredPersonId = insuredPersonId; this.cardNumber = cardNumber;
        this.examDate = examDate; this.documents = documents; this.claimAmount = claimAmount; this.status = status;
    }
    public String getId() { return id; }
    public String getCardNumber() { return cardNumber; }
    public String getInsuredPersonId() { return insuredPersonId; }
    public LocalDateTime getClaimDate() { return claimDate; }
    public LocalDateTime getExamDate() { return examDate; }
    public ArrayList<String> getDocuments() { return documents; }
    public double getClaimAmount() { return claimAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Claim [ID=" + id + ", Card=" + cardNumber + ", Amount=$" + claimAmount + ", Status=" + status + ", Docs=" + documents + "]";
    }
}
