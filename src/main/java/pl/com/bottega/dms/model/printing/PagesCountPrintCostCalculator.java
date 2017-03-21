package pl.com.bottega.dms.model.printing;

import pl.com.bottega.dms.model.document.Document;

import java.math.BigDecimal;

public class PagesCountPrintCostCalculator implements PrintCostCalculator{

    private static final BigDecimal HIGH_PAGES_COUNT_COST = new BigDecimal(40);
    private static final int PAGES_COUNT_LIMIT = 100;

    private PrintCostCalculator printCostCalculator;

    public PagesCountPrintCostCalculator(PrintCostCalculator printCostCalculator) {
        this.printCostCalculator = printCostCalculator;
    }

    @Override
    public BigDecimal calculateCost(Document document) {
        if(document.getPagesCount() > PAGES_COUNT_LIMIT)
            return printCostCalculator.calculateCost(document).add(HIGH_PAGES_COUNT_COST);
        else
            return printCostCalculator.calculateCost(document);
    }
}
