package pl.com.bottega.dms.application;

import org.apache.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.com.bottega.dms.model.events.DocumentPublishEvent;


@Component
public class PrintDocumentScheduler {

    @EventListener
    public void documentPublished(DocumentPublishEvent event) {
        Logger.getLogger(PrintDocumentScheduler.class).info("Scheduling document printing!");
    }
}
