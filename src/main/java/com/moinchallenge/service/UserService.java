package com.moinchallenge.service;

import com.moinchallenge.dto.request.SignRequest;
import com.moinchallenge.entity.User;
import com.moinchallenge.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;

    public void signup(SignRequest request) {
        if (request.getUserId() == null || request.getPassword() == null
                || request.getName() == null || request.getIdValue() == null
                || request.getIdType() == null
        ) {
            throw new IllegalArgumentException("잘못된 파라미터 입니다.");
        }

        //이미 가입된 userId
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 userId 입니다.");
        }

        //비밀번호 해싱
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        //주민등록번호 / 사업자 번호 암호화
        String encryptedIdValue =  encryptionService.encrypt(request.getIdValue());

        User user = User.builder()
                .userId(request.getUserId())
                .password(encodedPassword)
                .name(request.getName())
                .idType(request.getIdType())
                .idValue(encryptedIdValue)
                .build();
        userRepository.save(user);
    }
}
