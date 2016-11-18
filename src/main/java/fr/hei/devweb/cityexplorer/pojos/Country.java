package fr.hei.devweb.cityexplorer.pojos;

public enum Country {
	FRANCE("France"), UK("United Kingdom"), USA("U.S.A.");
	
	private String label;

	private Country(String libelle) {
		this.label = libelle;
	}

	public String getLabel() {
		return label;
	}
}
