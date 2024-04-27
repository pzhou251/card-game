import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.SystemColor;
import javax.swing.UIManager;
import java.awt.Font;

public class CardJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Card card;
	private int x;
	private int y;
	private int width;
	private int height;
	
	private JLabel lblHP;
	
	

	/**
	 * Create the panel.
	 */
	public CardJPanel(Card card) {
		this.card = card;
		UnitCard unit = (UnitCard)card;
		CardJPanel panel = this;
		
		this.setPreferredSize(new Dimension(140, 140));
        this.setVisible(true);
		

		setLayout(null);
		
		
		
		JLabel lblCardName = new JLabel(card.getName(),SwingConstants.CENTER);
		lblCardName.setBounds(0, 0, 140, 16);
		add(lblCardName);
		
		JLabel lblCardAttack = new JLabel("Attack: " + String.valueOf(unit.getAttack()));
		lblCardAttack.setHorizontalAlignment(SwingConstants.CENTER);
		lblCardAttack.setBounds(0, 16, 140, 16);
		panel.add(lblCardAttack);
		
		JLabel lblCardHP = new JLabel("HP: " + String.valueOf((unit.getHp())) + "/" + String.valueOf((unit.getMaxHp())));
		lblHP = lblCardHP;
		lblCardHP.setHorizontalAlignment(SwingConstants.CENTER);
		lblCardHP.setBounds(0, 32, 140, 16);
		panel.add(lblCardHP);
		
		JTextArea textAreaDesc = new JTextArea();
		textAreaDesc.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		JScrollPane scrollPaneText = new JScrollPane(textAreaDesc);
		scrollPaneText.setBounds(10, 50, 120, 80);
		textAreaDesc.setBackground(UIManager.getColor("Button.highlight"));
		textAreaDesc.setWrapStyleWord(true);
		textAreaDesc.setLineWrap(true);
		textAreaDesc.setText(card.getDescription());
		textAreaDesc.setEditable(false);
		panel.add(scrollPaneText);
		

		
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
			
		
	}
	
	//getter
	public Card getCard() {
		return this.card;
	}
	
	/* 
	 * Refreshes HP label of CardJPanel
     * Pre-Condition: none
     * Post-Condition: changes HP label to current
     */
	public void refreshHp() {
		UnitCard unit = null;
		if(card instanceof UnitCard) {
			unit = (UnitCard) card;
		}
		if(unit != null) {
			lblHP.setText("HP: " + String.valueOf((unit.getHp())) + "/" + String.valueOf((unit.getMaxHp())));
		}
	}
}
