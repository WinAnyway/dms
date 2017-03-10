package pl.com.bottega.dms.infrastructure;

import pl.com.bottega.dms.application.*;
import pl.com.bottega.dms.model.Confirmation;
import pl.com.bottega.dms.model.Document;
import pl.com.bottega.dms.model.DocumentNumber;
import pl.com.bottega.dms.model.DocumentStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class JPADocumentCatalog implements DocumentCatalog {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DocumentSearchResults find(DocumentQuery documentQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        DocumentSearchResults results = new DocumentSearchResults();

        List<DocumentDto> dtos = queryDocuments(documentQuery, criteriaBuilder);
        Long total = queryTotalCount(documentQuery, criteriaBuilder);

        results.setPagesCount(total / documentQuery.getPerPage() + (total % documentQuery.getPerPage() == 0 ? 0 : 1));
        results.setDocuments(dtos);
        results.setPerPage(documentQuery.getPerPage());
        results.setPageNumber(documentQuery.getPageNumber());

        return results;
    }

    private Long queryTotalCount(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Document> countRoot = countCriteriaQuery.from(Document.class);
        Set<Predicate> countPredicates = createPredicates(documentQuery, criteriaBuilder, countRoot);
        countCriteriaQuery.select(criteriaBuilder.count(countRoot));
        countCriteriaQuery.where(countPredicates.toArray(new Predicate[]{}));
        Query countQuery = entityManager.createQuery(countCriteriaQuery);
        return (Long) countQuery.getSingleResult();
    }

    private List<DocumentDto> queryDocuments(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        Root<Document> root = criteriaQuery.from(Document.class);
        root.fetch("confirmations", JoinType.LEFT);
        Set<Predicate> predicates = createPredicates(documentQuery, criteriaBuilder, root);
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));
        setSortByAndSortOrder(documentQuery, criteriaBuilder, criteriaQuery, root);
        Query query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(documentQuery.getPerPage());
        query.setFirstResult(getFirstResultOffset(documentQuery));
        List<Document> documents = query.getResultList();
        return getDocumentDtos(documents);
    }

    private void setSortByAndSortOrder(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, CriteriaQuery<Document> criteriaQuery, Root<Document> root) {
        if (documentQuery.getSortBy() != null) {
            if (documentQuery.getSortOrder() == null || documentQuery.getSortBy().equals("asc"))
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(documentQuery.getSortBy())));
            else
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(documentQuery.getSortBy())));
        }
    }

    private List<DocumentDto> getDocumentDtos(List<Document> documents) {
        List<DocumentDto> dtos = new LinkedList<>();
        for (Document document : documents) {
            dtos.add(createDocumentDto(document));
        }
        return dtos;
    }

    private int getFirstResultOffset(DocumentQuery documentQuery) {
        return (documentQuery.getPageNumber() - 1) * documentQuery.getPerPage();
    }

    private Set<Predicate> createPredicates(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root) {
        Set<Predicate> predicates = new HashSet<>();
        addPhrasesPredicate(documentQuery, criteriaBuilder, root, predicates);
        addStatusPredicate(documentQuery, criteriaBuilder, root, predicates);
        addCreatorIdPredicate(documentQuery, criteriaBuilder, root, predicates);
        addCreatedAfterPredicate(documentQuery, criteriaBuilder, root, predicates);
        addCreatedBeforePredicate(documentQuery, criteriaBuilder, root, predicates);
        addChangedAfterPredicate(documentQuery, criteriaBuilder, root, predicates);
        addChangedBeforePredicate(documentQuery, criteriaBuilder, root, predicates);
        addVerifiedAfterPredicate(documentQuery, criteriaBuilder, root, predicates);
        addVerifiedBeforePredicate(documentQuery, criteriaBuilder, root, predicates);
        addPublishedAfterPredicate(documentQuery, criteriaBuilder, root, predicates);
        addPublishedBeforePredicate(documentQuery, criteriaBuilder, root, predicates);
        addNotArchivedPredicate(criteriaBuilder, root, predicates);
        addEditorIdPredicate(documentQuery, criteriaBuilder, root, predicates);
        addVerifierIdPredicate(documentQuery, criteriaBuilder, root, predicates);
        addPublisherIdPredicate(documentQuery, criteriaBuilder, root, predicates);
        return predicates;
    }

    private void addPublisherIdPredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getPublisherId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("publisherId"), documentQuery.getPublisherId()));
        }
    }

    private void addVerifierIdPredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getVerifierId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("verifierId"), documentQuery.getVerifierId()));
        }
    }

    private void addEditorIdPredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getEditorId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("editorId"), documentQuery.getEditorId()));
        }
    }

    private void addNotArchivedPredicate(CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        predicates.add(criteriaBuilder.notEqual(root.get("status"), DocumentStatus.valueOf("ARCHIVED")));
    }

    private void addCreatedBeforePredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getCreatedBefore() != null)
            predicates.add(criteriaBuilder.lessThan(root.get("createdAt"), documentQuery.getCreatedBefore()));
    }

    private void addCreatedAfterPredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getCreatedAfter() != null)
            predicates.add(criteriaBuilder.greaterThan(root.get("createdAt"), documentQuery.getCreatedAfter()));
    }

    private void addChangedBeforePredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getChangedBefore() != null)
            predicates.add(criteriaBuilder.lessThan(root.get("changedAt"), documentQuery.getChangedBefore()));
    }

    private void addChangedAfterPredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getChangedAfter() != null)
            predicates.add(criteriaBuilder.greaterThan(root.get("changedAt"), documentQuery.getChangedAfter()));
    }

    private void addVerifiedBeforePredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getVerifiedBefore() != null)
            predicates.add(criteriaBuilder.lessThan(root.get("verifiedAt"), documentQuery.getVerifiedBefore()));
    }

    private void addVerifiedAfterPredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getVerifiedAfter() != null)
            predicates.add(criteriaBuilder.greaterThan(root.get("verifiedAt"), documentQuery.getVerifiedAfter()));
    }

    private void addPublishedBeforePredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getPublishedBefore() != null)
            predicates.add(criteriaBuilder.lessThan(root.get("publishedAt"), documentQuery.getPublishedBefore()));
    }

    private void addPublishedAfterPredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getPublishedAfter() != null)
            predicates.add(criteriaBuilder.greaterThan(root.get("publishedAt"), documentQuery.getPublishedAfter()));
    }

    private void addCreatorIdPredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getCreatorId() != null)
            predicates.add(criteriaBuilder.equal(root.get("creatorId"), documentQuery.getCreatorId()));
    }

    private void addStatusPredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getStatus() != null)
            predicates.add(criteriaBuilder.equal(root.get("status"), DocumentStatus.valueOf(documentQuery.getStatus())));
    }

    private void addPhrasesPredicate(DocumentQuery documentQuery, CriteriaBuilder criteriaBuilder, Root<Document> root, Set<Predicate> predicates) {
        if (documentQuery.getPhrase() != null) {
            String likeExpression = "%" + documentQuery.getPhrase() + "%";
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get("title"), likeExpression),
                    criteriaBuilder.like(root.get("content"), likeExpression),
                    criteriaBuilder.like(root.get("number").get("number"), likeExpression)));
        }
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
        documentDto.setCreatedAt(document.getCreatedAt());
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
