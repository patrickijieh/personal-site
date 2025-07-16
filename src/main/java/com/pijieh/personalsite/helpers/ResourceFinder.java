package com.pijieh.personalsite.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
}
