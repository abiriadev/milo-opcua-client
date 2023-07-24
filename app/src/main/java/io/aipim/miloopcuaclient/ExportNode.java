package io.aipim.miloopcuaclient;

import java.io.Serializable;
import lombok.Builder;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;

@Builder
public class ExportNode implements Serializable {

	public IdType idType;
	public StatusCode statusCode;
	public Object value;
}
