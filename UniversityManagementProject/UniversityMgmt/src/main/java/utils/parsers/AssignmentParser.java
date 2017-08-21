package utils.parsers;

import model.Assignment;
import model.identity.ID;

public class AssignmentParser implements IParser<Assignment> {

	@Override
	public Assignment parse(String data) {
		String[] details = data.split(",");
		Assignment assignment = new Assignment(createId(details[0]),
				createId(details[1]), Integer.parseInt(details[2]));
		return assignment;
	}

}
