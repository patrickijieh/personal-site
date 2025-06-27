package com.pijieh.personalsite.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pijieh.personalsite.database.DBService;
import com.pijieh.personalsite.helpers.OffsetDateTimeAdapter;
import com.pijieh.personalsite.models.BlogPost;
import com.pijieh.personalsite.models.PostIndex;

@Controller
@RequestMapping("/blog")
public class BlogController {
    private final static Logger logger = LoggerFactory.getLogger(BlogController.class);
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter().nullSafe()).create();

    @Autowired
    DBService dataSource;

    @GetMapping(value = { "", "/{id}" })
    public String blog(@PathVariable Optional<String> id) {
        if (id.isEmpty())
            return "/html/blog.html";

        return "/html/post.html";
    }

    @GetMapping("/posts")
    public ResponseEntity<String> getBlogPosts() {
        List<BlogPost> posts;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try (Connection conn = dataSource.getConnection()) {
            posts = DSL.using(conn, SQLDialect.POSTGRES)
                    .resultQuery("SELECT {0}, {1}, {2}, {3} FROM {4} ORDER BY ({0}, {3}) DESC LIMIT 5",
                            DSL.name("id"),
                            DSL.name("title"),
                            DSL.name("created_by"),
                            DSL.name("created_at"),
                            DSL.name("posts"))
                    .fetchStream().map(this::buildPostWithoutBody).toList();

            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (SQLException ex) {
            logger.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final String body = gson.toJson(Map.of("posts", posts));
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @PostMapping("/posts/newpage")
    public ResponseEntity<String> getBlogPostFromIdx(@Validated @RequestBody PostIndex postIdx) {
        List<BlogPost> posts;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try (Connection conn = dataSource.getConnection()) {
            posts = DSL.using(conn, SQLDialect.POSTGRES)
                    .resultQuery(
                            "SELECT {0}, {1}, {2}, {3} FROM {4} WHERE ({0}, {3}) < ({5}, {6}) ORDER BY ({0}, {3}) DESC LIMIT 5",
                            DSL.name("id"),
                            DSL.name("title"),
                            DSL.name("created_by"),
                            DSL.name("created_at"),
                            DSL.name("posts"),
                            DSL.val(postIdx.getId()),
                            DSL.val(postIdx.getCreatedAt()))
                    .fetchStream().map(this::buildPostWithoutBody).toList();

            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (SQLException ex) {
            logger.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final String body = gson.toJson(Map.of("posts", posts));
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<String> getBlogPostWithId(@PathVariable int id) {
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

        BlogPost blogPost = BlogPost.builder()
                .id(post.get("id", Integer.class))
                .title(post.get("title", String.class))
                .postBody(post.get("post_body", String.class))
                .createdBy(post.get("created_by", String.class))
                .createdAt(post.get("created_at", OffsetDateTime.class))
                .build();

        final String body = gson.toJson(blogPost);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    private BlogPost buildPostWithoutBody(Record record) {
        Integer id = record.get("id", Integer.class);
        String title = record.get("title", String.class);
        OffsetDateTime createdAt = record.get("created_at", OffsetDateTime.class);
        String createdBy = record.get("created_by", String.class);
        return BlogPost.builder()
                .id(id)
                .title(title)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .build();
    }
}
