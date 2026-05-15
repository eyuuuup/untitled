import java.util.Dictionary;
import java.util.Hashtable;

public class MemoryStorage {
    private final Dictionary<String, User> users;

    public MemoryStorage() {
        users = new Hashtable<>();
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public void putUser(User user) {
        users.put(user.username, user);
    }
}
