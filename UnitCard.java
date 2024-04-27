import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class UnitCard extends Card {
	protected int attack;
	protected int hp;
	protected int maxHP;
	protected String faction;
	protected boolean canAttack = false; //Units cannot attack the turn they're summoned
	protected boolean isAttacking = false;

	
	//Constructors
	public UnitCard(int hp) {
		this.hp = hp;
		this.maxHP = hp;
	}
	
	//CSV: name,cost,attack,maxHP,faction?,keywords?
	public UnitCard(String path, int row) {
		String line;
		try(BufferedReader reader = new BufferedReader(new FileReader(path))){
			for(int i = 0; i < row; i++) {
				line = reader.readLine();
			}
			line = reader.readLine();
			String stats[] = line.split(",");
			this.name = stats[0];
			this.cost = Integer.parseInt(stats[1]);
			this.attack = Integer.parseInt(stats[2]);
			this.maxHP = Integer.parseInt(stats[3]);
			this.hp = maxHP;
			this.faction = stats[4];
			
			if(!stats[5].equals("None")) { //If there are any effects
				String effects[] = stats[5].split(":");
				
				for(String effect : effects) {
					String effectInfo[] = effect.split("~");
					if(!this.description.equals("")) { //If more than one effect
						this.description += ", ";
					}
					try {
						this.description += effectInfo[0] + " " + effectInfo[2] + " " + effectInfo[1] + " " + effectInfo[3]; //Placeholder
					}
					catch (ArrayIndexOutOfBoundsException arrE){
						this.description = "None";
					}
				}
			}
		}
		
		catch(IOException e) {
			System.err.println("Error loading UnitCard csv");
		}
		catch(Exception e) {
			System.err.println("Error in UnitCard.java, row " + row);
			System.err.println(e.getMessage());;
		}
	}
	
	public UnitCard(UnitCard card) {
		this.name = card.getName();
		this.attack = card.getAttack();
		this.hp = card.getHp();
		this.maxHP = card.getMaxHp();
		this.cost = card.getCost();
		this.description = card.getDescription();
		this.faction = "";
	}
	
	// Getters/Setters
	public int getHp() {
		return hp;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int getMaxHp() {
		return maxHP;
	}
	
	public void setMaxHp(int hp) {
		this.maxHP = hp;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public void setAttack(int attack) {
		this.attack = attack;
	}
	
	public boolean canAttack() {
		return canAttack;
	}

	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}
	
	public boolean isAttacking() {
		return isAttacking;
	}

	public void setAttacking(boolean isAttacking) {
		this.isAttacking = isAttacking;
	}
	
	
	/* 
	 * UnitCard gains attack equal to int attack
     * Pre-Condition: takes an int attack value
     * Post-Condition: Returns nothing, but adds attack argument to the UnitCard's attack value
     */
	public void addAttack(int attack) {
		this.attack += attack;
	}
	
	/* 
	 * UnitCard gains both hp and maxHP equal to int health
     * Pre-Condition: takes an int health value
     * Post-Condition: Returns nothing, but adds health argument to the UnitCard's health values
     */
	public void addHealth(int health) {
		this.maxHP += health;
		this.hp += health;
	}
	

	/* 
	 * UnitCard heals hp equal to int health up to maxHP
     * Pre-Condition: takes an int health value
     * Post-Condition: Returns nothing, but adds health argument to the UnitCard's health value up to maxHP
     */
	public void heal(int health) {
		this.hp += health;
		if(hp > maxHP) {
			hp = maxHP;
		}
	}
	
	/* 
	 * UnitCard takes damage equal to int damage. Returns true if damage kills unit. Outputs log
     * Pre-Condition: takes an int damage value and JTextArea output to log damage
     * Post-Condition: Returns boolean of whether unit is killed, also subtracts damage from unit's hp
     */
	public boolean takeDamage(int damage, JTextArea output) {
		this.hp -= damage;
		output.append(name + " has taken " + damage + " damage.\n");
		if(hp <= 0) {
			output.append(name + " has died!\n");
			return true;
		}
		return false;
	}
	
	/* 
	 * Returns true if hp is > 0, false otherwise
     * Pre-Condition: none
     * Post-Condition: Returns boolean of whether unit has HP left
     */
	public boolean isAlive() {
		if(hp <= 0) {
			return false;
		}
		return true;
	}
	
	/* 
	 * Method prints card info to console for debugging
     * Pre-Condition: none
     * Post-Condition: prints to console
     */
	public void print() {
		super.print();
		System.out.print(" attack: " + attack + "\n");
		System.out.print(" hp: " + hp + "\n");
	}

	/* 
	 * Method displays card info in the textarea for card info
     * Pre-Condition: JTextArea output where card info will be displayed
     * Post-Condition: changes GUI with card info
     */
	public void display(JTextArea output) {
		super.display(output);
		output.append("Attack: " + attack + "\n");
		output.append("HP: " + hp + "\n");
	}
	
}