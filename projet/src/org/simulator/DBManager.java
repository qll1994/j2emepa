package org.simulator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DBManager {

	private static DBManager instance;

	private ResourceBundle properties;

	private static String resourceBundle = "config";

	private DBManager() {
		properties = ResourceBundle.getBundle(resourceBundle);

		try {
			Class.forName(properties.getString("DB_DRIVER"));
		} catch (ClassNotFoundException e) {
			System.out.println("Error in DBManager : " + e.getMessage() );
		}

	}

	public static DBManager getInstance() {
		if (instance == null) {
			synchronized (DBManager.class) {
				instance = new DBManager();
			}
		}
		return instance;
	}

	public Connection getConnection() {

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(properties.getString("JDBC_URL"), properties.getString("DB_LOGIN"),
					properties.getString("DB_PASSWORD"));

		} catch (SQLException sqle) {
			System.out.println("Error : " + sqle.getMessage()+". Code : " + sqle.getErrorCode()+ " - " + sqle.getSQLState() );
		}
		return connection;

	}

	public void cleanup(Connection connection, Statement stat, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println("Error : " + e.getMessage()+". Code : " + e.getErrorCode()+ " - " + e.getSQLState() );
			}
		}
		if (stat != null) {
			try {
				stat.close();
			} catch (SQLException e) {
				System.out.println("Error : " + e.getMessage()+". Code : " + e.getErrorCode()+ " - " + e.getSQLState() );
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("Error : " + e.getMessage()+". Code : " + e.getErrorCode()+ " - " + e.getSQLState() );
			}
		}
	}

	/**
	 * permet de tester la connexion à la DB
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Connection c = DBManager.getInstance().getConnection();
		ResultSet resultat = null;
		Statement statement = null;
		
		if (c != null) {
			try {
				System.out.println("Connection to db : " + c.getCatalog());
				statement = c.createStatement();
				resultat = statement.executeQuery("SELECT * FROM Pannes;");
				while(resultat.next())
				{
					System.out.println("id : "+resultat.getInt("id")+", machine : "+resultat.getString("machine")+", type machine : "+resultat.getString("typemachine") +", heure : "+resultat.getDate("heure")+" "+ resultat.getTime("heure")+ ", type panne : "+resultat.getString("typepanne") +", traitee : "+resultat.getBoolean("reparee"));
				}

			} catch (SQLException e) {
				System.out.println("Error : " + e.getMessage()+". Code : " + e.getErrorCode()+ " - " + e.getSQLState() );
			} finally {
				
				DBManager.getInstance().cleanup(c, statement, resultat);
			}
		}
	}
}
