package com.geobentea.geotweet.data;

import java.util.List;

public class BoundingBox {

	private String type;
	private List<List<List<Double>>> coordinates;
	
	public BoundingBox (String typeparam, List<List<List<Double>>> coords) {
		setType(typeparam);
		setList(coords);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<List<List<Double>>> getList() {
		return coordinates;
	}

	public void setList(List<List<List<Double>>> list) {
		this.coordinates = list;
	}
}
