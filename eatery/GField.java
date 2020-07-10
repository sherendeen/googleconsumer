package eatery;

public enum GField {
	NAME(".kno-ecr-pt"),
	TELEPHONE_NUMBER(".zdqRlf"),
	STREET_ADDRESS(".i4J0ge"),
	RATING(".Aq14fc"),//..EBe2gf
	HOURS_TABLE(".WgFkxc");

	private String cssSelector;
	
	GField(String cssSelector) {
		this.cssSelector = cssSelector;
	}
	
	public String getCssSelector() {
		return this.cssSelector;
	}
}
