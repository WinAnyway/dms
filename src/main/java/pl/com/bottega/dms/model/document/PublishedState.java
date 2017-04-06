package pl.com.bottega.dms.model.document;

import pl.com.bottega.dms.model.Confirmation;
import pl.com.bottega.dms.model.EmployeeId;
import pl.com.bottega.dms.model.commands.ChangeDocumentCommand;
import pl.com.bottega.dms.model.commands.ConfirmDocumentCommand;
import pl.com.bottega.dms.model.commands.ConfirmForDocumentCommand;
import pl.com.bottega.dms.model.commands.PublishDocumentCommand;
import pl.com.bottega.dms.model.printing.PrintCostCalculator;

import static pl.com.bottega.dms.model.document.DocumentStatus.ARCHIVED;

public class PublishedState implements DocumentState {

    private Document document;

    public PublishedState(Document document) {
        this.document = document;
    }

    @Override
    public void change(ChangeDocumentCommand cmd) {
        throw new DocumentStatusException("Document must be DRAFT or VERIFIED to change");
    }

    @Override
    public void verify(EmployeeId employeeId) {
        throw new DocumentStatusException("Document must be DRAFT to VERIFY");
    }

    @Override
    public void archive(EmployeeId employeeId) {
        document.status = ARCHIVED;
        document.documentState = new ArchivedState(document);
    }

    @Override
    public void publish(PublishDocumentCommand cmd, PrintCostCalculator printCostCalculator) {
        throw new DocumentStatusException("Document should be VERIFIED to PUBLISH");
    }

    @Override
    public void confirm(ConfirmDocumentCommand cmd) {
        Confirmation confirmation = document.getConfirmation(cmd.getEmployeeId());
        confirmation.confirm();
    }

    @Override
    public void confirmFor(ConfirmForDocumentCommand cmd) {
        Confirmation confirmation = document.getConfirmation(cmd.getEmployeeId());
        confirmation.confirmFor(cmd.getConfirmingEmployeeId());
    }
}
