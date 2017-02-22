package pl.com.bottega.dms.application;

import pl.com.bottega.dms.model.Confirmation;
import pl.com.bottega.dms.model.DocumentStatus;

import java.util.Set;

public class DocumentDto {


    private String title;
    private String number;
    private DocumentStatus status;
    private Set<Confirmation> confirmations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public Set<Confirmation> getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Set<Confirmation> confirmations) {
        this.confirmations = confirmations;
    }
}
