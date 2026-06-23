public interface Storage extends AutoCloseable {

    User getUser(String username);

    void putUser(User user);

    int totalUsers();

}
