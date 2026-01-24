package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Amount;
import model.Employee;
import model.Product;
import model.Sale;

public class DaoImplFile implements Dao{

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		// TODO Auto-generated method stub
		// locate file, path and name
		ArrayList<Product> productosLeidos = new ArrayList<Product>();
		File f = new File(System.getProperty("user.dir") + File.separator + "files/inputInventory.txt");
		
		try {			
			// wrap in proper classes
			FileReader fr;
			fr = new FileReader(f);				
			BufferedReader br = new BufferedReader(fr);
			
			// read first line
			String line = br.readLine();
			
			// process and read next line until end of file
			while (line != null) {
				// split in sections
				String[] sections = line.split(";");
				
				String name = "";
				double wholesalerPrice=0.0;
				int stock = 0;
				
				// read each sections
				for (int i = 0; i < sections.length; i++) {
					// split data in key(0) and value(1) 
					String[] data = sections[i].split(":");
					
					switch (i) {
					case 0:
						// format product name
						name = data[1];
						break;
						
					case 1:
						// format price
						wholesalerPrice = Double.parseDouble(data[1]);
						break;
						
					case 2:
						// format stock
						stock = Integer.parseInt(data[1]);
						break;
						
					default:
						break;
					}
				}
				productosLeidos.add(new Product());
				
				// read next line
				line = br.readLine();
			}
			fr.close();
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(productosLeidos);
		return productosLeidos;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		// TODO Auto-generated method stub
		LocalDate myObj = LocalDate.now();
		String fileName = "inventory_" + myObj.toString() + ".txt";
		int cantProductos = 0;
		//ArrayList<Sale> salesToWrite = new ArrayList<Sale>();
		// locate file, path and name
		// quitar el file separator para provocar el error
		File f = new File(System.getProperty("user.dir") + "files" + File.separator + fileName);
		try {
			// wrap in proper classes
			FileWriter fw;
			fw = new FileWriter(f, true);
			PrintWriter pw = new PrintWriter(fw);
			for (Product product : products) {	
				pw.write("Product: "+ product.getName()+"; Stock: "+product.getStock()+";\n");
				++cantProductos;
			}
			pw.write("Número total de productos: "+cantProductos);
			
			// close files
			pw.close();
			fw.close();
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
	}

	@Override
	public void addProduct(Product producto) {
		
		
	}

	@Override
	public void updateProduct(Product producto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProduct(int name) {
		// TODO Auto-generated method stub
		
	}


}
