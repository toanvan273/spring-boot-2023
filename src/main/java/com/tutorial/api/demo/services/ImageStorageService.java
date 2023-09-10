package com.tutorial.api.demo.services;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService{
    private final Path storargeFolder = Paths.get("uploads");
    public ImageStorageService(){
        try{
            Files.createDirectories(storargeFolder);
        }catch (IOException exception){
            throw new RuntimeException("Cannot initilize storage", exception);
        }
    }
    private boolean isImageFile(MultipartFile file){
        // let install FileNameUtils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png","jpg","jpeg","bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try{
            if(file.isEmpty()){
                throw new RuntimeException("Fail to store empty file.");
            }
            if(!isImageFile(file)){
                throw new RuntimeException("You can only upload image file");
            }
            float fileSizeInMegabytes = file.getSize()/ 1_000_000.0f;
            if(fileSizeInMegabytes>5.0f){
                throw new RuntimeException("File must be <= 5Mb");
            }
            //File must be renamed
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-","");
            generatedFileName = generatedFileName+"."+fileExtension;
            Path destinationFilePath = this.storargeFolder.resolve(
                    Paths.get(generatedFileName)).normalize().toAbsolutePath();
            if(!destinationFilePath.getParent().equals(this.storargeFolder.toAbsolutePath())){
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFileName;
        }catch (IOException exception){
            throw new RuntimeException("Fail to store file.",exception);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        // list all file from storage
        try{
            return Files.walk(this.storargeFolder,1)
                    .filter(path -> !path.equals(this.storargeFolder) && !path.toString().contains("._"))
                    .map(this.storargeFolder::relativize);
        }catch (IOException e){
            throw new RuntimeException("Failed to load stored files,", e);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storargeFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }else {
                throw new RuntimeException("Could not ead file: "+fileName);
            }
        } catch (IOException e){
            throw  new RuntimeException("Could not read file: "+ fileName, e);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
