package model;

import main.Logable;
import dao.*;
import javax.persistence.*;
@Entity
@Table(name = "employee") 
public class Employee extends Person implements Logable{
	    @Id
	    @Column
	    private int employeeId;

	    @Column
	    private String name;

	    @Column
	    private String password; 
	  
	    @Transient 
	    private Dao dao; 

	
	    public Employee() {
	        super();
	    }

	    public Employee(int employeeId, String name, String password) {
	        super(name);
	        this.employeeId = employeeId;
	        this.name = name;
	        this.password = password;
	    }
	
	/**
	 * @return the employeeId
	 */
	public int getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	public void setDao(Dao dao) {
        this.dao = dao;
    }
	/**
	 * @param user from application, password from application
	 * @return true if credentials are correct or false if not
	 */
	@Override
	public boolean login(int user, String password) {
        if (this.dao == null) {
            System.err.println("Error: El empleado no tiene DAO asignado.");
            return false;
        }
        Employee e = dao.getEmployee(user, password);
        return (e != null);
	}

}
