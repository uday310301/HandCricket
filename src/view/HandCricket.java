package view;
import java.awt.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import db.ConnectSQL;
import model.Batsman;
import model.Bowler;
import controller.Match;
class threadBuilder extends Thread{
	Match play;
	HandCricket hk;
	static int count; 
	static int target;
	public threadBuilder(HandCricket hk) {
		count++;
		this.hk=hk;
		if(count ==1 ) {
			this.play=new Match(hk.teamABat,hk.teamBBowl,hk,this);
			this.play.setTarget(14562);
		}
		if(count == 2) {
			this.play=new Match(hk.teamBBat,hk.teamABowl,hk,this);
			this.play.setTarget(target);

		}for(int i=0;i<6;i++) {
			try {
				play.initializeBatsman(i,hk.Connection.batsmanDetails(play.initializeBatsman(i)));
			}
			catch(Exception e) {
				e.printStackTrace();
				return ;
			}
		}
		for(int i=0;i<6;i++) {
			try {
				play.initializeBowler(i,hk.Connection.bowlerDetails(play.initializeBowler(i)));
			}
			catch(Exception e) {
				e.printStackTrace();
				
				return ;
			}
		}
		start();
	}
	public void run() {
		while(!play.innings_completed()) {
	    	while(!hk.isButtonClicked);
        	play.ball(hk.buttonClicked);
        	hk.isButtonClicked=false;
		}
		if(count==1) { 
			hk.tf.setText("Innings completed");
		    target=play.totalScore;
			  for(int i=0;i<6;i++) {
		        	hk.Connection.setDetails(play.SQLstatements(i,true));
		        }
		        for(int i=0;i<6;i++) {
		        	hk.Connection.setDetails(play.SQLstatements(i,false));
		        }
				
			
			hk.targetJb.setBounds(200,650,100,23);
			hk.TeamP.add(hk.targetJb);
			hk.displayTarget.setText(""+target);
			hk.TeamP.add(hk.displayTarget);
			hk.secondInnings=new threadBuilder(hk);
		}
		else if(count == 2) {
			if(play.target<play.totalScore)
				hk.displayCommentary.setText(hk.sTeam+" won the game");
			else
				hk.displayCommentary.setText(hk.oTeam+" won the game");
			for(int i=0;i<6;i++) {
	        	hk.Connection.setDetails(play.SQLstatements(i,true));
	        }
	        for(int i=0;i<6;i++) {
	        	hk.Connection.setDetails(play.SQLstatements(i,false));
	        }
	        hk.Connection.closeConnection();
		}
	}
}
public class HandCricket extends JFrame {
	JPanel Welcome;
	JPanel Game;
	JPanel TeamP;
	JPanel Stats;
	JButton startGame;
	JFrame copy;
	String sTeam="";
	String oTeam="";
	JButton Continue;
	Batsman teamABat[];
	Bowler teamABowl[];
	static int count=0;
	Batsman teamBBat[];
	Bowler teamBBowl[];
	int current_choice;
	public ArrayList<ArrayList<String>> l1,l2;
	ConnectSQL Connection;
	Match play;
	int buttonClicked;
	volatile boolean isButtonClicked;
	threadBuilder firstInnings;
	threadBuilder secondInnings;
	JButton button[];
	public TextField displayScore,displayCommentary,tf,displayOvers,displayTarget;
	public JLabel scoreImages[];
	public JLabel batting;
	public JLabel bowling;
	public JLabel targetJb;
	public JTextArea newBatsman;
	public JTextArea newBowler;
	public HandCricket() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		copy=this;
		Initialize();
		setTitle("HandCricket");
		Connection=new ConnectSQL();
		chooseTeam();
		setVisible(true);
	}
	public void Initialize() {
		Welcome = new JPanel();
		Welcome.setLayout(null);
		Game = new JPanel();
		Stats = new JPanel();
		TeamP=new JPanel();
		JLabel imag = new JLabel();
		imag.setIcon(new ImageIcon("E:\\Welcome.png"));// Change the path to your location (...\src\Images\Welcome.png)
		imag.setBounds(350,100,512,256);
		Welcome.add(imag);
		startGame=new JButton("Let's play");
		startGame.setBounds(534,421,99,28);
		Welcome.add(startGame);
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copy.remove(Welcome);
				copy.add(TeamP);
				copy.revalidate();
				copy.repaint();
			}
		});
		newBatsman=new JTextArea("");
		newBowler=new JTextArea("");
		this.add(Welcome);
		this.setSize(1300,800);
		Game.setSize(300,300);
		Welcome.setSize(300,300);
		TeamP.setSize(300,300);
	}
	public void chooseTeam() {
		TeamP.setLayout(null);
		JLabel jb1=new JLabel("Select your team");
		jb1.setBounds(400,300,1000,20);
		TeamP.add(jb1);
		JComboBox<String> jcb1 = new JComboBox<String>();
		JComboBox<String> jcb2 = new JComboBox<String>();
		jcb1.addItem("INDIA");
		jcb1.addItem("AUSTRALIA");
		jcb1.addItem("ENGLAND");
		jcb1.setBounds(400,320,250,20);
		TeamP.add(jcb1);
		JLabel jb2=new JLabel("Select your opponent team");
		jb2.setBounds(400,360,1000,20);
		TeamP.add(jb2);
		jcb2.addItem("INDIA");
		jcb2.addItem("AUSTRALIA");
		jcb2.addItem("ENGLAND");
		jcb2.setBounds(400,380,250,20);
		TeamP.add(jcb2);

		jcb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sTeam = (String) jcb1.getSelectedItem();
			}
		});
		jcb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oTeam = (String) jcb2.getSelectedItem();
			}
		});
		Continue=new JButton("Continue");
		Continue.setBounds(480,420,98,30);
		TeamP.add(Continue);
		Continue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TeamP.remove(jb1);
				TeamP.remove(jb2);
				TeamP.remove(jcb1);
				TeamP.remove(jcb2);
				TeamP.revalidate();
				TeamP.remove(Continue);
				TeamP.repaint();
				connectThem1();
			}
		});
	}
	public void connectThem1() {
		TeamP.setLayout(null);
		JCheckBox checkboxes[]=new JCheckBox[11];
		l1=Connection.QueryListPlayers(sTeam);
		for(int i=0;i<11;i++) {
			checkboxes[i]=new JCheckBox(l1.get(i).get(1));
			checkboxes[i].setBounds(100,100 + i*40,160,23);
			TeamP.add(checkboxes[i]);
			checkboxes[i].addItemListener(new MyListener());
		}
		JLabel jb1=new JLabel("Select 6 players from the team");
		jb1.setBounds(400,300,1000,20);
		TeamP.add(jb1);
		Continue=new JButton("Continue");
		Continue.setBounds(400,400,98,30);
		TeamP.add(Continue);
		Continue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(count!=6) throw new Exception(); count=0;
					teamABat=new Batsman[6];
					teamABowl=new Bowler[6];int cnt=0;
					for(int i=0;i<11;i++) {
						if(checkboxes[i].isSelected()) {
							teamABat[cnt]=new Batsman(l1.get(i).get(0),l1.get(i).get(1));
							teamABowl[cnt]=new Bowler(l1.get(i).get(0),l1.get(i).get(1));
							cnt++;
						}
					}
					for(int i=0;i<11;i++) TeamP.remove(checkboxes[i]);
					TeamP.remove(Continue);
					TeamP.remove(jb1);
					TeamP.revalidate();
					TeamP.repaint();
					connectThem2();
				}
				catch(Exception et) {
					et.printStackTrace();
					for(int i=0;i<11;i++) checkboxes[i].setSelected(false);
					count=0;
				}
			}
		});
	
	}
	public void connectThem2() {
		count=0;
		JCheckBox checkboxes[]=new JCheckBox[11];
		l2=Connection.QueryListPlayers(oTeam);
		for(int i=0;i<11;i++) {
			checkboxes[i]=new JCheckBox(l2.get(i).get(1));
			checkboxes[i].setBounds(100,100 + i*40,160,23);
			TeamP.add(checkboxes[i]);
			checkboxes[i].addItemListener(new MyListener());
		}

		JLabel jb1=new JLabel("Select 6 players from the team");
		jb1.setBounds(400,300,1000,20);
		TeamP.add(jb1);
		JButton Continue2=new JButton("Continue");
		Continue2.setBounds(400,400,98,30);
		Continue2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(count!=6) throw new Exception();
					count=0;
					teamBBat=new Batsman[7];
					teamBBowl=new Bowler[7];int cnt=0;
					for(int i=0;i<11;i++) {
						if(checkboxes[i].isSelected()) {
							teamBBat[cnt]=new Batsman(l2.get(i).get(0),l2.get(i).get(1));
							teamBBowl[cnt]=new Bowler(l2.get(i).get(0),l2.get(i).get(1));
							cnt++;
						}
					}
					for(int i=0;i<11;i++) TeamP.remove(checkboxes[i]);
					TeamP.remove(Continue2);
					TeamP.remove(jb1);
					TeamP.revalidate();
					TeamP.repaint();
					playGame();
				}
				catch(Exception et) {
					et.printStackTrace();
					for(int i=0;i<11;i++) checkboxes[i].setSelected(false);
					count=0;
				}
			}
		});
		TeamP.add(Continue2);
	}	
	class MyListener implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			int count2=0;
			if(e.getStateChange()==1)
				count2++;
			else
				count2--;
			count+=count2;
		}
	}
	class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
		}
	}
	public void playGame(){
		displayScore=new TextField("");
		displayOvers=new TextField("");
		displayCommentary=new TextField("");
		displayCommentary.setEditable(false);
		displayOvers.setEditable(false);
		
		displayScore.setEditable(false);
		scoreImages = new JLabel[14];
		batting=new JLabel();
		bowling=new JLabel();
		batting.setBounds(150,20,400,400);
		bowling.setBounds(530,20,400,400);
		newBatsman.setBounds(1000,320,300,200);
		newBowler.setBounds(1000,530,300,200);
		newBatsman.setEditable(false);
		newBowler.setEditable(false);
		batting.setIcon(new ImageIcon("E:\\L0.jpg"));// Change the path to your location (...\src\Images\L0.png)
		bowling.setIcon(new ImageIcon("E:\\R0.jpg"));// Change the path to your location (...\src\Images\R0.png)
		for(int i=0;i<14;i++)
			scoreImages[i] = new JLabel();
		for(int i=0;i<7;i++)
			if(i!=5) {
				scoreImages[i].setIcon(new ImageIcon("E:\\L"+i+".jpg"));// Change the path to your location (...\src\Images\abcd.png)
				scoreImages[i].setBounds(150,50,250,250);
			}
		for(int i=7;i<14;i++)
			if(i!=12) {
				scoreImages[i].setIcon(new ImageIcon("E:\\R"+(i-7)+".jpg"));// Change the path to your location (...\src\Images\abcd.png)
				scoreImages[i].setBounds(400,50,250,250);
			}
		displayScore.setBounds(300,500,600,23);
		displayOvers.setBounds(300,550,100,23);
		displayCommentary.setBounds(300,600,300,23);
		isButtonClicked=false;
		TeamP.setLayout(null);
		button = new JButton[6];
		for(int i=0;i<5;i++) {
			button[i] = new JButton(Integer.toString(i));	
			button[i].setBounds(200 + i*75,440 ,50,23);
			button[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					for(int i=0;i<6;i++) {
						if(e.getSource()==button[i]) {
							buttonClicked = Integer.parseInt(button[i].getText());
							isButtonClicked=true;
							break;
						}
					}
				}
			});
			TeamP.add(button[i]);
		}
		button[5] = new JButton("6");
		button[5].setBounds(575,440,50,23);
		button[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<6;i++) {
					if(e.getSource() == button[i]) {
						buttonClicked = Integer.parseInt(button[i].getText());
						isButtonClicked=true;
						break;
					}
				}
			}
		});
		TeamP.add(button[5]);
		tf= new TextField("");
		tf.setEditable(false);
		JLabel scoreJb = new JLabel("Score: ");
		JLabel commJb = new JLabel("Commentary: ");
		JLabel oversJb = new JLabel("Overs: ");
		displayTarget = new TextField();
		displayTarget.setEditable(false);
		displayTarget.setBounds(300,650,50,23);
		targetJb = new JLabel("Target: ");
		targetJb.setBounds(200,650,100,23);
		scoreJb.setBounds(200,500,100,23);
		commJb.setBounds(200,600,100,23);
		oversJb.setBounds(200,550,100,23);
		newBatsman.setBackground(getBackground());
		newBowler.setBackground(getBackground());
		TeamP.add(scoreJb);
		TeamP.add(commJb);
		TeamP.add(oversJb);
		TeamP.add(displayOvers);
		TeamP.add(displayScore);
		TeamP.add(batting);
		TeamP.add(bowling);
		TeamP.add(newBatsman);
		TeamP.add(newBowler);
		TeamP.add(displayCommentary);
		threadBuilder firstInnings=new threadBuilder(this);
        
	}
	public static void main(String args[]) {
		HandCricket hm=new HandCricket();
	}
}
