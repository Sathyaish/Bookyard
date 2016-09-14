package bookyard.contracts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

data class OperationResult<T> @JsonCreator constructor(@JsonProperty("successful") val successful : Boolean,
                                                       @JsonProperty("errorMessage") val errorMessage : String?,
                                                       @JsonProperty("data") val data : T?) {
}