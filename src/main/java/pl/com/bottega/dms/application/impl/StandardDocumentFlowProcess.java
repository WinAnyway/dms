package pl.com.bottega.dms.application.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.dms.application.DocumentFlowProcess;
import pl.com.bottega.dms.application.user.CurrentUser;
import pl.com.bottega.dms.application.user.RequiresAuth;
import pl.com.bottega.dms.model.Document;
import pl.com.bottega.dms.model.DocumentFactory;
import pl.com.bottega.dms.model.DocumentNumber;
import pl.com.bottega.dms.model.DocumentRepository;
import pl.com.bottega.dms.model.commands.ChangeDocumentCommand;
import pl.com.bottega.dms.model.commands.CreateDocumentCommand;
import pl.com.bottega.dms.model.commands.PublishDocumentCommand;
import pl.com.bottega.dms.model.events.DocumentPublishEvent;
import pl.com.bottega.dms.model.printing.PrintCostCalculator;

@Transactional(isolation = Isolation.REPEATABLE_READ)
public class StandardDocumentFlowProcess implements DocumentFlowProcess {

    private DocumentFactory documentFactory;
    private PrintCostCalculator printCostCalculator;
    private DocumentRepository documentRepository;
    private CurrentUser currentUser;
    private ApplicationEventPublisher publisher;

    public StandardDocumentFlowProcess(DocumentFactory documentFactory, PrintCostCalculator printCostCalculator,
                                       DocumentRepository documentRepository, CurrentUser currentUser, ApplicationEventPublisher publisher) {
        this.documentFactory = documentFactory;
        this.printCostCalculator = printCostCalculator;
        this.documentRepository = documentRepository;
        this.currentUser = currentUser;
        this.publisher = publisher;
    }

    @Override
    @RequiresAuth(roles = {"QUALITY_STAFF"})
    public DocumentNumber create(CreateDocumentCommand cmd) {
        Document document = documentFactory.createDocument(cmd);
        documentRepository.put(document);
        return document.getNumber();
    }

    @Override
    @RequiresAuth(roles = {"QUALITY_STAFF"})
    public void change(ChangeDocumentCommand cmd) {
        DocumentNumber documentNumber = new DocumentNumber(cmd.getNumber());
        Document document = documentRepository.get(documentNumber);
        document.change(cmd);
    }

    @Override
    @RequiresAuth(roles = {"QUALITY_STAFF"})
    public void verify(DocumentNumber documentNumber) {
        Document document = documentRepository.get(documentNumber);
        document.verify(currentUser.getEmployeeId());
    }

    @Override
    @RequiresAuth(roles = {"STAFF_MANAGER"})
    public void publish(PublishDocumentCommand cmd) {
        DocumentNumber documentNumber = new DocumentNumber(cmd.getDocumentNumber());
        Document document = documentRepository.get(documentNumber);
        document.publish(cmd, printCostCalculator);
        publisher.publishEvent(new DocumentPublishEvent(documentNumber));
    }

    @Override
    @RequiresAuth(roles = {"STAFF_MANAGER"})
    public void archive(DocumentNumber documentNumber) {
        Document document = documentRepository.get(documentNumber);
        document.archive(currentUser.getEmployeeId());
    }
}
