package model.requests;

import model.enums.RequestStatusEnum;
import model.identity.ID;

import java.util.function.Supplier;

public class Request {

	private RequestMetadataDetails metadata;
	
	private RequestStatusEnum status;
	
	private Supplier<String> statusMessage;

	/*
	 *
	 * @param metadata      The requests metadata information
	 * @param status        The status of the request
	 * @param statusMessage The status message for the request
	 */
	public Request(RequestMetadataDetails metadata, RequestStatusEnum status,
			Supplier<String> statusMessage) {
		this.metadata = metadata;
		this.status = status;
		this.statusMessage = statusMessage;
	}

	
	public ID getStudentId() {
		return metadata.getStudentId();
	}

	
	public ID getClassId() {
		return this.metadata.getClassId();
	}


	public RequestStatusEnum getStatus() {
		return status;
	}


	public String getStatusMessage() {
		return this.statusMessage.get();
	}

	public RequestMetadataDetails getMetadata() {
		return metadata;
	}
}
