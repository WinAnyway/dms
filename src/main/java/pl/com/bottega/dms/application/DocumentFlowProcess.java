package pl.com.bottega.dms.application;

import pl.com.bottega.dms.model.DocumentNumber;
import pl.com.bottega.dms.model.EmployeeId;
import pl.com.bottega.dms.model.commands.ChangeDocumentCommand;
import pl.com.bottega.dms.model.commands.CreateDocumentCommand;
import pl.com.bottega.dms.model.commands.PublishDocumentCommand;

public interface DocumentFlowProcess {

    DocumentNumber create(CreateDocumentCommand cmd);

    void change(ChangeDocumentCommand cmd);

    void verify(DocumentNumber documentNumber, EmployeeId employeeId);

    void publish(PublishDocumentCommand cmd);

    void archive(DocumentNumber documentNumber, EmployeeId employeeId);

}
