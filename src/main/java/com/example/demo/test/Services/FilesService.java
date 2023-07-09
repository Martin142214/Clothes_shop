package com.example.demo.test.Services;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.test.FileUploadUtils;

@Service
public class FilesService {

    @Autowired
    public FilesService() { }

    public void uploadImage(MultipartFile imageUrl, String imageName){
        //String uploadDir = "E:/Java IntelliJ projects/demo-TEST-2/src/main/resources/static/images/";
        Path rootPath = FileSystems.getDefault().getPath("").toAbsolutePath();
        Path filePath = Paths.get(rootPath.toString(), "src", "main", "resources", "static", "images");
        try {
            FileUploadUtils.saveFile(filePath.toString(), imageName, imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}