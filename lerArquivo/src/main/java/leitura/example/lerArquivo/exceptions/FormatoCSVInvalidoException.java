package leitura.example.lerArquivo.exceptions;

public class FormatoCSVInvalidoException extends Throwable {
    public FormatoCSVInvalidoException(String s) {
        super("O CSV deve ter exatamente três colunas");
    }
}
