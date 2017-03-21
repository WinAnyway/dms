package pl.com.bottega.dms.model.commands;

import pl.com.bottega.dms.model.document.DocumentType;
import pl.com.bottega.dms.model.EmployeeId;

public class CreateDocumentCommand implements EmployeeAware, Validatable{
    private String title;
    private EmployeeId employeeId;
    private DocumentType documentType;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setEmployeeId(EmployeeId employeeId) {
        this.employeeId = employeeId;
    }

    public EmployeeId getEmployeeId() {
        return employeeId;
    }

    @Override
    public void validate(ValidationErrors errors) {
        if(title == null || title.isEmpty())
            errors.add("title", "Can't be blank");
        if(documentType == null)
            errors.add("title", "Can't be blank");
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

}
