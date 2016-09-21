package in.vasista.vsales.transporter;

/**
 * Created by upendra on 12/7/16.
 */
public class Transporter {
    String id;
    String name;
    String phone;
    String address;

    public Transporter(String id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return id;
    }
}
