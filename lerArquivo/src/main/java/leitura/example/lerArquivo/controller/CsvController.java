package leitura.example.lerArquivo.controller;



import leitura.example.lerArquivo.service.CsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class CsvController {

    private final CsvService csvService;
    private static final Logger log = LoggerFactory.getLogger(CsvController.class);

    @Autowired
    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/processarCSV")
    public ResponseEntity<String> processarCSV (@RequestParam("arquivo") MultipartFile arquivo, @RequestParam(name = "delimitador", defaultValue = ",") char delimitador) {
        csvService.processarCSV(arquivo, delimitador);
        return ResponseEntity.status(HttpStatus.OK).body("Arquivo CSV processado com sucesso.");
    }


}
