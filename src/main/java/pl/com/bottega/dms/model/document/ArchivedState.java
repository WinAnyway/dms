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

    }

    @Override
    public void verify(EmployeeId employeeId) {

    }

    @Override
    public void archive(EmployeeId employeeId) {

    }

    @Override
    public void publish(PublishDocumentCommand cmd, PrintCostCalculator printCostCalculator) {

    }

    @Override
    public void confirm(ConfirmDocumentCommand cmd) {

    }

    @Override
    public void confirmFor(ConfirmForDocumentCommand cmd) {

    }
}
