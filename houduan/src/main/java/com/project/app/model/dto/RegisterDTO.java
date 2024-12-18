package com.project.app.model.dto;


import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private String role;
    private String address;
    private String gender;
}
