package com.pijieh.personalsite.controllers;

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

import com.pijieh.personalsite.helpers.ResourceFinder;

@Controller
public class RootController {
    private final static Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    ResourceFinder rsFinder;

    @GetMapping("/")
    public String index() {
        return "html/index.html";
    }

    @GetMapping("/resume")
    public ResponseEntity<byte[]> resume() throws IOException {
        final byte[] resumeBytes = rsFinder.getResourceBytes("Ijieh_Patrick.pdf");
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setCacheControl("no-cache");
        return new ResponseEntity<>(resumeBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/icon")
    public ResponseEntity<byte[]> icon() throws IOException {
        final byte[] iconBytes = rsFinder.getResourceBytes("icon.png");
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setCacheControl("no-cache");
        return new ResponseEntity<>(iconBytes, headers, HttpStatus.OK);
    }
}
