
public class Game {
	private Player p1;
	private Player p2;
	
	// Constructor
	public Game(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	//Accessors
	public Player getP1() {
		return this.p1;
	}
	public Player getP2() {
		return this.p2;
	}
}
