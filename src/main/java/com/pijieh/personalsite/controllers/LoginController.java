package com.pijieh.personalsite.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.jooq.impl.DSL;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.pijieh.personalsite.database.DBService;
import com.pijieh.personalsite.models.LoginForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    BCryptPasswordEncoder bcrypt;

    @Autowired
    DBService dataSource;

    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final static Gson gson = new Gson();

    @GetMapping("")
    public String loginHtml(HttpSession session, HttpServletRequest request) {
        if (null != session.getAttribute("username"))
            return "redirect:/admin";

        return "/html/login.html";
    }

    @PostMapping("")
    public ResponseEntity<String> login(HttpSession session, HttpServletRequest request,
            @Validated @RequestBody LoginForm loginForm) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String user = loginForm.getUsername();
        String pass = loginForm.getPassword();

        Record adminUser;

        try (Connection conn = dataSource.getConnection()) {
            adminUser = DSL.using(conn, SQLDialect.POSTGRES).resultQuery("SELECT * FROM {0} WHERE {1}={2}",
                    DSL.name("dashboard_users"),
                    DSL.name("username"),
                    DSL.val(user)).fetchOne();
        } catch (SQLException ex) {
            logger.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (null == adminUser) {
            final String body = gson.toJson(Map.of("message", "failure"));
            return new ResponseEntity<>(body, headers, HttpStatus.FORBIDDEN);
        }

        String pwdHash = adminUser.get("password", String.class);
        if (!bcrypt.matches(pass, pwdHash)) {
            final String body = gson.toJson(Map.of("message", "failure"));
            return new ResponseEntity<>(body, headers, HttpStatus.FORBIDDEN);
        }

        session.setAttribute("username", user);
        String body = gson.toJson(Map.of("message", "success", "location", "admin"));
        logger.info("User logged into admin dashboard with addr {}", request.getRemoteAddr());

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
