package com.rat.init;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class Comment {
	@JsonProperty("domain")
	public String domain = null;
	@JsonProperty("VertexLabelField")
	public String VertexLabelField = null;
	@JsonProperty("VertexContentField")
	public String VertexContentField = null;
	@JsonProperty("url")
	public String url = null;
}
