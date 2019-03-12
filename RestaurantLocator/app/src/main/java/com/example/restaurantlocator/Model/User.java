package com.example.restaurantlocator.Model;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private static String username, image, address;
    private static String email;
    private String password;


    public User()
    {

    }


    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
        this.address = address;
    }

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        User.image = image;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        User.address = address;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getUsername()
    {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public  static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

