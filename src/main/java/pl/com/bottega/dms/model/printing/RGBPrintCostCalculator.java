package pl.com.bottega.dms.model.printing;

import pl.com.bottega.dms.model.document.Document;

import java.math.BigDecimal;

public class RGBPrintCostCalculator implements PrintCostCalculator {

    public BigDecimal calculateCost(Document document) {
        return new BigDecimal(5);
    }

}
