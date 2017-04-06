package pl.com.bottega.dms.model.document;

import pl.com.bottega.dms.model.EmployeeId;
import pl.com.bottega.dms.model.commands.ChangeDocumentCommand;
import pl.com.bottega.dms.model.commands.ConfirmDocumentCommand;
import pl.com.bottega.dms.model.commands.ConfirmForDocumentCommand;
import pl.com.bottega.dms.model.commands.PublishDocumentCommand;
import pl.com.bottega.dms.model.printing.PrintCostCalculator;

public class ArchivedState implements DocumentState {

    private Document document;

    public ArchivedState(Document document) {

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
        throw new DocumentStatusException("You can't archive ARCHIVED document");
    }

    @Override
    public void publish(PublishDocumentCommand cmd, PrintCostCalculator printCostCalculator) {
        throw new DocumentStatusException("Document must be VERIFIED to PUBLISH");
    }

    @Override
    public void confirm(ConfirmDocumentCommand cmd) {
        throw new DocumentStatusException("Document must be PUBLISHED to Confirm");
    }

    @Override
    public void confirmFor(ConfirmForDocumentCommand cmd) {
        throw new DocumentStatusException("Document must be PUBLISHED to ConfirmFor");
    }
}
