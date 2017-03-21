package pl.com.bottega.dms.model.document;

public interface DocumentRepository {
    void put(Document document);

    Document get(DocumentNumber nr);
}
