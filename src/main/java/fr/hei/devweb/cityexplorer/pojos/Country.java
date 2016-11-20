package fr.hei.devweb.cityexplorer.pojos;

public enum Country {
	FRANCE("France"), UK("United Kingdom"), USA("U.S.A.");
	
	private String label;

	private Country(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}	
}
