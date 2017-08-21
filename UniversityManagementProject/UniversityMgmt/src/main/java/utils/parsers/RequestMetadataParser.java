package utils.parsers;

import model.requests.RequestMetadataDetails;

public class RequestMetadataParser implements IParser<RequestMetadataDetails> {

	@Override
	public RequestMetadataDetails parse(String data) {
		String[] details = data.split(",");
		return new RequestMetadataDetails(createId(details[0]), createId(details[1]));
	}
}
