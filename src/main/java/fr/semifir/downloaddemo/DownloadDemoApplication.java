package fr.semifir.downloaddemo;

import fr.semifir.downloaddemo.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
// On active les propriété custom sur spring
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class DownloadDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DownloadDemoApplication.class, args);
    }

}
