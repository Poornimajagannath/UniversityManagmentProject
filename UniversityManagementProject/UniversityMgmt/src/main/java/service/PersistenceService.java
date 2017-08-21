package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static utils.Configuration.DB_URL;
import static utils.Configuration.JDBC_DRIVER;
import static utils.Configuration.PASSWORD;
import static utils.Configuration.USER;

public class PersistenceService {

	/**
	 * The connection object to DB
	 */
	private Connection connection;

	public void connect() {

		try {
			if (connection == null || connection.isClosed()) {
				Class.forName(JDBC_DRIVER);

				this.connection = DriverManager
						.getConnection(DB_URL, USER, PASSWORD);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void disconnect() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public PreparedStatement prepareStatment(String sql) {
		try {
			return this.connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void closeStatement(PreparedStatement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
