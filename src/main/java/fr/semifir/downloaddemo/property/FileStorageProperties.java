package fr.semifir.downloaddemo.property;


import org.springframework.boot.context.properties.ConfigurationProperties;

// On définis une propriété custom et on indique à spring ou se trouve le dossier des fichiers téléchargé
@ConfigurationProperties("file")
public class FileStorageProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}