package com.labs44.interview.domain.user;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class User {

    private int id;
    private String email;
    private String nickname;
    private String password;
    private Authority role;
    private int deleted;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public void register(String password){
        this.role = Authority.USER;
        this.password = password;
        this.created_at = LocalDateTime.now().toString();
        this.updated_at = LocalDateTime.now().toString();
        this.deleted = 0;
    }

    public void addUser(String password) {
        this.created_at = LocalDateTime.now().toString();
        this.updated_at = LocalDateTime.now().toString();
        this.password = password;
    }

    public void updateUser(User updateUser) {
        this.nickname = updateUser.getNickname();
        this.password = updateUser.getPassword();
        this.role = updateUser.getRole();
        this.updated_at = LocalDateTime.now().toString();
    }

    public void deleteUser() {
        this.deleted = 1;
        this.deleted_at = LocalDateTime.now().toString();
    }
}
