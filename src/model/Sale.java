package model;

public class Sale {

	private String barcode;
	private String name;
	private double unitamount;
	private double quantity;
	private double discount;
	private double totalamount;


	
	
	
	
	public Sale(String barcode, String name, double unitamount, int quantity, double discount, double totalamount) {
		super();
		this.barcode = barcode;
		this.name = name;
		this.unitamount = unitamount;
		this.quantity = quantity;
		this.discount = discount;
		this.totalamount = totalamount;

	}

	public Sale() {

	}

	
	
	public Sale(String name, double unitamount, int quantity, double totalamount) {
		super();
		this.name = name;
		this.unitamount = unitamount;
		this.quantity = quantity;
		this.totalamount = totalamount;
	}

	public Sale(double totalamount) {
		
	}



	
	


	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getUnitamount() {
		return unitamount;
	}

	public void setUnitamount(double unitamount) {
		this.unitamount = unitamount;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}

}
