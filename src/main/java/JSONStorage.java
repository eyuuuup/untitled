import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class JSONStorage implements Storage, AutoCloseable {
    private Map<String, User> users;
    private final ObjectMapper objectMapper;

    public JSONStorage() {
        users = new HashMap<>();
        objectMapper = new ObjectMapper();

        File jsonFile = new File("users.json");
        if(jsonFile.exists()) {
            users = objectMapper.readValue(jsonFile, new TypeReference<>() {});
            System.out.println("Users loaded into memory.");
        } else {
            System.out.println("Userbase not found, new file will be made.");
        }
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
        try {
            File jsonFile = new File("users.json");
            if(jsonFile.createNewFile()) {
                System.out.println("File created");
            } else {
                System.out.println("File updated.");
            }
            objectMapper.writeValue(jsonFile, users);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
