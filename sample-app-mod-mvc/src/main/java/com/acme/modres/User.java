package com.acme.modres;

public class User {
    private String name;
    private int userId;
    private String password;

    public User(String name, int userId, String password) {
        this.name = name;
        this.userId = userId;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
        // Assisted by watsonx Code Assistant 

    //Sort an array of Users by userId.
    public static User[] sort(User[] users) {
        for (int out = 0; out < users.length; out++) {
            for (int in = 0; in < users.length - 1; in++) {
                if (users[in].getUserId() > users[in + 1].getUserId()) {
                    User temp = users[in];
                    users[in] = users[in + 1];
                    users[in + 1] = temp;
                }
            }
        }
        return users;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userId=" + userId +
                ", password='" + password + '\'' +
                '}';
    }
    // Assisted by watsonx Code Assistant 

}
