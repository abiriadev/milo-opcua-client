package io.aipim.miloopcuaclient;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ExportData implements Serializable {

	public HashMap<String, ExportNode> targets;
	public LocalDateTime exportTime = LocalDateTime.now();

	public ExportData(HashMap<String, ExportNode> targets) {
		this.targets = targets;
	}
}
