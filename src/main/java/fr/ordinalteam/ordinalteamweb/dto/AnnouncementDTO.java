package fr.ordinalteam.ordinalteamweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class AnnouncementDTO {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 5000)
    private String description;

    @NotNull
    private Long categoryId;

    private MultipartFile[] images;


    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(final Long categoryId) {
        this.categoryId = categoryId;
    }

    public MultipartFile[] getImages() {
        return this.images;
    }
    public void setImages(final MultipartFile[] images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "AnnouncementDTO{" +
                "title='" + this.title + '\'' +
                ", description='" + this.description + '\'' +
                ", categoryId=" + this.categoryId +
                '}';
    }
}
