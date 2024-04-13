package org.example;

public class User {
    private String email;
    private String role;
    private String firstname;
    private String lastname;
    public User (String newemail,String newrole,String first_name,String last_name){
        email = newemail;
        role = newrole;
        firstname = first_name;
        lastname = last_name;
    }
    public String getRole(){return role;}
    public String getFirst(){return firstname;}
    public String getLast(){return lastname;}
    public String getEmail(){return email;}
}