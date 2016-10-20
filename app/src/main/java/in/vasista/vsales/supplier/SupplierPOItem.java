package in.vasista.vsales.supplier;

public class SupplierPOItem {

	String poid, prodid, itemname, spec, orderNum,status;
	double unitPrice, itemQty, dispatchQty, balanceQty;

	public SupplierPOItem(String poid, String prodid, String itemname, String spec, String orderNum, String status, double unitPrice, double itemQty, double dispatchQty, double balanceQty) {
		this.poid = poid;
		this.prodid = prodid;
		this.itemname = itemname;
		this.spec = spec;
		this.orderNum = orderNum;
		this.status = status;
		this.unitPrice = unitPrice;
		this.itemQty = itemQty;
		this.dispatchQty = dispatchQty;
		this.balanceQty = balanceQty;
	}

	public String getPoid() {
		return poid;
	}


	public String getProdid() {
		return prodid;
	}

	public void setProdid(String prodid) {
		this.prodid = prodid;
	}

	public void setPoid(String poid) {
		this.poid = poid;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getItemQty() {
		return itemQty;
	}

	public void setItemQty(double itemQty) {
		this.itemQty = itemQty;
	}

	public double getDispatchQty() {
		return dispatchQty;
	}

	public void setDispatchQty(double dispatchQty) {
		this.dispatchQty = dispatchQty;
	}

	public double getBalanceQty() {
		return balanceQty;
	}

	public void setBalanceQty(double balanceQty) {
		this.balanceQty = balanceQty;
	}
}
