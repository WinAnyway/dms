package pl.com.bottega.dms.model;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.dms.model.commands.*;
import pl.com.bottega.dms.model.numbers.NumberGenerator;
import pl.com.bottega.dms.model.printing.PrintCostCalculator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pl.com.bottega.dms.model.DocumentStatus.*;

public class DocumentTest {

    private static final Long DATE_EPS = 500L;
    private CreateDocumentCommand createDocumentCommand;
    private ChangeDocumentCommand changeDocumentCommand;
    private PublishDocumentCommand publishDocumentCommand;
    private PrintCostCalculator printCostCalculator;
    private NumberGenerator numberGenerator;
    private Document document;
    private EmployeeId employeeId;

    @Before
    public void setUp() {
        employeeId = new EmployeeId(1);
        createDocumentCommand = new CreateDocumentCommand(employeeId);
        changeDocumentCommand = new ChangeDocumentCommand();
        publishDocumentCommand = new PublishDocumentCommand();
        printCostCalculator = new StubPrintCostCalculator();
        numberGenerator = new StubNumberGenerator();
        createDocumentCommand.setTitle("test title");
        document = new Document(createDocumentCommand, numberGenerator);
    }

    @Test
    public void shouldBeDraftAfterCreate() {
        //given
        //when
        //then
        assertEquals(DRAFT, document.getStatus());
    }

    @Test
    public void shouldGenerateNumberOnCreate() {
        //given
        //when
        //then
        assertEquals(new DocumentNumber("1"), document.getNumber());
    }

    @Test
    public void shouldSetTitleOnCreate() {
        //given
        //when
        //then
        assertEquals("test title", document.getTitle());
    }

    @Test
    public void shouldChangeTitleAndContent() {
        changeDocumentCommand.setTitle("changed title");
        changeDocumentCommand.setContent("changed content");

        document.change(changeDocumentCommand);

        assertEquals("changed title", document.getTitle());
        assertEquals("changed content", document.getContent());
    }

    @Test
    public void shouldBeVerifiedAfterVerification() {

        document.verify(employeeId);

        assertEquals(VERIFIED, document.getStatus());
    }

    @Test(expected = DocumentStatusException.class)
    public void shouldNotAllowVerifyingIfVerified() {
        document.verify(employeeId);

        document.verify(employeeId);
    }

    @Test(expected = DocumentStatusException.class)
    public void shouldNotAllowVerifyingIfPublished() {
        document.publish(publishDocumentCommand, printCostCalculator);

        document.verify(employeeId);
    }

    @Test
    public void shouldBeDraftAfterChanging() {
        document.verify(employeeId);

        document.change(changeDocumentCommand);

        assertEquals(DRAFT, document.getStatus());
    }

    @Test
    public void shouldBePublishedAfterPublishing() {
        document.verify(employeeId);

        document.publish(publishDocumentCommand, printCostCalculator);

        assertEquals(PUBLISHED, document.getStatus());
    }

    @Test(expected = DocumentStatusException.class)
    public void shouldNotAllowChangingIfPublished() {
        document.publish(publishDocumentCommand, printCostCalculator);

        document.change(changeDocumentCommand);
    }

    @Test
    public void shouldRememberItsCreationDate() {
        LocalDateTime creationDate = document.getCreationDate();
        LocalDateTime expected = now();

        assertSameTime(expected, creationDate);
    }

    @Test
    public void shouldRememberLastVerificationDate() {
        document.verify(employeeId);
        LocalDateTime lastVerificationDate = document.getLastVerificationDate();
        LocalDateTime expected = now();

        assertSameTime(expected, lastVerificationDate);
    }

    @Test
    public void shouldRememberItsPublicationDate() {
        document.verify(employeeId);
        document.publish(publishDocumentCommand, printCostCalculator);
        LocalDateTime publicationDate = document.getPublicationDate();
        LocalDateTime expected = now();

        assertSameTime(expected, publicationDate);
    }

    @Test
    public void shouldRememberLastEditionDate() {
        document.change(changeDocumentCommand);
        LocalDateTime editionDate = document.getLastEditionDate();
        LocalDateTime expected = now();

        assertSameTime(expected, editionDate);
    }

    @Test
    public void shouldRememberCreatorId() {
        assertEquals(employeeId, document.getCreatorId());
    }

    @Test
    public void shouldRememberLastVerificatorId() {
        document.verify(employeeId);

        assertEquals(employeeId, document.getLastVerificatorId());
    }

    @Test
    public void shouldRememberLastEditorId() {
        changeDocumentCommand.setEditorId(employeeId);
        document.change(changeDocumentCommand);

        assertEquals(employeeId, document.getLastEditorId());
    }

    @Test
    public void shouldRememberPublisherId() {
        document.verify(employeeId);
        publishDocumentCommand.setPublisherId(employeeId);
        document.publish(publishDocumentCommand, printCostCalculator);

        assertEquals(employeeId, document.getPublisherId());
    }

    @Test
    public void shouldBeArchivedAfterArchivization() {
        document.archive();

        assertEquals(ARCHIVED, document.getStatus());
    }

    @Test(expected = DocumentStatusException.class)
    public void shouldNotAllowChangingIfArchived() {
        document.archive();

        document.change(changeDocumentCommand);
    }


    @Test(expected = DocumentStatusException.class)
    public void shouldNotAllowPublishingIfArchived() {
        document.archive();

        document.publish(publishDocumentCommand, printCostCalculator);
    }


    @Test(expected = DocumentStatusException.class)
    public void shouldNotAllowVerifyingIfArchived() {
        document.archive();

        document.verify(employeeId);
    }

    @Test(expected = DocumentStatusException.class)
    public void shouldNotAllowConfirmingIfArchived() {
        ConfirmDocumentCommand confirmDocumentCommand = new ConfirmDocumentCommand();
        document.archive();

        document.confirm(confirmDocumentCommand);
    }

    @Test(expected = DocumentStatusException.class)
    public void shouldNotAllowConfirmingForIfArchived() {
        ConfirmForDocumentCommand confirmForDocumentCommand = new ConfirmForDocumentCommand();
        document.archive();

        document.confirmFor(confirmForDocumentCommand);
    }

    @Test(expected = DocumentStatusException.class)
    public void shouldNotAllowPublishingIfNotVerified() {
        document.publish(publishDocumentCommand, printCostCalculator);
    }


    private void assertSameTime(LocalDateTime expected, LocalDateTime actual) {
        assertTrue(ChronoUnit.MILLIS.between(expected, actual) < DATE_EPS);
    }


    class StubNumberGenerator implements NumberGenerator {

        public DocumentNumber generate() {
            return new DocumentNumber("1");
        }
    }

    class StubPrintCostCalculator implements PrintCostCalculator {

        public BigDecimal calculateCost(Document document) {
            return new BigDecimal(1);
        }
    }
}
