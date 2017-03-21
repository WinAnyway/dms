package pl.com.bottega.dms.model.validation;

import pl.com.bottega.dms.model.document.Document;
import pl.com.bottega.dms.model.document.DocumentStatus;

public class ExpiresAtValidator extends DocumentValidator {

    private DocumentStatus targetStatus;

    public ExpiresAtValidator(DocumentStatus targetStatus) {

        this.targetStatus = targetStatus;
    }

    @Override
    public boolean isValid(Document document, DocumentStatus targetStatus) {
        if (targetStatus.equals(this.targetStatus) && document.getExpiresAt() == null)
            return false;
        else
            return next.isValid(document, targetStatus);
    }
}
