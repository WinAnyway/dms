package pl.com.bottega.dms.infrastructure;

import pl.com.bottega.dms.application.*;
import pl.com.bottega.dms.model.Confirmation;
import pl.com.bottega.dms.model.Document;
import pl.com.bottega.dms.model.DocumentNumber;
import pl.com.bottega.dms.model.EmployeeId;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;

public class JPADocumentCatalog implements DocumentCatalog {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public DocumentSearchResults find(DocumentQuery documentQuery) {
        return null;
    }

    @Override
    public DocumentDto get(DocumentNumber documentNumber) {
        Document document = entityManager.find(Document.class, documentNumber);
        DocumentDto documentDto = new DocumentDto();
        documentDto.setNumber(document.getNumber().getNumber());
        documentDto.setTitle(document.getTitle());
        documentDto.setStatus(document.getStatus().toString());
        documentDto.setConfirmations(changeConfirmationsToDtos(document.getConfirmations()));
        return documentDto;
    }

    private Set<ConfirmationDto> changeConfirmationsToDtos(Set<Confirmation> confirmations) {
        Set<ConfirmationDto> dtos = new HashSet<>();
        for (Confirmation confirmation : confirmations) {
            ConfirmationDto dto = new ConfirmationDto();
            dto.setOwner(confirmation.getOwner().getId());
            EmployeeId proxy = confirmation.getProxy();
            if (proxy != null)
                dto.setProxy(proxy.getId());
            dto.setConfirmationDate(confirmation.getConfirmationDate());
            dtos.add(dto);
        }
        return dtos;
    }
}
