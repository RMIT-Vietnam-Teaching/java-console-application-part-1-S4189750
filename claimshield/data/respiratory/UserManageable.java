package claimshield.respiratory;
import claimshield.model.User;
import java.util.List;

public interface UserManageable {
    void addUser(User user, String actorId);
    User getUserById(String id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    void softDeleteUser(String id, String actorId);
}

