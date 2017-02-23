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
import pl.com.bottega.dms.model.DocumentNumber;
import pl.com.bottega.dms.model.EmployeeId;
import pl.com.bottega.dms.model.commands.ChangeDocumentCommand;
import pl.com.bottega.dms.model.commands.CreateDocumentCommand;
import pl.com.bottega.dms.model.commands.PublishDocumentCommand;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DmsApplication.class)
public class DocumentFlowTest {

    @Autowired
    private DocumentFlowProcess documentFlowProcess;

    @Autowired
    private DocumentCatalog documentCatalog;

    @Test
    public void shouldCreateDocument() {
        //when - I create document
        CreateDocumentCommand createDocumentCommand = new CreateDocumentCommand();
        createDocumentCommand.setTitle("test");
        DocumentNumber documentNumber = documentFlowProcess.create(createDocumentCommand);
        //then - the document is available in catalog
        DocumentDto dto = documentCatalog.get(documentNumber);
        assertThat(dto.getTitle()).isEqualTo("test");
        assertThat(dto.getNumber()).isEqualTo(documentNumber.getNumber());
    }

    @Test
    public void shouldChangeDocument() {
        //given - Document with a title
        CreateDocumentCommand createDocumentCommand = new CreateDocumentCommand();
        createDocumentCommand.setTitle("test");
        DocumentNumber documentNumber = documentFlowProcess.create(createDocumentCommand);
        //when - I change Document title
        ChangeDocumentCommand changeDocumentCommand = new ChangeDocumentCommand();
        changeDocumentCommand.setTitle("not a test");
        changeDocumentCommand.setNumber(documentNumber.getNumber());
        documentFlowProcess.change(changeDocumentCommand);
        //then - Document title is changed
        DocumentDto dto = documentCatalog.get(documentNumber);
        assertThat(dto.getTitle()).isEqualTo("not a test");
    }

    @Test
    public void shouldVerifyDocument() {
        //given - Unverified document
        CreateDocumentCommand createDocumentCommand = new CreateDocumentCommand();
        DocumentNumber documentNumber = documentFlowProcess.create(createDocumentCommand);
        //when - I verify document
        documentFlowProcess.verify(documentNumber);
        //then - Document is VERIFIED
        DocumentDto dto = documentCatalog.get(documentNumber);
        assertThat(dto.getStatus()).isEqualTo("VERIFIED");
    }

    @Test
    public void shouldPublishDocument() {
        //given - Verified Document
        EmployeeId employeeId = new EmployeeId(1L);
        CreateDocumentCommand createDocumentCommand = new CreateDocumentCommand();
        DocumentNumber documentNumber = documentFlowProcess.create(createDocumentCommand);
        documentFlowProcess.verify(documentNumber);
        //when - I publish Document
        PublishDocumentCommand publishDocumentCommand = new PublishDocumentCommand();
        publishDocumentCommand.setNumber(documentNumber.getNumber());
        publishDocumentCommand.setRecipients(new ArrayList<>());
        documentFlowProcess.publish(publishDocumentCommand);
        //then - Document is PUBLISHED
        DocumentDto dto = documentCatalog.get(documentNumber);
        assertThat(dto.getStatus()).isEqualTo("PUBLISHED");
    }

    @Test
    public void shouldArchiveDocument() {
        //given - Document
        CreateDocumentCommand createDocumentCommand = new CreateDocumentCommand();
        DocumentNumber documentNumber = documentFlowProcess.create(createDocumentCommand);
        //when - I archive Document
        documentFlowProcess.archive(documentNumber);
        //then - Document is ARCHIVED
        DocumentDto dto = documentCatalog.get(documentNumber);
        assertThat(dto.getStatus()).isEqualTo("ARCHIVED");
    }

}
