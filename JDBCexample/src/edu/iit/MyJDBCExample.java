package edu.iit;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class MyJDBCExample {

	public static final String JDBC_DRIVER = "org.postgresql.Driver";
	// postgres URLs are of the form: jdbc:postgresql://host:port/database
	public static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/cs425";
	public static final String DBUSER = "postgres";
	public static final String DBPASSWD = "test";
	
	public static void main (String[] args) throws Exception {
		try {
			// load the driver
			Class.forName(JDBC_DRIVER);
			// create a connection
			Connection c = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
			// create a statement object to execute commands
			Statement s = c.createStatement();

			// now we can use the statement object to execute SQL commands and retrieve results
			// let's first execute a query, the executeQuery method runs a query and returns a ResultSet object that encapsulates the query result
			ResultSet r = s.executeQuery("SELECT name FROM student ORDER BY name ASC;");

			// the ResultSet object has a method next() that move a pointer to the next row of the query result
			// initially the pointer points to one row before the first result, i.e., the first call to next() will advance the pointer to the first result
			// the next() method returns true if the pointer is now pointing to an existing row and false if it has advanced beyond the last query result
			// often next() if used as a loop condition to loop through all results as shown below:
			int i = 1;
			while(r.next()) {
				// the ResultSet object is also used to access the values of the current row
				// for this it implements getX() methods that takes as argument either a position of a column in the result (counting from 1) or the name of a column
				// there are multiple getX methods to return the value of the column as a particular Java data type, e.g., getString() returns the value as a Java String
				String curName = r.getString(1);
				System.out.println("Row: " + i + " values of attribute name is: " + curName);
				System.out.println("or to get it using the name of the column: " + r.getString("name"));
				i++;
			}
			// after you are done with a result set call the close() method
			r.close();

			// now lets use this interface for a query that returns multiple columns with different data types
			r = s.executeQuery("SELECT id, tot_cred, name FROM student ORDER BY name ASC;");
			while(r.next()) {
				String id = r.getString("id");
				String name = r.getString("name");
				int tot_cred = r.getInt("tot_cred");
				System.out.println(id + "," + name + "," + tot_cred);
			}
			r.close();
		
			// close statment
			s.close();
			// close connection
			c.close();
		}
		catch (Exception e) {
			System.err.println("An error occurred: " + e.toString());
			System.err.println("\n\nFOR THIS PROGRAM TO WORK YOU HAVE TO HAVE A POSTGRES SERVER RUNNING LOCALLY (OR DOCKER) AT 127.0.0.1 WITH PORT 5432 AND DATABASE cs425 AND USER postgres WITH PASSWORD test");
		}
	}
	
}
