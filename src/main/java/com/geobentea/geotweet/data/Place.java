package com.geobentea.geotweet.data;

public class Place {
    private String name;
	private BoundingBox bounding_box;

    public Place(String name, BoundingBox box) {
        this.name = name;
		this.setBoundingBox(box);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '}';
    }

	public BoundingBox getBoundingBox() {
		return bounding_box;
	}

	public void setBoundingBox(BoundingBox bounding_box) {
		this.bounding_box = bounding_box;
	}
}