package in.vasista.vsales.stocks;

import java.util.Date;

public class Stock {


	String prodid,prodname,depot,supp,spec;
	double qty,price;

	public Stock(String prodid, String prodname, String depot, String supp, String spec, double qty, double price) {
		this.prodid = prodid;
		this.prodname = prodname;
		this.depot = depot;
		this.supp = supp;
		this.spec = spec;
		this.qty = qty;
		this.price = price;
	}

	public String getProdid() {
		return prodid;
	}

	public void setProdid(String prodid) {
		this.prodid = prodid;
	}

	public String getProdname() {
		return prodname;
	}

	public void setProdname(String prodname) {
		this.prodname = prodname;
	}

	public String getDepot() {
		return depot;
	}

	public void setDepot(String depot) {
		this.depot = depot;
	}

	public String getSupp() {
		return supp;
	}

	public void setSupp(String supp) {
		this.supp = supp;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
