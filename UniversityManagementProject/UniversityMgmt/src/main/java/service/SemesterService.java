package service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static service.PersistenceService.closeStatement;

public class SemesterService {

	private PersistenceService connectionService;

	public SemesterService(PersistenceService connectionService) {
		this.connectionService = connectionService;
	}

	public int getCurrent() {
		int result = 0;
		PreparedStatement statement = null;
		try {
			String sql = "SELECT semesterIndex FROM currentSemester ";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					result = resultSet.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public boolean setCurrent(int currentSemester) {
		boolean result = false;
		PreparedStatement statement = null;
		try {
			clear();
			String insert = "INSERT INTO currentSemester (semesterIndex) VALUES (?)";
			statement = connectionService.prepareStatment(insert);
			statement.setInt(1, currentSemester);
			int rows = statement.executeUpdate();
			result = rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;

	}

	public void clear() {
		PreparedStatement statement = null;
		try {
			String sql = "DELETE FROM currentSemester";
			statement = connectionService.prepareStatment(sql);
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
	}
}
