package com.pijieh.personalsite.controllers;

import com.google.gson.Gson;
import com.pijieh.personalsite.database.DatabaseService;
import com.pijieh.personalsite.models.BlogForm;
import com.pijieh.personalsite.models.LoginForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Map;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
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

// Authenticated through AuthenticationInterceptor.java
/**
 * The Controller class for the /admin route.
 *
 * @author patrickijieh
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    BCryptPasswordEncoder bcrypt;

    @Autowired
    DatabaseService dataSource;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final Gson gson = new Gson();

    /**
     * GET /admin mapping.
     *
     * @param request full http request
     * @return the admin dashboard html page
     */
    @GetMapping("")
    public String admin(HttpServletRequest request) {
        return "/html/admin.html";
    }

    /**
     * GET /admin/create-post mapping.
     *
     * @return the create post html page
     */
    @GetMapping("/create-post")
    public String createPostHtml() {
        return "/html/create_post.html";
    }

    /**
     * GET /admin/create-user mapping.
     *
     * @return the create user html page
     */
    @GetMapping("/create-user")
    public String createUserHtml() {
        return "/html/create_user.html";
    }

    /**
     * GET /admin/logout mapping.
     *
     * @return redirects to / in RootController and invalidates user session
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        String user = session.getAttribute("username").toString();
        session.invalidate();
        logger.info("{} manual logout", user);
        return "redirect:/";
    }

    /**
     * POST /admin/posts mapping.
     *
     * @param session  user session
     * @param blogForm blog post information from user input
     * @return a JSON object of the form {"message": "success", "id":
     *         id}, or a 500 response if
     *         the route fails
     */
    @PostMapping("/posts")
    public ResponseEntity<String> createPost(HttpSession session,
            @Validated @RequestBody BlogForm blogForm) {
        String id = "-1";
        final HttpHeaders headers = new HttpHeaders();
        final OffsetDateTime postTime = OffsetDateTime.now();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final String postTitle = blogForm.getPostTitle();
        final String postBody = blogForm.getPostBody();
        final String created_by = session.getAttribute("username").toString();

        try (Connection conn = dataSource.getConnection()) {
            int count = DSL.using(conn, SQLDialect.POSTGRES).resultQuery("""
                    INSERT INTO {0}
                    ({1}, {2}, {3}, {4})
                    VALUES
                    ({5}, {6}, {7}, {8})
                    """,
                    DSL.name("posts"),
                    DSL.name("title"),
                    DSL.name("post_body"),
                    DSL.name("created_at"),
                    DSL.name("created_by"),
                    DSL.val(postTitle),
                    DSL.val(postBody),
                    DSL.val(postTime),
                    DSL.val(created_by)).execute();

            if (count < 1) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Record post = DSL.using(conn, SQLDialect.POSTGRES)
                    .resultQuery("SELECT id FROM {0} WHERE {1}={2} AND {3}={4} AND {5}={6}",
                            DSL.name("posts"),
                            DSL.name("title"),
                            DSL.val(postTitle),
                            DSL.name("created_by"),
                            DSL.val(created_by),
                            DSL.name("created_at"),
                            DSL.val(postTime))
                    .fetchOne();

            if (null != post) {
                id = post.get("id", String.class);
            }
        } catch (SQLException ex) {
            logger.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("post with id {} created by {} at time {}", id, created_by, postTime);
        final String body = gson.toJson(Map.of("message", "success", "id", id));
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    /**
     * POST /admin/users mapping.
     *
     * @param userForm user information from user input
     * @return a JSON object of the form {"message": "success"}, or a 500 response
     *         if
     *         the route fails
     */
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@Validated @RequestBody LoginForm userForm) {
        String username = userForm.getUsername();
        String password = bcrypt.encode(userForm.getPassword());

        int count = -1;
        try (Connection conn = dataSource.getConnection()) {
            count = DSL.using(conn, SQLDialect.POSTGRES).resultQuery("""
                    INSERT INTO {0}
                    ({1}, {2})
                    VALUES
                    ({3}, {4})
                    """,
                    DSL.name("dashboard_users"),
                    DSL.name("username"),
                    DSL.name("password"),
                    DSL.val(username),
                    DSL.val(password)).execute();
        } catch (SQLException ex) {
            logger.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (count < 1) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("user created with username {}", username);
        final String body = gson.toJson(Map.of("message", "success"));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
