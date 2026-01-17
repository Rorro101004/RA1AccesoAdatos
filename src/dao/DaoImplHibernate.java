package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import model.Amount;
import model.Employee;
import model.Product;
import model.ProductHistory;

public class DaoImplHibernate implements Dao {
	private SessionFactory sessionFactory;
	@Override
	public void connect() {
	
	}

	@Override
	public void disconnect() {

	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		prepareSessionFactory(); 
	    Session session = sessionFactory.openSession();
	    Employee employee = null;
	    try {
	        String hql = "FROM Employee WHERE employeeId = :id AND password = :pass";
	        
	        employee = session.createQuery(hql, Employee.class)
	                .setParameter("id", employeeId)
	                .setParameter("pass", password)
	                .uniqueResult(); 
	                
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
	    return employee;
	}
	
	private void prepareSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration()
                        .configure("hibernate.cfg.xml")
                        .addAnnotatedClass(Product.class)
                        .addAnnotatedClass(Employee.class)
                        .addAnnotatedClass(ProductHistory.class)
                        .buildSessionFactory();
            } catch (Exception e) {
                System.err.println("Error fatal iniciando Hibernate");
            }
        }
    }
	@Override
	public ArrayList<Product> getInventory() {
		prepareSessionFactory();
		Session session = sessionFactory.openSession();
        ArrayList<Product> products = new ArrayList<>();
        try {
            List<Product> list = session.createQuery("FROM Product", Product.class).list();
            products.addAll(list);
        } catch (Exception e) {
            System.err.println("Error al obtener productos "  + e.getMessage());
        } finally {
            session.close();
        }
        System.out.println("Productos obtenidos correctamente");
        System.out.println(products);
        return products;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		prepareSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null; 
        try {
        	tx = session.beginTransaction(); 
            for (Product product : products) {
            	ProductHistory history = new ProductHistory(product);
				session.save(history);
			}
            tx.commit(); 
    	} catch (Exception e) {
        	if (tx != null) {
	            tx.rollback();
	        }
            System.err.println("Error al exportar productos "  + e.getMessage());
            return false;
        } finally {
            session.close();
        }
        System.out.println("Productos exportados correctamente");
        return true;
	}

	@Override
	public void addProduct(Product producto) {
		prepareSessionFactory(); 
	    Session session = sessionFactory.openSession();
	    Transaction tx = null; 
	    
	    try {
	        tx = session.beginTransaction(); 
	        
	        session.save(producto); 
	        tx.commit(); 
	        System.out.println("Producto guardado correctamente");
	        
	    } catch (Exception e) {
	        if (tx != null) {
	            tx.rollback();
	        }
	        System.err.println("Error guardando el producto: " + e.getMessage());
	    } finally {
	        session.close();
	    }
		
	}

	@Override
	public void updateProduct(Product producto) {
		prepareSessionFactory(); 
	    Session session = sessionFactory.openSession();
	    Transaction tx = null; 
	    
	    try {
	        tx = session.beginTransaction(); 
	        
	        session.update(producto); 
	        tx.commit(); 
	        System.out.println("Producto actualizado correctamente");
	        
	    } catch (Exception e) {
	        if (tx != null) {
	            tx.rollback();
	        }
	        System.err.println("Error actualizando el producto: " + e.getMessage());
	    } finally {
	        session.close();
	    }
		
	}

	@Override
	public void deleteProduct(int id) {
		prepareSessionFactory(); 
	    Session session = sessionFactory.openSession();
	    Transaction tx = null; 
	    
	    try {
	        tx = session.beginTransaction(); 
	       
	        String hql = "DELETE FROM Product WHERE id = :id";
	        Query query = session.createQuery(hql);
	        query.setParameter("id", id);
	        query.executeUpdate();
	        tx.commit(); 
	        System.out.println("Producto eliminado correctamente");
	        
	    } catch (Exception e) {
	        if (tx != null) {
	            tx.rollback();
	        }
	        System.err.println("Error eliminando el producto: " + e.getMessage());
	    } finally {
	        session.close();
	    }
		
	}
	
}
