package in.vasista.vsales.indent;

import java.util.Date;

import in.vasista.vsales.IndentActivity;
import in.vasista.vsales.db.MySQLiteHelper;

public class Indent {
	int id;

	String tallyRefNo, POorder, poSquenceNo;
	boolean isgeneratedPO;
	String supplierPartyId,tId, storeName, supplierPartyName,orderNo, orderId;
	Date orderDate;
	String statusId;

	double orderTotal, paidAmt, balance,totDiscountAmt;
	String schemeType,prodstoreid;


	public Indent(int id, String tallyRefNo, String POorder, String poSquenceNo, boolean isgeneratedPO,
				  String supplierPartyId,String tId, String storeName, String supplierPartyName, String orderNo,
				  String orderId, Date orderDate, String statusId, double orderTotal, double paidAmt, double balance,
				  String schemeType, String prodstoreid, float totDiscountAmt) {
		this.id = id;
		this.tallyRefNo = tallyRefNo;
		this.POorder = POorder;
		this.poSquenceNo = poSquenceNo;
		this.isgeneratedPO = isgeneratedPO;
		this.supplierPartyId = supplierPartyId;
		this.tId = tId;
		this.storeName = storeName;
		this.supplierPartyName = supplierPartyName;
		this.orderNo = orderNo;
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.statusId = statusId;
		this.orderTotal = orderTotal;
		this.paidAmt = paidAmt;
		this.balance = balance;
		this.schemeType = schemeType;
		this.prodstoreid = prodstoreid;
		this.totDiscountAmt = totDiscountAmt;
	}


	public String getSchemeType() {
		return schemeType;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}



	public Date getOrderDate() {
		return orderDate;
	}

	public boolean isgeneratedPO() {
		return isgeneratedPO;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public double getPaidAmt() {
		return paidAmt;
	}

	public double getBalance() {
		return balance;
	}

	public String getTallyRefNo() {
		return tallyRefNo;
	}

	public String getPOorder() {
		return POorder;
	}

	public String getPoSquenceNo() {
		return poSquenceNo;
	}

	public String getSupplierPartyId() {
		return supplierPartyId;
	}

	public String gettId() {
		return tId;
	}

	public String getStoreName() {
		return storeName;
	}

	public String getSupplierPartyName() {
		return supplierPartyName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getStatusId() {
		return statusId;
	}

	public String getProdstoreid() {
		return prodstoreid;
	}

	public double getTotDiscountAmt() {
		return totDiscountAmt;
	}

	@Override
	public String toString() {
		return "Indent{" +
				"id=" + id +
				", tallyRefNo='" + tallyRefNo + '\'' +
				", POorder='" + POorder + '\'' +
				", poSquenceNo='" + poSquenceNo + '\'' +
				", isgeneratedPO=" + isgeneratedPO +
				", supplierPartyId='" + supplierPartyId + '\'' +
				", storeName='" + storeName + '\'' +
				", supplierPartyName='" + supplierPartyName + '\'' +
				", orderNo='" + orderNo + '\'' +
				", orderId='" + orderId + '\'' +
				", orderDate=" + orderDate +
				", statusId='" + statusId + '\'' +
				", orderTotal=" + orderTotal +
				", paidAmt=" + paidAmt +
				", balance=" + balance +
				'}';
	}
}
