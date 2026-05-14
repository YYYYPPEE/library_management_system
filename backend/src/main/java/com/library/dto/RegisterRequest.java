package com.library.dto;

public class RegisterRequest {
    private String username;
    private String password;
    private String realName;
    private String studentNo;
    private Integer role;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String realName, String studentNo, Integer role) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.studentNo = studentNo;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
