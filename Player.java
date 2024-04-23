import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class Player extends UnitCard {
	private LinkedList<Card> deck;
	private int mana = 1;
	private int maxMana = 1;
	private int armor = 0;
	private int deckSize = 0;
	private ArrayList<Card> hand = new ArrayList<Card>();
	
	
	public Player(String deckPath, ArrayList<Card> allCards, int hp) {
		super(hp);
		boolean added;
		deck = new LinkedList<Card>();
		String line;
		try(BufferedReader reader = new BufferedReader(new FileReader(deckPath))){
			line = reader.readLine();
			String cardNames[] = line.split(",");
			for(String cardName : cardNames) {	
				added = false;
				for(Card card : allCards) {
					if(card.getName().equals(cardName)) {
						deck.add(card);
						added = true;
					}
				}
				if(!added) {
					System.err.println("Error adding card: " + cardName);
				}
			}
		}
	
		catch(IOException e) {
			System.err.println("Error loading cards file");
		}
	}
		
//	public Player(String deckPath, int row, ArrayList<Card> allCards) {
//		super(hp);
//		boolean added;
//		deck = new LinkedList<Card>();
//		String line;
//		try (BufferedReader reader = new BufferedReader(new FileReader(deckPath))){
//			line = reader.readLine();
//			String cards[] = line.split(",");
//			for(String cardInfo : cards) {
//				String[] stats = cardInfo.split(":");
//				String cardName = stats[0];
//				int numToAdd = Integer.parseInt(stats[1]);
//				added = false;
//				int numAdded = 0;
//				for(int i = 0; i < numToAdd; i++) {
//					for(Card card : allCards) {
//						if(card.getName().equals(cardName)) {
//							deck.add(card);
//							added = true;
//							numAdded += 1;
//							this.deckSize += 1;
//						}
//					}
//					if(!added) {
//						System.err.println("Error adding card: " + cardName);
//					}
//				}
//				System.out.println("Added " + numAdded + " " + cardName);
//			}
//		}
//	
//		catch(IOException e) {
//			System.err.println("Error loading cards file");
//		}
//	}
	
	//Getters & Setters

	public void setMaxHp(int maxHP) {
		this.maxHP = maxHP;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	};
	
	public void useMana(int mana) { //Calling this method requires checking if there's enough mana first
		this.mana -= mana;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}
	
	public LinkedList<Card> getDeck() {
		return deck;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public void setDeck(LinkedList<Card> deck) {
		this.deck = deck;
	}

	public int getDeckSize() {
		return deckSize;
	}

	public void setDeckSize(int deckSize) {
		this.deckSize = deckSize;
	}

	public String[] getHandNamesArray() {
		ArrayList<String> cardNames = new ArrayList<>();
		for (Card card : hand) {
			cardNames.add(card.getName() + " - " + card.getCost());
		}
		String[] tempArray = new String[cardNames.size()];
		return cardNames.toArray(tempArray);
	}
	
	
	//Pre-condition: Player takes damage
	//Post-condition: Player loses armor then loses hp equal to excess damage. Returns true if fainted
	public void takeDamage(int damage) {
		armor -= damage;
		if(armor < 0) {
			hp += armor; //Subtracts extra damage from hp
			armor = 0;
		}
	}
	
	//Pre-condition: Player heals amount
	//Post-condition: Player gains hp up to max HP
	public void heal(int amount) {
		hp += amount;
		if(hp < maxHP) {
			hp = maxHP;
		}
	}
	
	//Pre-condition: Player draws a number of cards
	//Post-condition: All cards drawn are added to player's hand
	public void draw(int amount) {
		for(int i = 0; i < amount; i++) {
			Card card = deck.pollFirst();
			hand.add(card);
		}
	}
	
	
	//Pre-condition: Player discards a specific card
	//Post-condition: Card is removed from player's hand
	public void discard(Card card) {
		hand.remove(card);
	}
	
	//Pre-condition: Player uses a card on enemy player
	//Post-condition: Card effect happens based on card type, player loses mana equal to cost. Card is then discarded
	public void useCard(Card card, Player target, JTextArea output) {
		if(this.mana >= card.getCost()) {
			card.useCard(target, null,output); // CHECK ME
			discard(card);
			mana -= card.getCost();
			System.out.println("Player used " + card.getName() + " on enemy player");
		}
		else {
			output.setText("Not enough mana");
		}
	}
	
	//Pre-condition: Player uses a card on unit
	//Post-condition: Card effect happens based on card type, player loses mana equal to cost. Card is then discarded
	public void useCard(Card card, UnitCard target, JTextArea output) {
		if(this.mana >= card.getCost()) {
			card.useCard(target, null, output); // CHECK ME
			discard(card);
			mana -= card.getCost();
			System.out.println("Player used " + card.getName() + " on " + target.getName());
		}
		else {
			output.setText("Not enough mana");
		}
	}
	
	//Pre-condition: Player uses a card
	//Post-condition: Card effect happens based on card type, player loses mana equal to cost. Card is then discarded
	public void useCard(Card card, JTextArea output) {
		if(this.mana >= card.getCost()) {
			card.useCard(output);
			discard(card);
			mana -= card.getCost();
			System.out.println("Player used " + card.getName());
		}
		else {
			output.setText("Not enough mana");
		}
	}
	
	//Raises max mana by 1, refreshes mana, draws 1 new card
	public void endTurn() {
		if(maxMana < 10) {
			maxMana += 1;
		}
		mana = maxMana;
		draw(1);
	}
	
//	public static void main(String[] args) {
//		ArrayList<Card> allCards = new ArrayList<>();
//		String unitPath = "./csvs/Units.csv";
//		String deckPath = "./csvs/playerDecks.csv";
//		UnitCard newCard = new UnitCard(unitPath,1);
//		int row = 1;
//		while(!newCard.getName().equals("Gruul")) { //Check if last card in csv has been added
//			newCard = new UnitCard(unitPath,row);
//			allCards.add(newCard);
//			row++;
//		}	
//		System.out.println("Finished adding " + (row-1) + " cards.");
//		Player player = new Player(deckPath,1,allCards);
//		System.out.println("Deck size: " + player.getDeck().size());
//	}
}
