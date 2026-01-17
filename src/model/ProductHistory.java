package model;

import java.sql.Timestamp;

import javax.persistence.*;
@Entity
@Table(name = "historical_inventory")
public class ProductHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id; 

    @Column(name = "id_product")
    private int idProduct; 

    @Column(name = "created_at")
    private Timestamp createdAt; 

    @Column(name = "name")
    private String name;
    
    @Column
    private double price;

    @Column
    private int stock;

    @Column
    private boolean available;

    public ProductHistory() {}

    public ProductHistory(Product product) {
        this.idProduct = product.getId();
        this.name = product.getName();
        this.price = product.getPrice(); 
        this.stock = product.getStock();
        this.available = product.isAvailable();
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
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

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
    
}
