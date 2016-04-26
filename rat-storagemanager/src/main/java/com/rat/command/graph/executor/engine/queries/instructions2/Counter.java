package com.rat.command.graph.executor.engine.queries.instructions2;

import java.util.LinkedList;
import java.util.List;

import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class Counter {
	public enum StepType {
		In(0),
		Has(1);
		
		private final int _stepType;
		
		private StepType(final int stepType) { 
			_stepType = stepType; 
		}
	}
	
	private static Counter _instance = null;
	public int count = 0;
	private  GremlinPipeline<Vertex, Vertex> _content = null;
	private Vertex rootVertex = null;
	private LinkedList<Step> _list = new LinkedList<Step>();
	
	public static Counter getInstance(){
		if(_instance == null){
			_instance = new Counter();
		}
		
		return _instance;
	}
	
	public void start(Vertex rootVertex){
		this.rootVertex = rootVertex;
		_list.clear();
	}
	
	public void setContent(GremlinPipeline<Vertex, Vertex> content){
		_content = content;
	}
	
	public GremlinPipeline<Vertex, Vertex> getContent(){
		return _content;
	}
	
	public void addStep(Step step){
		_list.add(step);
	}
	
	public void run(){
		GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
		
		for(Step step : _list){
			switch(step.stepType){
			case In:
				pipe.in(step.paramValue);
				break;
			case Has:
				pipe.has(step.paramName, step.paramValue);
				break;
			}
		}
		
		System.out.println("Counter: " + pipe.toString());
		List<Vertex> results = (List<Vertex>) pipe.toList();
		System.out.println(results.size());
	}
	
	public void inc(){
		++count;
	}
}
