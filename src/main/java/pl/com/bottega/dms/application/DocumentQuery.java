package pl.com.bottega.dms.application;

import java.time.LocalDateTime;

public class DocumentQuery {

    private String phrase;
    private String status;

    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private LocalDateTime changedAfter;
    private LocalDateTime changedBefore;
    private LocalDateTime verifiedAfter;
    private LocalDateTime verifiedBefore;
    private LocalDateTime publishedAfter;
    private LocalDateTime publishedBefore;

    private Long creatorId;
    private Integer pageNumber = 1;

    private Integer perPage = 50;
    private String sortBy;

    private String sortOrder;
    private Long editorId;
    private Long verifierId;
    private Long publisherId;

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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
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

    public LocalDateTime getChangedAfter() {
        return changedAfter;
    }

    public void setChangedAfter(LocalDateTime changedAfter) {
        this.changedAfter = changedAfter;
    }

    public LocalDateTime getChangedBefore() {
        return changedBefore;
    }

    public void setChangedBefore(LocalDateTime changedBefore) {
        this.changedBefore = changedBefore;
    }

    public LocalDateTime getVerifiedAfter() {
        return verifiedAfter;
    }

    public void setVerifiedAfter(LocalDateTime verifiedAfter) {
        this.verifiedAfter = verifiedAfter;
    }

    public LocalDateTime getVerifiedBefore() {
        return verifiedBefore;
    }

    public void setVerifiedBefore(LocalDateTime verifiedBefore) {
        this.verifiedBefore = verifiedBefore;
    }

    public LocalDateTime getPublishedAfter() {
        return publishedAfter;
    }

    public void setPublishedAfter(LocalDateTime publishedAfter) {
        this.publishedAfter = publishedAfter;
    }

    public LocalDateTime getPublishedBefore() {
        return publishedBefore;
    }

    public void setPublishedBefore(LocalDateTime publishedBefore) {
        this.publishedBefore = publishedBefore;
    }

    public Long getEditorId() {
        return editorId;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
    }

    public Long getVerifierId() {
        return verifierId;
    }

    public void setVerifierId(Long verifierId) {
        this.verifierId = verifierId;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }
}
