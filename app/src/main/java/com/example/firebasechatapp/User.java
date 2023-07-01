package com.example.firebasechatapp;

public class User {
    private String id;
    private String name;
    private String email;
    private int avatarResource;

    public User(){

    }


    public User(String id, String name, String email){
        this.email = email;
        this.id = id;
        this.name = name;
    }

    public User(String id, String name, String email,int avatarResource){
        this.email = email;
        this.id = id;
        this.name = name;
        this.avatarResource = avatarResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAvatarResource() {
        return avatarResource;
    }

    public void setAvatarResource(int avatarResource) {
        this.avatarResource = avatarResource;
    }
}
