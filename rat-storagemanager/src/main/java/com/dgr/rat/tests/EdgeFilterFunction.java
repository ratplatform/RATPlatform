/**
 * @author Daniele Grignani (dgr)
 * @date Oct 18, 2015
 */

package com.dgr.rat.tests;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.PipesFunction;

public class EdgeFilterFunction extends PipesFunction<Vertex, Boolean>{
	private String _filterPropertyValue = null;
	private String _propertyName = null;
	
	public EdgeFilterFunction(String propertyName, String filterPropertyValue) {
		_propertyName = propertyName;
		_filterPropertyValue = filterPropertyValue;
	}

	/* (non-Javadoc)
	 * @see com.tinkerpop.pipes.PipeFunction#compute(java.lang.Object)
	 */
	@Override
	public Boolean compute(Vertex vertex) {
		boolean result = false;
		String value = vertex.getProperty(_propertyName).toString();

		result = value.equalsIgnoreCase(_filterPropertyValue) ? true : false;
		return result;
	}

}
