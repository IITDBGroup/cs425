package edu.iit;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class MyJDBCExample {

	public static final String JDBC_DRIVER = "org.postgresql.Driver";
	// postgres URLs are of the form: jdbc:postgresql://host:port/database
	public static final String JDBC_DB = "university";
	public static final String JDBC_PORT = "5450";
	public static final String JDBC_HOST = "127.0.0.1";
	public static final String JDBC_URL = "jdbc:postgresql://" + JDBC_HOST + ":" + JDBC_PORT + "/" + JDBC_DB;
	public static final String DBUSER = "postgres";
	public static final String DBPASSWD = "test";

	// method for printing results for any query
	public static void printQueryResults(Connection c, String sql) {
		try {
			Statement s = c.createStatement();
			ResultSet r = s.executeQuery(sql);
			ResultSetMetaData md = r.getMetaData();
			int numCols = md.getColumnCount();

			System.out.printf("================================================================================" +
							  "\nQUERY: %s" +
							  "\n================================================================================\n",
							  sql);
			while(r.next()) {
				System.out.print("(");
				for(int i = 1; i <= numCols; i++) {
					System.out.printf("%s: %s%s",
									  md.getColumnName(i),
									  r.getString(i),
									  i < numCols ? ", ": ""
						);					
				}
				System.out.println(")");					
			}
			
			r.close();
			s.close();
		}
		catch (SQLException e) {
			System.err.println("An error occurred: " + e.toString());			
		}
	}
	
	public static void main (String[] args) throws Exception {
		try {
			// load the driver based on the drivers class name
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

			
			// transactions
			// turn of autocommit
			c.setAutoCommit(false);
			
			s = c.createStatement();

			// run updates an abort transaction
			s.executeUpdate("DELETE FROM student;");
			s.executeUpdate("DELETE FROM instructor;");

			c.rollback();

			// student table should still be the same
			r = s.executeQuery("SELECT count(*) FROM student;");
			while(r.next())
			{
				System.out.println("The count is: " + r.getString(1));
			}
			r.close();
			
			// student table should still be the same
			r = s.executeQuery("SELECT sum(tot_cred) FROM student;");
			while(r.next())
			{
				System.out.println("The total of tot_cred is: " + r.getString(1));
			}
			r.close();
			
			// commit a transaction
			s.executeUpdate("UPDATE student SET tot_cred = tot_cred + 1;");
			c.commit();

			// student table should still be the same
			r = s.executeQuery("SELECT sum(tot_cred) FROM student;");
			while(r.next())
			{
				System.out.println("The total of tot_cred is: " + r.getString(1));
			}			
			r.close();
			
			// prepared statements act as template SQL with placeholders
			// advantage: SQL injection + reduced cost for statements that are executed frequently
			r = s.executeQuery("SELECT count(*) FROM instructor;");
			while(r.next())
			{
				System.out.println("The total of number of instructors is: " + r.getString(1));
			}			
			r.close();

			s.executeUpdate("DELETE FROM instructor WHERE id = '99999';");
			c.commit();
			
			PreparedStatement pStmt = c.prepareStatement("INSERT INTO instructor VALUES(?,?,?,?)");
			
			pStmt.setString(1, "99999");
			pStmt.setString(2, "XXXPersonXXX");
			pStmt.setString(3, "Finance");
			pStmt.setInt(4, 125000);

			pStmt.executeUpdate();
			c.commit();

			r = s.executeQuery("SELECT count(*) FROM instructor;");
			while(r.next())
			{
				System.out.println("The total of number of instructors is: " + r.getString(1));
			}			
			r.close();

			// SQL injection
			String name = "X'::TEXT OR true OR name = '";

			// retrieves all instructors
			System.out.println("SELECT * FROM instructor WHERE name::TEXT = '" + name + "'::TEXT");
			r = s.executeQuery("SELECT * FROM instructor WHERE name::TEXT = '" + name + "'::TEXT");
			// SELECT * FROM instructor u name = 'X' OR true OR name = '';
			while(r.next())
			{
				System.out.println("Another instructor: " + r.getString(1) + "," + r.getString("name"));
			}
			r.close();

			// manipulate the database
			name = "X'; DELETE FROM instructor; SELECT 'a";
			s.execute("SELECT * FROM instructor WHERE name = '" + name + "'");
			// SELECT * FROM instructor WHERE name = 'X'; DELETE FROM instructor;
			r.close();

			printQueryResults(c, "SELECT count(*) FROM instructor;");
			c.rollback();

			// print arbitrary query results
			printQueryResults(c, "SELECT count(*) AS nums, dept_name FROM student GROUP BY dept_name;");
			
            // close connection
			s.close();
			c.close();			
		}
		catch (Exception e) {
			System.err.println("An error occurred: " + e.toString());
			System.err.println("\n\nFOR THIS PROGRAM TO WORK YOU HAVE TO HAVE A POSTGRES SERVER RUNNING LOCALLY (OR DOCKER) AT "
							   + JDBC_HOST
							   + " WITH PORT " + JDBC_PORT
							   + " AND DATABASE " + JDBC_DB
							   + " AND USER " + DBUSER
							   + " WITH PASSWORD " + DBPASSWD);
		}
	}
	
}
