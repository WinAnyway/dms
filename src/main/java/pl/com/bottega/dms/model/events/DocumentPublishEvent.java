package pl.com.bottega.dms.model.events;

import pl.com.bottega.dms.model.DocumentNumber;

public class DocumentPublishEvent {

    private DocumentNumber documentNumber;

    public DocumentPublishEvent(DocumentNumber documentNumber) {
        this.documentNumber = documentNumber;
    }

    public DocumentNumber getDocumentNumber() {
        return documentNumber;
    }

}
