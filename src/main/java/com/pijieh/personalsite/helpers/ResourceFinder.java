package com.pijieh.personalsite.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ResourceFinder {
    public byte[] getResourceBytes(String resourceId) throws IOException {
        final String filePath = String.format("src/main/resources/%s", resourceId);
        final byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

        return fileBytes;
    }

    public String getEnvironmentVariable(String key) throws IOException {
        Properties props = new Properties();
        Path envPath = Paths.get(".env");
        InputStream is = Files.newInputStream(envPath);
        props.load(is);

        is.close();
        return props.getProperty(key);
    }
}
