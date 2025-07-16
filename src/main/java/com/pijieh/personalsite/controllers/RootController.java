package com.pijieh.personalsite.controllers;

import com.pijieh.personalsite.helpers.ResourceFinder;
import java.io.IOException;
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

/**
 * The Controller class for the / (root) route.
 *
 * @author patrickijieh
 */
@Controller
@RequestMapping("/")
public class RootController {
    private static final Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    ResourceFinder rsFinder;

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
    public ResponseEntity<byte[]> resume() {
        try {
            final byte[] resumeBytes = rsFinder.getResourceBytes("Ijieh_Patrick.pdf");
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setCacheControl("no-cache");
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
}
