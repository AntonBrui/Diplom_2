package model;

public class UserCreate {

    public String email;
    public String password;
    public String name;

    public UserCreate(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}