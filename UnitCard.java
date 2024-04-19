import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTextArea;

public class UnitCard extends Card {
	private int attack;
	protected int hp;
	protected int maxHP;
	private String faction;
//	ArrayList<String> effects = new ArrayList<>(); //Could create subclass of effects?
	
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
	
	public int getHp() {
		return hp;
	}
	
	public int getMaxHp() {
		return maxHP;
	}
	
	public int getAttack() {
		return attack;
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
		output.append("attack: " + attack + "\n");
		output.append("hp: " + hp + "\n");
	}
	
}