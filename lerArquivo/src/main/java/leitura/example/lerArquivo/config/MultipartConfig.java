package leitura.example.lerArquivo.config;

import org.apache.commons.fileupload.FileUploadException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class MultipartConfig {

    @Bean
    public CommonsMultipartResolver multipartResolver() throws FileUploadException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(Long.MAX_VALUE);
        resolver.setMaxUploadSizePerFile(Long.MAX_VALUE);
        return resolver;
    }
}
