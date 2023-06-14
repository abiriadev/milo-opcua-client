package io.aipim.miloopcuaclient;

import java.util.Arrays;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.nodes.objects.ServerTypeNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;

public class App {

	public static void main(String[] args)
		throws Exception {
		var client = OpcUaClient.create(
			"opc.tcp://milo.digitalpetri.com:62541/milo"
		);
		client.connect().get();

		ServerTypeNode sn = (ServerTypeNode) client
			.getAddressSpace()
			.getObjectNode(
				Identifiers.Server,
				Identifiers.ServerType
			);

		var sa = sn.getServerArray();
		var nsa = sn.getNamespaceArray();

		System.out.println(
			"ServerArrary=" + Arrays.toString(sa)
		);
		System.out.println(
			"NamespaceArrary=" + Arrays.toString(nsa)
		);

		var ss = sn.getServerStatus();

		System.out.println("ServerStatus=" + ss);

		var ssn = sn.getServerStatusNode();
		var bi = ssn.getBuildInfo();
		var st = ssn.getStartTime();
		var ct = ssn.getCurrentTime();
		var stte = ssn.getState();

		System.out.println("BuildInfo=" + bi);
		System.out.println("StartTime=" + st);
		System.out.println("CurrentTime=" + ct);
		System.out.println("State=" + stte);

		client.disconnect().get();
	}
}
