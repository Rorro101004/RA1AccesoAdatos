package dao;

import java.util.ArrayList;

import model.Employee;
import model.Product;

public interface Dao {
	
	public void connect();

	public void disconnect();

	public Employee getEmployee(int employeeId, String password);
	
	// Devuelve lista de productos
	public ArrayList<Product> getInventory();
	// Devuelve true si se ejecuta correctamente
	public boolean writeInventory(ArrayList<Product> products);
	//Crea un producto
	public void addProduct(Product producto);
	//Actualiza un producto
	public void updateProduct(Product producto);
	//Borrar un producto
	public void deleteProduct(String name);

}
