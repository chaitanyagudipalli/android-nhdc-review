package in.vasista.vsales.indent;

/**
 * Created by upendra on 25/7/16.
 */
public class IndentItemNHDC {
    String productId,remarks,yarnUOM,taxRateList;

    String quantity,baleQuantity,bundleWeight,bundleUnitPrice,basicPrice,serviceCharge,serviceChargeAmt;

    String totalAmt;

    float vatPercent, vatAmount, cstPercent, cstAmount, shippedQty,discountAmount,otherCharges;

    String spec;

    int id, indentId;
    public IndentItemNHDC(int id, int indentId, String productId, String quantity, String remarks, String baleQuantity, String bundleWeight, String bundleUnitPrice, String yarnUOM, String basicPrice, String serviceCharge, String serviceChargeAmt, String totalAmt, float vatPercent, float vatAmount, float cstPercent, float cstAmount, float shippedQty, float discountAmount, float otherCharges, String specification) {
        this.productId = productId;
        this.quantity = quantity;
        this.remarks = remarks;
        this.baleQuantity = baleQuantity;
        this.bundleWeight = bundleWeight;
        this.bundleUnitPrice = bundleUnitPrice;
        this.yarnUOM = yarnUOM;
        this.basicPrice = basicPrice;
        this.serviceCharge = serviceCharge;
        this.serviceChargeAmt = serviceChargeAmt;
        this.totalAmt = totalAmt;
        this.vatPercent = vatPercent;
        this.vatAmount = vatAmount;
        this.cstPercent = cstPercent;
        this.cstAmount = cstAmount;
        this.shippedQty = shippedQty;
        this.discountAmount = discountAmount;
        this.otherCharges = otherCharges;

        this.id = id;
        this.indentId = indentId;

        this.spec = specification;
    }

    public String getProductId() {
        return productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getBaleQuantity() {
        return baleQuantity;
    }

    public String getBundleWeight() {
        return bundleWeight;
    }

    public String getBundleUnitPrice() {
        return bundleUnitPrice;
    }

    public String getYarnUOM() {
        return yarnUOM;
    }

    public String getBasicPrice() {
        return basicPrice;
    }

    public String getTaxRateList() {
        return taxRateList;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public String getServiceChargeAmt() {
        return serviceChargeAmt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndentId() {
        return indentId;
    }

    public void setIndentId(int indentId) {
        this.indentId = indentId;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public float getVatPercent() {
        return vatPercent;
    }

    public float getVatAmount() {
        return vatAmount;
    }

    public float getCstPercent() {
        return cstPercent;
    }

    public float getCstAmount() {
        return cstAmount;
    }

    public float getShippedQty() {
        return shippedQty;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public float getOtherCharges() {
        return otherCharges;
    }

    public String getSpec() {
        return spec;
    }
}
