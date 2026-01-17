package model;

import javax.persistence.*;
@Entity 
@Table(name = "inventory")
public class Product {
	@Id                                       
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column                   
	private int id;
	@Column(name = "name")
    private String name;
	@Transient 
    private Amount publicPrice; 
    @Transient
    private Amount wholesalerPrice;
    @Column
    private double price;
    @Column
    private boolean available;
    @Column
    private int stock;
    @Transient
    private static int totalProducts;
    @PostLoad
    public void loadAmounts() {
        this.publicPrice = new Amount(this.price);
        this.wholesalerPrice = new Amount(this.price / 1.5);
    }
    public final static double EXPIRATION_RATE = 0.60;
    
    public Product() {
    }
    
	public Product(int id, String name, Amount publicPrice, Amount wholesalerPrice,  boolean available,
			int stock) {
		super();
		this.id = id;
		this.name = name;
		this.publicPrice = publicPrice;
		this.wholesalerPrice = wholesalerPrice;
		this.available = available;
		this.stock = stock;
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
	public Amount getPublicPrice() {
		return publicPrice;
	}

	public void setPublicPrice(Amount publicPrice) {
		this.publicPrice = publicPrice;
	}

	public Amount getWholesalerPrice() {
		return wholesalerPrice;
	}

	public void setWholesalerPrice(Amount wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public static int getTotalProducts() {
		return totalProducts;
	}

	public static void setTotalProducts(int totalProducts) {
		Product.totalProducts = totalProducts;
	}
	
	public void expire() {
		this.publicPrice.setValue(this.getPublicPrice().getValue()*EXPIRATION_RATE); ;
	}
	

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", publicPrice=" + publicPrice + ", wholesalerPrice=" + wholesalerPrice
				+ ", available=" + available + ", stock=" + stock + "]";
	}

	
	
	
	
	

    

    
}
