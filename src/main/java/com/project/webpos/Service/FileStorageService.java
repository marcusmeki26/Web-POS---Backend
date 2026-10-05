package com.project.webpos.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service 
public class FileStorageService {
  private final Path uploadPath;

  public FileStorageService(
    @Value("${app.file.upload-dir}") String uploadDir
  ){
    this.uploadPath = Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize();

    try {
        Files.createDirectories(this.uploadPath);
    } catch (IOException e) {
        throw new RuntimeException("Could not create upload directory", e);
    }
  }

  public String saveFile(MultipartFile file){
    if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();

        String extension = "";

        if (originalFilename != null) {
            String filename = Paths.get(originalFilename)
                    .getFileName()
                    .toString();

            int index = filename.lastIndexOf(".");

            if (index > 0) {
                extension = filename.substring(index);
            }
        }

        String storedFilename = UUID.randomUUID() + extension;

        Path destination = uploadPath.resolve(storedFilename);

        try {
            file.transferTo(destination);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file", e);
        }

        return storedFilename;
  }

  public Path getPath(){
    return uploadPath;
  }
}
