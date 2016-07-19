package in.vasista.vsales.catalog;

public class Product {
	String id;
	String name = "",internalname ="", brandname = "";
	String description = "",typeid="",UOMid = "";
	float price;
	float includedquantity;

	String productCategoryId = "";


	public Product(String id, String name, String internalname, String brandname, String description, String typeid, String UOMid, float price, float includedquantity, String productCategoryId) {
		this.id = id;
		this.name = name;
		this.internalname = internalname;
		this.brandname = brandname;
		this.description = description;
		this.typeid = typeid;
		this.UOMid = UOMid;
		this.price = price;
		this.includedquantity = includedquantity;
		this.productCategoryId = productCategoryId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getInternalname() {
		return internalname;
	}

	public String getBrandname() {
		return brandname;
	}

	public String getDescription() {
		return description;
	}

	public String getTypeid() {
		return typeid;
	}

	public String getUOMid() {
		return UOMid;
	}

	public float getPrice() {
		return price;
	}

	public float getIncludedquantity() {
		return includedquantity;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}
}
