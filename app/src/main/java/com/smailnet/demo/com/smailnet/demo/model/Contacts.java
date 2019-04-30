package com.smailnet.demo.com.smailnet.demo.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Contacts extends RealmObject {

    @PrimaryKey
    private String id;
    private String nickName;
    private String email;

    public Contacts(String nickName, String email) {
        this.nickName = nickName;
        this.email = email;
        id = UUID.randomUUID().toString();
    }

    public Contacts() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
