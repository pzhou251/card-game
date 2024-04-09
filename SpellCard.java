
public class SpellCard extends Card {
	private String effect;
	
	public SpellCard(String name, int cost, String description, String effect) {
		super(name, cost, description);
		this.effect = effect;
	}
	
	public void print() {
		super.print();
		System.out.print(" effect: " + effect + "\n");
	}
	
	public static void main(String[] args) {
		Card testCard = new Card("TestBaseCard", 2, "our test card");
		UnitCard testUnitCard = new UnitCard("TestUnitCard", 2, "our test card", 10, 10, 10);
		SpellCard testSpellCard = new SpellCard("TestSpellCard", 2, "our test card", "does a thing");
		
		testCard.print();
		testUnitCard.print();
		testSpellCard.print();
	}
}