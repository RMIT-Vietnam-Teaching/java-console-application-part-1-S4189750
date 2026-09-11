package claimshield.respiratory;
import claimshield.model.Claim;
import java.util.List;

public interface ClaimManageable {
    void addClaim(Claim claim, String actorId);
    Claim getClaimById(String id);
    List<Claim> getAllClaims();
    void updateClaimStatus(String claimId, String newStatus, String officerId) throws Exception;
}
