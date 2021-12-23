package fr.semifir.downloaddemo;

import fr.semifir.downloaddemo.entities.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File, String> {
}
