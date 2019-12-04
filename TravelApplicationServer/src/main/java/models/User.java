package models;

import java.util.Objects;

public class User {
    private String name;
    private String password;
    private String username;

    public User(String name, String password, String username) {
        this.name = name;
        this.password = password;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getPassword().equals(user.getPassword()) &&
                getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPassword(), getUsername());
    }
}
