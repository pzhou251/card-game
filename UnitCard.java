
public class UnitCard extends Card {
	private int attack;
	private int hp;
	private int maxHP;
	
	public UnitCard(String name, int cost, String description, int attack, int hp, int maxHP) {
		super(name, cost, description);
		this.attack = attack;
		this.maxHP = maxHP;
		this.hp = hp;
	}
	
	public void print() {
		super.print();
		System.out.print(" attack: " + attack + "\n");
		System.out.print(" hp: " + hp + "\n");
	}
}