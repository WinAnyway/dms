package pl.com.bottega.dms.application;

import pl.com.bottega.dms.model.EmployeeId;
import pl.com.bottega.dms.model.document.DocumentBuiler;
import pl.com.bottega.dms.model.document.DocumentNumber;
import pl.com.bottega.dms.model.document.DocumentStatus;
import pl.com.bottega.dms.model.document.DocumentType;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class DocumentDtoBuilder implements DocumentBuiler {

    private DocumentDto documentDto = new DocumentDto();

    public DocumentDtoBuilder() {
        documentDto.setConfirmations(new LinkedList<>());
    }

    public DocumentDto getResult() {
        return documentDto;
    }

    @Override
    public void buildNumber(DocumentNumber documentNumber) {
        documentDto.setNumber(documentNumber.getNumber());
    }

    @Override
    public void buildTitle(String title) {
        documentDto.setTitle(title);
    }

    @Override
    public void buildContent(String content) {
        documentDto.setContent(content);
    }

    @Override
    public void buildStatus(DocumentStatus documentStatus) {
        documentDto.setStatus(documentStatus.name());
    }

    @Override
    public void buildType(DocumentType documentType) {
        documentDto.setType(documentType.name());
    }

    @Override
    public void buildCreatedAt(LocalDateTime createdAt) {
        documentDto.setCreatedAt(createdAt);
    }

    @Override
    public void buildConfirmation(EmployeeId owner, EmployeeId proxy, LocalDateTime confirmationDate) {
        ConfirmationDto confirmationDto = new ConfirmationDto();
        confirmationDto.setOwnerEmployeeId(owner.getId());
        if (proxy != null)
            confirmationDto.setProxyEmployeeId(proxy.getId());
        confirmationDto.setConfirmedAt(confirmationDate);
        confirmationDto.setConfirmed(confirmationDate != null);
        documentDto.getConfirmations().add(confirmationDto);
    }
}
