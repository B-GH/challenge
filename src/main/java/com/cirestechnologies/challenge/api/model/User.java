package com.cirestechnologies.challenge.api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String birthDate;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String avatar;

    @Column
    private String company;

    @Column
    private String jobPosition;

    @Column
    private String mobile;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String role;
}
















