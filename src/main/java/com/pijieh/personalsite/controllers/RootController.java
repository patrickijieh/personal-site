package com.pijieh.personalsite.controllers;

import com.google.gson.Gson;
import com.pijieh.personalsite.helpers.ResourceFinder;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.RedisClient;
import redis.clients.jedis.params.SetParams;

/**
 * The Controller class for the / (root) route.
 *
 * @author patrickijieh
 */
@Controller
@RequestMapping("/")
public class RootController {
    private static final Logger logger = LoggerFactory.getLogger(RootController.class);
    private static final Logger analyticsLogger = LoggerFactory.getLogger("ANALYTICS");
    private static final Gson gson = new Gson();
    @Autowired
    ResourceFinder rsFinder;

    @Autowired
    Git gitRepository;

    @Autowired
    RedisClient redisClient;

    /**
     * GET / mapping.
     *
     * @return the index html page
     */
    @GetMapping("")
    public String index() {
        return "/html/index.html";
    }

    /**
     * GET /resume mapping.
     *
     * @return the bytes of the resume file, or 500 response if the route fails
     */
    @GetMapping("/resume")
    public ResponseEntity<byte[]> resume(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (remoteAddr == null || remoteAddr.isEmpty()) {
            remoteAddr = request.getRemoteAddr();
        }
        try {
            final byte[] resumeBytes = rsFinder.getResourceBytes("Ijieh_Patrick.pdf");
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setCacheControl("no-cache");

            analyticsLogger.info("IP {} requested CV", remoteAddr);
            return new ResponseEntity<>(resumeBytes, headers, HttpStatus.OK);
        } catch (IOException ex) {
            logger.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /icon mapping.
     *
     * @return the bytes of the icon png file, or 500 response if the route fails
     */
    @GetMapping("/icon")
    public ResponseEntity<byte[]> icon() {
        try {
            final byte[] iconBytes = rsFinder.getResourceBytes("icon.png");
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setCacheControl("no-cache");
            return new ResponseEntity<>(iconBytes, headers, HttpStatus.OK);
        } catch (IOException ex) {
            logger.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /last-updated mapping.
     *
     * @return the date the local git repository was last updated
     */
    @GetMapping("/last-updated")
    public ResponseEntity<String> lastUpdated() throws GitAPIException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (redisClient.exists("last-updated")) {
            String date = redisClient.get("last-updated");
            String body = gson.toJson(Map.of("last_updated", date));
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }

        RevCommit commit = gitRepository.log().setMaxCount(1).call().iterator().next();
        PersonIdent author = commit.getAuthorIdent();
        LocalDateTime datetime = LocalDateTime.ofInstant(
                author.getWhenAsInstant(), author.getZoneId());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        String date = datetime.format(formatter);
        redisClient.set("last-updated", date, new SetParams().ex(3600));
        String body = gson.toJson(Map.of("last_updated", date));
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    /**
     * GET /raspberrypi mapping.
     *
     * @return the bytes of the raspberry pi png, or 500 status if it fails
     */
    @GetMapping("/raspberrypi")
    public ResponseEntity<byte[]> pi() throws GitAPIException {
        try {
            final byte[] iconBytes = rsFinder.getResourceBytes("raspberry_pi.png");
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setCacheControl("no-cache");
            return new ResponseEntity<>(iconBytes, headers, HttpStatus.OK);
        } catch (IOException ex) {
            logger.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
