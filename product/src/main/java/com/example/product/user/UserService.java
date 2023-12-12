package com.example.product.user;

import com.example.product.core.error.exception.Exception400;
import com.example.product.core.error.exception.Exception401;
import com.example.product.core.security.CustomUserDetails;
import com.example.product.core.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        Optional<User> byEmail = userRepository.findByEmail(requestDTO.getEmail());

        if(byEmail.isPresent()){
            throw new Exception400("이미 존재하는 email 입니다" + requestDTO.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
        requestDTO.setPassword(encodedPassword);

        userRepository.save(requestDTO.toEntity());
    }

    @Transactional
    public String login(UserRequest.JoinDTO requestDTO) {
        String jwt = "";

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword());

            Authentication authentication =  authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken
            );

            if (authentication.getPrincipal() == null){
                throw new Exception401("Principal is null");
            }

            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();

            jwt  = JwtTokenProvider.create(customUserDetails.getUser());

        } catch (Exception e){
            throw new Exception401("인증되지 않음.");
        }

        return jwt;
    }
}
