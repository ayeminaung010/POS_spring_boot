package com.example.demo.dto;

public class CartItem {
	private String Id;
	private String name;
	private double price;
	private String image;

	private double stock;
	private int quantity;

	@Override
	public String toString() {
		return "CartItem{" + "id='" + Id + '\'' + ", name='" + name + '\'' + ", price=" + price + ", image='" + image
				+ '\'' + ", stock=" + stock + ", quantity=" + quantity + '}';
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
