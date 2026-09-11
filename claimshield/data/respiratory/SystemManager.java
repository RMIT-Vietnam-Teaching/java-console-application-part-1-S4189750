package claimshield.respiratory;

import claimshield.model.*;
import claimshield.exception.InvalidStatusTransitionException;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;


public class SystemManager implements UserManageable, ClaimManageable {
    private final List<User> users = new ArrayList<>();
    private final List<Claim> claims = new ArrayList<>();
    private final List<InsuranceCard> cards = new ArrayList<>();
    private final List<String> auditLogs = new ArrayList<>();

    @Override
    public void addUser(User user, String actorId) {
        users.add(user);
        logAction(actorId, "ADD_USER", user.getUserId());
        saveAllData();
    }

    @Override
    public User getUserById(String id) {
        for (User u : users) {
            if (u.getUserId().equals(id)) return u;
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) return u;
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() { return users; }

    @Override
    public void softDeleteUser(String id, String actorId) {
        User u = getUserById(id);
        if (u != null) {
            u.setStatus("INACTIVE");
            logAction(actorId, "SOFT_DELETE_USER", id);
            saveAllData();
        }
    }

    public void addCard(InsuranceCard card, String actorId) {
        cards.add(card);
        for (User u : users) {
            if (u instanceof Customer && ((Customer) u).getCustomerId().equals(card.getCardHolderId())) {
                ((Customer) u).setInsuranceCard(card);
            }
        }
        logAction(actorId, "ADD_CARD", card.getCardNumber());
        saveAllData();
    }

    public InsuranceCard getCardByNumber(String num) {
        for (InsuranceCard c : cards) {
            if (c.getCardNumber().equals(num)) return c;
        }
        return null;
    }

    public List<InsuranceCard> getAllCards() { return cards; }

    @Override
    public void addClaim(Claim claim, String actorId) {
        claims.add(claim);
        logAction(actorId, "CREATE_CLAIM", claim.getClaimId());
        saveAllData();
    }

    @Override
    public Claim getClaimById(String id) {
        for (Claim c : claims) {
            if (c.getClaimId().equals(id)) return c;
        }
        return null;
    }

    @Override
    public List<Claim> getAllClaims() { return claims; }

    @Override
    public void updateClaimStatus(String claimId, String newStatus, String officerId) throws Exception {
        Claim claim = getClaimById(claimId);
        if (claim == null) {
            throw new Exception("Claim system error: Reference tracking ID not found.");
        }

        String current = claim.getStatus().toUpperCase();
        String target = newStatus.toUpperCase();

        if (current.equals("DONE")) {
            throw new InvalidStatusTransitionException("Transaction locked permanently. Modification denied.");
        }
        if (current.equals("PROCESSING") && target.equals("NEW")) {
            throw new InvalidStatusTransitionException("Status pipeline constraint error: Reverse sequence blocked.");
        }

        claim.setStatus(target);
        claim.setProcessedByOfficerId(officerId);

        if (target.equals("DONE")) {
            User u = getUserById(getUserIdByCustomerId(claim.getInsuredPersonId()));
            if (u instanceof Customer) {
                ((Customer) u).addApprovedAmount(claim.getClaimAmount());
            }
        }

        logAction(officerId, "UPDATE_CLAIM_STATUS_" + target, claimId);
        saveAllData();
    }

    private String getUserIdByCustomerId(String custId) {
        for (User u : users) {
            if (u instanceof Customer && ((Customer) u).getCustomerId().equals(custId)) {
                return u.getUserId();
            }
        }
        return "unknown";
    }

    public void logAction(String userId, String action, String targetId) {
        String logEntry = LocalDateTime.now() + "|" + userId + "|" + action + "|" + targetId;
        auditLogs.add(logEntry);
        try (PrintWriter pw = new PrintWriter(new FileWriter("data/logs.txt", true))) {
            pw.println(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAuditLogs() { return auditLogs; }

    public void runTimeframeReport(LocalDateTime start, LocalDateTime end) {
        double totalPayout = 0, totalCoPay = 0;
        for (Claim c : claims) {
            if (c.getStatus().equals("DONE") && !c.getClaimDate().isBefore(start) && !c.getClaimDate().isAfter(end)) {
                User u = getUserById(getUserIdByCustomerId(c.getInsuredPersonId()));
                if (u instanceof Customer) {
                    double rate = ((Customer) u).getEffectiveCoPayRate();
                    double copay = c.getClaimAmount() * rate;
                    totalCoPay += copay;
                    totalPayout += (c.getClaimAmount() - copay);
                }
            }
        }
        System.out.println("\n--- TIMEFRAME MONITORING METRICS ---");
        System.out.printf("Total Insurance Approved Expenditure: $%.2f\n", totalPayout);
        System.out.printf("Total Customer Out-Of-Pocket Co-Pays: $%.2f\n", totalCoPay);
    }

    public void runOfficerReport() {
        System.out.println("\n--- CLAIMS OFFICER PERFORMANCE LEDGER ---");
        Map<String, Double> officerPayouts = new HashMap<>();
        for (Claim c : claims) {
            if (c.getStatus().equals("DONE") && c.getProcessedByOfficerId() != null) {
                officerPayouts.put(c.getProcessedByOfficerId(), officerPayouts.getOrDefault(c.getProcessedByOfficerId(), 0.0) + c.getClaimAmount());
            }
        }
        for (String offId : officerPayouts.keySet()) {
            System.out.printf("Officer ID: %s | Aggregated Claims Transacted Volume: $%.2f\n", offId, officerPayouts.get(offId));
        }
    }

    public void runTierSummaryReport() {
        System.out.println("\n--- MEMBERSHIP TIER BALANCE ANALYTICS ---");
        double[] claimsWeight = new double[4];
        for (Claim c : claims) {
            if (c.getStatus().equals("DONE")) {
                User u = getUserById(getUserIdByCustomerId(c.getInsuredPersonId()));
                if (u instanceof Customer) {
                    String tier = ((Customer) u).getMembershipTier();
                    int idx = tier.equals("Platinum") ? 3 : tier.equals("Gold") ? 2 : tier.equals("Silver") ? 1 : 0;
                    claimsWeight[idx] += c.getClaimAmount();
                }
            }
        }
        System.out.printf("Standard Tier Total Approved Volume: $%.2f\n", claimsWeight[0]);
        System.out.printf("Silver Tier Total Approved Volume  : $%.2f\n", claimsWeight[1]);
        System.out.printf("Gold Tier Total Approved Volume    : $%.2f\n", claimsWeight[2]);
        System.out.printf("Platinum Tier Total Approved Volume: $%.2f\n", claimsWeight[3]);
    }

    public void loadAllData() {
        DataManager.loadAllData(this.users, this.cards, this.claims);
        for (InsuranceCard card : cards) {
            for (User u : users) {
                if (u instanceof Customer && ((Customer) u).getCustomerId().equals(card.getCardHolderId())) {
                    ((Customer) u).setInsuranceCard(card);
                }
            }
        }
    }

    public void saveAllData() {
        DataManager.saveAllData(this.users);
    }
}



