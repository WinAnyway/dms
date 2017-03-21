package pl.com.bottega.dms.model.printing;

import pl.com.bottega.dms.model.document.Document;
import pl.com.bottega.dms.model.document.DocumentType;

import java.math.BigDecimal;

public class ManualPrintCostCalculator implements PrintCostCalculator {

    private static final BigDecimal  COST_FACTOR = BigDecimal.valueOf(1.3);

    private PrintCostCalculator printCostCalculator;

    public ManualPrintCostCalculator(PrintCostCalculator printCostCalculator) {
        this.printCostCalculator = printCostCalculator;
    }

    @Override
    public BigDecimal calculateCost(Document document) {
        if (document.getType().equals(DocumentType.MANUAL)) {
            BigDecimal result = printCostCalculator.calculateCost(document);
            return result.multiply(COST_FACTOR);
        } else
            return printCostCalculator.calculateCost(document);
    }
}
