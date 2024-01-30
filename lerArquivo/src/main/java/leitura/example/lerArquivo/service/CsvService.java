package leitura.example.lerArquivo.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import leitura.example.lerArquivo.controller.CsvController;
import leitura.example.lerArquivo.exceptions.FormatoCSVInvalidoException;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

@Service
public class CsvService {
    private static final Logger log = LoggerFactory.getLogger(CsvController.class);

    public void processarLinhas(List<String[]> linhas) throws FormatoCSVInvalidoException {
        linhas.stream()
                .map(linha -> String.join(", ", linha))
                .forEach(System.out::println);
    }

    public ResponseEntity<String> processarCSV(@RequestParam("arquivo") MultipartFile arquivo,
                                               @RequestParam(name = "delimitador", defaultValue = ",") char delimitador) {
        if (arquivo == null || arquivo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum arquivo enviado");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(detectarInputStream(arquivo.getInputStream()), detectarCharset(arquivo)))) {
            List<String[]> dados = readCsv(br, delimitador);

            processarLinhas(dados);

            dados.forEach(linha -> {
                String linhaString = String.join(",", linha);
                System.out.println(linhaString);
            });

            return ResponseEntity.ok("CSV processado com sucesso");
        } catch (IOException e) {
            log.error("Erro ao ler o arquivo CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o CSV: " + e.getMessage());
        } catch (FormatoCSVInvalidoException e) {
            log.error("Formato de CSV inválido", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de CSV inválido: " + e.getMessage());
        } catch (CsvException e) {
            log.error("Erro ao processar o CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o CSV: " + e.getMessage());
        }
    }

    private InputStream detectarInputStream(InputStream inputStream) throws IOException {
        return new BOMInputStream(inputStream, false);
    }

    private Charset detectarCharset(MultipartFile arquivo) {
        return Charset.defaultCharset();
    }

    private List<String[]> readCsv(BufferedReader br, char delimitador) throws IOException, CsvException {
        CSVParser parser = new CSVParserBuilder().withSeparator(delimitador).build();
        return new CSVReaderBuilder(br)
                .withCSVParser(parser)
                .build()
                .readAll();
    }
}
