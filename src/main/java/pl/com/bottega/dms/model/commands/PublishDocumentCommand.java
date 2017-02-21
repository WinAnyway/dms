package pl.com.bottega.dms.model.commands;

import pl.com.bottega.dms.model.EmployeeId;

import java.util.Collection;

public class PublishDocumentCommand {
    private EmployeeId employeeId;
    private Collection<EmployeeId> recipients;

    public EmployeeId getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(EmployeeId employeeId) {
        this.employeeId = employeeId;
    }

    public Collection<EmployeeId> getRecipients() {
        return recipients;
    }

    public void setRecipients(Collection<EmployeeId> recipients) {
        this.recipients = recipients;
    }
}