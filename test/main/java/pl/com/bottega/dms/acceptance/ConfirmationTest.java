package pl.com.bottega.dms.acceptance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.com.bottega.dms.DmsApplication;
import pl.com.bottega.dms.application.DocumentCatalog;
import pl.com.bottega.dms.application.DocumentDto;
import pl.com.bottega.dms.application.DocumentFlowProcess;
import pl.com.bottega.dms.application.ReadingConfirmator;
import pl.com.bottega.dms.model.DocumentNumber;
import pl.com.bottega.dms.model.EmployeeId;
import pl.com.bottega.dms.model.commands.ConfirmDocumentCommand;
import pl.com.bottega.dms.model.commands.ConfirmForDocumentCommand;
import pl.com.bottega.dms.model.commands.CreateDocumentCommand;
import pl.com.bottega.dms.model.commands.PublishDocumentCommand;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DmsApplication.class)
public class ConfirmationTest {

    @Autowired
    private ReadingConfirmator readingConfirmator;

    @Autowired
    private DocumentCatalog documentCatalog;

    @Autowired
    private DocumentFlowProcess documentFlowProcess;

    @Test
    public void shouldConfirm() {
        //given - PUBLISHED Document
        DocumentNumber documentNumber = given().publishedDocument();
        EmployeeId employeeId = new EmployeeId(1L);
        //when - I confirm Document
        ConfirmDocumentCommand confirmDocumentCommand = new ConfirmDocumentCommand();
        confirmDocumentCommand.setEmployeeId(employeeId);
        confirmDocumentCommand.setNumber(documentNumber.getNumber());
        readingConfirmator.confirm(confirmDocumentCommand);
        //then - Document is confirmed
        DocumentDto dto = documentCatalog.get(documentNumber);
        assertThat(dto.getConfirmations().size()).isEqualTo(1);
        assertThat(dto.getConfirmation(employeeId.getId()).getOwner()).isEqualTo(employeeId.getId());
        assertThat(dto.getConfirmation(employeeId.getId()).getConfirmationDate()).isNotNull();
    }

    @Test
    public void shouldConfirmFor() {
        //given - PUBLISHED Document
        DocumentNumber documentNumber = given().publishedDocument();
        EmployeeId employeeId = new EmployeeId(1L);
        //when - I confirmFor Document
        ConfirmForDocumentCommand confirmForDocumentCommand = new ConfirmForDocumentCommand();
        confirmForDocumentCommand.setEmployeeId(employeeId);
        EmployeeId confirmingEmployeeId = new EmployeeId(2L);
        confirmForDocumentCommand.setConfirmingEmployeeId(confirmingEmployeeId);
        confirmForDocumentCommand.setNumber(documentNumber.getNumber());
        readingConfirmator.confirmFor(confirmForDocumentCommand);
        //then - Document is confirmed, and confirming employee is saved
        DocumentDto dto = documentCatalog.get(documentNumber);
        assertThat(dto.getConfirmations().size()).isEqualTo(1);
        assertThat(dto.getConfirmation(employeeId.getId()).getOwner()).isEqualTo(employeeId.getId());
        assertThat(dto.getConfirmation(employeeId.getId()).getProxy()).isEqualTo(confirmingEmployeeId.getId());
        assertThat(dto.getConfirmation(employeeId.getId()).getConfirmationDate()).isNotNull();
    }

    public DocumentAssembler given() {
        return new DocumentAssembler();
    }

    class DocumentAssembler {

        public DocumentNumber publishedDocument() {
            DocumentNumber documentNumber = documentFlowProcess.create(new CreateDocumentCommand());
            PublishDocumentCommand publishDocumentCommand = new PublishDocumentCommand();
            EmployeeId employeeId = new EmployeeId(1L);
            documentFlowProcess.verify(documentNumber);
            publishDocumentCommand.setRecipients(Arrays.asList(employeeId));
            publishDocumentCommand.setNumber(documentNumber.getNumber());
            documentFlowProcess.publish(publishDocumentCommand);
            return documentNumber;
        }
    }
}
