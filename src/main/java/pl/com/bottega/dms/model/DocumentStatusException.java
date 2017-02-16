package pl.com.bottega.dms.model;

public class DocumentStatusException extends RuntimeException {

    public DocumentStatusException() {
        super("Wrong DocumentStatus!");
    }
}
