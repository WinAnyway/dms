package pl.com.bottega.dms.application;

import java.time.LocalDateTime;
import java.util.List;

public class DocumentDto {
    private String title;
    private String number;

    private String content;

    private String status;
    private List<ConfirmationDto> confirmations;
    private Long creatorId;
    private LocalDateTime createdAt;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ConfirmationDto> getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(List<ConfirmationDto> confirmations) {
        this.confirmations = confirmations;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
