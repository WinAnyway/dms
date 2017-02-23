package pl.com.bottega.dms.application;

import java.util.Set;

public class DocumentDto {


    private String title;
    private String number;
    private String status;
    private Set<ConfirmationDto> confirmations;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<ConfirmationDto> getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Set<ConfirmationDto> confirmations) {
        this.confirmations = confirmations;
    }

    public ConfirmationDto getConfirmation(Long employeeId) {
        for (ConfirmationDto confirmationDto : confirmations)
            if (confirmationDto.getOwner().equals(employeeId))
                return confirmationDto;

        return null;
    }
}
