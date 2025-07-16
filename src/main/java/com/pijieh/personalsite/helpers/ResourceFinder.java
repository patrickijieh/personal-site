package com.pijieh.personalsite.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * File IO helper class.
 *
 * @author patrickijieh
 */
public class ResourceFinder {
    /**
     * Gets the resource given an identifier.
     *
     * @param resourceId the resource Id
     * @return byte array of the contents of the file
     * @throws IOException - if the resource cannot be read
     */
    public byte[] getResourceBytes(String resourceId) throws IOException {
        final String filePath = String.format("src/main/resources/%s", resourceId);
        final byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

        return fileBytes;
    }

    /**
     * Gets an environment variable given a key.
     *
     * @param key the resource Id
     * @return value associated with the key
     * @throws IOException - if the environment variables cannot be read
     */
    public String getEnvironmentVariable(String key) throws IOException {
        Properties props = new Properties();
        Path envPath = Paths.get(".env");
        InputStream is = Files.newInputStream(envPath);
        props.load(is);

        is.close();
        return props.getProperty(key);
    }
}
