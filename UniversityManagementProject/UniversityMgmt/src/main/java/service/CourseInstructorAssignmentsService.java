package service;

import model.Assignment;
import model.identity.ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static service.PersistenceService.closeStatement;

public class CourseInstructorAssignmentsService {

	
	private PersistenceService connectionService;

	public CourseInstructorAssignmentsService(
			PersistenceService connectionService) {
		this.connectionService = connectionService;
	}

	public List<Assignment> getAll() {
		List<Assignment> result = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM courseInstructorAssignment";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Assignment ca = createAssignment(resultSet);
					result.add(ca);
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public List<Assignment> getByCourse(ID courseId, int semesterId) {
		List<Assignment> result = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM courseInstructorAssignment WHERE (courseId, semesterId) = (?, ?)";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				statement.setString(1, courseId.toString());
				statement.setInt(2, semesterId);
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					result.add(createAssignment(resultSet));
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public boolean add(Assignment assignment, int semesterId) {
		boolean result = false;
		PreparedStatement statement = null;
		try {
			String insert = "INSERT INTO courseInstructorAssignment (courseId,instructorId,semesterId,capacity) VALUES (?, ?, ?, ?) ";
			statement = connectionService.prepareStatment(insert);
			statement.setString(1, assignment.getClassId().toString());
			statement.setString(2, assignment.getInstructorId().toString());
			statement.setInt(3, semesterId);
			statement.setInt(4, assignment.getCapacity());

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
			String prereSql = "DELETE FROM courseInstructorAssignment";
			statement = connectionService.prepareStatment(prereSql);
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
	}

	
	private Assignment createAssignment(ResultSet resultSet)
			throws SQLException {
		String courseId = resultSet.getString("courseId");
		String instructorId = resultSet.getString("instructorId");
		int capacity = resultSet.getInt("capacity");
		return new Assignment(new ID(courseId), new ID(instructorId), capacity);
	}
}
