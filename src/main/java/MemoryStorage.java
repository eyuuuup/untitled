
import java.util.HashMap;
import java.util.Map;

public class MemoryStorage implements Storage{
    private final Map<String, User> users;

    public MemoryStorage() {
        users = new HashMap<>();
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

    public void exit() {
        System.out.println("No persistence in user data.");
    }

}
