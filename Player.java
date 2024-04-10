import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JLabel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class Player {
	private int maxHP = 30;
	private int hp = 30;
	private LinkedList<Card> deck;
	private int mana = 1;
	private int maxMana = 1;
	private int armor = 0;
	private ArrayList<Card> hand = new ArrayList<Card>();
	
	
	public Player(String deckPath, ArrayList<Card> allCards) {
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
	
	//Getters & Setters
	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
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

	public ArrayList<Card> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
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
	public void useCard(Card card, Player target, JLabel output) {
		if(this.mana >= card.getCost()) {
			card.useCard(target, output);
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
	public void useCard(Card card, UnitCard target, JLabel output) {
		if(this.mana >= card.getCost()) {
			card.useCard(target, output);
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
	public void useCard(Card card, JLabel output) {
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
}
