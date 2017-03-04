package pl.com.bottega.dms.integration;

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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JPADocumentCatalogTest {

    @Autowired
    private JPADocumentCatalog catalog;

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentsByPhrase() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setPhrase("fancy");
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(3);
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
        assertThat(searchResults.getDocuments().get(1).getNumber()).isEqualTo("fancy");
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
        assertThat(searchResults.getDocuments().size()).isEqualTo(3);
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
        assertThat(searchResults.getDocuments().size()).isEqualTo(2);
        assertThat(searchResults.getDocuments().get(0).getCreatorId()).isEqualTo(2L);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("2");
    }

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    @Transactional
    public void shouldFindDocumentByPhraseAndStatusAndCreatorId() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setCreatorId(2L);
        documentQuery.setPhrase("fancy");
        documentQuery.setStatus("ARCHIVED");
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(1);
        assertThat(searchResults.getDocuments().get(0).getCreatorId()).isEqualTo(2L);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("2");
        assertThat(searchResults.getDocuments().get(0).getStatus()).isEqualTo("ARCHIVED");
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
        assertThat(searchResults.getDocuments().size()).isEqualTo(2);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("1");
        assertThat(searchResults.getDocuments().get(1).getNumber()).isEqualTo("2");
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
        assertThat(searchResults.getDocuments().size()).isEqualTo(2);
        assertThat(searchResults.getDocuments().get(0).getNumber()).isEqualTo("4");
        assertThat(searchResults.getDocuments().get(1).getNumber()).isEqualTo("fancy");
        assertThat(searchResults.getPagesCount()).isEqualTo(2);
        assertThat(searchResults.getPageNumber()).isEqualTo(2);
        assertThat(searchResults.getPerPage()).isEqualTo(2);
    }

}
