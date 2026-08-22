package dev.blog.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

        @Email(message = "Invalid email")
        private String email;

        private String name;

        private String photo;

        private String bio;

        @JsonIgnore
        private boolean emailProvided;

        @JsonIgnore
        private boolean nameProvided;

        @JsonIgnore
        private boolean photoProvided;

        @JsonIgnore
        private boolean bioProvided;

        @JsonSetter("email")
        public void setEmail(String email) {
                this.emailProvided = true;
                this.email = email;
        }

        @JsonSetter("name")
        public void setName(String name) {
                this.nameProvided = true;
                this.name = name;
        }

        @JsonSetter("photo")
        public void setPhoto(String photo) {
                this.photoProvided = true;
                this.photo = photo;
        }

        @JsonSetter("bio")
        public void setBio(String bio) {
                this.bioProvided = true;
                this.bio = bio;
        }

        @JsonIgnore
        public boolean isEmailProvided() {
                return emailProvided;
        }

        @JsonIgnore
        public boolean isNameProvided() {
                return nameProvided;
        }

        @JsonIgnore
        public boolean isPhotoProvided() {
                return photoProvided;
        }

        @JsonIgnore
        public boolean isBioProvided() {
                return bioProvided;
        }
}