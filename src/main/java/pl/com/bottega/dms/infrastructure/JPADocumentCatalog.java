package pl.com.bottega.dms.infrastructure;

import pl.com.bottega.dms.application.*;
import pl.com.bottega.dms.model.Confirmation;
import pl.com.bottega.dms.model.Document;
import pl.com.bottega.dms.model.DocumentNumber;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

public class JPADocumentCatalog implements DocumentCatalog {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DocumentSearchResults find(DocumentQuery documentQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        Root<Document> root = criteriaQuery.from(Document.class);
        String likeExpression = "%" + documentQuery.getPhrase() + "%";
        root.fetch("confirmations", JoinType.LEFT);
        criteriaQuery.where(criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), likeExpression),
                criteriaBuilder.like(root.get("content"), likeExpression),
                criteriaBuilder.like(root.get("number").get("number"), likeExpression)));
        Query query = entityManager.createQuery(criteriaQuery);
        List<Document> documents = query.getResultList();
        DocumentSearchResults results = new DocumentSearchResults();
        List<DocumentDto> dtos = new LinkedList<>();
        for(Document document : documents) {
            dtos.add(createDocumentDto(document));
        }
        results.setDocuments(dtos);
        return results;
    }

    @Override
    public DocumentDto get(DocumentNumber documentNumber) {
        Query query = entityManager.createQuery("FROM Document d LEFT JOIN FETCH d.confirmations WHERE d.number = :nr");
        query.setParameter("nr", documentNumber);
        Document document = (Document) query.getResultList().get(0);
        DocumentDto documentDto = createDocumentDto(document);
        return documentDto;
    }

    private DocumentDto createDocumentDto(Document document) {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setNumber(document.getNumber().getNumber());
        documentDto.setTitle(document.getTitle());
        documentDto.setContent(document.getContent());
        documentDto.setStatus(document.getStatus().name());
        List<ConfirmationDto> confirmationDtos = new LinkedList<>();
        for(Confirmation confirmation : document.getConfirmations()) {
            ConfirmationDto dto = createConfirmationDto(confirmation);
            confirmationDtos.add(dto);
        }
        documentDto.setConfirmations(confirmationDtos);
        return documentDto;
    }

    private ConfirmationDto createConfirmationDto(Confirmation confirmation) {
        ConfirmationDto dto = new ConfirmationDto();
        dto.setConfirmed(confirmation.isConfirmed());
        dto.setConfirmedAt(confirmation.getConfirmationDate());
        dto.setOwnerEmployeeId(confirmation.getOwner().getId());
        if(confirmation.hasProxy())
            dto.setProxyEmployeeId(confirmation.getProxy().getId());
        return dto;
    }
}
