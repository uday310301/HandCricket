package db;
import java.sql.*;
import java.util.*;

public class ConnectSQL {
	Statement stmt;
	Connection con;
	public ConnectSQL() {
		try{  
			//step1 load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			con=DriverManager.getConnection(  
			"jdbc:oracle:thin:@localhost:1521:xe","system","nikhil12"); 
			stmt=con.createStatement(); 
			}catch(Exception e){ System.out.println(e);} 
	}
	public ArrayList<ArrayList<String>> QueryListPlayers(String team){
		ArrayList<ArrayList<String>> l=new ArrayList<ArrayList<String>>();
		for(int i=0;i<11;i++) l.add(new ArrayList<String>());
		try {
			ResultSet rs=stmt.executeQuery("select * from PLAYER where PTEAM='"+team+"'");  
			int cnt=0;
			while(rs.next()) { 
				l.get(cnt).add(rs.getString(1));
				l.get(cnt).add(rs.getString(2));
				cnt++;
			}
		}
		catch(Exception e) { e.printStackTrace();}
		//step5 close the connection object 
		/*for(int i=0;i<11;i++) {
			for(String j : l.get(i)) System.out.println(j);
		}*/
		return l;
	}
	public int[] batsmanDetails(String s) throws Exception {
		int arr[]=new int[11];
		int i=0;
		try {
			ResultSet rs=stmt.executeQuery(s);
			while(rs.next()){
				arr[i++]=rs.getInt(i);
				arr[i++]=rs.getInt(i);
				arr[i++]=rs.getInt(i);
				arr[i++]=rs.getInt(i);
				arr[i++]=rs.getInt(i);
				arr[i++]=rs.getInt(i);
				arr[i++]=rs.getInt(i);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return arr;
	}
	public int[] bowlerDetails(String s) throws Exception{
		int arr[]=new int[6];
		int i=0;
		try {
			ResultSet rs=stmt.executeQuery(s);
			while(rs.next()) {
				arr[i++]=rs.getInt(i);
				arr[i++]=rs.getInt(i);
				arr[i++]=rs.getInt(i);
				arr[i++]=rs.getInt(i);
				arr[i++]=rs.getInt(i);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return arr;
	}
	public void setDetails(String s[]) {
		for(String j:s) {
			try {
				ResultSet rs=stmt.executeQuery(j);
			}
			catch(Exception e) {
				e.printStackTrace();
				
			}
		}
	}
	public void closeConnection() {
		try {
			con.close();
		}
		catch(Exception e) { 
			e.printStackTrace();}
	}
}
