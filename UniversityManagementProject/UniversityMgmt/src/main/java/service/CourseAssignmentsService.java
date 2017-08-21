package service;

import model.CourseAssignment;
import model.identity.ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static service.PersistenceService.closeStatement;

/**
 * Service to comunicate with courseAssignment table
 */
public class CourseAssignmentsService {

	
	private PersistenceService connectionService;


	public CourseAssignmentsService(PersistenceService connectionService) {
		this.connectionService = connectionService;
	}

	
	public List<CourseAssignment> getAll() {
		List<CourseAssignment> result = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM courseAssignment";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					CourseAssignment ca = createCourseAssignment(resultSet);
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

	
	public boolean add(CourseAssignment ca) {
		boolean result = false;
		PreparedStatement statement = null;
		CourseAssignment courseAssignment = getByCourse(ca.getCourseId(),
				ca.getSemesterId());
		int maxSeats = ca.getMaxSeats();
		if (courseAssignment != null) {
			maxSeats += courseAssignment.getMaxSeats();
		}
		try {
			String insert = "INSERT INTO courseAssignment (courseId,semesterId,maxSeats) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE maxSeats = VALUES(maxSeats)";
			statement = connectionService.prepareStatment(insert);
			statement.setString(1, ca.getCourseId().toString());
			statement.setInt(2, ca.getSemesterId());
			statement.setInt(3, maxSeats);

			int rows = statement.executeUpdate();
			result = rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	/**
	 * Fetch the takenSeats value given a courseId and semesterId
	 *
	 * @param courseId   The course id
	 * @param semesterId The semester id
	 * @return The value of takenSeats in case such entry exists, zero otherwise
	 */
	public int getTakenSeats(ID courseId, int semesterId) {
		int result = 0;
		PreparedStatement statement = null;
		try {
			String sql = "SELECT takenSeats FROM courseAssignment WHERE (courseId, semesterId) = (?, ?)";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				statement.setString(1, courseId.toString());
				statement.setInt(2, semesterId);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					result = resultSet.getInt("takenSeats");
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	
	public int getMaxSeats(ID courseId, int semesterId) {
		int result = 0;
		PreparedStatement statement = null;
		try {
			String sql = "SELECT maxSeats FROM courseAssignment WHERE (courseId, semesterId) = (?, ?)";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				statement.setString(1, courseId.toString());
				statement.setInt(2, semesterId);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					result = resultSet.getInt("maxSeats");
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	
	public boolean incrementTaken(ID courseId, int semesterId) {
		boolean result = false;
		PreparedStatement statement = null;
		try {

			int taken = 1;
			taken += getTakenSeats(courseId, semesterId);
			int maxSeats = getMaxSeats(courseId, semesterId);
			if (maxSeats < taken) {
				return false;
			}

			String insert = "UPDATE courseAssignment SET takenSeats = ? WHERE (courseId, semesterId) = (?, ?)";
			statement = connectionService.prepareStatment(insert);
			statement.setInt(1, taken);
			statement.setString(2, courseId.toString());
			statement.setInt(3, semesterId);

			int rows = statement.executeUpdate();
			result = rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	/**
	 * Clears all entries from courseAssignment table
	 */
	public void clearAll() {
		PreparedStatement statement = null;
		try {
			String prereSql = "DELETE FROM courseAssignment";
			statement = connectionService.prepareStatment(prereSql);
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
	}

	
	private CourseAssignment getByCourse(ID courseId, int semseterId) {
		CourseAssignment result = null;
		PreparedStatement statement = null;
		try {
			String sql = "SELECT * FROM courseAssignment WHERE (courseId, semesterId) = (?, ?)";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				statement.setString(1, courseId.toString());
				statement.setInt(2, semseterId);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					result = createCourseAssignment(resultSet);
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	
	private CourseAssignment createCourseAssignment(ResultSet resultSet)
			throws SQLException {
		String courseId = resultSet.getString("courseId");
		int semesterId = resultSet.getInt("semesterId");
		int maxSeats = resultSet.getInt("maxSeats");
		return new CourseAssignment(new ID(courseId), semesterId, maxSeats);
	}
}
