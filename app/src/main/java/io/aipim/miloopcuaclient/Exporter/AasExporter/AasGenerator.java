package io.aipim.miloopcuaclient.Exporter.AasExporter;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.SerializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonSerializer;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetKind;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXSD;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ModellingKind;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetInformation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;

@Slf4j
public class AasGenerator {

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
}
