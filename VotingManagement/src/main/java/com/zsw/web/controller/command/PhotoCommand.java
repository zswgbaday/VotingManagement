package com.zsw.web.controller.command;

import org.springframework.web.multipart.MultipartFile;

public class PhotoCommand {
    
    private MultipartFile photo;

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
