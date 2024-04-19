import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JLabel;
import javax.swing.JTextArea;

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
		output.append("cost: " + cost + "\n");
		output.append("description: \n" + description + "\n");
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
