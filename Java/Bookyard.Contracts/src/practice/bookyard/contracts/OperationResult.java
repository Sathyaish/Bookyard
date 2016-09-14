package practice.bookyard.contracts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationResult<T> {
	protected final boolean successful;
	protected final String errorMessage;
	protected final T data;
	
	@JsonCreator
	public OperationResult(@JsonProperty("successful") boolean successful, 
			@JsonProperty("errorMessage") String errorMessage, 
			@JsonProperty("data") T data)  {
		this.successful = successful;
		this.errorMessage = errorMessage;
		this.data = data;
	}
	
	public boolean getSuccessful() { 
		return this.successful;
	}
	
	public String getErrorMessage() { 
		return this.errorMessage;
	}
	
	public T getData() {
		return this.data;
	}
}
