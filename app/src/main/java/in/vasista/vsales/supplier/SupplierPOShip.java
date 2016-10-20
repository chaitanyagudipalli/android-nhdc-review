package in.vasista.vsales.supplier;

public class SupplierPOShip {

	String shipid, poid, productid, orderdate, itemname, itemSeqId;
	double qty, unitPrice, itemAmnt;

	public SupplierPOShip(String shipid, String poid, String productid, String orderdate, String itemname, String itemSeqId, double qty, double unitPrice, double itemAmnt) {
		this.shipid = shipid;
		this.poid = poid;
		this.productid = productid;
		this.orderdate = orderdate;
		this.itemname = itemname;
		this.itemSeqId = itemSeqId;
		this.qty = qty;
		this.unitPrice = unitPrice;
		this.itemAmnt = itemAmnt;
	}

	public String getShipid() {
		return shipid;
	}

	public void setShipid(String shipid) {
		this.shipid = shipid;
	}

	public String getPoid() {
		return poid;
	}

	public void setPoid(String poid) {
		this.poid = poid;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getItemSeqId() {
		return itemSeqId;
	}

	public void setItemSeqId(String itemSeqId) {
		this.itemSeqId = itemSeqId;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getItemAmnt() {
		return itemAmnt;
	}

	public void setItemAmnt(double itemAmnt) {
		this.itemAmnt = itemAmnt;
	}
}
