package pl.com.bottega.dms.application;

import java.util.List;

public class DocumentSearchResults {

    private List<DocumentDto> documents;

    public List<DocumentDto> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDto> documents) {
        this.documents = documents;
    }
}
