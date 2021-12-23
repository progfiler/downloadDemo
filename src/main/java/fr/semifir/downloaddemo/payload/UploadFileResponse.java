package fr.semifir.downloaddemo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// On customise le payload JSON (Sorte de DTO)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}