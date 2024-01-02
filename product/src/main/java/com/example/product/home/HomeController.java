package com.example.product.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("access_token") != null)
            return "logout";
        return "index";
    }
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }
}
