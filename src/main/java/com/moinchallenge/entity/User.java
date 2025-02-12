package com.moinchallenge.entity;

import com.moinchallenge.config.PersonalInfoEncryptor;
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

    //userID = 이메일(unique)
    @Column(nullable = false, unique = true)
    private String userId; // 암호화 필요?

    private String password; //암호화 필요

    @Enumerated(EnumType.STRING)
    private IdType idType;

    //    @Convert(converter = PersonalInfoEncryptor.class) //JPA가 관리하기 때문에 충돌 잠재적 위험
    private String idValue;

    private String name;

    //    @Convert(converter = PersonalInfoEncryptor.class)
//    private String residentRegistrationNumber;

    public boolean canTransfer(double newSum) {
        return newSum <= idType.getLimitAmount();
    }
}
