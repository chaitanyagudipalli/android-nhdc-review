package in.vasista.vsales.catalog;

public class Product {
	String id;
	String name = "",internalname ="", brandname = "";
	String description = "",typeid="",UOMid = "";
	float price;
	float includedquantity;

	String productCategoryId = "";
	String productParentCategoryId = "";

	String scheme = "";


	public Product(String id, String name, String internalname, String brandname, String description, String typeid, String UOMid, float price, float includedquantity, String productCategoryId, String productParentCategoryId, String scheme) {
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
		this.productParentCategoryId = productParentCategoryId;
		this.scheme = scheme;
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

	public String getProductParentCategoryId() {
		return productParentCategoryId;
	}

	public String getScheme() {
		return scheme;
	}
}
