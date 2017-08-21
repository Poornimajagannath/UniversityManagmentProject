package service;

import model.StatisticsSummarize;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static service.PersistenceService.closeStatement;

public class StatisticsHistoryService {

	private PersistenceService connectionService;

	public StatisticsHistoryService(PersistenceService connectionService) {
		this.connectionService = connectionService;
	}

	public StatisticsSummarize getHistory() {
		StatisticsSummarize result = null;
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM statisticsHistory";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					int granted = resultSet.getInt("granted");
					int failed = resultSet.getInt("failed");
					int wait = resultSet.getInt("wait");
					result = new StatisticsSummarize(granted, failed, wait);
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public boolean add(StatisticsSummarize history) {
		boolean result = false;
		clearAll();
		PreparedStatement statement = null;
		try {
			String insert = "INSERT INTO statisticsHistory (granted, failed, wait) VALUES (?, ?, ?) ";
			statement = connectionService.prepareStatment(insert);
			statement.setInt(1, history.getGranted());
			statement.setInt(2, history.getFailed());
			statement.setInt(3, history.getWait());

			int rows = statement.executeUpdate();
			result = rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;

	}

	public void clearAll() {
		PreparedStatement statement = null;
		try {
			String sql = "DELETE FROM statisticsHistory";
			statement = connectionService.prepareStatment(sql);
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
	}

}
