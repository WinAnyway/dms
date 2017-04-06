package pl.com.bottega.dms.model.document;

import pl.com.bottega.dms.model.EmployeeId;

import java.time.LocalDateTime;

public interface DocumentBuilder {

    void buildNumber(DocumentNumber documentNumber);

    void buildTitle(String title);

    void buildContent(String content);

    void buildStatus(DocumentStatus documentStatus);

    void buildType(DocumentType documentType);

    void buildCreatedAt(LocalDateTime createdAt);

    void buildConfirmation(EmployeeId owner, EmployeeId proxy, LocalDateTime confirmationDate);

    void buildCreatorId(EmployeeId creatorId);
}
