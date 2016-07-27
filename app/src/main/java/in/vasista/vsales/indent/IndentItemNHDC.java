package in.vasista.vsales.indent;

/**
 * Created by upendra on 25/7/16.
 */
public class IndentItemNHDC {
    String productId,remarks,yarnUOM,taxRateList;
    String quantity,baleQuantity,bundleWeight,bundleUnitPrice,basicPrice,serviceCharge,serviceChargeAmt;

    public IndentItemNHDC(String productId, String quantity, String remarks, String baleQuantity, String bundleWeight, String bundleUnitPrice, String yarnUOM, String basicPrice, String serviceCharge, String serviceChargeAmt) {
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
}
