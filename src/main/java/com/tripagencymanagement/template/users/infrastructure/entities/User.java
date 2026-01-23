package com.tripagencymanagement.template.users.infrastructure.entities;

import com.tripagencymanagement.template.general.entities.repositoryEntites.BaseAbstractEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class User extends BaseAbstractEntity {

    @Column(nullable = true, length = 100, unique = true, name = "user_name")
    private String userName;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 255, name = "password_hash")
    private String passwordHash;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Staff staff;
}
