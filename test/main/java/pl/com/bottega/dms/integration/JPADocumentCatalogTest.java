package pl.com.bottega.dms.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.com.bottega.dms.application.DocumentQuery;
import pl.com.bottega.dms.application.DocumentSearchResults;
import pl.com.bottega.dms.infrastructure.JPADocumentCatalog;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JPADocumentCatalogTest {

    @Autowired
    private JPADocumentCatalog catalog;

    @Test
    @Sql("/fixtures/documentByPhrase.sql")
    public void shouldFindDocumentsByPhrase() {
        //when
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setPhrase("fancy");
        DocumentSearchResults searchResults = catalog.find(documentQuery);
        //then
        assertThat(searchResults.getDocuments().size()).isEqualTo(3);
    }

}
