package pl.com.bottega.dms.model.printing;

import pl.com.bottega.dms.model.document.Document;

import java.math.BigDecimal;

public interface PrintCostCalculator {

    BigDecimal calculateCost(Document document);

}
