package com.moinchallenge.entity;

import com.moinchallenge.constant.IdType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    private String password;

    @Enumerated(EnumType.STRING)
    private IdType idType;

    private String idValue;

    private String name;

    public boolean canTransfer(double newSum) {
        return newSum <= idType.getLimitAmount();
    }
}
