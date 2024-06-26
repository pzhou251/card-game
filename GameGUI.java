import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private int gameState = 0; //controls GUI appearance
	private Player p1;
	private Player p2;
	private static boolean playerTurn = true; //true means player 1 (left), false means player 2 (right)
	private static ArrayList<Card> allCards = null;
	private static Card selectedCard = null;
	private ArrayList<Card> hand1;
	private ArrayList<Card> hand2;
	private UnitCard[] p1ActiveUnits = new UnitCard[7]; //Stores all units on the board for each player
	private UnitCard[] p2ActiveUnits = new UnitCard[7];
	private ArrayList<UnitCard> attackingUnits = new ArrayList<>(); //Stores units that are attacking in gameState 3
	private UnitCard activeAttacker = null;
	
	private JLabel lblInstructions;
	private JTextArea textAreaLog;
	private JLabel lblTurnInfo;
	
	private JTextArea textAreaInfo1;
	private JList<String> listHand1;
	private JLabel lblDeck1;
	private JLabel lblHP1;
	private JLabel lblArmor1;
	private JLabel lblMana1;
	private JLabel lblHand1;
	private JButton btnUseCard1;
	private JButton btnAttack1;
	private JButton btnEndTurn1;
	
	private JTextArea textAreaInfo2;
	private JList<String> listHand2;
	private JLabel lblDeck2;
	private JLabel lblHP2;
	private JLabel lblArmor2;
	private JLabel lblMana2;
	private JLabel lblHand2;
	private JButton btnUseCard2;
	private JButton btnAttack2;
	private JButton btnEndTurn2;
	
	

	/*
	 * Main method is the entry point to the program. Loads cards, creates players, creates game object, and starts GUI.
	 * Pre-Condition: takes a String[] of arguments
	 * Post-Condition: returns nothing, but creates the game object and most of the GUI pages
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int NUM_UNITS = 50;
					int NUM_SPELLS = 9;
					String unitPath = "./csvs/Units.csv";
					String spellPath = "./csvs/Spells.csv";
					String deckPath = "./csvs/sampledeck.csv";
					
					allCards = new ArrayList<>();
					try {
						for(int i = 1; i < NUM_UNITS+1; i++) {
							UnitCard unit = new UnitCard(unitPath,i);
							allCards.add(unit);
							unit.print();		}
					}
					catch(Exception e) {
						System.out.println("Incorrect num cards set for Units.csv");
					}
					
					try {
						for(int i = 1; i < NUM_SPELLS+1; i++) {
							SpellCard spell = new SpellCard(spellPath,i);
							allCards.add(spell);
							spell.print();		}
					}
					catch(Exception e) {
						System.out.println("Incorrect num cards set for Spells.csv");
					}
					
					Player player1 = new Player(deckPath, allCards, 30); 
					Player player2 = new Player(deckPath, allCards, 30); 
					Game game = new Game(player1, player2);
					GameGUI frame = new GameGUI(game);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor creates the frame with game object holding player info.
	 */
	public GameGUI(Game game) {
		GameGUI frame = this;
		setTitle("Card Battle!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(0, 0, 1200, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//set up the players and their decks
		p1 = game.getP1();
		p2 = game.getP2();
		LinkedList<Card> deck1 = p1.getDeck();
		Collections.shuffle(deck1);
		LinkedList<Card> deck2 = p2.getDeck();
		Collections.shuffle(deck2);
		
		//initial hand
		p1.draw(5);
		hand1 = p1.getHand();
		p2.draw(5);
		hand2 = p2.getHand();
		
		
		//information at the top of the board
		lblTurnInfo = new JLabel("It's Player 1's Turn.");
		lblTurnInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTurnInfo.setBounds(500, 17, 200, 16);
		contentPane.add(lblTurnInfo);
		
		lblInstructions = new JLabel("Play cards from hand and send minions to attack.");
		lblInstructions.setHorizontalAlignment(SwingConstants.CENTER);
		lblInstructions.setBounds(420, 37, 360, 16);
		contentPane.add(lblInstructions);
		
		//separator line at center of the board
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(594, 80, 12, 500);
		contentPane.add(separator);
		
		//log at the bottom of the board
		JLabel lblLog = new JLabel("Action Log:");
		lblLog.setHorizontalAlignment(SwingConstants.CENTER);
		lblLog.setBounds(550, 620, 100, 16);
		contentPane.add(lblLog);
		
		textAreaLog = new JTextArea();
		textAreaLog.setEditable(false);
		JScrollPane scrollPaneLog = new JScrollPane(textAreaLog);
		scrollPaneLog.setBounds(420, 640, 360, 120);
		contentPane.add(scrollPaneLog);
		
		//player 1 info on left of board
		JLabel lblPlayer1 = new JLabel("Player 1");
		lblPlayer1.setBounds(35, 17, 100, 30);
		lblPlayer1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer1.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		contentPane.add(lblPlayer1);
		
		lblHP1 = new JLabel("HP: " + p1.getHp() + "/" + p1.getMaxHp());
		lblHP1.setBounds(35, 56, 100, 16);
		lblHP1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblHP1);
		
		lblArmor1 = new JLabel("Armor: " + p1.getArmor());
		lblArmor1.setBounds(35, 76, 100, 16);
		lblArmor1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblArmor1);
		
		lblDeck1 = new JLabel("Deck: " + deck1.size());
		lblDeck1.setBounds(35, 96, 100, 16);
		lblDeck1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblDeck1);
		
		lblMana1 = new JLabel("Mana: " + p1.getMana() + "/" + p1.getMaxMana());
		lblMana1.setBounds(35, 116, 100, 16);
		lblMana1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblMana1);
		
		lblHand1 = new JLabel("Hand:");
		lblHand1.setBounds(35, 175, 100, 16);
		lblHand1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblHand1);
		
		//player 1 list of cards in hand
		listHand1 = new JList<String>(p1.getHandNamesArray());
		listHand1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) { //action listener for when an item is selected in list
				try {
					String[] nameSplit = listHand1.getSelectedValue().split(" - ");
					String name = nameSplit[0];
					System.out.println(name);
					for(Card card : p1.getHand()) {
						if(card.getName().equals(name)) {
							selectedCard = card;
						}
					}
					selectedCard.display(textAreaInfo1);			
					
				} catch (NullPointerException nfe) {
					 textAreaInfo1.setText("Select a card to see its facts here.");
				}
			}
		});
		listHand1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPaneList1 = new JScrollPane(listHand1);
		scrollPaneList1.setBounds(6, 200, 170, 120);
		contentPane.add(scrollPaneList1);
		
		//displays the card info that is selected in the hand
		textAreaInfo1 = new JTextArea();	
		textAreaInfo1.setEditable(false);
		textAreaInfo1.setLineWrap(true);
		textAreaInfo1.setWrapStyleWord(true);
		JScrollPane scrollPaneCard1 = new JScrollPane(textAreaInfo1);
		scrollPaneCard1.setBounds(6, 350, 170, 170);
		contentPane.add(scrollPaneCard1);

		//player 1's buttons
		btnUseCard1 = new JButton("Use Card");
		btnUseCard1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedCard != null) {
					changeState(2);
				}
				if (selectedCard instanceof SpellCard) {
					SpellCard spell = (SpellCard) selectedCard;
					if(spell.getTargetType().equals("MultiTarget") || spell.getTargetType().equals("PlayerTarget")) {
						if(p1.getMana() >= spell.getCost()) {
							spellTargetSelection(spell);
						}
						else {
							lblInstructions.setText("Not enough mana");
						}
					}
					else {
						changeState(5);
					}
				}
				
			}
		});
		btnUseCard1.setBounds(35, 542, 115, 29);
		contentPane.add(btnUseCard1);
		
		btnAttack1 = new JButton("Attack");
		btnAttack1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gameState == 4) {
					boolean hitsFace = true;
					for(UnitCard defUnit : p1ActiveUnits) {
						if(defUnit != null) {
							if(defUnit.isAttacking()) { //if is defending
								hitsFace = false; //if there is at least one defender, attacker will not hit player
								textAreaLog.append(defUnit.getName() + " defends against " + activeAttacker.getName() + "\n");
								//Deal and take damage to active attacking unit
								defUnit.takeDamage(activeAttacker.getAttack(), textAreaLog);
								activeAttacker.takeDamage(defUnit.getAttack(), textAreaLog);
								defUnit.setAttacking(false);
								defUnit.setCanAttack(false);
								attackingUnits.remove(activeAttacker);
								activeAttacker.setAttacking(false);
							}
						}
					}
					if(hitsFace) {
						textAreaLog.append("Player 1 gets hit by " + activeAttacker.getName() + " for " + activeAttacker.getAttack() + " damage.\n");
						//player takes damage equal to attacking unit's attack
						p1.takeDamage(activeAttacker.getAttack());
						attackingUnits.remove(activeAttacker);
						activeAttacker.setAttacking(false);
					}
					activeAttacker = null;
					if(attackingUnits.size() == 0) { //if no more attacking units
						btnAttack1.setText("Attack");
						changeState(1);
					}
					else {
						activeAttacker = attackingUnits.get(0);
						changeState(4);
					}
					refresh();
				}
				else {
					changeState(3);
				}
			}
		});
		btnAttack1.setBounds(35, 616, 115, 29);
		btnAttack1.setEnabled(false);
		contentPane.add(btnAttack1);
		
		btnEndTurn1 = new JButton("End Turn");
		btnEndTurn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerTurn = false;
				boolean unitsAttacking = false;
				for(UnitCard unit : p1ActiveUnits) {
					if(unit != null) {
						if(unit.isAttacking()) {
							attackingUnits.add(unit);
							unitsAttacking = true;
						}
					}
				}
				if(unitsAttacking) {
					changeState(4);
				}
				else {
					changeState(1);
				}
				p1.endTurn();
				for(UnitCard unit : p1ActiveUnits) {
					if(unit != null) {
						unit.setCanAttack(true);
					}
				}
				refresh();
			}
		});
		btnEndTurn1.setBounds(35, 691, 115, 29);
		contentPane.add(btnEndTurn1);
		
		
		//player 1's board panels
		JPanel panel1_1 = new JPanel();
		panel1_1.setName("panel11");
		panel1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && playerTurn) {
					playCard(panel1_1,0);
				}
				if (gameState == 3 && playerTurn) {
					UnitCard unit = p1ActiveUnits[0];
					toggleAttack(unit, panel1_1);
				}
				if (gameState == 4 && playerTurn) {
					UnitCard unit = p1ActiveUnits[0];
					toggleDefend(unit, panel1_1);
				}
				if(gameState == 5) {
					if(p1ActiveUnits[0] != null) {
						castSpell(selectedCard, p1ActiveUnits[0]);
					}
				}
			}
		});
		panel1_1.setBounds(240, 80, 140, 140);
		panel1_1.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel1_1);
		
		
		
		JPanel panel1_2 = new JPanel();
		panel1_2.setName("panel12");
		panel1_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && playerTurn) {
					playCard(panel1_2,1);
				}
				if (gameState == 3 && playerTurn) {
					UnitCard unit = p1ActiveUnits[1];
					toggleAttack(unit, panel1_2);
				}
				if (gameState == 4 && playerTurn) {
					UnitCard unit = p1ActiveUnits[1];
					toggleDefend(unit, panel1_2);
				}
				if(gameState == 5) {
					if(p1ActiveUnits[1] != null) {
						castSpell(selectedCard, p1ActiveUnits[1]);
					}
				}

			}
		});
		panel1_2.setBounds(240, 260, 140, 140);
		panel1_2.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel1_2);
		
		
		JPanel panel1_3 = new JPanel();
		panel1_3.setName("panel13");
		panel1_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && playerTurn) {
					playCard(panel1_3,2);
				}
				if (gameState == 3 && playerTurn) {
					UnitCard unit = p1ActiveUnits[2];
					toggleAttack(unit, panel1_3);
				}
				if (gameState == 4 && playerTurn) {
					UnitCard unit = p1ActiveUnits[2];
					toggleDefend(unit, panel1_3);
				}
				if(gameState == 5) {
					if(p1ActiveUnits[2] != null) {
						castSpell(selectedCard, p1ActiveUnits[2]);
					}
				}

			}
		});
		panel1_3.setBounds(240, 440, 140, 140);
		panel1_3.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel1_3);
		
		
		JPanel panel1_4 = new JPanel();
		panel1_4.setName("panel14");
		panel1_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && playerTurn) {
					playCard(panel1_4,3);
				}
				if (gameState == 3 && playerTurn) {
					UnitCard unit = p1ActiveUnits[3];
					toggleAttack(unit, panel1_4);
				}
				if (gameState == 4 && playerTurn) {
					UnitCard unit = p1ActiveUnits[3];
					toggleDefend(unit, panel1_4);
				}
				if(gameState == 5) {
					castSpell(selectedCard, p1ActiveUnits[3]);
					if(p1ActiveUnits[3] != null) {
						castSpell(selectedCard, p1ActiveUnits[3]);
					}
				}

			}
		});
		panel1_4.setBounds(240, 620, 140, 140);
		panel1_4.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel1_4);
		
		JPanel panel1_5 = new JPanel();
		panel1_5.setName("panel15");
		panel1_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && playerTurn) {
					playCard(panel1_5,4);
				}
				if (gameState == 3 && playerTurn) {
					UnitCard unit = p1ActiveUnits[4];
					toggleAttack(unit, panel1_5);
				}
				if (gameState == 4 && playerTurn) {
					UnitCard unit = p1ActiveUnits[4];
					toggleDefend(unit, panel1_5);
				}
				if(gameState == 5) {
					if(p1ActiveUnits[4] != null) {
						castSpell(selectedCard, p1ActiveUnits[4]);
					}
				}

			}
		});
		panel1_5.setBounds(420, 80, 140, 140);
		panel1_5.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel1_5);
		
		JPanel panel1_6 = new JPanel();
		panel1_6.setName("panel16");
		panel1_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && playerTurn) {
					playCard(panel1_6,5);
				}
				if (gameState == 3 && playerTurn) {
					UnitCard unit = p1ActiveUnits[5];
					toggleAttack(unit, panel1_6);
				}
				if (gameState == 4 && playerTurn) {
					UnitCard unit = p1ActiveUnits[5];
					toggleDefend(unit, panel1_6);
				}
				if(gameState == 5) {
					if(p1ActiveUnits[5] != null) {
						castSpell(selectedCard, p1ActiveUnits[5]);
					}
				}

			}
		});
		panel1_6.setBounds(420, 260, 140, 140);
		panel1_6.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel1_6);
		
		JPanel panel1_7 = new JPanel();
		panel1_7.setName("panel17");
		panel1_7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && playerTurn) {
					playCard(panel1_7,6);
				}
				if (gameState == 3 && playerTurn) {
					UnitCard unit = p1ActiveUnits[6];
					toggleAttack(unit, panel1_7);
				}
				if (gameState == 4 && playerTurn) {
					UnitCard unit = p1ActiveUnits[6];
					toggleDefend(unit, panel1_7);
				}
				if(gameState == 5) {
					if(p1ActiveUnits[6] != null) {
						castSpell(selectedCard, p1ActiveUnits[6]);
					}
				}

			}
		});
		panel1_7.setBounds(420, 440, 140, 140);
		panel1_7.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel1_7);
		
		
		

		//player 2 info on right of board
		JLabel lblPlayer2 = new JLabel("Player 2");
		lblPlayer2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer2.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lblPlayer2.setBounds(1065, 17, 100, 30);
		contentPane.add(lblPlayer2);
		
		lblHP2 = new JLabel("HP: " + p2.getHp() + "/" + p2.getMaxHp());
		lblHP2.setHorizontalAlignment(SwingConstants.CENTER);
		lblHP2.setBounds(1065, 56, 100, 16);
		contentPane.add(lblHP2);
		
		lblArmor2 = new JLabel("Armor: " + p2.getArmor());
		lblArmor2.setHorizontalAlignment(SwingConstants.CENTER);
		lblArmor2.setBounds(1065, 76, 100, 16);
		contentPane.add(lblArmor2);
		
		lblDeck2 = new JLabel("Deck: " + deck2.size());
		lblDeck2.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeck2.setBounds(1065, 96, 100, 16);
		contentPane.add(lblDeck2);
		
		lblMana2 = new JLabel("Mana: " + + p2.getMana() + "/" + p2.getMaxMana());
		lblMana2.setHorizontalAlignment(SwingConstants.CENTER);
		lblMana2.setBounds(1065, 116, 100, 16);
		contentPane.add(lblMana2);
		
		
		//player 2 list of cards in hand
		lblHand2 = new JLabel("Hand:");
		lblHand2.setHorizontalAlignment(SwingConstants.CENTER);
		lblHand2.setBounds(1065, 175, 100, 16);
		contentPane.add(lblHand2);
		
		
		listHand2 = new JList<String>(p2.getHandNamesArray());
		listHand2.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				try {
					String[] nameSplit = listHand2.getSelectedValue().split(" - ");
					String name = nameSplit[0];
					System.out.println(name);
					for(Card card : p2.getHand()) {
						if(card.getName().equals(name)) {
							selectedCard = card;
						}
					}
					selectedCard.display(textAreaInfo2);
					
				} catch (NullPointerException nfe) {
					 textAreaInfo2.setText("Select a card to see its facts here.");
				}
			}
		});
		listHand1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPaneList2 = new JScrollPane(listHand2);
		scrollPaneList2.setBounds(1024, 200, 170, 120);
		contentPane.add(scrollPaneList2);
		
		//displays info about a selected card in hand
		textAreaInfo2 = new JTextArea();
		textAreaInfo2.setEditable(false);
		textAreaInfo2.setWrapStyleWord(true);
		textAreaInfo2.setLineWrap(true);
		JScrollPane scrollPaneInfo2 = new JScrollPane(textAreaInfo2);
		scrollPaneInfo2.setBounds(1024, 350, 170, 170);
		contentPane.add(scrollPaneInfo2);
		
		//player 2's buttons
		btnUseCard2 = new JButton("Use Card");
		btnUseCard2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedCard != null) {
					changeState(2);
				}
				if (selectedCard instanceof SpellCard) {
					SpellCard spell = (SpellCard) selectedCard;
					if(spell.getTargetType().equals("MultiTarget") || spell.getTargetType().equals("PlayerTarget")) {
						if(p2.getMana() >= spell.getCost()) {
							spellTargetSelection(spell);
						}
						else {
							lblInstructions.setText("Not enough mana");
						}
					}
					else {
						changeState(5);
					}
				}
			}
		});
		btnUseCard2.setBounds(1053, 542, 115, 29);
		contentPane.add(btnUseCard2);
		
		btnAttack2 = new JButton("Attack");
		btnAttack2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gameState == 4) {
					boolean hitsFace = true;
					for(UnitCard defUnit : p2ActiveUnits) {
						if(defUnit != null) {
							if(defUnit.isAttacking()) { //if is defending
								textAreaLog.append(defUnit.getName() + " defends against " + activeAttacker.getName() + "\n");
								hitsFace = false; //if there is at least one defender, attacker will not hit player
								//Deal and take damage to active attacking unit
								defUnit.takeDamage(activeAttacker.getAttack(), textAreaLog);
								activeAttacker.takeDamage(defUnit.getAttack(), textAreaLog);
								defUnit.setAttacking(false); //Used as defending here
								defUnit.setCanAttack(false); //Unit cannot attack turn it defends
								attackingUnits.remove(activeAttacker);
								activeAttacker.setAttacking(false);
							}
						}
					}
					if(hitsFace) {
						textAreaLog.append("Player 2 gets hit by " + activeAttacker.getName() + " for " + activeAttacker.getAttack() + " damage.\n");
						//player takes damage equal to attacking unit's attack
						p2.takeDamage(activeAttacker.getAttack());
						attackingUnits.remove(activeAttacker);
						activeAttacker.setAttacking(false);
					}
					activeAttacker = null;
					if(attackingUnits.size() == 0) { //if no more attacking units
						btnAttack2.setText("Attack");
						changeState(1);
					}
					else {
						activeAttacker = attackingUnits.get(0);
						changeState(4);
					}
					refresh();
				}
				else {
					changeState(3);
				}
			}
		});
		btnAttack2.setEnabled(false);
		btnAttack2.setBounds(1053, 616, 115, 29);
		contentPane.add(btnAttack2);
		
		btnEndTurn2 = new JButton("End Turn");
		btnEndTurn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerTurn = true;
				boolean unitsAttacking = false;
				for(UnitCard unit : p2ActiveUnits) {
					if(unit != null) {
						if(unit.isAttacking()) {
							attackingUnits.add(unit);
							unitsAttacking = true;
						}
					}
				}
				if(unitsAttacking) {
					activeAttacker = attackingUnits.get(0);
					changeState(4);
				}
				else {
					changeState(1);
				}
				p2.endTurn();
				for(UnitCard unit : p2ActiveUnits) {
					if(unit != null) {
						unit.setCanAttack(true);
						System.out.println(unit.getName() + " can attack");
					}
				}
				refresh();
			}
		});
		btnEndTurn2.setBounds(1053, 691, 115, 29);
		contentPane.add(btnEndTurn2);
		
		
		//player 2's panels on board
		JPanel panel2_1 = new JPanel();
		panel2_1.setName("panel21");
		panel2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && !playerTurn) {
					playCard(panel2_1,0);
				}
				if (gameState == 3 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[0];
					toggleAttack(unit, panel2_1);
				}
				if (gameState == 4 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[0];
					toggleDefend(unit, panel2_1);
				}
				if (gameState == 5) {
					if(p2ActiveUnits[0] != null) {
						castSpell(selectedCard, p2ActiveUnits[0]);
					}
				}
			}
		});
		panel2_1.setBounds(820, 80, 140, 140);
		panel2_1.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel2_1);
		
		
		JPanel panel2_2 = new JPanel();
		panel2_2.setName("panel22");
		panel2_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && !playerTurn) {
					playCard(panel2_2,1);
				}
				if (gameState == 3 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[1];
					toggleAttack(unit, panel2_2);
				}
				if (gameState == 4 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[1];
					toggleDefend(unit, panel2_2);
				}
				if (gameState == 5) {
					if(p2ActiveUnits[1] != null) {
						castSpell(selectedCard, p2ActiveUnits[1]);
					}
				}
			}
		});
		panel2_2.setBounds(820, 260, 140, 140);
		panel2_2.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel2_2);
		
		
		JPanel panel2_3 = new JPanel();
		panel2_3.setName("panel23");
		panel2_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && !playerTurn) {
					playCard(panel2_3,2);
				}
				if (gameState == 3 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[2];
					toggleAttack(unit, panel2_3);
				}
				if (gameState == 4 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[2];
					toggleDefend(unit, panel2_3);
				}
				if (gameState == 5) {
					if(p2ActiveUnits[2] != null) {
						castSpell(selectedCard, p2ActiveUnits[2]);
					}
				}
			}
		});
		panel2_3.setBounds(820, 440, 140, 140);
		panel2_3.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel2_3);
		
		
		JPanel panel2_4 = new JPanel();
		panel2_4.setName("panel24");
		panel2_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && !playerTurn) {
					playCard(panel2_4,3);
				}
				if (gameState == 3 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[3];
					toggleAttack(unit, panel2_4);
				}
				if (gameState == 4 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[3];
					toggleDefend(unit, panel2_4);
				}
				if (gameState == 5) {
					if(p2ActiveUnits[3] != null) {
						castSpell(selectedCard, p2ActiveUnits[3]);
					}
				}
			}
		});
		panel2_4.setBounds(820, 620, 140, 140);
		panel2_4.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel2_4);
		
		JPanel panel2_5 = new JPanel();
		panel2_5.setName("panel25");
		panel2_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && !playerTurn) {
					playCard(panel2_5,4);
				}
				if (gameState == 3 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[4];
					toggleAttack(unit, panel2_5);
				}
				if (gameState == 4 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[4];
					toggleDefend(unit, panel2_5);
				}
				if (gameState == 5) {
					if(p2ActiveUnits[4] != null) {
						castSpell(selectedCard, p2ActiveUnits[4]);
					}
				}
			}
		});
		panel2_5.setBounds(640, 80, 140, 140);
		panel2_5.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel2_5);
		
		
		JPanel panel2_6 = new JPanel();
		panel2_6.setName("panel26");
		panel2_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameState == 2 && !playerTurn) {
					playCard(panel2_6,5);
				}
				if (gameState == 3 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[5];
					toggleAttack(unit, panel2_6);
				}
				if (gameState == 4 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[5];
					toggleDefend(unit, panel2_6);
				}
				if (gameState == 5) {
					if(p2ActiveUnits[5] != null) {
						castSpell(selectedCard, p2ActiveUnits[5]);
					}
				}
			}
		});
		panel2_6.setBounds(640, 260, 140, 140);
		panel2_6.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel2_6);
		
		
		JPanel panel2_7 = new JPanel();
		panel2_7.setName("panel27");
		panel2_7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(playerTurn);
				if (gameState == 2 && !playerTurn) {
					playCard(panel2_7,6);
				}
				if (gameState == 3 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[6];
					toggleAttack(unit, panel2_7);
				}
				if (gameState == 4 && !playerTurn) {
					UnitCard unit = p2ActiveUnits[6];
					toggleDefend(unit, panel2_7);
				}
				if (gameState == 5) {
					if(p2ActiveUnits[6] != null) {
						castSpell(selectedCard, p2ActiveUnits[6]);
					}
				}
			}
		});
		panel2_7.setBounds(640, 440, 140, 140);
		panel2_7.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel2_7);
		
		
		addComponentListener(new ComponentAdapter() {//when frame is made visible
			@Override
			public void componentShown(ComponentEvent e) {
				frame.changeState(0);
			}
		});
	}
	
	/*
	 * Method adds a card to a JPanel on the board, removing the card from the player's hand.
	 * Pre-Condition: takes a JPanel p to add the card to
	 * Post-Condition: returns nothing, but changes GUI and log depending on selected card
	 */
	public void addCardToPanel(JPanel j) {
		CardJPanel c = new CardJPanel(selectedCard);
		j.add(c);
		if (playerTurn) { //player 1's turn
			hand1.remove(selectedCard);
			listHand1.setListData(p1.getHandNamesArray());
			textAreaInfo1.setText("");
			textAreaLog.append("Player 1 played " + selectedCard.getName() + "\n");
		}
		else { //player 2's turn
			hand2.remove(selectedCard);
			listHand2.setListData(p2.getHandNamesArray());
			textAreaInfo2.setText("");
			textAreaLog.append("Player 2 played " + selectedCard.getName() + "\n");
		}
		
		selectedCard = null;
		changeState(1);
		
	}
	
	/*
	 * Method changes game state for other methods to reference
	 * Pre-Condition: takes an int game state that controls a switch for the GUI
	 * Post-Condition: returns nothing, but changes GUI depending on game state
	 */
	public void changeState(int state) {
		String playerName = "";
		if (playerTurn) {
			playerName = "Player 1";
		}
		else {
			playerName = "Player 2";
		}
		switch (state) { //within each gamestate, use playerName and playerTurn to choose which side of board to set up
			
			case 0: //gameState 0 is upon game start - sets up board for the player specified by playerTurn.
				textAreaLog.setText("");
			case 1: //gameState 1 is standard turn mode - select and view cards from list. depending on playerTurn, sets up board for that player.
				lblInstructions.setText("Play cards from hand and send minions to attack.");
				lblTurnInfo.setText("It's " + playerName + "'s Turn.");
				if (playerTurn) {
					//set up board for player 1
					listHand1.setVisible(true);
					btnUseCard1.setEnabled(true);
					btnEndTurn1.setEnabled(true);
					btnAttack1.setEnabled(true);
					listHand2.setVisible(false);
					btnUseCard2.setEnabled(false);
					btnEndTurn2.setEnabled(false);
					btnAttack2.setEnabled(false);
				}
				else {
					//set up board for player 2
					listHand2.setVisible(true);
					btnUseCard2.setEnabled(true);
					btnEndTurn2.setEnabled(true);
					btnAttack2.setEnabled(true);
					listHand1.setVisible(false);
					btnUseCard1.setEnabled(false);
					btnEndTurn1.setEnabled(false);
					btnAttack1.setEnabled(false);
				}
				gameState = 1;
				
				break;
			case 2: //gameState 2 is card placement mode - place a card from your hand
				lblInstructions.setText("Click a board position to add the card to that position");
				gameState = 2;
				
				break;
			case 3: //gameState 3 is attacking mode - active player chooses minions to attack
				lblInstructions.setText("Select units to attack");
				gameState = 3;
				break;
			case 4: //gameState 4 is defending mode - attacked player must decide who will be hit. depending on playerTurn, sets up board.
				if(attackingUnits.size() != 0) {
					activeAttacker = attackingUnits.get(0);
				}
				lblInstructions.setText("Select units to defend against " + activeAttacker.getName());
				if(playerTurn) {
					listHand1.setVisible(true);
					btnUseCard1.setEnabled(false);
					btnEndTurn1.setEnabled(false);
					btnAttack1.setEnabled(true);
					btnAttack1.setText("Defend");
					listHand2.setVisible(false);
					btnUseCard2.setEnabled(false);
					btnEndTurn2.setEnabled(false);
					btnAttack2.setEnabled(false);
				}
				else {
					listHand2.setVisible(true);
					btnUseCard2.setEnabled(false);
					btnEndTurn2.setEnabled(false);
					btnAttack2.setEnabled(true);
					btnAttack2.setText("Defend");
					listHand1.setVisible(false);
					btnUseCard1.setEnabled(false);
					btnEndTurn1.setEnabled(false);
					btnAttack1.setEnabled(false);
				}
				gameState = 4;
				break;
			
			case 5: // Gamestate 5 is casting spell mode - player will cast a spell on a target depending on the spell selected
				lblInstructions.setText("Select a target according to your spell.");
				if (playerTurn) {
					// Player1's buttons
					listHand1.setVisible(true);
					btnUseCard1.setEnabled(true);
					btnEndTurn1.setEnabled(true);
					btnAttack1.setEnabled(true);
					listHand1.setVisible(true);
					// Player2's buttons
					listHand2.setVisible(false);
					btnUseCard2.setEnabled(false);
					btnEndTurn2.setEnabled(false);
					btnAttack2.setEnabled(false);
				} else {
					// Player2's buttons
					listHand2.setVisible(true);
					btnUseCard2.setEnabled(true);
					btnEndTurn2.setEnabled(true);
					btnAttack2.setEnabled(true);
					// Player1's buttons
					listHand1.setVisible(false);
					btnUseCard1.setEnabled(false);
					btnEndTurn1.setEnabled(false);
					btnAttack1.setEnabled(false);
					listHand1.setVisible(false);
				}
				gameState = 5;
				break;
			default:
				break;
		}
	}
	
	/*
	 * Method adds unit card from hand to JPanel and adds unit to active units. Turns JPanel blue if unit cannot attack on turn it is played.
	 * Pre-Condition: takes a JPanel p and an int index of the board position in unit array
	 * Post-Condition: returns nothing, but changes GUI depending on user's mana and selection
	 */
	public void playCard(JPanel p, int index) {
		boolean enoughMana = false;
		if(selectedCard instanceof UnitCard) { //Adds unit to list of active units
			if(playerTurn) {
				if(p1.getMana() >= selectedCard.getCost()) {
					p1ActiveUnits[index] = (UnitCard) selectedCard;
					enoughMana = true;
					p1.useMana(selectedCard.getCost());
					lblMana1.setText("Mana: " + p1.getMana() + "/" + p1.getMaxMana());
				}
				else {
					lblInstructions.setText("Not enough mana");
				}
			}
			else {
				if(p2.getMana() >= selectedCard.getCost()) {
					p2ActiveUnits[index] = (UnitCard) selectedCard;
					enoughMana = true;
					p2.useMana(selectedCard.getCost());
					lblMana2.setText("Mana: " + p2.getMana() + "/" + p2.getMaxMana());
				}
				else {
					lblInstructions.setText("Not enough mana");
				}
			}
			if(enoughMana) {
				if(!((UnitCard) selectedCard).canAttack()) {
					p.setBorder(BorderFactory.createLineBorder(Color.blue));
				}
				addCardToPanel(p);
			}
		}
	}
	
	
	/*
	 * Method called when player is using a single-target spell card. Removes spell card from hand, uses Mana, and causes damage to a single unit
	 * Pre-Condition: takes a Card card that should be an instance of SpellCard, and a Unit unit that is targeted by the spell
	 * Post-Condition: returns nothing, but changes GUI to cast spell and hurt target
	 */
	public void castSpell(Card card, UnitCard unit) {
		if(card instanceof SpellCard) {
			if (playerTurn) { //player 1's turn
				hand1.remove(selectedCard);
				p1.useMana(card.getCost());
				listHand1.setListData(p1.getHandNamesArray());
				textAreaInfo1.setText("");
				textAreaLog.append("Player 1 played " + selectedCard.getName() + "\n");
			}
			else { //player 2's turn
				hand2.remove(selectedCard);
				p2.useMana(card.getCost());
				listHand2.setListData(p2.getHandNamesArray());
				textAreaInfo2.setText("");
				textAreaLog.append("Player 2 played " + selectedCard.getName() + "\n");
			}
			SpellCard spell = (SpellCard) card;
			unit.takeDamage(spell.getPower(), textAreaLog);
			
			selectedCard = null;
			changeState(1);
		}
		refresh();
	}
		
	/*
	 * Method changes target logic based on spell target type
	 * Pre-Condition: takes a Card selectedCard that should be an instance of SpellCard
	 * Post-Condition: returns nothing, but changes GUI based on SpellCard target type
	 */
	private void spellTargetSelection(Card selectedCard) {
		if (selectedCard instanceof SpellCard) { // Adds spell card to active hand
			SpellCard spellCard = (SpellCard) selectedCard;
			
		switch(spellCard.getTargetType()) {
		
			case "SingleTarget":
				lblInstructions.setText("Select a single enemy unit to Cast Spell on.");
				if (playerTurn) {
					// Player 1 targeting a Single Target
					for (UnitCard unitCard: p2ActiveUnits) { // Iterates over the opponents UnitCards that are on the board
						JPanel panelWithUnitCard = getPanelWithUnitCard(unitCard); // checks if the UnitCard is on JPanel
						if (panelWithUnitCard != null) {
							panelWithUnitCard.addMouseListener(new MouseAdapter() { // Assigns the mouseListener to the Panel with the UnitCard
								@Override
								public void mouseClicked(MouseEvent e) {
									UnitCard targetUnit = unitCard;
									spellCard.activate(targetUnit, null, p2, textAreaLog);
									panelWithUnitCard.setBorder(BorderFactory.createLineBorder(Color.GREEN));
								}
						});
						}
					}	
				} 
				
				// Player 2 Targeting a single target
				else {
					// Player 2 targeting a Single Target
						for (UnitCard unitCard: p1ActiveUnits) { // Iterates over the opponents UnitCards that are on the board
							JPanel panelWithUnitCard = getPanelWithUnitCard(unitCard); // checks if the UnitCard is on JPanel
							if (panelWithUnitCard != null) {
								panelWithUnitCard.addMouseListener(new MouseAdapter() { // Assigns the mouseListener to the Panel with the UnitCard	
									@Override
									public void mouseClicked(MouseEvent e) {
										UnitCard targetUnit = unitCard;
										spellCard.activate(targetUnit, null, p1, textAreaLog);
										panelWithUnitCard.setBorder(BorderFactory.createLineBorder(Color.GREEN));
									}
							});
							}
						}	
					}
				break;

			case "PlayerTarget": //Spell will be casted on enemy player when used.
				// Player 1	targeting player 2
				if (playerTurn) {
					if (playerTurn) { //player 1's turn
						hand1.remove(selectedCard);
						listHand1.setListData(p1.getHandNamesArray());
						textAreaInfo1.setText("");
						textAreaLog.append("Player 1 played " + selectedCard.getName() + "\n");
					}
					else { //player 2's turn
						hand2.remove(selectedCard);
						listHand2.setListData(p2.getHandNamesArray());
						textAreaInfo2.setText("");
						textAreaLog.append("Player 2 played " + selectedCard.getName() + "\n");
					}
					
					p2.takeDamage(spellCard.getPower());
					p1.useMana(spellCard.getCost());
					textAreaLog.append("Player 2 has taken " + spellCard.getPower() + " damage from " + spellCard.getName() + "\n");
					
					selectedCard = null;
					changeState(1);
					} 
				// Player 2 targeting player 1
				else {
					if (playerTurn) { //player 1's turn
						hand1.remove(selectedCard);
						listHand1.setListData(p1.getHandNamesArray());
						textAreaInfo1.setText("");
						textAreaLog.append("Player 1 played " + selectedCard.getName() + "\n");
					}
					else { //player 2's turn
						hand2.remove(selectedCard);
						listHand2.setListData(p2.getHandNamesArray());
						textAreaInfo2.setText("");
						textAreaLog.append("Player 2 played " + selectedCard.getName() + "\n");
					}
					
					p1.takeDamage(spellCard.getPower());
					p2.useMana(spellCard.getCost());
					textAreaLog.append("Player 1 has taken " + spellCard.getPower() + " damage from " + spellCard.getName() + "\n");
					
					selectedCard = null;
					changeState(1);
				}
				refresh();
				break;
		
			case "MultiTarget": //spell cast on all enemy units on board
				lblInstructions.setText("Select described number of enemy units.");
				// Player 1 selecting multiple targets
				if (playerTurn) { //player 1's turn
					hand1.remove(selectedCard);
					listHand1.setListData(p1.getHandNamesArray());
					textAreaInfo1.setText("");
					textAreaLog.append("Player 1 played " + selectedCard.getName() + "\n");
				}
				else { //player 2's turn
					hand2.remove(selectedCard);
					listHand2.setListData(p2.getHandNamesArray());
					textAreaInfo2.setText("");
					textAreaLog.append("Player 2 played " + selectedCard.getName() + "\n");
				}
				
				if (playerTurn) {
					p1.useMana(spellCard.getCost());
					for(UnitCard unit : p2ActiveUnits) {
						if(unit != null) {
							unit.takeDamage(spellCard.getPower(), textAreaLog);
						}
					}
				} 
				// Player 2 selecting multiple targets
				else {
					p2.useMana(spellCard.getCost());
					for(UnitCard unit : p1ActiveUnits) {
						if(unit != null) {
							unit.takeDamage(spellCard.getPower(), textAreaLog);
						}
					}
				}
				
				selectedCard = null;
				changeState(1);
				refresh();
				break;
		
			default:
				lblInstructions.setText("Spell card has invalid target type");
				break;
				
		}
	}}
	
	/*
	 * Method toggles JPanel color if unit is going to attack and sets unit to attacking
	 * Pre-Condition: takes a UnitCard unit and a JPanel p, changing the JPanel based on whether the unit can attack
	 * Post-Condition: returns nothing, but changes GUI based on Unit Card attacking status
	 */
	public void toggleAttack(UnitCard unit, JPanel p) {
		if(unit != null) {
			if(unit.canAttack()) { //red border = attacking
				if(!unit.isAttacking()) {
					unit.setAttacking(true);
					p.setBorder(BorderFactory.createLineBorder(Color.red));
				}
				else { //black border = not attacking, but can if needed
					if(unit.isAttacking()) {
						unit.setAttacking(false);
						p.setBorder(BorderFactory.createLineBorder(Color.black));
					}
				}
			}
		}
	}
	
	/*
	 * Method toggles JPanel color if unit is going to defend and sets unit to attacking
	 * Pre-Condition: takes a UnitCard unit and a JPanel p, changing the JPanel based on whether the unit can attack
	 * Post-Condition: returns nothing, but changes GUI based on Unit Card attacking status
	 */
	public void toggleDefend(UnitCard unit, JPanel p) {
		if(unit != null) {
			if(unit.canAttack()) { //Doubles as can defend in this case
				if(!unit.isAttacking()) { //yellow border = defending
					unit.setAttacking(true);
					p.setBorder(BorderFactory.createLineBorder(Color.yellow));
				}
				else {
					if(unit.isAttacking()) { //black border = not defending, but can if needed
						unit.setAttacking(false);
						p.setBorder(BorderFactory.createLineBorder(Color.black));
					}
				}
			}
		}
	}
	

	
	/*
	 * Method to find the panel containing a given UnitCard
	 * Pre-Condition: takes a UnitCard unit 
	 * Post-Condition: returns JPanel containing unit
	 */
	private JPanel getPanelWithUnitCard(UnitCard unitCard) {
	    for (Component comp : getContentPane().getComponents()) {
	        if (comp instanceof JPanel && hasUnitCard((JPanel) comp, unitCard)) {
	            return (JPanel) comp;
	        }
	    }
	    return null;
	}

	/*
	 * Method to check whether a JPanel has a cerain UnitCard
	 * Pre-Condition: takes a JPanel panel and UnitCard unitCard 
	 * Post-Condition: returns boolean result of whether JPanel has that unitCard as its component
	 */
	private boolean hasUnitCard(JPanel panel, UnitCard unitCard) {
	    for (Component comp : panel.getComponents()) {
	        if (comp.equals(unitCard)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/*
	 * Method refreshes both player's info, hand, and board panels. Also checks if game has ended and changes GUI if so.
	 * Pre-Condition: none
	 * Post-Condition: returns nothing, updates GUI with current state of game
	 */
    public void refresh() {
        // Update player information
        lblHP1.setText("HP: " + p1.getHp() + "/" + p1.getMaxHp());
        lblMana1.setText("Mana: " + p1.getMana() + "/" + p1.getMaxMana());
        lblDeck1.setText("Deck: " + p1.getDeck().size());
        lblArmor1.setText("Armor: " + p1.getArmor());

        lblHP2.setText("HP: " + p2.getHp() + "/" + p2.getMaxHp());
        lblMana2.setText("Mana: " + p2.getMana() + "/" + p2.getMaxMana());
        lblDeck2.setText("Deck: " + p2.getDeck().size());
        lblArmor2.setText("Armor: " + p2.getArmor());

        // Update hand list data for both players
        listHand1.setListData(p1.getHandNamesArray());
        listHand2.setListData(p2.getHandNamesArray());
        textAreaInfo1.setText("");
        textAreaInfo2.setText("");
        

        // Update unit panels for both players
        updateUnitPanels(p1ActiveUnits, true); // Update Player 1's panels
        updateUnitPanels(p2ActiveUnits, false); // Update Player 2's panels
        
        // End game implementation
        String endMsg = null;
        if(p2.getHp() <= 0) {
        	endMsg = "Player 1 wins!";
        }
        else if(p1.getHp() <= 0) {
        	endMsg = "Player 2 wins!";
        }
        if(endMsg != null) {
        	contentPane.removeAll();
        	contentPane.revalidate();
        	contentPane.repaint();
        	
    		lblInstructions = new JLabel(endMsg);
    		lblInstructions.setHorizontalAlignment(SwingConstants.CENTER);
    		lblInstructions.setBounds(420, 37, 360, 16);
    		contentPane.add(lblInstructions);
        }
        
        // Print refresh status to console for debugging
        System.out.println("GUI Refreshed");
    }
	
    
    /*
	 * Method updates the display of a unit on a given panel
	 * Pre-Condition: none
	 * Post-Condition: returns nothing, updates a panel's outline based on whether unit can attack
	 */
    private void updateUnitDisplay(JPanel panel, UnitCard unit) {
        panel.setBorder(BorderFactory.createLineBorder(unit.canAttack() ? Color.black : Color.blue));
    }
    
    
    /*
	 * Utility method to get the panel corresponding to a unit index
	 * Pre-Condition: int index and String panelPrefix
	 * Post-Condition: returns JPanel with name based on info provided
	 */
    private JPanel getUnitPanel(int index, String panelPrefix) {
        String panelName = panelPrefix + (index + 1);
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JPanel && comp.getName() != null && comp.getName().equals(panelName)) {
                return (JPanel) comp;
            }
        }
        return null;
    }
    
    /*
	 * Method returns CardJPanel in JPanel
	 * Pre-Condition: JPanel p
	 * Post-Condition: returns CardJPanel
	 */
    public CardJPanel getCardPanel(JPanel p) {
    	for(Component comp : p.getComponents()) {
    		if(comp instanceof CardJPanel) {
    			return (CardJPanel) comp;
    		}
    	}
    	return null;
    }
	
    /*
	 * Method updates unit panels' HP display based on current game state and clears inactive units
	 * Pre-Condition: UnitCard array of active units, boolean for current player 
	 * Post-Condition: returns nothing, updates GUI based on unit HP
	 */
    private void updateUnitPanels(UnitCard[] activeUnits, boolean isPlayerOne) {
        String panelPrefix = isPlayerOne ? "panel1" : "panel2";
        for (int i = 0; i < activeUnits.length; i++) {
            UnitCard unit = activeUnits[i];
            JPanel panel = getUnitPanel(i, panelPrefix); // Fetch the correct panel for each unit based on its index
            if (  (unit != null && unit.getHp() <= 0)) {
                // If no unit is assigned to this slot or the unit is dead, clear the panel
                if (panel != null) {
                    panel.removeAll();
                    panel.revalidate();
                    panel.repaint();
                    panel.setBorder(BorderFactory.createLineBorder(Color.black)); // Reset the border
                }
                activeUnits[i] = null; // Remove dead or null units from the array
            } else if (unit != null){
                // If the unit is alive, update its display
                CardJPanel cardPanel = getCardPanel(panel);
                cardPanel.refreshHp();
                updateUnitDisplay(panel, unit);
            }
        }
    }
}
