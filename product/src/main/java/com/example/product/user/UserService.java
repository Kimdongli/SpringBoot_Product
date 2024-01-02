package com.example.product.user;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.product.core.error.exception.Exception400;
import com.example.product.core.error.exception.Exception401;
import com.example.product.core.error.exception.Exception500;
import com.example.product.core.security.CustomUserDetails;
import com.example.product.core.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
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
        checkEmail(requestDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
        requestDTO.setPassword(encodedPassword);

        userRepository.save(requestDTO.toEntity());
    }

    public void checkEmail(String email){
        Optional<User> byEmail = userRepository.findByEmail(email);

        if(byEmail.isPresent()){
            throw new Exception400("이미 존재하는 email 입니다" + email);
        }
    }

    @Transactional
    public String authenticateAndCreateToken(UserRequest.JoinDTO joinDto) {
        // ** 인증 작업
        try{
            // 사용자로부터 받은 이메일과 비밀번호를 가지고 토큰을 생성.
            UsernamePasswordAuthenticationToken token
                    = new UsernamePasswordAuthenticationToken(
                    joinDto.getEmail(), joinDto.getPassword());
            // 토큰을 이용해 인증을 시도.
            Authentication authentication
                    = authenticationManager.authenticate(token);
            // ** 인증 완료 값을 받아온다.
            // 인증키
            // 인증된 사용자의 정보를 가져옵니다.
            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
            // JWT 토큰을 생성.
            String prefixJwt = JwtTokenProvider.create(customUserDetails.getUser());
            // "Bearer "를 제거해서 순수한 토큰만을 가져옵니다.
            String access_token = prefixJwt.replace(JwtTokenProvider.TOKEN_PREFIX, "");
            // JWT 리프레시 토큰을 생성.
            String refreshToken = JwtTokenProvider.createRefresh(customUserDetails.getUser());

            User user = customUserDetails.getUser();
            // 생성된 토큰들을 사용자 정보에 저장.
            user.setAccess_token(access_token);
            user.setRefresh_token(refreshToken);

            // 사용자 정보를 저장.
            userRepository.save(user);

            return prefixJwt;
        }catch (Exception e){
            throw new Exception401("인증되지 않음.");
        }
    }

    public void login(UserRequest.JoinDTO requestDTO, HttpSession session) {
        try {
            final String oauthUrl = "http://localhost:8080/user/oauth";
            ResponseEntity<JsonNode> response = userPost(oauthUrl,null, requestDTO);
            String access_token = response.getHeaders().getFirst(JwtTokenProvider.HEADER);
            session.setAttribute("access_token", access_token);
            session.setAttribute("platform", "user");

            setUserInfoInSession(session);
        } catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

    public User setUserInfoInSession(HttpSession session) {
        // 세션에서 액세스 토큰을 가져옵니다.
        String access_token = (String) session.getAttribute("access_token");

        // 사용자 정보를 가져오기 위한 URL을 설정.
        final String infoUrl = "http://localhost:8080/user/user_id";
        // 새로운 HTTP 헤더를 생성.
        HttpHeaders headers = new HttpHeaders();
        // 헤더의 컨텐츠 타입을 JSON으로 설정.
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 액세스 토큰을 헤더에 추가.
        headers.set("Authorization", access_token);
        // HTTP POST 요청을 보내고, 응답을 받아옵니다. 이때, 응답 본문에서 "response" 필드를 long 타입으로 변환하여 사용자 ID를 가져옵니다.
        Long user_id = userPost(infoUrl, headers, null).getBody().get("response").asLong();
        // 사용자 ID를 이용해 사용자를 찾아 반환.
        return userRepository.findById(user_id).get();
    }

    public <T> ResponseEntity<JsonNode> userPost(String requestUrl, HttpHeaders headers, T body){
        try{
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);

            return restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, JsonNode.class);
        } catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

    @Transactional
    public String logout(HttpSession session) {
        try {
           {
                User user = setUserInfoInSession(session);
                userRepository.save(clearTokens(user));
                session.invalidate();
            }
            return "/";
        } catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }
    public User clearTokens(User user){
        user.setAccess_token(null);
        user.setRefresh_token(null);
        return user;
    }

    @Transactional
    public void refresh(Long id, HttpSession session) {
        // 사용자 아이디를 통해 리프레시 토큰을 가져옵니다. 만약 사용자를 찾을 수 없다면 500 에러를 반환.
        String refresh_token = userRepository.findById(id)
                .orElseThrow(() -> new Exception500("사용자를 찾을 수 없습니다.")).getRefresh_token();

        // 리프레시 토큰을 검증하고 복호화.
        DecodedJWT decodedJWT = JwtTokenProvider.verify(refresh_token);

        // === Access Token 재발급 === //
        // 복호화된 JWT에서 사용자 이름을 가져옵니다.
        String username = decodedJWT.getSubject();
        // 사용자 이메일을 통해 사용자 정보를 가져옵니다. 만약 사용자를 찾을 수 없다면 500 에러를 반환.
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new Exception500("사용자를 찾을 수 없습니다."));
        // 가져온 사용자의 리프레시 토큰과 처음에 검증한 리프레시 토큰이 일치하는지 확인. 일치하지 않다면 401 에러를 반환.
        if (!user.getRefresh_token().equals(refresh_token))
            throw new Exception401("유효하지 않은 Refresh Token 입니다.");
        // 새로운 액세스 토큰을 생성.
        String new_access_Token = JwtTokenProvider.create(user);
        // 사용자 정보에 새로운 액세스 토큰 저장
        user.setAccess_token(new_access_Token);
        // 세션에 새로운 액세스 토큰을 저장
        session.setAttribute("access_token", new_access_Token);

        // === 현재시간과 Refresh Token 만료날짜를 통해 남은 만료기간 계산 === //
        // === Refresh Token 만료시간 계산해 5일 미만일 시 refresh token도 발급 === //
        long endTime = decodedJWT.getClaim("exp").asLong() * 1000;
        long diffDay = (endTime - System.currentTimeMillis()) / 1000 / 60 / 60 / 24;
        if (diffDay < 5) {
            String new_refresh_token = JwtTokenProvider.createRefresh(user);
            user.setRefresh_token(new_refresh_token);
        }

        userRepository.save(user);
    }

    public Long getCurrnetUserId(HttpSession session) {
        return setUserInfoInSession(session).getId();
    }

}
