package service;

import model.Record;
import model.enums.GradeEnum;
import model.identity.ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static service.PersistenceService.closeStatement;

public class RecordsService {

	private PersistenceService connectionService;

	public RecordsService(PersistenceService connectionService) {
		this.connectionService = connectionService;
	}

	public List<Record> getAll() {
		List<Record> result = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String str = "SELECT * FROM records";
			statement = connectionService.prepareStatment(str);
			if (statement != null) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					String studentId = resultSet.getString("studentId");
					String instructorId = resultSet.getString("instructorId");
					String courseId = resultSet.getString("courseId");
					String comments = resultSet.getString("comments");
					String grade = resultSet.getString("grade");

					Record record = new Record(new ID(studentId),
							new ID(courseId), new ID(instructorId), comments,
							GradeEnum.valueOf(grade));
					result.add(record);
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public List<Record> getForStudent(ID studentId) {
		List<Record> result = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String str = "SELECT * FROM records WHERE studentId = ?";
			statement = connectionService.prepareStatment(str);
			if (statement != null) {
				statement.setString(1, studentId.toString());
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					String instructorId = resultSet.getString("instructorId");
					String courseId = resultSet.getString("courseId");
					String comments = resultSet.getString("comments");
					String grade = resultSet.getString("grade");

					Record record = new Record(studentId, new ID(courseId),
							new ID(instructorId), comments,
							GradeEnum.valueOf(grade));
					result.add(record);
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public List<Record> getForStudentAndCourse(ID studentId, ID courseId) {
		List<Record> result = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String sql = "SELECT instructorId, comments, grade FROM records WHERE (studentId, courseId) = (?, ?)";
			statement = connectionService.prepareStatment(sql);
			if (statement != null) {
				statement.setString(1, studentId.toString());
				statement.setString(2, courseId.toString());
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					String instructorId = resultSet.getString("instructorId");
					String comments = resultSet.getString("comments");
					String grade = resultSet.getString("grade");

					result.add(new Record(studentId, courseId,
							new ID(instructorId), comments,
							GradeEnum.valueOf(grade)));
				}
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
		return result;
	}

	public boolean add(Record record) {

		boolean result = false;
		PreparedStatement statement = null;
		try {
			String insert = "INSERT INTO records (studentId, courseId, instructorId, comments, grade) VALUES (?, ?, ?, ?, ?)";
			statement = connectionService.prepareStatment(insert);
			statement.setString(1, record.getStudentId().toString());
			statement.setString(2, record.getClassId().toString());
			statement.setString(3, record.getInstructorId().toString());
			statement.setString(4, record.getComments());
			statement.setString(5, record.getGrade().name());

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
			String prereSql = "DELETE FROM records";
			statement = connectionService.prepareStatment(prereSql);
			statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			closeStatement(statement);
		}
	}

}
