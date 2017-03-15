package pl.com.bottega.dms.model.commands;

import pl.com.bottega.dms.model.EmployeeId;

import java.util.Collection;

public class PublishDocumentCommand implements EmployeeAware, Validatable{
    private EmployeeId employeeId;
    private Collection<EmployeeId> recipients;
    private String documentNumber;

    public EmployeeId getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(EmployeeId employeeId) {
        this.employeeId = employeeId;
    }

    public void setRecipients(Collection<EmployeeId> recipients) {
        this.recipients = recipients;
    }

    public Collection<EmployeeId> getRecipients() {
        return recipients;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Override
    public void validate(ValidationErrors errors) {
        if(documentNumber == null || documentNumber.isEmpty())
            errors.add("documentNumber", "Can't be blank");
        if(recipients == null || recipients.size() == 0)
            errors.add("recipients", "Can't be blank");
    }
}
