package dao;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import model.Employee;
import model.Product;
public class DaoImplObjectDB implements Dao{
	private EntityManagerFactory emf;
    private EntityManager em;
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		emf = Persistence.createEntityManagerFactory("objects/db_shop.odb");
		em = emf.createEntityManager();
	    try {
	        em.getTransaction().begin();
	        Employee admin = new Employee(123, "Admin", "test");
	        em.persist(admin); 
	        em.getTransaction().commit(); 
	        System.out.println("Empleado de prueba añadido con éxito.");
	        
	    } catch (Exception e) {
	        if (em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	    }
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (em != null) em.close(); 
	    if (emf != null) emf.close(); 
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		try {
	        TypedQuery<Employee> query = em.createQuery(
	            "SELECT e FROM Employee e WHERE e.employeeId = :id AND e.password = :pw", 
	            Employee.class
	        );
	        query.setParameter("id", employeeId);
	        query.setParameter("pw", password);
	        
	        return query.getSingleResult(); 
	    } catch (NoResultException e) {
	        return null; 
	    }
	}

	@Override
	public ArrayList<Product> getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addProduct(Product producto) {
		// TODO Auto-generated method stub
		
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
