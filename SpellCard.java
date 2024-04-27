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
	
	//CSV: name,cost,description,effect,power,targetType
	public SpellCard(String path, int row) {
		super();
		String line;
		try(BufferedReader reader = new BufferedReader(new FileReader(path))){
			for(int i = 0; i < row; i++) {
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



	/* 
	 * Method activates the SpellCard depending on the type of target
     * Pre-Condition: takes various items based on target: UnitCard target for a singletarget attack, ArrayList of UnitCards targets for multitarget attacks,
     * 					Player opponent for playertarget attack. Also takes a JTextArea to write output to.
     * Post-Condition: Returns nothing, but calls different method based on spell's target type.
     */
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
			
			default:
				output.setText("Invalid target type");
				break;
				
		}
	}
	
	/* 
	 * Method called if single target, causes damage to a single unit and writes to log
     * Pre-Condition: takes a UnitCard targetUnit and a JTextArea to log result
     * Post-Condition: Returns nothing, but causes damage and writes to log
     */
	private void singleTarget(UnitCard targetUnit, JTextArea output) {
		int damage = this.power; // power = damage of the spell
		boolean targetDestroyed = targetUnit.takeDamage(damage, output); 
		
		if (targetDestroyed == true) {
			output.append(effect + "\n");
		} else {
			output.append(this.name + " was used on " + targetUnit.getName() + "\n");
		}
		
	}
	
	/* 
	 * Method called if targeting a player, causes damage to the player and writes to log
     * Pre-Condition: takes a Player opponent and a JTextArea to log result
     * Post-Condition: Returns nothing, but causes damage and writes to log
     */
	private void playerTarget(Player opponent, JTextArea output) {
		int damage = this.power;
		boolean targetDestroyed = opponent.takeDamage(damage, output);
		
		if (targetDestroyed == true) {
			output.append(effect + "\n");
		} else {
			output.append(this.name + " was used on your opponent." + "\n");
		}
	}
	
	/* 
	 * Method called if multi target, causes damage to all units in arraylist and writes to log
     * Pre-Condition: takes a UnitCard ArrayList targets and a JTextArea to log result
     * Post-Condition: Returns nothing, but causes damage and writes to log
     */
	private void multiTarget(ArrayList<UnitCard> targets, JTextArea output) {
		int damage = this.power;
		boolean anyTargetDestroyed = false;
		
		for (UnitCard target: targets) {
			boolean targetDestroyed = target.takeDamage(damage, output);
			
			if (targetDestroyed) {
				anyTargetDestroyed = true;
			}
		}
		if (anyTargetDestroyed == true) {
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
	
	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}
	
	/* 
	 * Method prints card info to console for debugging
     * Pre-Condition: none
     * Post-Condition: prints to console
     */
	public void print() {
		super.print();
		System.out.print(" effect: " + effect + "\n");
		System.out.print(" target type: " + targetType + "\n");
	}
	
}