package pl.com.bottega.dms.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.dms.application.DocumentQuery;
import pl.com.bottega.dms.application.DocumentSearchResults;
import pl.com.bottega.dms.infrastructure.JPADocumentCatalog;
import pl.com.bottega.dms.shared.AuthHelper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class JPADocumentCatalogTest {

    @Autowired
    private JPADocumentCatalog catalog;
//    private JPQLDocumentCatalog catalog;

    @Autowired
    private AuthHelper authHelper;

    @Before
    public void authenticate() {
        authHelper.authenticate();
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentsByPhrase() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setPhrase("fancy");
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(2);
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByPhraseAndStatus() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setPhrase("fancy");
        documentQuery.setStatus("DRAFT");
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(2);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("1");
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByCreatorId() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setCreatorId(2L);
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(2);
        assertThat(searchResults.getDocuments().get(0).getCreatorId()).isEqualTo(2L);
        assertThat(searchResults.getDocuments().get(1).getCreatorId()).isEqualTo(2L);
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByPhraseAndCreatorId() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setCreatorId(2L);
        documentQuery.setPhrase("fancy");
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(1);
        assertThat(searchResults.getDocuments().get(0).getCreatorId()).isEqualTo(2L);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("fancy");
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByPhraseAndStatusAndCreatorId() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setCreatorId(1L);
        documentQuery.setPhrase("fancy");
        documentQuery.setStatus("DRAFT");
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(1);
        assertThat(searchResults.getDocuments().get(0).getCreatorId()).isEqualTo(1L);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("1");
        assertThat(searchResults.getDocuments().get(0).getStatus()).isEqualTo("DRAFT");
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByCreatedAfterAndCreatedBefore() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setCreatedAfter(LocalDateTime.of(2017, 1, 1, 10, 0));
        documentQuery.setCreatedBefore(LocalDateTime.of(2017, 1, 1, 11, 0));
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(1);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("1");
//        assertThat(searchResults.getDocuments().get(1).getNumber()).isEqualTo("2");
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldReturnPaginatedResults() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setPageNumber(2);
        documentQuery.setPerPage(2);
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(1);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("fancy");
//        assertThat(searchResults.getDocuments().get(1).getNumber()).isEqualTo("4");
        assertThat(searchResults.getPagesCount()).isEqualTo(2);
        assertThat(searchResults.getPageNumber()).isEqualTo(2);
        assertThat(searchResults.getPerPage()).isEqualTo(2);
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldNotReturnArchivedDocuments() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        DocumentSearchResults results = catalog.find(documentQuery);
        //then
        assertThat(results.getDocuments().size()).isEqualTo(3);
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByChangeDate() {
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setChangedAfter(LocalDateTime.of(2017, 1, 2, 10, 0));
        documentQuery.setChangedBefore(LocalDateTime.of(2017, 1, 2, 11, 0));
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(1);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("1");
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByVerificationDate() {
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setVerifiedAfter(LocalDateTime.of(2017, 1, 3, 10, 0));
        documentQuery.setVerifiedBefore(LocalDateTime.of(2017, 1, 4, 11, 0));
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(2);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("1");
        assertThat(searchResults.getDocuments().get(1).getNumber()).isEqualTo("fancy");
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByPublicationDate() {
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setPublishedAfter(LocalDateTime.of(2017, 1, 5, 10, 0));
        documentQuery.setPublishedBefore(LocalDateTime.of(2017, 1, 8, 11, 0));
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(2);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("4");
        assertThat(searchResults.getDocuments().get(1).getNumber()).isEqualTo("fancy");
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByEditorId() {
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setEditorId(1L);
        DocumentSearchResults searchResults = catalog.find(documentQuery);

        assertThat(searchResults.getDocuments().size()).isEqualTo(1);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("1");
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByVerifierId() {
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setVerifierId(2L);
        DocumentSearchResults searchResults = catalog.find(documentQuery);

        assertThat(searchResults.getDocuments().size()).isEqualTo(2);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("1");
        assertThat(searchResults.getDocuments().get(1).getNumber()).isEqualTo("4");
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByPublisherId() {
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setPublisherId(1L);
        DocumentSearchResults searchResults = catalog.find(documentQuery);

        assertThat(searchResults.getDocuments().size()).isEqualTo(2);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("4");
        assertThat(searchResults.getDocuments().get(1).getNumber()).isEqualTo("fancy");
    }

}
