import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UnitCard extends Card {
	private int attack;
	private int hp;
	private int maxHP;
	private String faction;
//	ArrayList<String> effects = new ArrayList<>(); //Could create subclass of effects?
	
	//CSV: name,cost,attack,maxHP,faction?,keywords?
	public UnitCard(String path, int row) {
		String line;
		try(BufferedReader reader = new BufferedReader(new FileReader(path))){
			for(int i = 1; i < row+1; i++) {
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
					this.description += effectInfo[0] + " " + effectInfo[2] + " " + effectInfo[1] + " " + effectInfo[3]; //Placeholder
//					this.effects.add(new Effect()); // Placeholder for effects?
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
	
	public void print() {
		super.print();
		System.out.print(" attack: " + attack + "\n");
		System.out.print(" hp: " + hp + "\n");
	}
	
	public static void main(String[] args) {
		String unitPath = "./csvs/Units.csv";
		String spellPath = "./csvs/Spells.csv";
		
		UnitCard wisp = new UnitCard(unitPath,1);
		
		wisp.print();
	}
}