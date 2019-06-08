package com.zsw.web.controller.command;

import org.springframework.web.multipart.MultipartFile;

public class UpLoadVoteImageCommand {
    
    private MultipartFile photo;
    private  String id;

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
