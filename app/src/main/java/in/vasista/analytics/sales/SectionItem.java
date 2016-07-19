package in.vasista.analytics.sales;

/**
 * Created by vasista on 17/5/16.
 */
public class SectionItem implements Item{

    private final String title;
    private final String totelQuantity;
    private final String totelRevenue;

    public SectionItem(String title, String quantity, String revenue) {
        this.title = title;
        this.totelQuantity = quantity;
        this.totelRevenue = revenue;
    }

    public String getTitle(){
        return title;
    }

    public String getTotelQuantity() {
        return totelQuantity;
    }

    public String getTotelRevenue() {
        return totelRevenue;
    }

    @Override
    public boolean isSection() {
        return true;
    }

}