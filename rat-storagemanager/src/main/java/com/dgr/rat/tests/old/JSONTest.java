/**
 * @author Daniele Grignani (dgr)
 * @date Aug 30, 2015
 */

package com.dgr.rat.tests.old;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.dgr.rat.json.utils.RATJsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONTest {
	public class Parameter{
		public String name;
		public Object value;
		public String type;
	}

	@Test
	public void test() {
		Map<String, Parameter>map = new HashMap<String, Parameter>();
		Parameter param = new Parameter();
		param.name = "userName";
		param.value = "dgr";
		param.type = "string";
		map.put(param.name, param);
		
		param = new Parameter();
		param.name = "password";
		param.value = "12345";
		param.type = "integer";
		map.put(param.name, param);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = RATJsonUtils.jsonPrettyPrinter(mapper.writeValueAsString(map));
			System.out.println(json);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		fail("Not yet implemented");
	}

}
