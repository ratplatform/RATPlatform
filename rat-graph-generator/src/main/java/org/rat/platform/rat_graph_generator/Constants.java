package org.rat.platform.rat_graph_generator;

import java.nio.file.FileSystems;

public class Constants {
	public static final String ConfFolder = "conf";
	public static final String PathSeparator = FileSystems.getDefault().getSeparator();
	public static final String RATGraphGeneratorPropertyFile = /*"conf" + PathSeparator +*/ "ratgraphgenerator.properties";
	public static final String IndexFolder = /*"conf" + PathSeparator +*/ "UUIDIndex";
	//public static final String CommandTemplatesFolder = /*"conf" + PathSeparator +*/ "CommandTemplates";

}
