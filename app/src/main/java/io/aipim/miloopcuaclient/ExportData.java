package io.aipim.miloopcuaclient;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

public class ExportData {

	public HashMap<String, Map.Entry<UaMonitoredItem, DataValue>> hsm;

	ExportData(
		HashMap<String, Map.Entry<UaMonitoredItem, DataValue>> hsm
	) {
		this.hsm = hsm;
	}
}
