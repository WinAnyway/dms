package pl.com.bottega.dms.model.numbers;

import pl.com.bottega.dms.model.document.DocumentNumber;

public class DemoNumberGenerator implements NumberGenerator{

    NumberGenerator numberGenerator;

    public DemoNumberGenerator(NumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    @Override
    public DocumentNumber generate() {
        return new DocumentNumber("DEMO-" + numberGenerator.generate().getNumber());
    }
}
