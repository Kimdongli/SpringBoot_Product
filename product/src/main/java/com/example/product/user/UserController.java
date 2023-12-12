package com.example.product.user;

import com.example.product.core.error.exception.Exception400;
import com.example.product.core.error.exception.Exception401;
import com.example.product.core.security.CustomUserDetails;
import com.example.product.core.security.JwtTokenProvider;
import com.example.product.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/join")
    public ModelAndView joinPage() {
        return new ModelAndView("join");
    }

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Errors errors){
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
    public ResponseEntity login(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Error error){
        if (error != null) {
            // handle error
        }

        String jwt = userService.login(requestDTO);

        return ResponseEntity.ok().header(JwtTokenProvider.HEADER, jwt)
                .body(ApiUtils.success(null));
    }
}
