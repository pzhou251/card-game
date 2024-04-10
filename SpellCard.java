import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SpellCard extends Card {
	private String effect;
	private int power;
	
	//CSV: name,cost,description,effect,power
	public SpellCard(String path, int row) {
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
		}
		
		catch(IOException e) {
			System.err.println("Error loading SpellCard csv");
		}
		catch(Exception e) {
			System.err.println("Error in SpellCard.java");
			System.err.println(e.getMessage());;
		}
	}
	
	public void print() {
		super.print();
		System.out.print(" effect: " + effect + "\n");
	}
	
	public static void main(String[] args) {
		String unitPath = "./csvs/Units.csv";
		String spellPath = "./csvs/Spells.csv";
		
		UnitCard wisp = new UnitCard(unitPath,50);
		
		wisp.print();
		
	}
}