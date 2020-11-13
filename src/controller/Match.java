package controller;
import view.HandCricket;
import model.Batsman;
import model.Bowler;
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.math.*;
public class Match {
	Batsman teamA[];
	Bowler teamB[];
	Random rand;
	int randball;
	public int totalScore;
	public int totalOvers;
	public int thisOver;
	public int onStrike;
	public int nonStriker;
	int bowler;
	int wickets_fallen;
	public int runs;
	int max_wickets;
	public int target;
	String commentary;
	String commentaryForOut[];
	String commentaryForShot[];
	boolean inningsCompleted;
	public int totalWickets;
	String teamBat;
	TextField displayScore;
	TextField displayCommentary;
	TextField displayOvers;
	JLabel scoreImages[];
	JLabel batting;
	JLabel bowling;
	JTextArea newBatsman;
	JTextArea newBowler;
	Thread copy;
	public Match(Batsman A[],Bowler B[],HandCricket hk,Thread copy){
		rand=new Random();
		this.teamA=A;
		this.teamB=B;
		max_wickets=5;
		target=1457896;
		inningsCompleted=false;
		startInnings();
		this.displayScore=hk.displayScore;
		this.displayCommentary=hk.displayCommentary;
		this.displayOvers = hk.displayOvers;
		this.scoreImages = hk.scoreImages;
		teamBat=teamA[0].team;
		this.batting=hk.batting;
		this.bowling=hk.bowling;
		this.newBatsman=hk.newBatsman;
		this.newBowler=hk.newBowler;
		this.copy=copy;
	}
	public void startInnings() {
		setCommentary();	
		onStrike=0;
		nonStriker=1;
	}
	public String getCommentary() {
		return commentary;
	}
	public void setTarget(int a) {
		this.target=a;
	}
	public int bowl() {
		int randball=rand.nextInt(7);
		while(randball==0 || randball==5) randball=rand.nextInt(7);
		return randball;
	}
	public boolean isOut(int a,int b) {
		return a==b && a!=0;
	}
	public void setCommentary() {
		setCommentaryForOut();
		setCommentaryForShot();
	}
	public void setCommentaryForOut() {
		commentaryForOut=new String[4];
		commentaryForOut[0]="Tried for a big one.....caught at the long on.";
		commentaryForOut[1]="Outside off stump....Caught by the man at the first slip";
		commentaryForOut[2]="What a catch by the man at the point.......";
		commentaryForOut[3]="Slight edge to dismiss the batsman";
	}
	public void setCommentaryForShot() {
		commentaryForShot=new String[4];
		commentaryForShot[0]="Maginificient cover drive.";
		commentaryForShot[1]="Straight down the ground.";
		commentaryForShot[2]="The Batsman pulls to clear the boundary line";
		commentaryForShot[3]="Lofts it over long on.";
	}
	public String getCommentaryForOut() {
		return commentaryForOut[rand.nextInt(commentaryForOut.length)];
	}
	public String getCommentaryForShot() {
		return commentaryForShot[rand.nextInt(commentaryForShot.length)];
	}
	public boolean isFour(int a) {
		return a==4;
	}
	public boolean isSix(int a) {
		return a==6;
	}
	public void swap() {
		int temp=this.onStrike;
		this.onStrike=this.nonStriker;
		this.nonStriker=temp;
	}
	public int max(int a,int b) {
		return a>b?a:b;
	}
	public void out(int idx) {
		totalWickets++;
		teamA[idx].gotOut=true;
		idx=max(onStrike,nonStriker)+1;
		this.onStrike=idx;
		if(max_wickets==totalWickets) inningsCompleted=true; 
	}
	public boolean innings_completed() {
		if(totalScore > target) {
			inningsCompleted=true;
		}
		return inningsCompleted;
	}
	public void initializeBatsman(int idx,int arr[]) {
		teamA[idx].prevruns=arr[0];
		teamA[idx].prevballs=arr[1];
		teamA[idx].prevFours=arr[2];
		teamA[idx].prevSixes=arr[3];
		teamA[idx].prevHighest=arr[4];
		teamA[idx].prevFifties=arr[5];
		teamA[idx].prevHundreds=arr[6];
		
	}
	public void initializeBowler(int idx,int arr[]) {
		teamB[idx].prevruns=arr[0];
		teamB[idx].prevballs=arr[1];
		teamB[idx].prevwickets=arr[2];
		teamB[idx].prevbestruns=arr[3];
		teamB[idx].prevbestwickets=arr[4];
		
	}
	public String initializeBatsman(int idx) {
		String statements;
		//get his prev runs ,balls faced , fours ,sixes, highest
		statements="select runs,ballsfaced,fours,sixes,highest,fifties,hundreds from player where pid='"+teamA[idx].BatID+"' and fid=1";
		return statements;
	}
	public String initializeBowler(int idx) {
		String statements="select runsconceded,ballsbowled,wickets,bestruns,bestwickets from player where pid='"+teamB[idx].BowlID+"' and fid=1";
		return statements;
	}
	public String[] SQLstatements(int idx,boolean role) {
		String[] statements;
		if(role) {
			statements=new String[11];
			//change the player's runs;
			statements[0]="update player p1 set p1.runs=(select runs from player where pid='"+teamA[idx].BatID+"' and fid = 1)+"+teamA[idx].runs+" where p1.pid='"+teamA[idx].BatID+"' and p1.fid =1";
			//change the no. of balls faced;
			statements[1]="update player p1 set p1.ballsfaced=(select ballsfaced from player where pid='"+teamA[idx].BatID+"' and fid = 1)+"+teamA[idx].balls+" where p1.pid='"+teamA[idx].BatID+"' and p1.fid =1";
			//change the no. of matches played;
			statements[2]="update player p1 set p1.matches=(select matches from player where pid='"+teamA[idx].BatID+"' and fid = 1)+"+1+" where p1.pid='"+teamA[idx].BatID+"' and p1.fid =1";
			//change the no. of innings played;
			statements[3]="update player p1 set p1.innings=(select innings from player where pid='"+teamA[idx].BatID+"' and fid = 1)+"+(teamA[idx].gotChance?1:0)+" where p1.pid='"+teamA[idx].BatID+"' and p1.fid =1";
			//change the avg and strike rate 
			if(teamA[idx].gotChance) {
				statements[4]="update player set avg=runs/innings where pid='"+teamA[idx].BatID+"' and fid =1";
				statements[5]="update player set sr=runs/ballsfaced where pid='"+teamA[idx].BatID+"' and fid =1";
				statements[6]="update player set highest="+max(teamA[idx].prevHighest,teamA[idx].runs)+"  where pid='"+teamA[idx].BatID+"' and fid =1";
				//century
				statements[7]="update player p1 set p1.hundreds=(select hundreds from player where pid='"+teamA[idx].BatID+"' and fid = 1)+"+(teamA[idx].runs>=100?1:0)+" where p1.pid='"+teamA[idx].BatID+"' and p1.fid =1";
				statements[8]="update player p1 set p1.fifties=(select fifties from player where pid='"+teamA[idx].BatID+"' and fid = 1)+"+(teamA[idx].runs>=50 && teamA[idx].runs<100 ?1:0)+" where p1.pid='"+teamA[idx].BatID+"' and p1.fid =1";
				statements[9]="update player p1 set p1.fours=(select fours from player where pid='"+teamA[idx].BatID+"' and fid = 1)+"+teamA[idx].fours+" where p1.pid='"+teamA[idx].BatID+"' and p1.fid =1";
				statements[10]="update player p1 set p1.sixes=(select sixes from player where pid='"+teamA[idx].BatID+"' and fid = 1)+"+teamA[idx].sixes+" where p1.pid='"+teamA[idx].BatID+"' and p1.fid =1";
				
			}
			else {
				statements[4]="select runs from player where pid='IND01'";
				statements[5]="select runs from player where pid='IND01'";
				statements[6]="select runs from player where pid='IND01'";
				statements[7]="select runs from player where pid='IND01'";
				statements[8]="select runs from player where pid='IND01'";
				statements[9]="select runs from player where pid='IND01'";
				statements[10]="select runs from player where pid='IND01'";
			}
		}
		else {
			statements=new String[0];
			if(teamB[idx].gotChance) {
				statements=new String[7];
			//change the player's runs 
				statements[0]="update player p1 set p1.runsconceded=(select runsconceded from player where pid='"+teamB[idx].BowlID+"' and fid = 1)+"+teamB[idx].runs+" where p1.pid='"+teamB[idx].BowlID+"' and p1.fid =1";
			//change the no. of balls bowled;
				statements[1]="update player p1 set p1.ballsbowled=(select ballsbowled from player where pid='"+teamB[idx].BowlID+"' and fid = 1)+"+teamB[idx].balls+" where p1.pid='"+teamB[idx].BowlID+"' and p1.fid =1";
			//change the no. of economy
			statements[2]="update player set economy=runsconceded/ballsbowled where pid='"+teamB[idx].BowlID+"' and fid =1";
			//change the no. of fifers
			statements[3]="update player p1 set fifers=(select fifers from player where p1.pid='"+teamB[idx].BowlID+"' and fid = 1)+"+(teamA[idx].gotChance?1:0)+" where p1.pid='"+teamA[idx].BatID+"' and p1.fid =1";
			//change the best
				if(teamB[idx].wickets >= teamB[idx].prevbestwickets) {
					statements[4]="update player p1 set p1.bestwickets="+teamB[idx].wickets+" where p1.pid='"+teamB[idx].BowlID+"' and fid=1";
					statements[5]="update player p1 set p1.bestruns="+(teamB[idx].runs < teamB[idx].prevbestruns ? teamB[idx].runs:teamB[idx].prevbestruns)+" where p1.pid='"+teamB[idx].BowlID+"' and p1.fid=1";
				}
				else {
					statements[4]="select runs from player where pid='IND01'";
					statements[5]="select runs from player where pid='IND01'";
				}
				statements[6]="update player p1 set p1.wickets=(select wickets from player where pid='"+teamB[idx].BowlID+"' and fid=1)";
			}
		}
		return statements;
	}
	public void ball(int a) {
		this.randball=bowl();
		if(teamA[onStrike].gotChance==false) {
			newBatsman.setText("Batsman Stats:\nName : "+teamA[onStrike].name+"\nRuns : "+teamA[onStrike].prevruns+"\nBalls : "+teamA[onStrike].prevballs+"\nHighest : "+teamA[onStrike].prevHighest+"\nFifties : "+teamA[onStrike].prevFifties+"\nHundreds : "+teamA[onStrike].prevHundreds+" ");
		}
		else {
			newBatsman.setText("Batsman Stats:");
		}
		if(teamB[bowler].gotChance==false) {
			newBowler.setText("Bowler Stats:\nName : "+teamB[bowler].name+"\nRuns Conceded : "+teamB[bowler].prevruns+"\nBalls Bowled : "+teamB[bowler].prevballs+"\nBest Bowling : "+teamB[bowler].prevbestwickets+" / "+teamB[bowler].prevbestruns+" ");
		}
		else {
			newBowler.setText("Bowler Stats:");
		}
		teamA[onStrike].balls++;
		teamB[bowler].balls++;
		teamA[onStrike].gotChance=true;
		teamB[bowler].gotChance=true;
		if(isOut(a,this.randball)) {
			commentary=getCommentaryForOut();
			teamB[bowler].wicket();
			out(onStrike);
		}
		else if(isFour(a)) { 
			teamA[onStrike].four();
			teamB[bowler].runs(4);
			totalScore+=4;
			commentary=getCommentaryForShot();
		}
		else if(isSix(a)) {
			teamA[onStrike].six();
			teamB[bowler].runs(6);
			totalScore+=6;
			commentary=getCommentaryForShot();
		}
		else {
			teamB[bowler].runs(a);
			totalScore+=a;
			if(a==3 || a==1) {
				teamA[onStrike].run(a);
				swap();
			}
			else {
				teamA[onStrike].run(a);
			}
		}
		if(thisOver==5) {
			swap();
			bowler=(bowler+1)%(max_wickets+1);
			totalOvers++;
			thisOver = 0;
		}
		else {
			thisOver++;
		}
		if(onStrike!=6 && nonStriker!=6)
		displayScore.setText(teamBat+" : "+totalScore+" / "+totalWickets+"    "+teamA[onStrike].name+" "+teamA[onStrike].runs+" ("+teamA[onStrike].balls+")*  "+teamA[nonStriker].name+" "+teamA[nonStriker].runs+" ("+teamA[nonStriker].balls+")  "+teamB[bowler].name+" "+teamB[bowler].wickets+" / "+teamB[bowler].runs);
		else
		displayScore.setText("Innings completed");
		displayOvers.setText(totalOvers+"."+thisOver);
		batting.setIcon(scoreImages[a].getIcon());
		bowling.setIcon(scoreImages[7+this.randball].getIcon());
		displayCommentary.setText(commentary);
	}
}
