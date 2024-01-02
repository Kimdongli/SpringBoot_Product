package com.example.product.user;



import com.example.product.core.security.CustomUserDetails;
import com.example.product.core.security.JwtTokenProvider;
import com.example.product.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @PostMapping("/check")
    public ResponseEntity<Object> check(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Error error){
        userService.checkEmail(requestDTO.getEmail());

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Errors errors){
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }

        if (requestDTO.getEmail() == null || requestDTO.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email cannot be null or empty");
        }

        userService.join(requestDTO);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserRequest.JoinDTO requestDTO, HttpServletRequest request, Error error){

        userService.login(requestDTO, request.getSession());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, Error error){
        return userService.logout(request.getSession());
    }


    @PostMapping("/oauth")
    public ResponseEntity<Object> connect(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Error error){
        String jwt = userService.authenticateAndCreateToken(requestDTO);

        return ResponseEntity.ok().header(JwtTokenProvider.HEADER, jwt)
                .body(ApiUtils.success(null));
    }

    @PostMapping("/user_id")
    public ResponseEntity<ApiUtils.ApiResult<Long>> getCurrentUser(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        if (customUserDetails.getUser() == null){
            return ResponseEntity.ok(ApiUtils.error("현재 로그인된 user가 없습니다.", HttpStatus.UNAUTHORIZED));
        }
        //User user = userService.getUserById(customUserDetails.getUser().getId());
        // user.output();
        return ResponseEntity.ok(ApiUtils.success(customUserDetails.getUser().getId()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> tokenRefresh(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest req){
        if (customUserDetails.getUser() == null){
            return ResponseEntity.ok(ApiUtils.error("현재 로그인된 user가 없습니다.", HttpStatus.UNAUTHORIZED));
        }
        userService.refresh(customUserDetails.getUser().getId(), req.getSession());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("/send_userid")
    public ResponseEntity<ApiUtils.ApiResult<Long>> getCurrnetUserId(HttpServletRequest req){
        return ResponseEntity.ok(ApiUtils.success(userService.getCurrnetUserId(req.getSession())));
    }

}
