public interface Storage {

    User getUser(String username);

    void putUser(User user);

    int totalUsers();

    void exit();
}
