package com.example.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
//import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@Entity
//@RedisHash("Role")


public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name = "name", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @Column(name = "description", nullable = false)
    private String description;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}