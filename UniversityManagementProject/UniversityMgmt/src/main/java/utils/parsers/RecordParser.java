package utils.parsers;

import model.enums.GradeEnum;
import model.identity.ID;
import model.Record;

public class RecordParser implements IParser<Record> {

	@Override
	public Record parse(String data) {
		String[] details = data.split(",");
		Record newRecord = new Record(new ID<>(details[0]),
				new ID<>(details[1]), new ID<>(details[2]), details[3],
				GradeEnum.valueOf(details[4]));
		return newRecord;
	}

}
