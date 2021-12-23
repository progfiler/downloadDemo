package fr.semifir.downloaddemo;


import fr.semifir.downloaddemo.entities.File;
import fr.semifir.downloaddemo.exception.FileStorageException;
import fr.semifir.downloaddemo.exception.MyFileNotFoundException;
import fr.semifir.downloaddemo.payload.UploadFileResponse;
import fr.semifir.downloaddemo.property.FileStorageProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

    // On défini le path
    private final Path fileStorageLocation;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Permet de sauvegarder un fichier sur le serveur
     * @param file
     * @return
     */
    public UploadFileResponse storeFile(MultipartFile file) {
        // Je normalise le nom du fichier
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            UploadFileResponse response = this.getUploadFileResponse(file, fileName);
            this.save(response);
            return response;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Permet de récuperer un fichier sur le serveur
     * @param fileName
     * @return
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    /**
     * Permet de formater le UploadFileResponse
     * @param file
     * @param fileName
     * @return
     */
    private UploadFileResponse getUploadFileResponse(MultipartFile file, String fileName) {
        // On défini L'uri du fichier pour le téléchargement
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    /**
     * Permet de sauvegarder en BDD un fichier
     * @param file
     * @return
     */
    public UploadFileResponse save(UploadFileResponse file) {
        File fileToSave = this.modelMapper.map(file, File.class);
        File fileSaved = this.fileRepository.save(fileToSave);
        return this.modelMapper.map(fileSaved, UploadFileResponse.class);
    }

    /**
     * Permet de retourner la liste des fichiers sauvegardé
     * @return
     */
    public List<UploadFileResponse> findAll() {
        List<UploadFileResponse> uploadFileResponseList = new ArrayList<>();
        this.fileRepository.findAll().forEach(file -> {
            uploadFileResponseList.add(modelMapper.map(file, UploadFileResponse.class));
        });
        return uploadFileResponseList;
    }
}