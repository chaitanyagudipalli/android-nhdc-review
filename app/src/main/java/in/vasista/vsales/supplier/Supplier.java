package in.vasista.vsales.supplier;

/**
 * Created by upendra on 12/7/16.
 */
public class Supplier {
    String id;
    String name;
    String roletypeid;
    String partytypeid;


    public Supplier(String id, String name, String roletypeid, String partytypeid) {
        super();
        this.id = id;
        this.name = name;
        this.roletypeid = roletypeid;
        this.partytypeid = partytypeid;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoletypeid() {
        return roletypeid;
    }

    public String getPartytypeid() {
        return partytypeid;
    }

    @Override
    public String toString() {
        return id;
    }
}
