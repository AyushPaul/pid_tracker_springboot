package com.ayush.pidtracker.service;

import com.ayush.pidtracker.entity.ImageData;
import com.ayush.pidtracker.repository.StorageRepository;
import com.ayush.pidtracker.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;

    public String uploadImage(MultipartFile file, String comment , String pass, String Id) throws IOException, ParseException {

        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .comment(comment)
                .user_id(Id)
                .reviewed(false)
                .creationDate(new Date(System.currentTimeMillis()))
                .imageData(ImageUtils.compress(file.getBytes())).build());
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename() + " by User Id : " + Id;
        }
        return null;
    }

    public String uploadImage2(MultipartFile file, String comment , String reviewer, String user, Boolean status) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis())); // Using today's date
        c.add(Calendar.DATE, 5); // Adding 5 days
        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .comment(comment)
                .user_id(user)
                .reviewer_id(reviewer)
                .reviewed(status)
                .creationDate(c.getTime())
                .imageData(ImageUtils.compress(file.getBytes())).build());
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename() + " by User Id : " + user;
        }
        return null;
    }

    public byte[] downloadImage(String fileName) throws DataFormatException, IOException {
        Optional<ImageData> dbImageData = repository.findByName(fileName);

        byte[] images= ImageUtils.decompress(dbImageData.get().getImageData());
        return images;
    }

    public List<ImageData> getAllFiles(){
        List<ImageData> files = repository.findAll();
        return files;
    }

    public List<ImageData> findFilesForDev(String name , Boolean status){
        return repository.findFilesForDev(name,status);
    }

    public List<ImageData> findFilesForRev(String name , Boolean status){
        return repository.findFilesForRev(name,status);
    }

    public Optional<ImageData>findFileByName(String name){
        return repository.findByName(name);
    }

    public int deleteFileByName(String name){
        return repository.deleteFileByName(name);
    }

    public List<ImageData> findFilesByStatus(Boolean status){
        List<ImageData> files = repository.findFilesByStatus(status);
        return files;
    }
}

