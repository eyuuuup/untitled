
import java.util.HashMap;
import java.util.Map;

public class MemoryStorage implements Storage{
    private final Map<String, User> users;

    public MemoryStorage() {
        users = new HashMap<>();
    }

    public void init() {
        System.out.println("Temporary memory initialized.");
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public void putUser(User user) {
        users.put(user.username, user);
    }

    public int totalUsers() {
        return users.size();
    }

    @Override
    public void close() {
        System.out.println("No persistence in user data.");
    }
}
