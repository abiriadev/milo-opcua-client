package io.aipim.miloopcuaclient.Exporter.AasExporter;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import io.aipim.miloopcuaclient.App;
import io.aipim.miloopcuaclient.ExportData;
import io.aipim.miloopcuaclient.Exporter.Exporter;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import java.io.InputStreamReader;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.SerializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonDeserializer;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonSerializer;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

@Slf4j
public class AasExporter implements Exporter {

	private Environment env;

	private static <T> T with(
		T obj,
		Consumer<T> initializer
	) {
		initializer.accept(obj);
		return obj;
	}

	public AasExporter(String filename) {
		try {
			this.env =
				new JsonDeserializer()
					.read(
						CharStreams.toString(
							new InputStreamReader(
								App.class.getClassLoader()
									.getResourceAsStream(
										filename
									),
								Charsets.UTF_8
							)
						)
					);
		} catch (Exception e) {
			log.error("JSON parsing failed: ", e);
		}
	}

	private SubmodelElement trav(String idshort) {
		return env
			.getSubmodels()
			.get(0)
			.getSubmodelElements()
			.stream()
			.filter(elem ->
				elem.getIdShort().equals(idshort)
			)
			.findAny()
			.orElse(null);
	}

	@Override
	public void export(ExportData exportData) {
		for (final var k : exportData.targets.keySet()) {
			var elem = trav(k);
			if (elem != null) {
				with(
					(Property) elem,
					prop -> {
						var exportNode =
							exportData.targets.get(k);
						prop.setEmbeddedDataSpecifications(
							List.of(
								new ExportDataSpecificationContent(
									exportData.exportTime,
									exportNode
								)
									.embed()
							)
						);
						prop.setExtensions(
							ExportExtension.build(
								exportData.exportTime,
								exportNode
							)
						);
						prop.setValue(
							exportNode.value.toString()
						);
					}
				);
			}
		}

		try {
			System.out.println(
				new JsonSerializer().write(env)
			);
		} catch (SerializationException e) {
			log.error("JSON serialization failed: ", e);
		}
	}
}
