package pl.com.bottega.dms.application;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.com.bottega.dms.model.events.DocumentPublishEvent;

@Component
public class DocumentPublishedEmailNotifier {

    @TransactionalEventListener
    public void documentPublished(DocumentPublishEvent event) {
        Logger.getLogger(PrintDocumentScheduler.class).info("Mailing to recipients!");
    }

}
