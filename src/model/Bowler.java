package model;

public class Bowler {
	public String name;
	public String team;
	public int runs;
	public int prevruns;
	public int balls;
	public int prevballs;
	public int wickets;
	public int prevwickets;
	public int prevbestwickets;
	public int prevbestruns;
	public String BowlID;
	public boolean gotChance;
	public Bowler() {
		
	}
	public Bowler(String BowlerID,String name){
		this.name=name;
		this.BowlID=BowlerID;
		this.team=BowlerID.substring(0,3);
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setID(String ID) {
		this.BowlID=ID;
	}
	public void wicket() {
		this.wickets++;
	}
	public void runs(int a) {
		this.runs+=a;
	}
	public void setprev(int a,int b) {
		this.prevbestwickets=a;
		this.prevbestruns=b;
	}
}
