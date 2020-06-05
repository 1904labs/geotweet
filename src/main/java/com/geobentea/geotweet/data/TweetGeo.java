package com.geobentea.geotweet.data;

import java.util.List;

public class TweetGeo {
	private String type;
	private List<Double> coordinates;
	
	public TweetGeo(String type, List<Double> coordinates) {
		this.setType(type);
		this.setCoordinates(coordinates);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Double> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}
}
