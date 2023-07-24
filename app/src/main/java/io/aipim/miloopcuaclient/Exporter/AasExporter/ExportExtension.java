package io.aipim.miloopcuaclient.Exporter.AasExporter;

import io.aipim.miloopcuaclient.ExportNode;
import java.time.LocalDateTime;
import java.util.List;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXSD;
import org.eclipse.digitaltwin.aas4j.v3.model.Extension;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultExtension;

public class ExportExtension {

	static List<Extension> build(
		LocalDateTime ldt,
		ExportNode exportNode
	) {
		return List.of(
			new DefaultExtension.Builder()
				.name("type")
				.value(exportNode.idType.toString())
				.valueType(DataTypeDefXSD.STRING)
				.build(),
			new DefaultExtension.Builder()
				.name("time")
				.value(ldt.toString())
				.valueType(DataTypeDefXSD.DATE_TIME)
				.build(),
			new DefaultExtension.Builder()
				.name("status")
				.value(
					"" + exportNode.statusCode.getValue()
				)
				.valueType(DataTypeDefXSD.STRING)
				.build()
		);
	}
}
