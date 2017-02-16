package pl.com.bottega.dms.model.commands;

import pl.com.bottega.dms.model.EmployeeId;

public class ChangeDocumentCommand {


    private String title;
    private String content;
    private EmployeeId editorId;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public EmployeeId getEditorId() {
        return editorId;
    }

    public void setEditorId(EmployeeId editorId) {
        this.editorId = editorId;
    }
}
