package pl.com.bottega.dms.infrastructure;

import com.sun.deploy.util.StringUtils;
import org.springframework.stereotype.Component;
import pl.com.bottega.dms.application.*;
import pl.com.bottega.dms.model.Confirmation;
import pl.com.bottega.dms.model.Document;
import pl.com.bottega.dms.model.DocumentNumber;
import pl.com.bottega.dms.model.DocumentStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class JPQLDocumentCatalog implements DocumentCatalog{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DocumentSearchResults find(DocumentQuery documentQuery) {
        DocumentSearchResults results = new DocumentSearchResults();

        String jpqlQuery = "SELECT d FROM Document d LEFT JOIN FETCH d.confirmations ";
        String countJpqlQuery = "SELECT COUNT(d) FROM Document d ";
        Set<String> predicates = createPredicates(documentQuery);

        if(!predicates.isEmpty()) {
            String where = "WHERE " + StringUtils.join(predicates, " AND ");
            jpqlQuery += where;
            countJpqlQuery += where;
        }

        Query countQuery = entityManager.createQuery(countJpqlQuery);
        Query query = entityManager.createQuery(jpqlQuery);
        applyPredicateParameters(query, documentQuery);
        applyPredicateParameters(countQuery, documentQuery);
        query.setFirstResult(getFirstResultOffset(documentQuery));
        query.setMaxResults(documentQuery.getPerPage());

        List<Document> documents = query.getResultList();
        List<DocumentDto> dtos = getDocumentDtos(documents);

        Long total = (Long)countQuery.getSingleResult();

        results.setDocuments(dtos);
        results.setPagesCount(total / documentQuery.getPerPage() + (total % documentQuery.getPerPage() == 0 ? 0 : 1));
        results.setPerPage(documentQuery.getPerPage());
        results.setPageNumber(documentQuery.getPageNumber());
        return results;
    }

    private int getFirstResultOffset(DocumentQuery documentQuery) {
        return (documentQuery.getPageNumber() - 1) * documentQuery.getPerPage();
    }

    private void applyPredicateParameters(Query query, DocumentQuery documentQuery) {
        query.setParameter("archived", DocumentStatus.valueOf("ARCHIVED"));
        if(documentQuery.getStatus() != null)
            query.setParameter("status", DocumentStatus.valueOf(documentQuery.getStatus()));
        if(documentQuery.getCreatorId() != null)
            query.setParameter("id", documentQuery.getCreatorId());
        if(documentQuery.getPhrase() != null) {
            String likeExpression = "%" + documentQuery.getPhrase() + "%";
            query.setParameter("likeExpression", likeExpression);
        }
        if(documentQuery.getCreatedBefore() != null)
            query.setParameter("createdBefore", documentQuery.getCreatedBefore());
        if(documentQuery.getCreatedAfter() != null)
            query.setParameter("createdAfter", documentQuery.getCreatedAfter());
        if(documentQuery.getChangedBefore() != null)
            query.setParameter("changedBefore", documentQuery.getChangedBefore());
        if(documentQuery.getChangedAfter() != null)
            query.setParameter("changedAfter", documentQuery.getChangedAfter());
        if(documentQuery.getVerifiedBefore() != null)
            query.setParameter("verifiedBefore", documentQuery.getVerifiedBefore());
        if(documentQuery.getVerifiedAfter() != null)
            query.setParameter("verifiedAfter", documentQuery.getVerifiedAfter());
        if(documentQuery.getPublishedBefore() != null)
            query.setParameter("publishedBefore", documentQuery.getPublishedBefore());
        if(documentQuery.getPublishedAfter() != null)
            query.setParameter("publishedAfter", documentQuery.getPublishedAfter());
        if(documentQuery.getEditorId() != null)
            query.setParameter("editorId", documentQuery.getEditorId());
        if(documentQuery.getVerifierId() != null)
            query.setParameter("verifierId", documentQuery.getVerifierId());
        if(documentQuery.getPublisherId() != null)
            query.setParameter("publisherId", documentQuery.getPublisherId());
    }

    private Set<String> createPredicates(DocumentQuery documentQuery) {
        Set<String> predicates = new HashSet<>();
        predicates.add("d.status != :archived");
        if(documentQuery.getStatus() != null)
            predicates.add("d.status = :status");
        if(documentQuery.getCreatorId() != null)
            predicates.add("d.creatorId.id = :id");
        if(documentQuery.getPhrase() != null)
            predicates.add("(d.number.number LIKE :likeExpression OR d.content LIKE :likeExpression OR d.title LIKE :likeExpression)");
        if(documentQuery.getCreatedBefore() != null)
            predicates.add("d.createdAt < :createdBefore");
        if(documentQuery.getCreatedAfter() != null)
            predicates.add("d.createdAt > :createdAfter");
        if(documentQuery.getChangedBefore() != null)
            predicates.add("d.changedAt < :changedBefore");
        if(documentQuery.getChangedAfter() != null)
            predicates.add("d.changedAt > :changedAfter");
        if(documentQuery.getVerifiedBefore() != null)
            predicates.add("d.verifiedAt < :verifiedBefore");
        if(documentQuery.getVerifiedAfter() != null)
            predicates.add("d.verifiedAt > :verifiedAfter");
        if(documentQuery.getPublishedBefore() != null)
            predicates.add("d.publishedAt < :publishedBefore");
        if(documentQuery.getPublishedAfter() != null)
            predicates.add("d.publishedAt > :publishedAfter");
        if(documentQuery.getEditorId() != null)
            predicates.add("d.editorId.id = :editorId");
        if(documentQuery.getVerifierId() != null)
            predicates.add("d.verifierId.id = :verifierId");
        if(documentQuery.getPublisherId() != null)
            predicates.add("d.publisherId.id = :publisherId");
        return predicates;
    }

    private List<DocumentDto> getDocumentDtos(List<Document> documents) {
        List<DocumentDto> dtos = new LinkedList<>();
        for (Document document : documents) {
            dtos.add(createDocumentDto(document));
        }
        return dtos;
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
        documentDto.setCreatorId(document.getCreatorId().getId());
        List<ConfirmationDto> confirmationDtos = new LinkedList<>();
        for (Confirmation confirmation : document.getConfirmations()) {
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
        if (confirmation.hasProxy())
            dto.setProxyEmployeeId(confirmation.getProxy().getId());
        return dto;
    }

}
