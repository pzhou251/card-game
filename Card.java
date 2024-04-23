import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.util.ArrayList;

public class Card {
	
	protected String name;
	protected int cost;
	protected String description;
	
	public Card() {
		this.name = "test";
		this.cost = 0;
		this.description = "";
	}
	
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
	
	public void display(JTextArea output) {
		output.setText(name + "\n");
		output.append("Cost: " + cost + "\n");
		output.append("Description: \n" + description + "\n");
	}
	
	// Default useCard constructor
	public void useCard(JTextArea output) {
		output.setText("");
	}
	
	// useCard method to activate a spell targets multiple units
	public void useCard(ArrayList<UnitCard> targets, SpellCard spell, JTextArea output) {
		spell.activate(null, targets, null, output);
	}
	
	// useCard method to activate a spell that targets a unit
	public void useCard(UnitCard unit, SpellCard spell, JTextArea output) {
		spell.activate(unit, null, null, output);
	}
	
	// useCard method to activate a spell that targets the opponent player
	public void useCard(Player opponent, SpellCard spell, JTextArea output) {
		spell.activate(null, null, opponent, output);
	}
}
