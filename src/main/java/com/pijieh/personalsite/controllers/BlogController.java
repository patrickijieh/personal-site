package com.pijieh.personalsite.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pijieh.personalsite.database.DBService;
import com.pijieh.personalsite.helpers.OffsetDateTimeAdapter;
import com.pijieh.personalsite.models.Blog;
import com.pijieh.personalsite.models.BlogForm;

import jakarta.servlet.http.HttpSession;

@Controller
public class BlogController {
    private final static Logger logger = LoggerFactory.getLogger(BlogController.class);
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter().nullSafe()).create();

    @Autowired
    DBService dataSource;

    @GetMapping("/blog")
    public String blog(@RequestParam Optional<String> id) {
        if (id.isEmpty())
            return "html/blog.html";

        return "html/post.html";
    }

    @PostMapping("/blog")
    public ResponseEntity<String> createBlog(HttpSession session, @Validated @RequestBody BlogForm blogForm) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String postId = "1";
        if (null != session.getAttribute("username")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        logger.info("Created new blog post with id: {}", postId);
        final String body = gson.toJson(Map.of("message", "success", "id", postId));
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<String> getBlogPostWithId(@PathVariable("id") int id) {
        Record post;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try (Connection conn = dataSource.getConnection()) {
            post = DSL.using(conn, SQLDialect.POSTGRES).resultQuery("SELECT * FROM {0} WHERE {1}={2}",
                    DSL.name("posts"),
                    DSL.name("id"),
                    DSL.val(id)).fetchOne();

            if (null == post) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (SQLException ex) {
            logger.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Blog blogPost = Blog.builder()
                .id(post.get("id", Integer.class))
                .title(post.get("title", String.class))
                .postBody(post.get("post_body", String.class))
                .createdBy(post.get("created_by", String.class))
                .createdAt(post.get("created_at", OffsetDateTime.class))
                .build();

        final String body = gson.toJson(blogPost);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
