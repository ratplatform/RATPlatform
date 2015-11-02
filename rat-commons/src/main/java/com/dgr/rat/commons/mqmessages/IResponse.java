package com.dgr.rat.commons.mqmessages;

import java.util.Map;

public interface IResponse {
	public JsonHeader getHeader();
	public Map<String, Object> getResult();
}
