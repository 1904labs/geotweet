package com.geobentea.geotweet.data;

public class Tweet {
    private String id;
    private String text;
    private String created_at;
    private User user;
	private Place place;
	private TweetGeo geo;

    public Tweet(String id, String text, TweetGeo geo, String created_at, Place place, User user) {
        this.setId(id);
        this.setText(text);
		this.setGeo(geo);
        this.setCreatedAt(created_at);
		this.setPlace(place);
        this.setUser(user);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String lang) {
        this.created_at = lang;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", geo='" + geo + '\'' +
                ", created_at='" + created_at + '\'' +
                ", place=" + place +
                ", user=" + user +
                '}';
    }

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public TweetGeo getGeo() {
		return geo;
	}

	public void setGeo(TweetGeo geo) {
		this.geo = geo;
	}
}