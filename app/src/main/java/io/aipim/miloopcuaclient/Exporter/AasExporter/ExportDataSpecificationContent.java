package io.aipim.miloopcuaclient.Exporter.AasExporter;

import io.aipim.miloopcuaclient.ExportNode;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.eclipse.digitaltwin.aas4j.v3.model.DataSpecificationContent;
import org.eclipse.digitaltwin.aas4j.v3.model.EmbeddedDataSpecification;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEmbeddedDataSpecification;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;

public class ExportDataSpecificationContent
	implements DataSpecificationContent, Serializable {

	static String REF = "https://aipim.io/dataspec/1";

	public String type;
	public String time;
	public ExportUaStatus status;

	public ExportDataSpecificationContent(
		LocalDateTime ldt,
		ExportNode exportNode
	) {
		type = exportNode.idType.toString();
		time = ldt.toString();
		status = new ExportUaStatus(exportNode.statusCode);
	}

	EmbeddedDataSpecification embed() {
		return new DefaultEmbeddedDataSpecification.Builder()
			.dataSpecification(
				new DefaultReference.Builder()
					.type(ReferenceTypes.EXTERNAL_REFERENCE)
					.keys(
						new DefaultKey.Builder()
							.type(KeyTypes.GLOBAL_REFERENCE)
							.value(REF)
							.build()
					)
					.build()
			)
			.dataSpecificationContent(this)
			.build();
	}
}
