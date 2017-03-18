package pl.com.bottega.dms.model;

import pl.com.bottega.dms.model.commands.CreateDocumentCommand;
import pl.com.bottega.dms.model.numbers.NumberGenerator;

public class DocumentFactory {

    NumberGenerator numberGenerator;

    public DocumentFactory(NumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    public Document createDocument(CreateDocumentCommand cmd) {
        return new Document(cmd, numberGenerator.generate());
    }

}
