package com.moinchallenge.entity;

import com.moinchallenge.config.PersonalInfoEncryptor;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //userID = 이메일(unique)
    @Column(nullable = false,unique = true)
    private String userId; // 암호화 필요?

    private String password; //암호화 필요

    private String idType;

//    @Convert(converter = PersonalInfoEncryptor.class) //JPA가 관리하기 때문에 충돌 잠재적 위험
    private String idValue;

    private String name;

    //    @Convert(converter = PersonalInfoEncryptor.class)
//    private String residentRegistrationNumber;


}
