package in.vasista.analytics.sales;

/**
 * Created by Bekkam on 17/5/16.
 */
public class EntryItem implements Item{

    public final String productCategoryName;
    public final String productCategoryTQ;
    public final String productCategoryTR;

    public EntryItem(String categoryName, String categoryTQ, String categoryTR) {
        this.productCategoryName = categoryName;
        this.productCategoryTQ = categoryTQ;
        this.productCategoryTR = categoryTR;
    }

    @Override
    public boolean isSection() {
        return false;
    }

}