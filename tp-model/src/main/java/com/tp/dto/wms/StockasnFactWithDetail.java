package com.tp.dto.wms;

import java.util.List;

import com.tp.model.BaseDO;
import com.tp.model.wms.StockasnDetailFact;
import com.tp.model.wms.StockasnFact;

/**
 * Created by ldr on 7/8/2016.
 */
public class StockasnFactWithDetail extends BaseDO {

    private static final long serialVersionUID = -2453893030677435068L;

    private StockasnFact stockasnFact;

    private List<StockasnDetailFact> stockasnDetailFacts;

    public StockasnFactWithDetail(StockasnFact stockasnFact, List<StockasnDetailFact> stockasnDetailFacts) {
        this.stockasnFact = stockasnFact;
        this.stockasnDetailFacts = stockasnDetailFacts;
    }

    public StockasnFact getStockasnFact() {
        return stockasnFact;
    }

    public void setStockasnFact(StockasnFact stockasnFact) {
        this.stockasnFact = stockasnFact;
    }

    public List<StockasnDetailFact> getStockasnDetailFacts() {
        return stockasnDetailFacts;
    }

    public void setStockasnDetailFacts(List<StockasnDetailFact> stockasnDetailFacts) {
        this.stockasnDetailFacts = stockasnDetailFacts;
    }
}
