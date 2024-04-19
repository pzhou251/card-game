import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTextArea;

public class UnitCard extends Card {
	protected int attack;
	protected int hp;
	protected int maxHP;
	protected String faction;
//	ArrayList<String> effects = new ArrayList<>(); //Could create subclass of effects?
	protected boolean canAttack = false; //Units cannot attack the turn they're summoned
//	protected JTextArea output; //Could add output as variable to UnitCard for ease of use.
	
	//CSV: name,cost,attack,maxHP,faction?,keywords?
	
	public UnitCard(int hp) {
		this.hp = hp;
		this.maxHP = hp;
	}
	
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
			
			if(!stats[5].equals("None")) { //If there is any effects
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
	
	//Mutators
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
	
	//UnitCard gains attack equal to int attack
	public void addAttack(int attack) {
		this.attack += attack;
	}
	
	//UnitCard gains both hp and maxHP equal to int health
	public void addHealth(int health) {
		this.maxHP += health;
		this.hp += health;
	}
	
	//UnitCard heals hp equal to int health up to maxHP
	public void heal(int health) {
		this.hp += health;
		if(hp > maxHP) {
			hp = maxHP;
		}
	}
	
	//UnitCard takes damage equal to int damage. Returns true if damage kills unit. Outputs log
	public boolean takeDamage(int damage, JTextArea output) {
		this.hp -= damage;
		output.append(name + " has taken " + damage + " damage.\n");
		if(hp <= 0) {
			output.append(name + " has died!\n");
			return true;
		}
		return false;
	}
	
	//Returns true if hp is > 0, false otherwise
	public boolean isAlive() {
		if(hp <= 0) {
			return false;
		}
		return true;
	}
	
	//UnitCard attacks target. Target takes damage equal to this card's attack. This takes damage equal to target's attack
	//If unit dies, removes from board
	public void attack(UnitCard target, JTextArea output) {
		output.append(name + " has attacked " + target.getName() +".\n");
		if(target.takeDamage(this.attack, output)) {
//			this.remove(); //TBD
		}
		if(this.takeDamage(target.getAttack(), output)) {
//			target.remove(); //TBD
		}
	}
	
	public void print() {
		super.print();
		System.out.print(" attack: " + attack + "\n");
		System.out.print(" hp: " + hp + "\n");
	}
	
//	public static void main(String[] args) {
//		String unitPath = "./csvs/Units.csv";
//		String spellPath = "./csvs/Spells.csv";
//		
//		UnitCard wisp = new UnitCard(unitPath,1);
//		
//		wisp.print();
//	}

	public void display(JTextArea output) {
		super.display(output);
		output.append("Attack: " + attack + "\n");
		output.append("HP: " + hp + "\n");
	}
	
}