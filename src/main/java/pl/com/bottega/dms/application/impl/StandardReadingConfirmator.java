package pl.com.bottega.dms.application.impl;

import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.dms.application.ReadingConfirmator;
import pl.com.bottega.dms.model.Document;
import pl.com.bottega.dms.model.DocumentNumber;
import pl.com.bottega.dms.model.DocumentRepository;
import pl.com.bottega.dms.model.commands.ConfirmDocumentCommand;
import pl.com.bottega.dms.model.commands.ConfirmForDocumentCommand;

public class StandardReadingConfirmator implements ReadingConfirmator{

    private final DocumentRepository documentRepository;

    public StandardReadingConfirmator(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    @Transactional
    public void confirm(ConfirmDocumentCommand cmd) {
        Document document = documentRepository.get(new DocumentNumber(cmd.getNumber()));
        document.confirm(cmd);
    }

    @Override
    @Transactional
    public void confirmFor(ConfirmForDocumentCommand cmd) {
        Document document = documentRepository.get(new DocumentNumber(cmd.getNumber()));
        document.confirmFor(cmd);
    }
}
