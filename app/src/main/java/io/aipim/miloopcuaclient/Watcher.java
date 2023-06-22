package io.aipim.miloopcuaclient;

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
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

public class Watcher {

	Exporter exporter;
	UaClient client;
	Target target;
	ExportData expd = new ExportData(
		new HashMap<String, String>()
	);
	HashMap<NodeId, String> lkup = new HashMap<>();

	Watcher(Target target, Exporter exporter) {
		this.target = target;
		this.exporter = exporter;

		for (var k : target.keySet()) lkup.put(
			NodeId.parse(target.get(k)),
			k
		);

		for (var k : target.keySet()) expd.hsm.put(k, null);

		try {
			client =
				OpcUaClient
					.create(
						"opc.tcp://milo.digitalpetri.com:62541/milo"
					)
					.connect()
					.get();

			var subscription = client
				.getSubscriptionManager()
				.createSubscription(1000.0)
				.get();

			for (var k : target.keySet()) reg(
				subscription,
				NodeId.parse(target.get(k))
			);
			// Identifiers.Server_ServerStatus_CurrentTime,
		} catch (Exception e) {}
	}

	void reg(UaSubscription subscription, NodeId nid)
		throws InterruptedException, ExecutionException {
		System.out.println("reg: " + nid.toString());
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
		) System.out.println(
			"item created for nodeId=" +
			item.getReadValueId().getNodeId()
		); else System.out.println("warn");
	}

	private void onSubscriptionValue(
		UaMonitoredItem item,
		DataValue value
	) {
		var k = lkup.get(item.getReadValueId().getNodeId());
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
