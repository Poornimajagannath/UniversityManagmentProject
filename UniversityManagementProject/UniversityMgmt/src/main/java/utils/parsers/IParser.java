package utils.parsers;

import model.identity.ID;

public interface IParser<T> {


	T parse(String data);

	default ID createId(String str) {
		return new ID(str);
	}

}
