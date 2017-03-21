package pl.com.bottega.dms.application;

import pl.com.bottega.dms.model.document.DocumentNumber;

public interface DocumentCatalog {

    DocumentSearchResults find(DocumentQuery documentQuery);

    DocumentDto get(DocumentNumber documentNumber);

}
