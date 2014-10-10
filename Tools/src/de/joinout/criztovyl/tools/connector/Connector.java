package de.joinout.criztovyl.tools.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import de.joinout.criztovyl.tools.json.JSONList;
import de.joinout.criztovyl.tools.json.JSONMap;
import de.joinout.criztovyl.tools.json.creator.JSONCreator;
import de.joinout.criztovyl.tools.json.creator.JSONCreators;
/**
 * A class that can connect two objects one-way or bidirectional. The connectors also can have an ID.<br>
 * @author christoph
 *
 * @param <C> the object class
 */
public class Connector<C> {
	
	private static String CONMAP_KEY = "map";
	private static String CONNECTORLIST_KEY = "connectors";
	private static String IDMAP_KEY = "ids";
	
	/**
	 * The ID for objects that shouldn't identified.
	 */
	public static int FOO_ID = 0;
	
	private Map<Integer, Integer> conMap;
	private Map<Integer, List<Integer>> idMap;
	private List<C> connectors;
	private JSONCreator<C> creator;
	
	/**
	 * Creates a new instance
	 */
	public Connector(JSONCreator<C> creator){
		
		conMap = new HashMap<>();
		connectors = new ArrayList<>();
		idMap = new HashMap<>();
		this.creator = creator;
		
	}
	/**
	 * Creates a new instance from an JSON object.
	 * @param json the JSON object
	 * @param creator the {@link JSONCreator} for the connector object.
	 */
	public Connector(JSONObject json, JSONCreator<C> creator){
		
		if(json.has(CONMAP_KEY)){
			conMap = new HashMap<>(new JSONMap<>(json.getJSONObject(CONMAP_KEY), JSONCreators.INTEGER, JSONCreators.INTEGER).getMap());	
		}
		else{
			conMap = new HashMap<>();
		}
			
		if(json.has(IDMAP_KEY)){		
			idMap = new HashMap<>(new JSONMap<>(json.getJSONObject(IDMAP_KEY), JSONCreators.INTEGER, JSONCreators.INTEGER_LIST).getMap());
		}
		else{
			idMap = new HashMap<>();
		}
		
		if(json.has(CONNECTORLIST_KEY)){
			connectors = new ArrayList<>(new JSONList<>(json.getJSONObject(CONNECTORLIST_KEY), creator).getList());
		}
		else{
			connectors = new ArrayList<>();
		}
		
		this.creator = creator;
		
	}
	/**
	 * Adds a one-way connection between two objects.
	 * @param x a object
	 * @param y another object
	 */
	public void addConnection(C x, C y){
		
		Integer index_x, index_y;
		
		index_x = addConnector(x);
		index_y = addConnector(y);
		
		conMap.put(index_x, index_y);

	}
	/**
	 * Adds a one-way connection between two objects and defines the IDs.<br>
	 * Use {@link #FOO_ID} for objects, that shouldn't identified.
	 * @param x an object
	 * @param xId an ID
	 * @param y another object
	 * @param yId another ID
	 * @see #addConnector(Object, int)
	 * @see #addConnection(Object, Object)
	 */
	public void addConnection(C x, int xId, C y, int yId){
		
		addConnector(x, xId);
		addConnector(y, yId);
		
		addConnection(x, y);
	}
	/**
	 * Adds a bidirectional connection between two objects.<br>
	 * @param x an object
	 * @param y another object
	 * @see #addConnection(Object, Object)
	 */
	public void addBidirectionalConnection(C x, C y){
		
		//Pass-through
		addConnection(x, y);
		
		//Inverted pass-through
		addConnection(y, x);
		
	}
	/**
	 * Adds a bidirectional connection between two objects and sets the IDs.<br>
	 * Use {@link #FOO_ID} for objects, that shouldn't identified.
	 * @param x an object
	 * @param xId an ID
	 * @param y another object
	 * @param yId another ID
	 * @see #addConnection(Object, int, Object, int)
	 */
	public void addBidirectionalConnection(C x, int xId, C y, int yId){
		
		addConnection(x, xId, y, yId);
		addConnection(y, yId, x, xId);
		
	}
	/**
	 * Adds a connector.
	 * @param c the connector object.
	 * @return the index the connector got. (Mostly for internal use)
	 */
	public int addConnector(C c){
		
		int index = -1;
		
		if(connectors.contains(c))
			index = connectors.indexOf(c);
		else
			connectors.add(c);
		
		return index == -1 ? connectors.size()-1 : index;
		
	}
	/**
	 * Adds a connector with an ID.<br>
	 * Use {@link #FOO_ID} for objects, that shouldn't identified.
	 * @param c the connector
	 * @param id the ID
	 * @return the index the connector got. (Mostly for internal use)
	 */
	public int addConnector(C c, int id) throws IllegalArgumentException{
		
		int index = addConnector(c);
		
		if(!idMap.containsKey(id))
			idMap.put(id, new ArrayList<Integer>()); 
		
		if(!idMap.get(id).contains(index))
			idMap.get(id).add(index);
		
		return index;
	}
	/**
	 * Locates the connected object.
	 * @param c the connector
	 * @return the connected object or <code>null</code> if there is no connected object.
	 */
	public C getConnected(C c){
		
		int index = connectors.indexOf(c);
		
		if(conMap.containsKey((Integer) index))
			return connectors.get(conMap.get(index));
		
		else
			return null;
		
	}
	@Override
	public String toString(){
		
		return conMap.toString() + ":" + connectors.toString() + ":" + idMap.toString();
		
	}
	/**
	 * Generates a JSON object that holds the data of this object.
	 * @return a {@link JSONObject}
	 */
	public JSONObject getJSON(){
		
		JSONObject json = new JSONObject();
		
		JSONMap<Integer, Integer> conMap_json = new JSONMap<>(conMap, JSONCreators.INTEGER, JSONCreators.INTEGER);
		JSONMap<Integer, List<Integer>> idMap_json = new JSONMap<>(idMap, JSONCreators.INTEGER, JSONCreators.INTEGER_LIST);
		JSONList<C> connectors_json = new JSONList<>(connectors, creator);
		
		json.put(CONMAP_KEY, conMap_json.getJSON());
		json.put(IDMAP_KEY, idMap_json.getJSON());
		json.put(CONNECTORLIST_KEY, connectors_json.getJSON());
		
		return json;
		
	}
	
	/**
	 * Locates all non-identified connectors. (All connectors with ID {@link #FOO_ID})
	 * @return a {@link List} of connector objects or <code>null</code> of there are no non-identified connectors.
	 */
	public List<C> getConnectors(){
		return null;
	}
	/**
	 * Locates all connectors with the same ID.<br>
	 * Use {@link #FOO_ID} to get all non-identified connectors.
	 * @param ID the ID.
	 * @return a {@link List} of connector objects. May be empty.
	 */
	public List<C> getConnectors(int ID){
		
		List<C> list = new ArrayList<>();
		
		if(!idMap.containsKey(ID))
			return list;
		
		for(Integer i : idMap.get((Integer) ID))
			list.add(connectors.get(i));
		
		return list;
			
	}
}
