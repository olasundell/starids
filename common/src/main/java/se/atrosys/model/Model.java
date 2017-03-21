package se.atrosys.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.Region;

import java.io.Serializable;

/**
 * TODO write documentation
 */
@Builder
@Data
@Region("model")
@JsonDeserialize(builder = Model.ModelBuilder.class)
public class Model implements Serializable {
	@Id
	private Integer id;
	private String value;

	@JsonPOJOBuilder(withPrefix = "")
	public static class ModelBuilder {}
}
