package service;

import model.identity.ID;
import model.requests.RequestMetadataDetails;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import static service.PersistenceService.closeStatement;

public class RequestsService {

	private PersistenceService connectionService;

	public RequestsService(PersistenceService connectionService) {
		this.connectionService = connectionService;
	}

	public Queue<RequestMetadataDetails> getAll() {
		Queue<RequestMetadataDetails> result = new LinkedList<>();
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM requestsWaitlist ";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					String studentId = resultSet.getString("studentId");
					String courseId = resultSet.getString("courseId");
					RequestMetadataDetails metadataDetails = new RequestMetadataDetails(
							new ID(studentId), new ID(courseId));
					result.add(metadataDetails);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public boolean add(RequestMetadataDetails request) {
		boolean result = false;
		PreparedStatement statement = null;
		try {
			String insert = "INSERT INTO requestsWaitlist (studentId,courseId) VALUES (?, ?)";
			statement = connectionService.prepareStatment(insert);
			statement.setString(1, request.getStudentId().toString());
			statement.setString(2, request.getClassId().toString());

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
			String sql = "DELETE FROM requestsWaitlist";
			statement = connectionService.prepareStatment(sql);
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
	}

}
