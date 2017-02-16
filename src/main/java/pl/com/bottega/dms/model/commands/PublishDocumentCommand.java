package pl.com.bottega.dms.model.commands;

import pl.com.bottega.dms.model.EmployeeId;

public class PublishDocumentCommand {


    private EmployeeId publisherId;

    public EmployeeId getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(EmployeeId publisherId) {
        this.publisherId = publisherId;
    }
}
