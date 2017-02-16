package pl.com.bottega.dms.model;

import pl.com.bottega.dms.model.commands.*;
import pl.com.bottega.dms.model.numbers.NumberGenerator;
import pl.com.bottega.dms.model.printing.PrintCostCalculator;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static pl.com.bottega.dms.model.DocumentStatus.*;

public class Document {

    private DocumentNumber number;
    private DocumentStatus status;
    private String title;
    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime lastVerificationDate;
    private LocalDateTime publicationDate;
    private LocalDateTime lastEditionDate;
    private EmployeeId creatorId;
    private EmployeeId lastVerificatorId;
    private EmployeeId lastEditorId;
    private EmployeeId publisherId;

    public Document(CreateDocumentCommand cmd, NumberGenerator numberGenerator) {
        this.status = DRAFT;
        this.number = numberGenerator.generate();
        this.title = cmd.getTitle();
        this.creatorId = cmd.getCreatorId();
        this.creationDate = now();
    }


    public void change(ChangeDocumentCommand cmd) {
        if (status != DRAFT && status != VERIFIED)
            throw new DocumentStatusException();

        this.title = cmd.getTitle();
        this.content = cmd.getContent();
        this.status = DRAFT;
        this.lastEditorId = cmd.getEditorId();
        this.lastEditionDate = now();
    }

    public void verify(EmployeeId verificatorId) {
        if (status != DRAFT)
            throw new DocumentStatusException();

        this.status = VERIFIED;
        this.lastVerificatorId = verificatorId;
        this.lastVerificationDate = now();
    }

    public void archive() {
        this.status = ARCHIVED;
    }

    public void publish(PublishDocumentCommand cmd, PrintCostCalculator printCostCalculator) {
        if (status != VERIFIED)
            throw new DocumentStatusException();

        this.status = PUBLISHED;
        this.publisherId = cmd.getPublisherId();
        this.publicationDate = now();
    }

    public void confirm(ConfirmDocumentCommand cmd) {
        if (status == ARCHIVED)
            throw new DocumentStatusException();

    }

    public void confirmFor(ConfirmForDocumentCommand cmd) {
        if (status == ARCHIVED)
            throw new DocumentStatusException();
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public DocumentNumber getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastVerificationDate() {
        return lastVerificationDate;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public LocalDateTime getLastEditionDate() {
        return lastEditionDate;
    }

    public EmployeeId getCreatorId() {
        return creatorId;
    }

    public EmployeeId getLastVerificatorId() {
        return lastVerificatorId;
    }

    public EmployeeId getLastEditorId() {
        return lastEditorId;
    }

    public EmployeeId getPublisherId() {
        return publisherId;
    }

}
