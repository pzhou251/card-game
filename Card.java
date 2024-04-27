import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.util.ArrayList;

public class Card {
	
	protected String name;
	protected int cost;
	protected String description;
	
	// constructors
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

	// getters/setters
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
	
	/* 
	 * Method prints card info to console for debugging
     * Pre-Condition: none
     * Post-Condition: prints to console
     */
	public void print() {
		System.out.print(" name: " + name + "\n");
		System.out.print(" cost: " + cost + "\n");
		System.out.print(" description: " + description + "\n");
	}
	
	/* 
	 * Method displays card info in the textarea for card info depending on whether it is spell or unit
     * Pre-Condition: JTextArea output where card info will be displayed
     * Post-Condition: changes GUI with card info
     */
	public void display(JTextArea output) {
		output.setText("");
		if(this instanceof UnitCard) {
			output.append("Unit\n");
		}
		else if (this instanceof SpellCard) {
			output.append("Spell\n");
		}
		output.append(name + "\n");
		output.append("Cost: " + cost + "\n");
		output.append("Description: \n" + description + "\n");
	}
	
	// Default useCard method
	public void useCard(JTextArea output) {
		output.setText("");
	}
	
	/* 
	 * Method activates spell targeting multiple units
     * Pre-Condition: ArrayList of targets, SpellCard spell, JTextArea output where results will be logged
     * Post-Condition: calls method to activate spell
     */
	public void useCard(ArrayList<UnitCard> targets, SpellCard spell, JTextArea output) {
		spell.activate(null, targets, null, output);
	}
	
	/* 
	 * Method activates spell targeting single unit
     * Pre-Condition: UnitCard unit, SpellCard spell, JTextArea output where results will be logged
     * Post-Condition: calls method to activate spell
     */
	public void useCard(UnitCard unit, SpellCard spell, JTextArea output) {
		spell.activate(unit, null, null, output);
	}
	
	/* 
	 * Method activates spell targeting opposing player
     * Pre-Condition: Player opponent, SpellCard spell, JTextArea output where results will be logged
     * Post-Condition: calls method to activate spell
     */
	public void useCard(Player opponent, SpellCard spell, JTextArea output) {
		spell.activate(null, null, opponent, output);
	}
}
