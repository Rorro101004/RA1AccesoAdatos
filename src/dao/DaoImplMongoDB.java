package dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import model.Amount;
import model.Employee;
import model.Product;
public class DaoImplMongoDB implements Dao{
	private MongoClient client;
    private MongoDatabase db;
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		try {
            client = new MongoClient("localhost", 27017);
            db = client.getDatabase("shop");     
            System.out.println("Conexión a MongoDB exitosa.");
        } catch (Exception e) {
            System.err.println("Error conectando a MongoDB: " + e.getMessage());
        }
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (client != null) {
            client.close();
        }
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		connect();
		Employee employee = null;
        try {

            MongoCollection<Document> collection = db.getCollection("users");
            Bson filter = Filters.and(
                Filters.eq("employeeId", employeeId), 
                Filters.eq("password", password)
            );
            Document doc = collection.find(filter).first();
            if (doc != null) {
                employee = new Employee(
                    doc.getInteger("employeeId"), 
                    doc.getString("password"),
                    doc.getString("name")
                );
                System.out.println("Login con Mongo hecho correctamente");
            } else {
            	System.out.println("Login con Mongo hecho incorrectamente");
            }
            
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            e.printStackTrace();
        }
        
        return employee;
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> products = new ArrayList<>();
		MongoCursor<Document> cursor = null;
		try {
			MongoCollection<Document> collection = db.getCollection("inventory");
			cursor = collection.find().iterator();
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				Integer idManual = doc.getInteger("id"); 
				String name = doc.getString("name");
				Boolean available = doc.getBoolean("available");
				Integer stock = doc.getInteger("stock");
				Document priceDoc = (Document) doc.get("wholesalerPrice");
				Number priceNum = (Number) priceDoc.get("value");
				Double priceVal = priceNum.doubleValue();
				Amount wholesalerPrice = new Amount(priceVal);
				Amount publicPrice = new Amount(priceVal*1.5);
				Product p = new Product(idManual, name, publicPrice,wholesalerPrice,available, stock);
				products.add(p);
			}
			System.out.println("Inventario cargado: " + products.size() + " productos.");
			
		} catch (Exception e) {
			System.err.println("Error leyendo inventario de MongoDB: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return products;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		try {
			MongoCollection<Document> collection = db.getCollection("historical_inventory");
			List<Document> historicalInventory= new ArrayList<>();
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			for (Product p : products) {
				Document priceDoc = new Document()
						.append("value", p.getPublicPrice().getValue())
						.append("currency", "€");
				Document doc = new Document()
						.append("id", p.getId()) 
						.append("name", p.getName())
						.append("wholesalerPrice", priceDoc)
						.append("available", p.isAvailable())
						.append("stock", p.getStock())
						.append("created_at", currentTime); 
				historicalInventory.add(doc);
			}
			if (!historicalInventory.isEmpty()) {
				collection.insertMany(historicalInventory);
			}
			System.out.println("Historial exportado: " + historicalInventory.size() + " registros añadidos a historical_inventory.");
			return true;
		} catch (Exception e) {
			System.err.println("Error en la exportación histórica: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	private int getNextProductId() {
		int nextId = 1; 
		try {
			MongoCollection<Document> collection = db.getCollection("inventory");
			Document maxIdDoc = collection.find().sort(new Document("id", -1)).first();
			if (maxIdDoc != null && maxIdDoc.get("id") != null) {
				nextId = maxIdDoc.getInteger("id") + 1;
			}
		} catch (Exception e) {
			System.err.println("Error calculando el nuevo ID: " + e.getMessage());
		}
		
		return nextId;
	}
	@Override
	public void addProduct(Product producto) {
		try {
			MongoCollection<Document> collection = db.getCollection("inventory");
			int nuevoId = getNextProductId();
			producto.setId(nuevoId);
			Double valorPrecio = producto.getPublicPrice().getValue(); 
			Document priceDoc = new Document()
					.append("value", valorPrecio)
					.append("currency", "€"); 
			Document doc = new Document()
					.append("id", nuevoId)       
					.append("name", producto.getName())
					.append("wholesalerPrice", priceDoc)   
					.append("available", producto.isAvailable())
					.append("stock", producto.getStock());
			collection.insertOne(doc);
			System.out.println("Producto '" + producto.getName() + "' añadido correctamente.");
		} catch (Exception e) {
			System.err.println("Error añadiendo producto: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void updateProduct(Product producto) {
		// TODO Auto-generated method stub
		try {
			MongoCollection<Document> collection = db.getCollection("inventory");
			Bson filter = Filters.eq("id", producto.getId());
			Double nuevoPrecio = producto.getPublicPrice().getValue();
			Document priceDoc = new Document()
					.append("value", nuevoPrecio)
					.append("currency", "€");
			Bson updates = Updates.combine(
					Updates.set("name", producto.getName()),
					Updates.set("stock", producto.getStock()),
					Updates.set("available", producto.isAvailable()),
					Updates.set("wholesalerPrice", priceDoc) 
			);
			collection.updateOne(filter,updates);
			System.out.println("Producto con ID " + producto.getId() + " actualizado correctamente.");
		} catch (Exception e) {
			System.err.println("Error eliminando producto: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void deleteProduct(int id) {
		try {
			MongoCollection<Document> collection = db.getCollection("inventory");
			Bson filter = Filters.eq("id", id);
			collection.deleteOne(filter);
			System.out.println("Producto con ID " + id + " eliminado correctamente.");
			
		} catch (Exception e) {
			System.err.println("Error eliminando producto: " + e.getMessage());
			e.printStackTrace();
		}
		
	}

}
