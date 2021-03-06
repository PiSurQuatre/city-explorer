package fr.hei.devweb.cityexplorer.pojos;

public class City {

	private Integer id;
	private String name;
	private String summary;
	private Integer likes;
	private Integer dislikes;
	private Country country;	
	
	public City() {
	}


	public City(Integer id, String name, String summary, Country country, Integer likes, Integer dislikes) {
		super();
		this.id = id;
		this.name = name;
		this.summary = summary;
		this.likes = likes;
		this.dislikes = dislikes;
		this.country = country;
	}



	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}
	
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
	
}
