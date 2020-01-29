package com.mobile.store.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "User")
@Data
public class User {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name="FirstName" , nullable=false)
    private String firstname;

    @Column(name="LastName" , nullable=false)
    private String lastname;

    @Column(name="UserName" , nullable=false)
    private String username;

    @Column(name="Password" , nullable=false)
    private String password;
    
    @Column(name="Email" , nullable=false)
    private String email;
    
    @Column(name="Country" , nullable=false)
    private String country;
    
    
}
