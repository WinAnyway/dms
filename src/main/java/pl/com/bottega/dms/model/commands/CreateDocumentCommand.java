package pl.com.bottega.dms.model.commands;

import pl.com.bottega.dms.model.EmployeeId;

public class CreateDocumentCommand {


    private String title;
    private EmployeeId creatorId;

    public CreateDocumentCommand(EmployeeId creatorId) {
        this.creatorId = creatorId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public EmployeeId getCreatorId() {
        return creatorId;
    }
}
