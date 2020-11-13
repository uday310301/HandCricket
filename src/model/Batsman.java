package model;
public class Batsman {
	public String name;
	public String team;
	public int runs;
	public int prevruns;
	public int balls;
	public int prevballs;
	public int fours;
	public int prevFours;
	public int sixes;
	public int prevSixes;
	public int highest;
	public int prevHighest;
	public int prevFifties;
	public int prevHundreds;
	public String BatID;
	public double strikeRate;
	public boolean gotOut;
	public boolean gotChance;
	public Batsman() {
		
	}
	public Batsman(String BatID,String name) {
		this.BatID=BatID;
		this.name=name;
		this.gotOut=false;
		this.gotChance=false;
		this.team=BatID.substring(0,3);
	}
	public int getRuns() {
		return runs;
	}
	public void setRuns(int runs) {
		this.runs = runs;
	}
	public int getPrevruns() {
		return prevruns;
	}
	public void setPrevruns(int prevruns) {
		this.prevruns = prevruns;
	}
	public int getBalls() {
		return balls;
	}
	public void setBalls(int balls) {
		this.balls = balls;
	}
	public int getPrevballs() {
		return prevballs;
	}
	public void setPrevballs(int prevballs) {
		this.prevballs = prevballs;
	}
	public int getFours() {
		return fours;
	}
	public void setFours(int fours) {
		this.fours = fours;
	}
	public int getPrevFours() {
		return prevFours;
	}
	public void setPrevFours(int prevFours) {
		this.prevFours = prevFours;
	}
	public int getSixes() {
		return sixes;
	}
	public void setSixes(int sixes) {
		this.sixes = sixes;
	}
	public int getPrevSixes() {
		return prevSixes;
	}
	public void setPrevSixes(int prevSixes) {
		this.prevSixes = prevSixes;
	}
	public int getPrevHighest() {
		return prevHighest;
	}
	public void setPrevHighest(int prevHighest) {
		this.prevHighest = prevHighest;
	}
	public String getBatID() {
		return BatID;
	}
	public void setBatID(String batID) {
		BatID = batID;
	}
	public boolean isGotOut() {
		return gotOut;
	}
	public void setGotOut(boolean gotOut) {
		this.gotOut = gotOut;
	}
	public boolean isGotChance() {
		return gotChance;
	}
	public void setGotChance(boolean gotChance) {
		this.gotChance = gotChance;
	}
	public String getName() {
		return name;
	}
	public int getHighest() {
		return highest;
	}
	public void setStrikeRate(double strikeRate) {
		this.strikeRate = strikeRate;
	}
	
	public void four() {
		this.fours++;
		this.runs+=4;
	}
	public void six() {
		this.sixes++;
		this.runs+=6;
	}
	public void run(int runs) {
		this.runs+=runs;
	}
	public double getStrikeRate() {
		return (double)runs/balls * 100;
	}
	public void setHighest(int a) {
		this.highest=a;
	}
}
