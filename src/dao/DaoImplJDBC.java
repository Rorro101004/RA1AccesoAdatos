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
		// TODO Auto-generated method stub
		ArrayList<Product> inventario = new ArrayList<Product>();
		String useShop = "use shop";
		String query = "Select * from inventory";
		try {
			Statement stmt = connection.createStatement();
			stmt.execute(useShop);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
		        //String id = String.valueOf(rs.getInt("id"));
		        String name = rs.getString("name");
		        Amount price =  new Amount(rs.getDouble("wholesaler_price"));
		        boolean available = rs.getBoolean("available");
		        int stock = rs.getInt("stock");
		        Product producto = new Product(name, price, available, stock);
		        inventario.add(producto);
		        // ver el tema de find product
		        // Guardamos cada fila como array de Strings
		    }
			System.out.println(inventario);
			return inventario;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return null;
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


	@Override
	public void addProduct(Product producto) {
		String sql = "INSERT INTO inventory (name, wholesaler_price, available, stock) VALUES (?, ?, ?, ?)";
	    try {
	    	PreparedStatement stmt = connection.prepareStatement(sql); 
	        stmt.setString(1, producto.getName());
	        stmt.setDouble(2, producto.getWholesalerPrice().getValue());
	        stmt.setBoolean(3, true);
	        stmt.setInt(4, producto.getStock());
	        stmt.executeUpdate();
	    }
	    catch (Exception e) {
			System.err.println("Error al insertar producto "+ e.getMessage());
		}
		
	}

	@Override
	public void updateProduct(Product producto) {
	    String sql = "UPDATE inventory SET stock = ? WHERE name = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, producto.getStock());   // nuevo stock
	        stmt.setString(2, producto.getName());      // id del producto a actualizar
	        int rowsAffected = stmt.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            System.out.println("Stock actualizado correctamente para el producto con nombre " + producto.getName());
	        } else {
	            System.out.println("No se encontró producto con nombre " + producto.getName());
	        }
	    } catch (Exception e) {
	        System.err.println("Error al actualizar stock: " + e.getMessage());
	    }
	}


	@Override
	public void deleteProduct(String name) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM inventory WHERE name= ?";
		try {
		    PreparedStatement stmt = connection.prepareStatement(sql);
		    stmt.setString(1, name); 
		    int rowsAffected = stmt.executeUpdate();
		    if (rowsAffected > 0) {
		        System.out.println("Producto eliminado correctamente.");
		    } else {
		        System.out.println("No se encontró el producto con ese nombre.");
		    }
		} catch (Exception e) {
		    System.err.println("Error al eliminar producto: " + e.getMessage());
		}

		
	}
	

}
