package pl.com.bottega.dms.model.validation;

import pl.com.bottega.dms.model.document.Document;
import pl.com.bottega.dms.model.document.DocumentStatus;

public class VerifiedAuthorValidator extends DocumentValidator {

    @Override
    public boolean isValid(Document document, DocumentStatus targetStatus) {
        if (targetStatus.equals(DocumentStatus.VERIFIED) && document.getCreatorId() == null)
            return false;
        else
            return next.isValid(document, targetStatus);
    }
}
