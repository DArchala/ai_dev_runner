package org.example.ai_dev_runner.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.nio.file.Files;

public class FileUtil {

    private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    public static <T> T getFileContentAsObject(String filename, Class<T> clazz) {
        return MAPPER.readValue(getResourceContent(filename), clazz);
    }

    @SneakyThrows
    private static String getResourceContent(String fileName) {
        var resourcePath = RESOURCE_LOADER.getResource(fileName)
                                          .getFile()
                                          .toPath();
        var bytesContent = Files.readAllBytes(resourcePath);
        return new String(bytesContent);
    }

}
