import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class JSONStorage implements Storage{
    private Map<String, User> users;
    private final ObjectMapper objectMapper;

    public JSONStorage() {
        users = new HashMap<>();
        objectMapper = new ObjectMapper();
    }

    public void init() {
        File jsonFile = new File("users.json");
        if(jsonFile.exists()) {
            users = objectMapper.readValue(jsonFile, new TypeReference<>() {});
            System.out.println(users);
        }

    }

    public User getUser(String username) {
        System.out.println(users.get(username));
        return users.get(username);
    }

    public void putUser(User user) {
        users.put(user.username, user);
    }

    public int totalUsers() {
        return users.size();
    }

    public void exit() {
        try {
            File jsonFile = new File("users.json");
            if(jsonFile.createNewFile()) {
                objectMapper.writeValue(jsonFile, users);
                System.out.println("File created");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
