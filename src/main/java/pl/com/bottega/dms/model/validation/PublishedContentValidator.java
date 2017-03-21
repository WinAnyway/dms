package pl.com.bottega.dms.model.validation;

import pl.com.bottega.dms.model.document.Document;
import pl.com.bottega.dms.model.document.DocumentStatus;

public class PublishedContentValidator extends DocumentValidator {

    @Override
    public boolean isValid(Document document, DocumentStatus targetStatus) {
        if (targetStatus.equals(DocumentStatus.PUBLISHED) && (document.getContent() == null || document.getContent().isEmpty()))
            return false;
        else
            return next.isValid(document, targetStatus);
    }
}
