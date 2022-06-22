package com.example.crudanduploadfile.controller;

import com.example.crudanduploadfile.model.ReponseObject;
import com.example.crudanduploadfile.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/FileUpload")
public class FileUploadController {
    // Inject Storage Service here
    @Autowired
    private IStorageService storageService;

    @PostMapping("")
    public ResponseEntity<ReponseObject> uploadFile(@RequestParam("file")MultipartFile file){
        try{
            // Save files to a folder ==> use a service
            String generatedFileName = storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ReponseObject("Oke","Upload file successfully!",generatedFileName)
            );
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ReponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    //get image's url
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName){
        try{
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        }catch (Exception exception){
            return ResponseEntity.noContent().build();
        }
    }

    // How to load all uploaded files
    @GetMapping("")
    public ResponseEntity<ReponseObject> getUploadedFiles(){
        try {
            List<String> urls = storageService.loadAll().map(path -> {
                // convert fileName to url(send request "readDetailFile")
                String urlPath = MvcUriComponentsBuilder.
                        fromMethodName(FileUploadController.class,"readDetailFile",
                                path.getFileName().toString()).build().toUri().toString();
                return urlPath;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(new ReponseObject("OKe","List files  successfully!",urls));
        }catch (Exception exception){
            return ResponseEntity.ok(new ReponseObject("Failed","List files failed",new String[] {}));
        }
    }
}
