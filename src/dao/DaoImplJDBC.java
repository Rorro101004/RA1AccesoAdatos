package dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao {
	Connection connection;

	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1,employeeId);
    	  	ps.setString(2,password);
    	  	//System.out.println(ps.toString());
            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            		employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
            	}
            }
        } catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
    	return employee;
	}
	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> products = new ArrayList<Product>();
	    String query = "SELECT * FROM inventory"; 

	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) { 

	        while (rs.next()) {

	            int id = rs.getInt("id"); 
	            String name = rs.getString("name");
	            double price = rs.getDouble("wholesalerPrice"); 
	            int stock = rs.getInt("stock");
	            boolean available = rs.getBoolean("available");
	            Product p = new Product();
	            
	            p.setId(id);

	            products.add(p);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    
	    return products;
	}
	

	public boolean writeInventory(ArrayList<Product> products) {
	    String sql = "INSERT INTO historical_inventory ( name, wholesaler_price, available, stock, created_at) VALUES (?, ?, ?, ?, ?)";
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {

	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    	String now = LocalDateTime.now().format(formatter);

	        for (Product p : products) {
	            stmt.setString(1, p.getName());
	            stmt.setDouble(2, p.getWholesalerPrice().getValue());
	            stmt.setBoolean(3, p.isAvailable());
	            stmt.setInt(4, p.getStock());
	            stmt.setString(5, now); // created_at como DATETIME
	            stmt.addBatch(); // agrupa para ejecutar en lote
	        }

	        stmt.executeBatch(); // ejecuta todos los inserts juntos
	        System.out.println("Inventario exportado correctamente a historical_inventory.");
	        return true;
	    } catch (SQLException e) {
	        System.err.println("Error al exportar inventario: " + e.getMessage());
	        return false;
	    }
	}


	public void addProduct(Product producto) {
		
		
	}

	@Override
	public void updateProduct(Product producto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProduct(int id) {
		// TODO Auto-generated method stub
		
	}
}
