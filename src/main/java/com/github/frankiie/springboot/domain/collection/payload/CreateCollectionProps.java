package com.github.frankiie.springboot.domain.collection.payload;

import javax.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class CreateCollectionProps {
    @NotEmpty(message = "")
    private String title;

    private String description;  

    private MultipartFile file;  
    
    public MultipartFile getFile() {
      return this.file;
    }

    public void setFile(MultipartFile file) {
      this.file = file;
    }
}
