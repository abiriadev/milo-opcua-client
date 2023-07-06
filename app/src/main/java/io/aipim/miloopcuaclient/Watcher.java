package io.aipim.miloopcuaclient;

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import io.aipim.miloopcuaclient.Exporter.Exporter;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

@Slf4j
public class Watcher {

	Exporter exporter;
	UaClient client;
	Target target;
	ExportData expd = new ExportData(
		new HashMap<String, String>()
	);
	HashMap<NodeId, String> lkup = new HashMap<>();

	Watcher(
		Target target,
		Exporter exporter,
		Config config
	) {
		this.target = target;
		this.exporter = exporter;

		for (final var k : target.keySet()) lkup.put(
			NodeId.parse(target.get(k)),
			k
		);

		for (final var k : target.keySet()) expd.hsm.put(
			k,
			null
		);

		try {
			client =
				OpcUaClient
					.create(config.getUrl())
					.connect()
					.get();

			final var subscription = client
				.getSubscriptionManager()
				.createSubscription(1000.0)
				.get();

			for (final var k : target.keySet()) reg(
				subscription,
				NodeId.parse(target.get(k))
			);
		} catch (Exception e) {}
	}

	void reg(UaSubscription subscription, NodeId nid)
		throws InterruptedException, ExecutionException {
		for (UaMonitoredItem item : subscription
			.createMonitoredItems(
				TimestampsToReturn.Both,
				newArrayList(
					new MonitoredItemCreateRequest(
						new ReadValueId(
							nid,
							AttributeId.Value.uid(),
							null,
							QualifiedName.NULL_VALUE
						),
						MonitoringMode.Reporting,
						new MonitoringParameters(
							subscription.nextClientHandle(),
							1000.0,
							null,
							uint(10),
							true
						)
					)
				),
				(item, id) ->
					item.setValueConsumer(
						this::onSubscriptionValue
					)
			)
			.get()) if (
			item.getStatusCode().isGood()
		) log.info(
			"item created for nodeId=" +
			item.getReadValueId().getNodeId()
		); else log.warn("warn");
	}

	private void onSubscriptionValue(
		UaMonitoredItem item,
		DataValue value
	) {
		final var k = lkup.get(
			item.getReadValueId().getNodeId()
		);
		if (k != null) expd.hsm.put(
			k,
			value.getValue().getValue().toString()
		);
		send(expd);
	}

	void send(ExportData data) {
		exporter.export(data);
	}
}
