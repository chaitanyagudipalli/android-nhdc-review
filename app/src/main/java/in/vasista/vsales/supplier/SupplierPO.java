package in.vasista.vsales.supplier;

import java.util.Date;

public class SupplierPO {

	String poid, orderId, orderNum,status;

	String orderdate;

	public SupplierPO(String poid, String orderdate, String orderId, String orderNum, String status) {
		this.poid = poid;
		this.orderdate = orderdate;
		this.orderId = orderId;
		this.orderNum = orderNum;
		this.status = status;
	}

	public String getPoid() {
		return poid;
	}

	public void setPoid(String poid) {
		this.poid = poid;
	}

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
}
