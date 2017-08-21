package utils.parsers;

import model.Prerequisite;

public class PrerequisitesParser implements IParser<Prerequisite> {

	@Override
	public Prerequisite parse(String data) {
		String[] details = data.split(",");
		return new Prerequisite(createId(details[0]), createId(details[1]));
	}
}
