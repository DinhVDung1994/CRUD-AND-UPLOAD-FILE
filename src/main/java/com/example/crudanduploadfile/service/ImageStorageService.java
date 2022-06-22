package com.example.crudanduploadfile.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService{
    private final Path storageFolder = Paths.get("uploads");
    //constructor
    // Create folder save file image / Tạo folder để lưu trữ file ảnh khi tải lên
    public ImageStorageService(){
        try{
            Files.createDirectories(storageFolder);
        }catch (IOException exception){
            throw new RuntimeException("Cannot initialize storeage",exception);
        }
    }
    // Check if it's an image file / Kiểm tra file đưa lên có là file ảnh với đuôi được xác định
    private boolean isImageFile(MultipartFile file){
        String fileExtension = FilenameUtils.getExtension((file.getOriginalFilename()));
        return Arrays.asList(new String[] {"png","jpg","jpeg","bmp"}).contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try{
            if (file.isEmpty()){
                throw new RuntimeException("Failed to store empty file.");
            }
            if (!isImageFile(file)){
                throw new RuntimeException("You can only upload file image?");
            }
            float fileZiseInMegabytes = file.getSize()/1_000_000.0f;
            if (fileZiseInMegabytes >5.0f){
                throw new RuntimeException("File must be <=5Mb");
            }
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-","");
            generatedFileName = generatedFileName+"."+fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())){
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFileName;
        }catch (Exception exception){
            throw new RuntimeException("Failed to store file.",exception);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            // list all files in storageFolder
            return Files.walk(this.storageFolder,1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);
        }catch (Exception exception){
            throw new RuntimeException("Failed to load stored files", exception);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()){
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }else {
                throw new RuntimeException("Could not read file: "+fileName);
            }
        }catch (Exception exception){
            throw new RuntimeException("Could not read file: "+fileName,exception);
        }
    }

    @Override
    public void deleteAllFiles(String fileName) {
        try{
            File file = new File("C:\\Users\\dvdung3\\IdeaProjects\\CRUDAndUploadFile\\uploads\\"+fileName);
            if (file.exists()){
                file.delete();
                System.out.println("Delete file successfully!");
            }else {
                System.out.println("file dose not exist!");
            }
        }catch (Exception exception){
            throw new RuntimeException("Cannot delete file/file does not exist!",exception);
        }
    }
}
