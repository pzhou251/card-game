import javax.swing.JLabel;

public class Card {
	
	protected String name;
	protected int cost;
	protected String description;
	
	public Card(String name, int cost, String description) {
		this.name = name;
		this.cost = cost;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void print() {
		System.out.print(" name: " + name + "\n");
		System.out.print(" cost: " + cost + "\n");
		System.out.print(" description: " + description + "\n");
	}
	
	public void useCard(JLabel output) {
		
		
		output.setText("");
	}
	
	public void useCard(UnitCard unit, JLabel output) {
		
		
		output.setText("");
	}
	
	public void useCard(Player target, JLabel output) {
		
		
		output.setText("");
	}
}
