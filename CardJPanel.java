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

public class CardJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Card card;
	private int x;
	private int y;
	private int width;
	private int height;
	
	

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
		lblCardHP.setHorizontalAlignment(SwingConstants.CENTER);
		lblCardHP.setBounds(0, 32, 140, 16);
		panel.add(lblCardHP);
		
		JTextArea textAreaDesc = new JTextArea();
		JScrollPane scrollPaneText = new JScrollPane(textAreaDesc);
		scrollPaneText.setBounds(1, 48, 138, 90);
		textAreaDesc.setBackground(SystemColor.window);
		textAreaDesc.setWrapStyleWord(true);
		textAreaDesc.setLineWrap(true);
		textAreaDesc.setText(card.getDescription());
		textAreaDesc.setEditable(false);
		

		
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
			
		
	}
	
	public Card getCard() {
		return this.card;
	}
}
