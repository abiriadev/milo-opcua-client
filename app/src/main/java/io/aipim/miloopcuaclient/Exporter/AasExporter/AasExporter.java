package io.aipim.miloopcuaclient.Exporter.AasExporter;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import io.aipim.miloopcuaclient.App;
import io.aipim.miloopcuaclient.ExportData;
import io.aipim.miloopcuaclient.Exporter.Exporter;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.SerializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonDeserializer;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonSerializer;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetKind;
import org.eclipse.digitaltwin.aas4j.v3.model.DataSpecificationContent;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXSD;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ModellingKind;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetInformation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEmbeddedDataSpecification;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultExtension;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

class UaStatus {

	public long value;
	public String name;

	UaStatus(DataValue dv) {
		var sc = dv.getStatusCode();
		value = sc.getValue();
		name =
			sc.isGood()
				? "GOOD"
				: sc.isBad() ? "BAD" : "mol?ru";
	}
}

class Dst implements DataSpecificationContent {

	public String time;
	public UaStatus status;
	public String sourceTime;

	Dst(UaMonitoredItem ua, DataValue dv) {
		time = LocalDateTime.now().toString();
		status = new UaStatus(dv);
		// ua.getTimestamps().getTypeId
		sourceTime = dv.getSourceTime().toString();
	}
}

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

	public static void genTemplate() {
		var aasenv = new DefaultEnvironment.Builder()
			.assetAdministrationShells(
				new DefaultAssetAdministrationShell.Builder()
					.assetInformation(
						new DefaultAssetInformation.Builder()
							.assetKind(AssetKind.INSTANCE)
							.build()
					)
					.id("https://aipim.io/aas/1")
					.idShort("aas1")
					.submodels(
						new DefaultReference.Builder()
							.keys(
								new DefaultKey.Builder()
									.type(KeyTypes.SUBMODEL)
									.value(
										"https://aipim.io/sub/1"
									)
									.build()
							)
							.type(
								ReferenceTypes.MODEL_REFERENCE
							)
							.build()
					)
					.build()
			)
			.submodels(
				new DefaultSubmodel.Builder()
					.id("https://aipim.io/sub/1")
					.idShort("sub1")
					.kind(ModellingKind.INSTANCE)
					.submodelElements(
						new DefaultProperty.Builder()
							.idShort("prop1")
							.value("0")
							.valueType(
								DataTypeDefXSD.INTEGER
							)
							.build()
					)
					.build()
			)
			.build();

		try {
			var serializedEnvironment = new JsonSerializer()
				.write(aasenv);
			System.out.println(serializedEnvironment);
		} catch (SerializationException e) {
			log.error("JSON serialization failed: ", e);
		}
	}

	public AasExporter() {
		try {
			this.env =
				new JsonDeserializer()
					.read(
						CharStreams.toString(
							new InputStreamReader(
								App.class.getClassLoader()
									.getResourceAsStream(
										"aas.json"
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
	public void export(ExportData data) {
		for (final var k : data.hsm.keySet()) {
			var elem = trav(k);
			if (elem != null) {
				with(
					(Property) elem,
					prop -> {
						var pair = data.hsm.get(k);
						prop.setEmbeddedDataSpecifications(
							List.of(
								new DefaultEmbeddedDataSpecification.Builder()
									.dataSpecification(
										new DefaultReference.Builder()
											.type(
												ReferenceTypes.EXTERNAL_REFERENCE
											)
											.keys(
												new DefaultKey.Builder()
													.type(
														KeyTypes.GLOBAL_REFERENCE
													)
													.value(
														"https://aipim.io/dataspec/1"
													)
													.build()
											)
											.build()
									)
									.dataSpecificationContent(
										new Dst(
											pair.getKey(),
											pair.getValue()
										)
									)
									.build()
							)
						);
						prop.setExtensions(
							List.of(
								new DefaultExtension.Builder()
									.name("time")
									.value(
										LocalDateTime
											.now()
											.toString()
									)
									.valueType(
										DataTypeDefXSD.DATE_TIME
									)
									.build(),
								new DefaultExtension.Builder()
									.name("status")
									.value(
										"" +
										pair
											.getValue()
											.getStatusCode()
											.getValue()
									)
									.valueType(
										DataTypeDefXSD.STRING
									)
									.build(),
								new DefaultExtension.Builder()
									.name("sourceTime")
									.value(
										"" +
										pair
											.getValue()
											.getSourceTime()
											.toString()
									)
									.valueType(
										DataTypeDefXSD.DATE_TIME
									)
									.build()
							)
						);
						prop.setValue(
							pair
								.getValue()
								.getValue()
								.getValue()
								.toString()
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
