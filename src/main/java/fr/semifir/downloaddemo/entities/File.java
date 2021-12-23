package fr.semifir.downloaddemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class File {
    private String id;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
