package pl.com.bottega.dms.application;

import java.time.LocalDateTime;

public class DocumentQuery {

    private String phrase;
    private String status;

    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;

    private Long creatorId;

    private Long prageNumber;
    private Long perPage;

    private String sortBy;
    private String sortOrder;

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(LocalDateTime createdAfter) {
        this.createdAfter = createdAfter;
    }

    public LocalDateTime getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(LocalDateTime createdBefore) {
        this.createdBefore = createdBefore;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getPrageNumber() {
        return prageNumber;
    }

    public void setPrageNumber(Long prageNumber) {
        this.prageNumber = prageNumber;
    }

    public Long getPerPage() {
        return perPage;
    }

    public void setPerPage(Long perPage) {
        this.perPage = perPage;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
