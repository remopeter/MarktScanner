
package ch.bpeter.test;
 
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
 
public class RemoConnection { 
 
 private static final String DB_DRIVER_NAME = "com.ibm.db2.jcc.DB2Driver"; 
 private static final String DB_URL = "jdbc:db2://sysk.bit.admin.ch:5021/EQA1"; 
 private static final String DB_SCHEMA = "S983ERB"; 
  
 private static final String USERNAME = ""; 
 private static final String PASSWORD = ""; 
  
 public static void main(String[] args) throws Exception { 
 	 validateCreds(USERNAME, PASSWORD); 
 	  
 	 Connection connection = null; 
 	 PreparedStatement ps = null; 
 	 ResultSet rs = null; 
 	 	  
 	 try{ 
 	 	 Class.forName(DB_DRIVER_NAME); 
 	 	 connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); 
 	 	 ps = connection.prepareStatement("SELECT ROLLE_ID, ROLLE_NAME FROM " + DB_SCHEMA + ".T_ROLLE"); 
  
 	 	 rs = ps.executeQuery(); 
 	 	  
 	 	 while(rs.next()) { 
 	 	 	 System.out.println("ROLLE_ID: " + rs.getBigDecimal("ROLLE_ID")); 
 	 	 	 System.out.println("ROLLE_NAME: " + rs.getString("ROLLE_NAME")); 
 	 	 } 
 	 } catch (ClassNotFoundException e1) { 
 	 	 System.err.println(e1); 
 	 } catch (SQLException e2) { 
 	 	 System.err.println(e2); 
 	 } finally { 
 	 	 if (rs != null) { 
 	 	 	 rs.close(); 
 	 	 } 
 	 	 if (ps != null) { 
 	 	 	 ps.close(); 
 	 	 } 
 	 	 if (connection != null && !connection.isClosed()) { 
 	 	 	 connection.close(); 
 	 	 } 
 	 } 
 } 
  
 private static void validateCreds(String username, String password) throws Exception { 
 	 if (username == null || username.trim().length() <= 0 || password == null || password.trim().length() <= 0) { 
 	 	 throw new Exception("Username oder Password sind ungueltig!"); 
 	 } 
 } 
  
}