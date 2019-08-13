package il.co.ilrd.vendingmachine;

public class Product {
	private double price;
	private String name;

	public Product(String name, double price) {
		this.price = price;
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + "]";
	}
}