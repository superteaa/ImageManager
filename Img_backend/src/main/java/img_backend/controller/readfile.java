package img_backend.controller;

import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/images")
public class readfile {

    @PostMapping("/read")
    public List<String> readImageFiles(@RequestBody UserRequest userRequest) {
        Path directoryPath = Paths.get(System.getProperty("user.dir"), userRequest.getUsername());
        List<String> imageFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, "*.{jpg,png}")) {
            for (Path entry : stream) {
                imageFiles.add(entry.toString());
            }
        } catch (IOException | DirectoryIteratorException e) {
            System.err.println("Error reading files: " + e.getMessage());
        }
        return imageFiles;
    }

    // 用户请求类
    static class UserRequest {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
