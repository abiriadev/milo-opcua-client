package io.aipim.miloopcuaclient;

public class App {

	public static void main(String[] args) {
		new Initializer(
			new PropTargetReader(),
			new ConsoleExporter()
		);
		System.out.println("terminate");
	}
}
