package org.jboss.as.quickstarts.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;

/**
 * Session Bean implementation class SimpleTransaction
 */
@Stateless
@LocalBean

public class SimpleTransaction {

	@Resource(mappedName = "java:/TEST-XA")
	DataSource dsXA;
	@Resource(mappedName = "java:/TEST")
	DataSource ds;	

	/**
	 * Default constructor.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void doXA(String name) {

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = dsXA.getConnection();
			stmt = conn.createStatement();
			String sql = "INSERT INTO T " + "VALUES ('"+ name +"', '" + name + "')";
			stmt.executeUpdate(sql);
			System.out.println("Statement executed :" + name + "!");

		

		} catch (SQLException ex) {

			System.err.println(ex);

		}finally {

			if (conn != null)
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					System.err.println(e);
					e.printStackTrace();
				}
		}
		 
		 
		try {
			if ("XTT".equals(name)){
				Thread.sleep(30000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if ("XAT".equals(name)){
			throw new RuntimeException();
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void doNonXA(String name) {

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = ds.getConnection();
			stmt = conn.createStatement();
			String sql = "INSERT INTO T " + "VALUES ('a', 'a')";
			stmt.executeUpdate(sql);
			System.out.println("Statement executed !");

			Thread.sleep(1);

		} catch (SQLException ex) {

			System.err.println(ex);

		} catch (InterruptedException e) {
			System.err.println(e);
			e.printStackTrace();
		} finally {

			if (conn != null)
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					System.err.println(e);
					e.printStackTrace();
				}
		}
		if ("AT".equals(name)){
			throw new RuntimeException();
		}		

	}

}
