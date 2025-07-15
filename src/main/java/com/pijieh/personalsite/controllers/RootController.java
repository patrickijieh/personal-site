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

@Controller
@RequestMapping("/")
public class RootController {
    private static final Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    ResourceFinder rsFinder;

    @GetMapping("")
    public String index() {
        return "/html/index.html";
    }

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
