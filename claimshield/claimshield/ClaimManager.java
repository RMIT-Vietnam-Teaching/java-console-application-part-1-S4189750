import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class ClaimManager {
    private final ArrayList<Customer> customers = new ArrayList<>();
    private final ArrayList<InsuranceCard> cards = new ArrayList<>();
    private final ArrayList<Claim> claims = new ArrayList<>();

    public void addCustomer(Customer c) { customers.add(c); }
    public Customer getCustomer(String id) {
        for (Customer c : customers) if (c.getId().equals(id)) return c;
        return null;
    }
    public ArrayList<Customer> getAllCustomers() { return customers; }
    public void removeCustomer(Customer c) { customers.remove(c); }

    public void addCard(InsuranceCard card) { cards.add(card); }
    public InsuranceCard getCard(String num) {
        for (InsuranceCard c : cards) if (c.getCardNumber().equals(num)) return c;
        return null;
    }
    public ArrayList<InsuranceCard> getAllCards() { return cards; }

    public void addClaim(Claim claim) { claims.add(claim); }
    public Claim getClaim(String id) {
        for (Claim c : claims) if (c.getId().equals(id)) return c;
        return null;
    }
    public ArrayList<Claim> getAllClaims() { return claims; }

    public void loadData() {
        try {
            File cFile = new File("data/customers.txt");
            if (cFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(cFile));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    customers.add(new Customer(p[0], p[1], p[2], p[3].equals("null") ? null : p[3]));
                }
                br.close();
            }
            File cardFile = new File("data/cards.txt");
            if (cardFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(cardFile));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    cards.add(new InsuranceCard(p[0], p[1], p[2], LocalDateTime.parse(p[3])));
                }
                br.close();
            }
            File clFile = new File("data/claims.txt");
            if (clFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(clFile));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    ArrayList<String> docs = new ArrayList<>();
                    if (!p[5].equals("none")) {
                        Collections.addAll(docs, p[5].split(","));
                    }
                    claims.add(new Claim(p[0], LocalDateTime.parse(p[1]), p[2], p[3], LocalDateTime.parse(p[4]), docs, Double.parseDouble(p[6]), p[7]));
                }
                br.close();
            }
        } catch (Exception e) { System.out.println("Data loading initialized clean."); }
    }

    public void saveData() {
        try {
            File dir = new File("data");
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) System.out.println("Warning: Could not create data directory.");
            }
            PrintWriter pw = new PrintWriter(new FileWriter("data/customers.txt"));
            for (Customer c : customers) pw.println(c.getId() + "|" + c.getFullName() + "|" + c.getCustomerType() + "|" + c.getParentPolicyHolderId());
            pw.close();

            pw = new PrintWriter(new FileWriter("data/cards.txt"));
            for (InsuranceCard c : cards) pw.println(c.getCardNumber() + "|" + c.getCardHolderId() + "|" + c.getPolicyOwnerId() + "|" + c.getExpirationDate());
            pw.close();

            pw = new PrintWriter(new FileWriter("data/claims.txt"));
            for (Claim c : claims) {
                String dStr = c.getDocuments().isEmpty() ? "none" : String.join(",", c.getDocuments());
                pw.println(c.getId() + "|" + c.getClaimDate() + "|" + c.getInsuredPersonId() + "|" + c.getCardNumber() + "|" + c.getExamDate() + "|" + dStr + "|" + c.getClaimAmount() + "|" + c.getStatus());
            }
            pw.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}