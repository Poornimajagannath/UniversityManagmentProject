package model;

import model.enums.SemesterEnum;
import model.identity.ID;

import java.util.HashSet;
import java.util.Set;

public class Course {

	/* The id for this class */
	private final ID id;
	/* The name of the course */
	private String name;
	/* The course description - optional */
	private String description;
	/* Set of prerequisite course */
	private Set<ID> prerequisites;
	/* A set of semesters that this class is taught */
	private Set<SemesterEnum> semestersSet;

	/**
	 * C'tor for CourseClass object
	 *
	 * @param id          This id of the class
	 * @param name        The course name
	 * @param description The course description
	 */
	public Course(final ID id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.semestersSet = new HashSet<>();
		this.prerequisites = new HashSet<>();
	}

	public ID getId() {
		return id;
	}

	public boolean addSemester(SemesterEnum semester) {
		return this.semestersSet.add(semester);
	}

	public Set<SemesterEnum> getSemesters() {
		return this.semestersSet;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof Course) {
			Course other = (Course) obj;
			result = this.getId().equals(other.getId());
		}
		return result;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Set<ID> getPrerequisites() {
		return prerequisites;
	}

	public void addPrerequiste(ID course) {
		this.prerequisites.add(course);
	}

	public boolean hasPrerequisites() {
		return this.prerequisites.size() > 0;
	}

}
