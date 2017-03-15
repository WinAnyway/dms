package pl.com.bottega.dms.model.commands;

import pl.com.bottega.dms.model.EmployeeId;

public class ChangeDocumentCommand implements EmployeeAware, Validatable{
    private String title;
    private String content;
    private EmployeeId employeeId;
    private String number;

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

    public void setEmployeeId(EmployeeId employeeId) {
        this.employeeId = employeeId;
    }

    public EmployeeId getEmployeeId() {
        return employeeId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public void validate(ValidationErrors errors) {
        if(title == null || title.isEmpty())
            errors.add("title", "Can't be blank");
        if(content == null || content.isEmpty())
            errors.add("content", "Can't be blank");
        if(number == null || number.isEmpty())
            errors.add("number", "Can't be blank");
    }
}
