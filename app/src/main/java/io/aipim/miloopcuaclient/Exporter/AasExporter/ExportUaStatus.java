package io.aipim.miloopcuaclient.Exporter.AasExporter;

import java.io.Serializable;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

public class ExportUaStatus implements Serializable {

	public long value;
	public String name;

	static String toStringLable(StatusCode statusCode) {
		if (statusCode.isBad()) return "BAD";
		if (statusCode.isGood()) return "GOOD";
		if (statusCode.isUncertain()) return "UNCERTAIN";
		if (
			statusCode.isOverflowSet()
		) return "OVERFLOWSET";
		if (
			statusCode.isSecurityError()
		) return "SECURITY_ERROR"; else return "!unreachable status code";
	}

	ExportUaStatus(StatusCode statusCode) {
		value = statusCode.getValue();
		name = toStringLable(statusCode);
	}
}
