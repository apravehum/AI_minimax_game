import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.util.*;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;

class State {
	// tiek definēta klase State (stāvoklis), kas apraksta pašreizējo stāvokli
	int p1; // 1.spēlētāja punktu skaits
	int p2; // 2.spēlētāja punktu skaits
	String virkne; // tekošā virkne
	int level; // līmenis kurā atrodas stāvoklis
	int rating; // stāvokļa novērtējums
	ArrayList<State> children; // stāvokļa bērni
	
	// zemāk aprakstīti getters un setters, lai varētu piekļūt objekta parametriem
	public int getP1() {
		return p1;
	}

	public void setP1(int p1) {
		this.p1 = p1;
	}

	public int getP2() {
		return p2;
	}

	public void setP2(int p2) {
		this.p2 = p2;
	}

	public String getVirkne() {
		return virkne;
	}

	public void setVirkne(String virkne) {
		this.virkne = virkne;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<State> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<State> children) {
		this.children = children;
	}
	
	// noklusējuma konstruktors
	public State() {
		p1 = 0;
		p2 = 0;
		virkne = "22233";
		level = 0;
		rating = 0;
	}
	
	// konstruktors izmantojot virkni un līmeni
	public State(String virkne, int level) {
		p1 = 0;
		p2 = 0;
		this.virkne = virkne;
		this.level = level;
	}
	
	// konstruktors izmantojot punktu skaitu abiem spēlētājiem, virkne, līmenis
	public State(int p1, int p2, String virkne, int level) {
		this.p1 = p1;
		this.p2 = p2;
		this.virkne = virkne;
		this.level = level;
	}

}

// Jframe klase, kur tiek definēts grafiskais logs, citas metodes, kuras izmantotas, lai izveidotu stāvokļu koku
public class praktiskais extends JFrame {

	private JPanel contentPane;
	private JTextField input;
	private JTextField output;
	private JButton execute_btn;
	private JTextField PP_txt;
	private JTextField PCP_txt;
	private JLabel jlabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel;
	private JTextField winner;
	private JButton Start_btn;
	private JButton Exit_btn;
	private JButton choosePlayer;
	private JButton choosePc;
	private ArrayList<State> stateArr;
	private int GameMode = 1; // spēles režīms 1 - sāk spēlētājs, 2 - sāk PC
	
	// virkne bez i'tā elementa
	String stringExceptIndex(String s, int index) {
		String temp1 = s.substring(0, index);
		String temp2 = s.substring(index + 1, s.length());
		s = temp1 + temp2;
		s = s.trim();
		return s;
	}
	
	// virkne bez simbola
	String stringExceptChar(String s, String stringChar) {
		for (int i = 0; i < s.length(); i++) {
			if (Character.toString(s.charAt(i)).equals(stringChar)) {
				s = stringExceptIndex(s, i);
				break;
			}
		}

		return s;
	}
	
	// Stāvokļu koka izveide
	ArrayList<State> calculations(String virkne) {
		ArrayList<State> stateArr = new ArrayList<State>(); // Dinamisks masīvs, kurš tiek izmantots kā koks
		State temp = new State(virkne, 0); // 1.stāvoklis jeb sakne
		stateArr.add(temp); // pievieno sakni kokam
		int level = 1; // tekošais līmenis
		String tempSt = ""; // pagaidu virkne - tiek izmantota, lai apskatītu visus iespējamos izejošos stāvokļus
		String curVirkne = ""; // tekošā virkne no kuras tiek iegūta pagaidu virkne
		char tempChar; // pagaidu simbols, ko izmanto, lai izņemtu no tekošās viknes simbolu un izveidotu pagaidu virkni
		int curPoints; // tekošie punkti
		int charToInt; // punktu skaits, kas jāpievieno tekošajiem punktiem
		while (true) {
			if (temp.getVirkne().length() == 1) { // pārbauda vai virknes garums ir viens, ja, jā, beidz koka izveidi
				return stateArr;
			} else {
				if (level % 2 == 1) { // pēc tekošā līmeņa nosaka, kuram no spēlētājiem jāpieliek punkti
					for (int i = 0; i < stateArr.size(); i++) {
						if (stateArr.get(i).getLevel() == level - 1) { // izveidota pārbaude, kas nosaka, ka nākamie stāvokļi tiks izveidoti nākamajā līmenī
							ArrayList<State> tempArr = new ArrayList<State>();
							curVirkne = stateArr.get(i).getVirkne();
							curPoints = stateArr.get(i).getP1();
							temp = stateArr.get(i);
							for (int j = 0; j < curVirkne.length(); j++) {
								tempSt = stringExceptIndex(curVirkne, j);
								tempChar = curVirkne.charAt(j);
								charToInt = Character.getNumericValue(tempChar);
								if (j == 0 || !tempSt.equals(temp.getVirkne())) {
									tempArr.add(new State(curPoints + charToInt, temp.getP2(), tempSt, level));
									temp.setChildren(tempArr);
									stateArr.add(new State(curPoints + charToInt, temp.getP2(), tempSt, level));
									temp = new State(curPoints + charToInt, temp.getP2(), tempSt, level);
								}
							}
						}
					}
				}
				if (level % 2 == 0) {
					for (int i = 0; i < stateArr.size(); i++) {
						if (stateArr.get(i).getLevel() == level - 1) {
							ArrayList<State> tempArr = new ArrayList<State>();
							curVirkne = stateArr.get(i).getVirkne();
							curPoints = stateArr.get(i).getP2();
							temp = stateArr.get(i);
							for (int j = 0; j < curVirkne.length(); j++) {
								tempSt = stringExceptIndex(curVirkne, j);
								tempChar = curVirkne.charAt(j);
								charToInt = Character.getNumericValue(tempChar);
								if (j == 0 || !tempSt.equals(temp.getVirkne())) {
									tempArr.add(new State(temp.getP1(), curPoints + charToInt, tempSt, level));
									temp.setChildren(tempArr);
									stateArr.add(new State(temp.getP1(), curPoints + charToInt, tempSt, level));
									temp = new State(temp.getP1(), curPoints + charToInt, tempSt, level);
								}
							}
						}
					}
				}
			}
			level++;
		}
	}

	static ArrayList<State> evaluation(ArrayList<State> stateArr, int GameMode) {
		if (GameMode == 1) {
			// sāk spēlētājs - p1
			int p1; // 1.spēlētāja punktu skaits
			int p2;	// 2.spēlētāja punktu skaits
			int curLevel; // tekošais līmenis
			boolean levelType; // min or max līmenis, min - false, max - true
			int size = stateArr.size(); // koka izmērs
			int maxLevel = stateArr.get(size - 1).getLevel(); // līmeņu skaits kokā
			for (int i = size - 1; i >= 0; i--) { // cikls, kas apskata visus elementus no beigām
				p1 = stateArr.get(i).getP1();
				p2 = stateArr.get(i).getP2();
				curLevel = stateArr.get(i).getLevel();
				if (curLevel == maxLevel) { // ja ir koka pēdējais līmenis, tad vērtējumu nosaka pēc punktu skaita
					if (p1 < p2) {
						stateArr.get(i).setRating(1);
					} else if (p1 == p2) {
						stateArr.get(i).setRating(0);
					} else {
						stateArr.get(i).setRating(-1);
					}
				} else { // ja nav pēdējais līmenis, tad vērtējumu nosaka pēc bērnu stāvokļu vērtējumiem
					if (curLevel % 2 == 1) { // ja tekošais līmenis ir nepāra skaitlis, tad piešķir min līmeni, ja nē, tad max līmeni
						levelType = false; 
					} else {
						levelType = true;
					}
					if (levelType) { // ja ir max līmenis, tad iestata minimālo vērtību, ja min, tad iestata uz maksimālo vērtību
						stateArr.get(i).setRating(-2); 
					} else {
						stateArr.get(i).setRating(2);
					}
					for (int j = 0; j < stateArr.size(); j++) {
						if (stateArr.get(j).getLevel() == curLevel + 1) { // izveidota pārbaude, kas nosaka, ka novērtējums tiks aprēķināts iepriekšējā līmenī
							for (int k = 0; k < stateArr.get(i).getChildren().size(); k++) {
								if (stateArr.get(j).getVirkne() == stateArr.get(i).getChildren().get(k).getVirkne()
										&& stateArr.get(j).getP1() == stateArr.get(i).getChildren().get(k).getP1()
										&& stateArr.get(j).getP2() == stateArr.get(i).getChildren().get(k).getP2()) { // pārbauda vai iepriekšējā līmeņa stāvokļa bērnu virkne, punkti sakrīt ar tekošā stāvokļa virkni, punktu skaitu, lai atrastu bērnu novērtējumus
									if (levelType == true) { // ja ir max līmenis pārbauda, kurš ir labākais stāvokļa vērtējums
										if (stateArr.get(j).getRating() > stateArr.get(i).getRating()) {
											stateArr.get(i).setRating(stateArr.get(j).getRating());
										}
									} else { // ja ir min līmenis pārbauda, kurš ir sliktākais stāvokļa vērtējums
										if (stateArr.get(j).getRating() < stateArr.get(i).getRating()) {
											stateArr.get(i).setRating(stateArr.get(j).getRating());
										}
									}
								}
							}
						}
					}
				}
			}
		} else if (GameMode == 0) {
			// sāk pc
			// ja sāk pc tad atākrto iepriekšējo algoritmu tikai izmaina līmeni (min vai max) uz pretējo 
			int p1;
			int p2;
			int curLevel;
			boolean levelType;
			int size = stateArr.size();
			int maxLevel = stateArr.get(size - 1).getLevel();
			for (int i = size - 1; i >= 0; i--) {
				p1 = stateArr.get(i).getP1();
				p2 = stateArr.get(i).getP2();
				curLevel = stateArr.get(i).getLevel();
				if (curLevel == maxLevel) {
					if (p1 > p2) {
						stateArr.get(i).setRating(1);
					} else if (p1 == p2) {
						stateArr.get(i).setRating(0);
					} else {
						stateArr.get(i).setRating(-1);
					}
				} else {
					if (curLevel % 2 == 1) {
						levelType = true;
					} else {
						levelType = false;
					}
					if (levelType) {
						stateArr.get(i).setRating(-2);
					} else {
						stateArr.get(i).setRating(2);
					}
					for (int j = 0; j < stateArr.size(); j++) {
						if (stateArr.get(j).getLevel() == curLevel + 1) {
							for (int k = 0; k < stateArr.get(i).getChildren().size(); k++) {
								if (stateArr.get(j).getVirkne() == stateArr.get(i).getChildren().get(k).getVirkne()
										&& stateArr.get(j).getP1() == stateArr.get(i).getChildren().get(k).getP1()
										&& stateArr.get(j).getP2() == stateArr.get(i).getChildren().get(k).getP2()) {
									if (levelType == true) {
										if (stateArr.get(j).getRating() > stateArr.get(i).getRating()) {
											stateArr.get(i).setRating(stateArr.get(j).getRating());
										}
									} else {
										if (stateArr.get(j).getRating() < stateArr.get(i).getRating()) {
											stateArr.get(i).setRating(stateArr.get(j).getRating());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		// iepriekšējais algoritms noteica vērtējumus tikai kokam, bet ne atsevišķu stāvokļu bērniem
		for (int i = 0; i < stateArr.size(); i++) { // cikls atrod koka stāvokļa bērnus un piešķir tiem atbilstošus vērtējumus, no jau esoša koka
			for (int j = 0; j < stateArr.size(); j++) {
				if (stateArr.get(j).getChildren() != null) { // apakšējā līmeņa stāvokļiem nav bērnu tāpēc, meklē tikai tos stāvokļus, kuriem ir bērni
					for (int k = 0; k < stateArr.get(j).getChildren().size(); k++) {
						if (stateArr.get(i).getVirkne() == stateArr.get(j).getChildren().get(k).getVirkne()
								&& stateArr.get(i).getP1() == stateArr.get(j).getChildren().get(k).getP1()
								&& stateArr.get(i).getP2() == stateArr.get(j).getChildren().get(k).getP2()) {
							stateArr.get(j).getChildren().get(k).setRating(stateArr.get(i).getRating());
						}
					}
				}
			}
		}
		return stateArr;
	}
	
	// palaiž logu
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					praktiskais frame = new praktiskais();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public praktiskais() {
		
		// loga elementu konfigurācija

		setTitle("Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 454, 367);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		input = new JTextField();
		input.setBounds(10, 39, 161, 33);
		contentPane.add(input);
		input.setColumns(10);

		output = new JTextField();
		output.setEditable(false);
		output.setColumns(10);
		output.setBounds(267, 39, 161, 33);
		contentPane.add(output);

		execute_btn = new JButton("Execute");
		execute_btn.setEnabled(false);
		execute_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String Input = input.getText(); // input teksta laukuma vērtības nolasīšana
				String virkne = output.getText(); // tekošās virknes nolasīšana
				try {
					Integer.parseInt(Input); // pārbauda vai ievadīts cipars
					
				} catch (Exception e) {
					winner.setText("Jāievada cipars no tekošās virknes!!!");
					return;
				}
				if (Input.length() != 1) { // pārbauda vai ievadītais cipars ir 1
					winner.setText("Jāizvēlas 1 ciparu!!!");
					return;
				}
				if (!virkne.contains(Input)) { // pārbauda vai virknē ir ievadītais cipars
					winner.setText("Jāizvēlas ciparu no tekošas virknes");
					return;
				}
				//String virkne = output.getText(); // tekošās virknes nolasīšana
				virkne = stringExceptChar(virkne, Input); // virkne bez ievadītā cipara
				int PP = Integer.parseInt(PP_txt.getText()); // nolasa spēlētāja punktu skaitu
				PP += Integer.parseInt(Input); // pieskaita izņemto ciparu
				int PCP = Integer.parseInt(PCP_txt.getText()); // nolasa PC punktu skaitu
				winner.setText("Spēlētāja gājiens"); // informācijas izvade
				State temp = new State(); // pagaidu stāvoklis
				for (int i = 0; i < stateArr.size(); i++) { // cikls atrod kurš stavoklis atbilst tekošajam stāvoklim
					if (GameMode == 1) {
						if (stateArr.get(i).getVirkne().equals(virkne) && stateArr.get(i).getP1() == PP
								&& stateArr.get(i).getP2() == PCP) {
							temp = stateArr.get(i);
							break;
						}
					} else {
						if (stateArr.get(i).getVirkne().equals(virkne) && stateArr.get(i).getP1() == PCP
								&& stateArr.get(i).getP2() == PP) {
							temp = stateArr.get(i);
							break;
						}
					}
				}
				State bestRating = new State(); // iestata pagaidu stāvokli, lai noteiktu bērnu stāvokli ar labāko novērtējumu
				if (temp.getChildren() != null) { // pārbauda vai pagaidu stāvoklim ir bērni
					bestRating.setRating(-2); // iestata minimālo novērtējumu, jo jebkuram stāvoklim ir labāks novērtējums par -2
					for (int i = 0; i < temp.getChildren().size(); i++) { // cikls izskata visus stāvokļa bērnus un atrod stāvokli ar labāko novērtējumu
						if (temp.getChildren().get(i).getRating() > bestRating.getRating()) {
							bestRating = temp.getChildren().get(i);
						}
					}
				}
				// pārbauda vai virknes garums ir 1, ja, jā beidz spēli
				if (virkne.length() == 1) {

					if (PP > PCP) {
						execute_btn.setEnabled(false);
						output.setText(virkne);
						PP_txt.setText(String.valueOf(PP));
						PCP_txt.setText(String.valueOf(PCP));
						winner.setText("Spēle beigusies, uzvarēja player");
						return;
					} else if (PP < PCP) {
						execute_btn.setEnabled(false);
						output.setText(virkne);
						PP_txt.setText(String.valueOf(PP));
						PCP_txt.setText(String.valueOf(PCP));
						winner.setText("Spēle beigusies, uzvarēja PC");
						return;
					} else {
						execute_btn.setEnabled(false);
						output.setText(virkne);
						PP_txt.setText(String.valueOf(PP));
						PCP_txt.setText(String.valueOf(PCP));
						winner.setText("Spēle beigusies, neizšķirts");
						return;
					}
				}
				output.setText(bestRating.getVirkne()); // iestata tekošās virkni pēc PC gājiena
				winner.setText("Virkne pēc spēlētāja gājiena: " + virkne + ", spēlētāja gājiens"); // izvada spēlētāja virkni pēc spēlētāja gājiena
				if (GameMode == 1) { // pārbauda kurš sācis spēli un attiecīgi izvada punktu skaitu
					PP_txt.setText(String.valueOf(bestRating.getP1()));
					PCP_txt.setText(String.valueOf(bestRating.getP2()));
				} else {
					PP_txt.setText(String.valueOf(bestRating.getP2()));
					PCP_txt.setText(String.valueOf(bestRating.getP1()));
				}
				input.setText("");
				
				// vēl viena virknes pārbaude, pārbauda vai virknes garums ir 1, ja, jā beidz spēli, iestata tekošā stāvokļa punktu skaitu
				if (output.getText().length() == 1) {
					if (bestRating.getP1() > bestRating.getP2()) {
						execute_btn.setEnabled(false);
						winner.setText("Spēle beigusies, uzvarēja player");
					} else if (bestRating.getP1() < bestRating.getP2()) {
						execute_btn.setEnabled(false);
						winner.setText("Spēle beigusies, uzvarēja PC");
					} else {
						execute_btn.setEnabled(false);
						winner.setText("Spēle beigusies, neizšķirts");
					}
				}
			}
		});
		execute_btn.setBounds(177, 44, 84, 23);
		contentPane.add(execute_btn);

		PP_txt = new JTextField();
		PP_txt.setHorizontalAlignment(SwingConstants.CENTER);
		PP_txt.setEditable(false);
		PP_txt.setBounds(131, 111, 40, 40);
		contentPane.add(PP_txt);
		PP_txt.setColumns(10);

		PCP_txt = new JTextField();
		PCP_txt.setHorizontalAlignment(SwingConstants.CENTER);
		PCP_txt.setEditable(false);
		PCP_txt.setColumns(10);
		PCP_txt.setBounds(267, 111, 40, 40);
		contentPane.add(PCP_txt);

		jlabel = new JLabel("Player");
		jlabel.setHorizontalAlignment(SwingConstants.CENTER);
		jlabel.setBounds(121, 162, 60, 14);
		contentPane.add(jlabel);

		lblNewLabel_1 = new JLabel("PC");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(257, 162, 60, 14);
		contentPane.add(lblNewLabel_1);

		lblNewLabel = new JLabel("Points");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(197, 124, 46, 14);
		contentPane.add(lblNewLabel);

		if (GameMode == 1) { // pārbauda kurš pirmais sācis un iestata noklusējuma spēlētāju, kurš sāk
			winner = new JTextField("Player starts, ievadi virkni un spied play");
		} else {
			winner = new JTextField("PC starts, ievadi virkni un spied play");
		}
		winner.setHorizontalAlignment(SwingConstants.CENTER);
		winner.setEditable(false);
		winner.setBounds(10, 198, 418, 40);
		contentPane.add(winner);
		winner.setColumns(10);

		Start_btn = new JButton("Start or play again");
		Start_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				execute_btn.setEnabled(true);
				output.setText("");
				String Input = input.getText(); // nolasa input teksta lauka virkni

				for (int i = 0; i < Input.length(); i++) { // pārbauda vai ievadītajā virknē nav nepareizi simboli
					int ch = Input.charAt(i);
					if (ch < 48 || ch > 57) {
						winner.setText("Virknē jābūt tikai cipariem!!!");
						input.setText("");
						return;
					}
				}
				if (Input.length() % 2 == 0) { // virknes garumam jābut nepāra skaitlim
					winner.setText("Virknē jābūt nepāra skaitam ciparu!!!");
					input.setText("");
					return;
				}
				if (Input.length() < 2) { // virknes garumam jābūt lielākam par 1
					winner.setText("Virknē jābūt vairāk par vienu ciparu!!!");
					input.setText("");
					return;
				}
				output.setText(Input); // ievadīto virkni iestata tekošās virknes teksta laukā
				stateArr = calculations(Input); // izveido koku
				stateArr = evaluation(stateArr, GameMode); // novērtē koku
				PP_txt.setText("0"); // iestata sākuma punktu skaitu
				PCP_txt.setText("0"); 
				if (GameMode == 1) { // ja sāk spēlētājs, tad izmanto execute pogas algoritmu
					winner.setText("Player starts");
				} else { // ja sāk PC, tad ir jāizveido papildus iterācija execute pogas algoritmam, kas atrod pirmo labāko gājienu priekš PC
					winner.setText("PC starts");
					String virkne = output.getText();
					State temp = new State();
					int PCP = Integer.parseInt(PCP_txt.getText());
					int PP = Integer.parseInt(PP_txt.getText());
					for (int i = 0; i < stateArr.size(); i++) {
						if (stateArr.get(i).getVirkne().equals(virkne) && stateArr.get(i).getP1() == PP
								&& stateArr.get(i).getP2() == PCP) {
							temp = stateArr.get(i);
							break;
						}
					}
					State bestRating = new State();
					bestRating.setRating(-2);
					for (int i = 0; i < temp.getChildren().size(); i++) {
						if (temp.getChildren().get(i).getRating() > bestRating.getRating()) {
							bestRating = temp.getChildren().get(i);
						}
					}
					output.setText(bestRating.getVirkne());
					PP_txt.setText(String.valueOf(bestRating.getP2()));
					PCP_txt.setText(String.valueOf(bestRating.getP1()));
				}
				input.setText("");
			}
		});
		Start_btn.setBounds(141, 249, 150, 33);
		contentPane.add(Start_btn);

		Exit_btn = new JButton("Quit");
		Exit_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // loga aizvēršana
			}
		});
		Exit_btn.setBounds(183, 284, 60, 33);
		contentPane.add(Exit_btn);

		choosePlayer = new JButton("Player starts");
		choosePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameMode = 1;
				output.setText("");
				winner.setText("Player starts, ievadi virkni un nospied play");
				// spēli pirmais sāk spēlētājs
			}
		});
		choosePlayer.setBounds(10, 120, 111, 23);
		contentPane.add(choosePlayer);

		choosePc = new JButton("PC starts");
		choosePc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameMode = 0;
				output.setText("");
				winner.setText("PC starts, ievadi virkni un nospied play");
				// spēli pirmais sāk PC
			}
		});
		choosePc.setBounds(317, 120, 111, 23);
		contentPane.add(choosePc);
	}
}
