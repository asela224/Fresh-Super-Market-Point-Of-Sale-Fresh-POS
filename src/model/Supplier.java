package model;

public class Supplier {
	
	int id;
	String name;
	String address;
	String contact;
	String lastSupplied;
	

	
	public Supplier(int id, String name,String address,String contact, String lastSupplied) {
		super();
		this.id = id;
		this.name = name;
		this.address=address;
		this.contact=contact;
		this.lastSupplied = lastSupplied;
	}
	
	public Supplier() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getLastSupplied() {
		return lastSupplied;
	}

	public void setLastSupplied(String lastSupplied) {
		this.lastSupplied = lastSupplied;
	}
	
	

}
