import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.util.ArrayList;

public class SpellCard extends Card {
	private String effect;
	private int power;
	private String targetType;
	
	// Constructor with targetType variable
	//CSV: name,cost,description,effect,power, targetType
	public SpellCard(String path, int row, String targetType) {
		super();
		String line;
		try(BufferedReader reader = new BufferedReader(new FileReader(path))){
			for(int i = 1; i < row; i++) {
				line = reader.readLine();
			}
			line = reader.readLine();
			String stats[] = line.split(",");
			this.name = stats[0];
			this.cost = Integer.parseInt(stats[1]);
			this.description = stats[2];
			this.effect = stats[3];
			this.power = Integer.parseInt(stats[4]);
			this.targetType = stats[5];
		}
		
		catch(IOException e) {
			System.err.println("Error loading SpellCard csv");
		}
		catch(Exception e) {
			System.err.println("Error in SpellCard.java");
			System.err.println(e.getMessage());;
		}
	}
	// Original constructor
	//CSV: name,cost,description,effect,power
	public SpellCard(String path, int row) {
		super();
		String line;
		try(BufferedReader reader = new BufferedReader(new FileReader(path))){
			for(int i = 1; i < row; i++) {
				line = reader.readLine();
			}
			line = reader.readLine();
			String stats[] = line.split(",");
			this.name = stats[0];
			this.cost = Integer.parseInt(stats[1]);
			this.description = stats[2];
			this.effect = stats[3];
			this.power = Integer.parseInt(stats[4]);
			this.targetType = stats[5];
		}
		
		catch(IOException e) {
			System.err.println("Error loading SpellCard csv");
		}
		catch(Exception e) {
			System.err.println("Error in SpellCard.java");
			System.err.println(e.getMessage());;
		}
	}
	
	// Activates the SpellCard depending on the type of target
	public void activate(UnitCard target, ArrayList<UnitCard> targets,Player opponent, JTextArea output) {
		targetType = this.targetType;
		
		switch(targetType) {
			case "SingleTarget":
				singleTarget(target, output);
				break;
			
			case "MultiTarget":
				multiTarget(targets, output);
				break;
			
			case "PlayerTarget":
				playerTarget(opponent, output);
				break;
			
//			case "GeneralEffect":
				
//				break;
			default:
				output.setText("Invalid target type");
				break;
				
		}
	}
	// Method called if single target
	private void singleTarget(UnitCard targetUnit, JTextArea output) {
		int damage = this.power; // power = damage of the spell
		boolean targetDestroyed = targetUnit.takeDamage(damage, output); 
		
		if (targetDestroyed == true) {
			output.append(effect + "\n");
		} else {
			output.append(this.name + " was used on " + targetUnit.getName() + "\n");
		}
		
	}
	
	// Method called if target player
	private void playerTarget(Player opponent, JTextArea output) {
		int damage = this.power;
		boolean targetDestroyed = opponent.takeDamage(damage, output);
		
		if (targetDestroyed == true) {
			output.append(effect + "\n");
		} else {
			output.append(this.name + " was used on " + opponent.getName() + "\n");
		}
	}
	
	// Method called if there are multiple targets
	private void multiTarget(ArrayList<UnitCard> targets, JTextArea output) {
		int damage = this.power;
		boolean anyTargetDestroyed = false;
		
		for (UnitCard target: targets) {
			boolean targetDestroyed = target.takeDamage(damage, output);
			
			if (targetDestroyed) {
				anyTargetDestroyed = true;
			}
		}
		if (anyTargetDestroyed) {
			output.append(effect + "\n");
		}
		else {
			output.append(this.name + " was used.\n");
		}
		
		
		
	}
	
	// Getters and Setters
	public String getTargetType(){
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	
	public void print() {
		super.print();
		System.out.print(" effect: " + effect + "\n");
	}
	
	public static void main(String[] args) {

	}
}