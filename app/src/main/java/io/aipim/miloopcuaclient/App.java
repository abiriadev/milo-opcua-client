package io.aipim.miloopcuaclient;

public class App {

	public static void main(String[] args) {
		var ptr = new PropTargetReader();

		new Initializer(ptr, new ConsoleExporter());
		System.out.println("terminate");
	}
}
