package pl.com.bottega.dms.model.validation;

import pl.com.bottega.dms.model.document.Document;
import pl.com.bottega.dms.model.document.DocumentStatus;

public abstract class DocumentValidator {

    protected DocumentValidator next;

    public void setNext(DocumentValidator next) {
        this.next = next;
    }

    public abstract boolean isValid(Document document, DocumentStatus targetStatus);

}
