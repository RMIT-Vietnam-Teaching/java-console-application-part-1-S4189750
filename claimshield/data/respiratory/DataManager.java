package claimshield.respiratory;
import claimshield.model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;


public class DataManager {
    public static void loadAllData(List<User> users, List<InsuranceCard> cards, List<Claim> claims) {
        try {
            File uFile = new File("data/users.txt");
            if (uFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(uFile));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    if (p[5].equals("ADMIN")) {
                        users.add(new Admin(p[0], p[1], p[2], p[3], p[4], p[6]));
                    } else if (p[5].equals("OFFICER")) {
                        users.add(new ClaimsOfficer(p[0], p[1], p[2], p[3], p[4], p[6]));
                    }
                }
                br.close();
            }

            File cFile = new File("data/customers.txt");
            if (cFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(cFile));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    String dummyPass = "cust123";
                    if (p[2].equals("PolicyHolder")) {
                        users.add(new PolicyHolder(p[0], "user_" + p[0], dummyPass, p[1], p[0] + "@insurance.com", "ACTIVE", p[0], Double.parseDouble(p[4])));
                    } else {
                        users.add(new Dependent(p[0], "user_" + p[0], dummyPass, p[1], p[0] + "@insurance.com", "ACTIVE", p[0], p[3], Double.parseDouble(p[4])));
                    }
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
                    List<String> docs = new ArrayList<>();
                    if (!p[5].equals("none")) {
                        Collections.addAll(docs, p[5].split(","));
                    }
                    claims.add(new Claim(p[0], LocalDateTime.parse(p[1]), p[2], p[3], LocalDateTime.parse(p[4]), docs, Double.parseDouble(p[6]), p[7], p[8]));
                }
                br.close();
            }
        } catch (Exception e) {
            System.out.println("Exception intercepted during initial parsing boot sequence.");
        }
    }

    public static void saveAllData(List<User> users) {
        try {
            new File("data").mkdirs();
            PrintWriter pw = new PrintWriter(new FileWriter("data/users.txt"));
            for (User u : users) {
                if (u instanceof Admin || u instanceof ClaimsOfficer) {
                    pw.println(u.getUserId() + "|" + u.getUsername() + "|" + u.getPassword() + "|" + u.getFullName() + "|" + u.getRole() + "|" + u.getStatus());
                }
            }
            pw.close();

            pw = new PrintWriter(new FileWriter("data/customers.txt"));
            for (User u : users) {
                if (u instanceof Customer) {
                    Customer c = (Customer) u;
                    String parentId = (c instanceof Dependent) ? ((Dependent) c).getParentPolicyHolderId() : "null";
                    pw.println(c.getUserId() + "|" + c.getFullName() + "|" + c.getCustomerType() + "|" + parentId + "|" + c.getTotalClaimAmount());
                }
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
