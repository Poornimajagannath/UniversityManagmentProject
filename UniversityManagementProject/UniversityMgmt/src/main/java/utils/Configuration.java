package utils;

public class Configuration {

	private String csvBasePath;
	private static Configuration instance = new Configuration();

	private Configuration() {}

	public static Configuration getInstance() {
		return instance;
	}

	public String getCsvBasePath() {
		return csvBasePath;
	}

	public void setCsvBasePath(String csvBasePath) {
		this.csvBasePath = csvBasePath;
	}

	// JDBC driver name and database URL
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost/assignment8";

	//  Database credentials
	public static final String USER = "root";
	public static final String PASSWORD = "12345";

}
