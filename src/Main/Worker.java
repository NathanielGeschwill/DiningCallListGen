package Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JRadioButton;

public class Worker {
	String name, phoneNum, areaWorked;
	ArrayList<double[]> range = new ArrayList<double[]>();
	ArrayList<double[]> possibleH = new ArrayList<double[]>();
	boolean[] cHours;
	int pHC = 0;
	public Worker(String n, String p, String aW) {
		name = n;
		phoneNum = p;
		areaWorked = aW;
		cHours = new boolean[17];
	}
	public Worker(String n, String p, String aW, boolean[] cH) {
		name = n;
		phoneNum = p;
		areaWorked = aW;
		cHours = new boolean[17];
		for(int i=0; i<17; i++) {
			cHours[i] = cH[i];
		}
	}
	public void save(BufferedWriter out) {
		try {
			out.write(getName() + ":" + getPhoneNum() + ":" + getAreaWorked());
        	for(int i=0; i<17; i++) {
        		out.write(":" + cHours[i]);
        	}
        	out.newLine();
    	}catch(Exception e){System.err.println("Error: " + e.getMessage());}
	}
	public String getName() {
		return name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public String getAreaWorked() {
		return areaWorked;
	}
	public void setComHours(JRadioButton[] cH) {
		for(int i=0; i<17; i++) {
			cHours[i] = cH[i].isSelected();
		}
	}
	public double findStartHour(int i) {
		double num;
		if(i==0) {
			return 6;
		}
		else if(i==1) {
			return 7;
		}
		else if(i==2) {
			return 8;
		}
		else if(i==3) {
			return (9.05);
		}
		else if(i==4) {
			return (10.10);
		}
		else if(i==5) {
			return (11.15);
		}
		else if(i==6) {
			return (12.20);
		}
		else if(i==7) {
			return (13.25);
		}
		else if(i==8) {
			return (14.30);
		}
		else if(i==9) {
			num = (15.35);
			return num;
		}
		else if(i==10) {
			return (16.40);
		}
		else if(i==11) {
			return (17.45);
		}
		else if(i==12) {
			return (18.45);
		}
		else if(i==13) {
			return (19.45);
		}
		else if(i==14) {
			return (20.45);
		}
		else if(i==15) {
			return (21.45);
		}
		else{
			return 23;
		}
	}
	/*timeB[0] = new JRadioButton("6:00-7:00");
	timeB[1] = new JRadioButton("7:00-7:50");
	timeB[2] = new JRadioButton("8:00-8:55");
	timeB[3] = new JRadioButton("9:05-10:00");
	timeB[4] = new JRadioButton("10:10-11:05");
	timeB[5] = new JRadioButton("11:15-12:10");
	timeB[6] = new JRadioButton("12:20-1:15");
	timeB[7] = new JRadioButton("1:25-2:20");
	timeB[8] = new JRadioButton("2:30-3:25");
	timeB[9] = new JRadioButton("3:35-4:30");
	timeB[10] = new JRadioButton("4:40-5:35");
	timeB[11] = new JRadioButton("5:45-6:40");
	timeB[12] = new JRadioButton("6:45-7:40");
	timeB[13] = new JRadioButton("7:45-8:40");
	timeB[14] = new JRadioButton("8:45-9:40");
	timeB[15] = new JRadioButton("9:45-11:00");
	timeB[16] = new JRadioButton("11:00-12:30");*/
	public double findEndHour(int i) {
		if(i==0) {
			return 7;
		}
		else if(i==1) {
			return (7.50);
		}
		else if(i==2) {
			return (8.55);
		}
		else if(i==3) {
			return 10;
		}
		else if(i==4) {
			return (11.05);
		}
		else if(i==5) {
			return (12.10);
		}
		else if(i==6) {
			return (13.15);
		}
		else if(i==7) {
			return (14.20);
		}
		else if(i==8) {
			return (15.25);
		}
		else if(i==9) {
			return (16.30);
		}
		else if(i==10) {
			return (17.35);
		}
		else if(i==11) {
			return (18.40);
		}
		else if(i==12) {
			return (19.40);
		}
		else if(i==13) {
			return (20.40);
		}
		else if(i==14) {
			return (21.40);
		}
		else if(i==15) {
			return (23);
		}
		else{
			return 24.30;
		}
	}
	public void addPossibleHours(double a, double b){
		possibleH.add(new double[2]);
		possibleH.get(pHC)[0] = a;
		possibleH.get(pHC)[1] = b;
		pHC++;
	}
	public ArrayList<double[]> getPossibleHours(){
		return possibleH;
	}
	public ArrayList<double[]> findRange(){
		int min = 0;
		int max = 0;
		int counter = 0;
		boolean trig = false;
		for(int i=0; i<17; i++) {
			if(!cHours[i]) {
				System.out.println("min=" + i);
				System.out.println("Index Num: " + i);
				min = i;
				i++;
				while(i<17&&!cHours[i]) {
					i++;
				}
				i--;
				System.out.println("max=" + i);
				max = i;
				range.add(new double[2]);
				range.get(counter)[0] = findStartHour(min);
				range.get(counter)[1] = findEndHour(max);
				counter++;
				trig = true;
				
			}
		}
		if(trig)
			return range;
		else
			return null;
	}
	public boolean[] getComHours() {
		return cHours;
	}
	
}
