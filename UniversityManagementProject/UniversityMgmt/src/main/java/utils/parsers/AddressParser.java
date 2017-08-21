package utils.parsers;

import model.Address;

public class AddressParser implements IParser<Address> {

	public Address parse(String data) {
		String[] address = data.split("\\s");
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < address.length - 1; i++) {
			builder.append(address[i]);
			builder.append(" ");
		}
		return new Address(Integer.parseInt(address[0]),
				builder.toString().substring(0, builder.length() - 1),
				address[address.length - 1]);
	}

}
