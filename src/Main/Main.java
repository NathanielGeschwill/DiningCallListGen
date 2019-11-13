package Main;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
public class Main implements ActionListener{
	JFrame f;
	JPanel p1, p2;
	Container c1;
	JLabel title, timeT, headQ, footQ, space;
	JRadioButton[] timeB;
	JLabel[] border; 
	JLabel[] shiftL;
	ArrayList<JLabel[]> shiftsNeeded;
	JButton next, back, finish, sAll;
	String current, a, b, c, d, e;
	String[] tokens;
	@SuppressWarnings("rawtypes")
	JComboBox[][] comboAMPM;
	@SuppressWarnings("rawtypes")
	JComboBox[] comboShiftAmount;
	double[][] hoursNeeded;
	int counter;
	boolean atEnd, weekEnd;
	ArrayList<Worker> worker, possibleW;
	//ArrayList<Shift> shift;
	final int TIMESLOTS = 17;
	final int SHIFTS = 16;
	JTextField[][] shiftF = new JTextField[SHIFTS][4];
	ArrayList<double[]> finalHours;
	int response;
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Main() {
    	// create worker list
    	worker = new ArrayList<Worker>();
    	//shift = new ArrayList<Shift>();
    	// create shift label list
    	shiftsNeeded = new ArrayList<JLabel[]>();
    	
    	// 
    	hoursNeeded = new double[TIMESLOTS][2];
    	counter = -2;
    	
    	// name of window
    	f = new JFrame(" MSC Calling List Generator");
    	// size of window
    	f.setSize(225,850);
    	// don't let them change size otherwise it screws up the layout
		f.setResizable(false);
		// close program when you close
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// get content from frame
		c1 = f.getContentPane();
		
		// new panels
		p1 = new JPanel();
		p2 = new JPanel();
		
		// title in frame
		title = new JLabel("        MSC Calling List Generator        ");
		// add to p1, north side
		p1.add(title,BorderLayout.NORTH);
		
		// make string array with AM/PM
		String AMPM[] = {"AM","PM"};
		
		// make 32 AM/PM combo boxes
		comboAMPM = new JComboBox[SHIFTS*4][2];
		for(int i=0; i<SHIFTS*4; i++) {
			for(int ii=0; ii<2; ii++) {
				comboAMPM[i][ii] = new JComboBox(AMPM);
			}
		}
		
		// string for amount of shifts needed for one specific area
		String shiftAmount[] = {"0","1","2","3","4"};
		
		// create 16 combo boxes that ask for amount of shifts need per area
		comboShiftAmount = new JComboBox[SHIFTS];
		for(int i=0; i<SHIFTS; i++) {
			comboShiftAmount[i] = new JComboBox(shiftAmount);
			
		}
		
		// label for space - why do I have this???
		space = new JLabel("");
		// label to prompt area covered
		headQ = new JLabel("  What Areas Do You Need Covered?");
		p1.add(headQ);
		// another space
		footQ = new JLabel("");
		p1.add(footQ);
		
		// 42x1 grid
		p1.setLayout(new GridLayout(42, 1));
		// create time slot fields used to define open shift times
		createTimeSlotFields();
		// create labels that define which area this info is tied to
		createAreaLabels();
		createShifts();
		
		finish = new JButton("Finish");
		finish.addActionListener(this);
		back = new JButton("Back");
		back.addActionListener(this);
		next = new JButton("Next");
		next.addActionListener(this);
		sAll = new JButton("Invert Selection");
		sAll.addActionListener(this);
		
		p1.add(next);
		
		c1.add(p1);
		f.setVisible(true);
	}
    
    public void printList(ArrayList<Worker> w) {
    	try {
    		
    		FileWriter fstream = new FileWriter("out.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            DecimalFormat df = new DecimalFormat("####0.00");
            for(int i=0; i<w.size(); i++) {
        		for(int ii=0; ii<w.get(i).getPossibleHours().size(); ii++) {
        			System.out.println("" + w.get(i).getName() + "\t" + w.get(i).getPhoneNum() + "\t" + w.get(i).getAreaWorked() + "\t" + df.format(w.get(i).getPossibleHours().get(ii)[0]) + " to " + df.format(w.get(i).getPossibleHours().get(ii)[1]));
        			if(w.get(i).getPossibleHours().get(ii)[0]>=13&&w.get(i).getPossibleHours().get(ii)[1]>=13&&w.get(i).getPossibleHours().get(ii)[1]<24)
        				out.write("" + w.get(i).getName() + "\t" + w.get(i).getPhoneNum() + "\t" + w.get(i).getAreaWorked() + "\t" + df.format(w.get(i).getPossibleHours().get(ii)[0]-12) + "PM to " + df.format(w.get(i).getPossibleHours().get(ii)[1]-12) + "PM");
        			else if(w.get(i).getPossibleHours().get(ii)[0]>=13)
        				out.write("" + w.get(i).getName() + "\t" + w.get(i).getPhoneNum() + "\t" + w.get(i).getAreaWorked() + "\t" + df.format(w.get(i).getPossibleHours().get(ii)[0]-12) + "PM to " + df.format(w.get(i).getPossibleHours().get(ii)[1]) + "AM");
        			else if(w.get(i).getPossibleHours().get(ii)[1]>=13&&w.get(i).getPossibleHours().get(ii)[1]<24)
        				out.write("" + w.get(i).getName() + "\t" + w.get(i).getPhoneNum() + "\t" + w.get(i).getAreaWorked() + "\t" + df.format(w.get(i).getPossibleHours().get(ii)[0]) + "AM to " + df.format(w.get(i).getPossibleHours().get(ii)[1]-12) + "PM");
        			else
        				out.write("" + w.get(i).getName() + "\t" + w.get(i).getPhoneNum() + "\t" + w.get(i).getAreaWorked() + "\t" + df.format(w.get(i).getPossibleHours().get(ii)[0]) + "AM to " + df.format(w.get(i).getPossibleHours().get(ii)[1]) + "AM");
        			out.newLine();
        		}
        	}
            out.close();
    	}catch(Exception e){System.err.println("Error: " + e.getMessage());}
    	
    	
    }
    
    public ArrayList<Worker> findTwoHours(ArrayList<Worker> w) {
    	System.out.println("FIND TWO HOURS");
    	double min = 0;
    	double max = 0;
    	for(int i=0; i<w.size(); i++) {
    		for(int ii=0; ii<w.get(i).getPossibleHours().size(); ii++) {
    			min = w.get(i).getPossibleHours().get(ii)[0];
    			max = w.get(i).getPossibleHours().get(ii)[1];
    			if(max-min<2) {
    				System.out.println("REMOVED POSSIBLE" + w.get(i).getName()  + w.get(i).getAreaWorked());
    				w.get(i).getPossibleHours().remove(ii);
    				ii--;
    				if(w.get(i).getPossibleHours().isEmpty())
    					break;
    			}
    		}
    	}
    	return w;
    }
    
    public void genList(double[][] hoursNeeded, ArrayList<Worker> worker) {
    	System.out.println("GEN LIST");
    	ArrayList<double[]> callHours = new ArrayList<double[]>();
    	possibleW = new ArrayList<Worker>();
    	//double[] = new double[2];
    	int counter = 0;
    	for(int i=0; i<shiftsNeeded.size(); i++) {
    			System.out.println(shiftsNeeded.get(i)[0].getText() + " is Selected");
    			for(int ii=0; ii<worker.size(); ii++) {
    				System.out.println(worker.get(ii).getAreaWorked() + " compare " + shiftsNeeded.get(i)[0].getText());
    				if(worker.get(ii).getAreaWorked().equals(shiftsNeeded.get(i)[0].getText())) {
    					System.out.println(worker.get(ii).getName() + " works " + shiftsNeeded.get(i)[0].getText());
    					ArrayList<double[]> range = worker.get(ii).findRange();
    					if(range!=null)
    						
    					for(int iii=0; iii<range.size(); iii++) {
    						System.out.println("MIN=" + range.get(iii)[0] + " MAX=" +  range.get(iii)[1] + " NMIN=" + hoursNeeded[i][0] + " NMAX=" + hoursNeeded[i][1]);
    						System.out.println("Is " + range.get(iii)[0] + "<=" +  hoursNeeded[i][0] + "OR" + range.get(iii)[0] + "<" + hoursNeeded[i][1]);
    						if(range.get(iii)[0]<=hoursNeeded[i][0]) {
    							System.out.println("YES 1");
    							System.out.println("Is " + range.get(iii)[1] + ">" +  hoursNeeded[i][0]);
    							if(range.get(iii)[1]>hoursNeeded[i][0]) {
    								System.out.println("YES");
    								System.out.println("Is " + range.get(iii)[1] + ">=" +  hoursNeeded[i][1]);
    								if(range.get(iii)[1]>=hoursNeeded[i][1]) {
    									System.out.println("YES");
    									callHours.add(new double[2]);
    									callHours.get(counter)[0] = hoursNeeded[i][0];
    									callHours.get(counter)[1] = hoursNeeded[i][1];
    									System.out.println("FOUND POSSIBLE " + worker.get(ii).getName() + " could work " + shiftsNeeded.get(i)[0].getText());
    									possibleW.add(new Worker(worker.get(ii).getName(),worker.get(ii).getPhoneNum(),worker.get(ii).getAreaWorked()));
    									possibleW.get(counter).addPossibleHours(hoursNeeded[i][0],  hoursNeeded[i][1]);
    									System.out.println("" + hoursNeeded[i][0] + hoursNeeded[i][1]);
    									counter++;
    								}
    								else{
    									System.out.println("NO");
    									callHours.add(new double[2]);
    									callHours.get(counter)[0] = hoursNeeded[i][0];
    									callHours.get(counter)[1] = range.get(iii)[1];
    									System.out.println("FOUND POSSIBLE " + worker.get(ii).getName() + " could work " + shiftsNeeded.get(i)[0].getText());
    									
    									possibleW.add(new Worker(worker.get(ii).getName(),worker.get(ii).getPhoneNum(),worker.get(ii).getAreaWorked()));
    									possibleW.get(counter).addPossibleHours(hoursNeeded[i][0], range.get(iii)[1]);
    									System.out.println("" + hoursNeeded[i][0] + range.get(iii)[1]);
    									counter++;
    								}
    							}
    						}
    						else if(range.get(iii)[0]<hoursNeeded[i][1]) {
    							System.out.println("YES 2");
    							System.out.println("Is " + range.get(iii)[1] + "<" +  hoursNeeded[i][1]);
    							if(range.get(iii)[1]<hoursNeeded[i][1]) {
    								System.out.println("YES");
    								callHours.add(new double[2]);
									callHours.get(counter)[0] = range.get(iii)[0];
									callHours.get(counter)[1] = range.get(iii)[1];
									System.out.println("FOUND POSSIBLE " + worker.get(ii).getName() + " could work " + shiftsNeeded.get(i)[0].getText());
									possibleW.add(new Worker(worker.get(ii).getName(),worker.get(ii).getPhoneNum(),worker.get(ii).getAreaWorked()));
									possibleW.get(counter).addPossibleHours(range.get(iii)[0], range.get(iii)[1]);
									System.out.println("" + range.get(iii)[0] + range.get(iii)[1]);
									counter++;
    							}
    							else {
    								System.out.println("NO");
    								callHours.add(new double[2]);
									callHours.get(counter)[0] = range.get(iii)[0];
									callHours.get(counter)[1] = hoursNeeded[i][1];
									System.out.println("FOUND POSSIBLE " + worker.get(ii).getName() + " could work " + shiftsNeeded.get(i)[0].getText());
									possibleW.add(new Worker(worker.get(ii).getName(),worker.get(ii).getPhoneNum(),worker.get(ii).getAreaWorked()));
									possibleW.get(counter).addPossibleHours(range.get(iii)[0],  hoursNeeded[i][1]);
									System.out.println("" + range.get(iii)[0] +  hoursNeeded[i][1]);
									counter++;
    							}
    						}
    					}
    				}
    			}
    	}
    	printList(findTwoHours(possibleW));
    }
    
    public void createTimeSlotFields() {
    	// create 64 text fields for time slots
    	for(int i=0; i<SHIFTS; i++) {
    		for(int ii=0; ii<4; ii++) {
    			shiftF[i][ii] = new JTextField(2);
    		}
    	}
    }
    
    public void promptHours() {
    	
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.HORIZONTAL;
    	gbc.gridx = 0;
    	gbc.gridy = 0;
    	p1.remove(next);
    	c1.remove(p1);
    	c1.add(p2);
    	//int sNCounter = 0;
    	double shiftNum;
    	for(int i=0; i<SHIFTS; i++) {
    		shiftNum = Double.parseDouble((String) comboShiftAmount[i].getSelectedItem());
    		while(shiftNum!=0){
    			System.out.println("NEXT Shift Num " + shiftNum);
    			System.out.println("i " + i);
        		shiftsNeeded.add(new JLabel[4]);
        		shiftsNeeded.get(shiftsNeeded.size()-1)[0] = new JLabel(shiftL[i].getText());
        		shiftsNeeded.get(shiftsNeeded.size()-1)[1] = new JLabel(":");
        		shiftsNeeded.get(shiftsNeeded.size()-1)[2] = new JLabel("-");
        		shiftsNeeded.get(shiftsNeeded.size()-1)[3] = new JLabel(":");
        		shiftNum--;
        	}
    		
    	}
    	for(int i=0; i<shiftsNeeded.size(); i++) {
				System.out.println(i);
				gbc.gridx = 0;
				for(int ii=0; ii<4; ii++) {
			    	
					gbc.gridx += 1;
					if(ii==1||ii==3) 
						gbc.weightx = 1.0;
					
					else if(ii==2)
						gbc.weightx = 1.0;
					else
						gbc.weightx = 23.0;
					p2.add(shiftsNeeded.get(i)[ii],gbc);
					System.out.println("char");
					
					
					gbc.weightx = 0.0;
					gbc.gridx += 1;
					p2.add(shiftF[i][ii],gbc);
					System.out.println("field");
					
					if(ii==1||ii==3) {
						gbc.gridx += 1;
						if(ii==1)
							p2.add(comboAMPM[i][0],gbc);
						else
							p2.add(comboAMPM[i][1],gbc);
					}
				}
				gbc.gridy += 1;
			
		}
    	gbc.gridy += 1;
    	gbc.gridx = 11;
    	p2.add(next,gbc);
    	f.pack();
    }
    public void createAreaLabels() {
    	shiftL = new JLabel[SHIFTS];
    	shiftL[0] = new JLabel("BDM Brew Devils");
		shiftL[1] = new JLabel("BDM Buns & Bowls");
		shiftL[2] = new JLabel("BDM Cashier");
		shiftL[3] = new JLabel("BDM Floater");
		shiftL[4] = new JLabel("BDM Grill");
		shiftL[5] = new JLabel("BDM Pizza");
		shiftL[6] = new JLabel("BDM Stocker");
		shiftL[7] = new JLabel("Dish Room");
		shiftL[8] = new JLabel("Fireside Cashier");
		shiftL[9] = new JLabel("Fireside Comfort Zone");
		shiftL[10] = new JLabel("Fireside Floater");
		shiftL[11] = new JLabel("Fireside Grill");
		shiftL[12] = new JLabel("Fireside Heritage");
		shiftL[13] = new JLabel("Fireside Innovations");
		shiftL[14] = new JLabel("Fireside Poblanos");
		shiftL[15] = new JLabel("Jarvis Cart");
    }
    public void removeShifts() {
    	p1.remove(border[SHIFTS]);
		for(int i=0; i<SHIFTS; i++) {
			p1.remove(shiftL[i]);
			p1.remove(comboShiftAmount[i]);
		}
    }
    public void createShifts() {
    	// prompt window asking if you want weekday (1 day), Weekend (3 day), or use old info to generate with filling each employee
    	// MAJOR REWORK NEEDED - heavy redesign to allow containment of employees which also need an update method
    	Object[] options = {"Weekday", "Weekend", "Load Previous"};
    	response = JOptionPane.showOptionDialog(f, "Are You Making a List for a Weekday or the Weekend or Load From Previous", "Prompt Question",
    			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    	if(1 == response) {
    		counter = -6;
    		weekEnd = true;
    		footQ.setText("  for Friday");
    	}
    	
    	border = new JLabel[SHIFTS+1];
		border[SHIFTS] = new JLabel("---------------------------------");
		for(int i=0; i<SHIFTS; i++) {
			p1.add(shiftL[i]);
			p1.add(comboShiftAmount[i]);
		}
    }
    public void collectHoursNeeded() {
    	double total;
    	for(int i=0; i<shiftsNeeded.size(); i++) {
    		
    			for(int ii=0; ii<2; ii++) {
    					total = 0;
    					if(comboAMPM[i][ii].getSelectedItem().equals("PM")&&Double.parseDouble(shiftF[i][ii*2].getText())!=12) {
    						total += 12;
    					}
    					if(Double.parseDouble(shiftF[i][ii*2].getText())==12&&comboAMPM[i][ii].getSelectedItem().equals("AM")) {
    						total += 12;
    					}
    					total += Double.parseDouble(shiftF[i][ii*2].getText());
    					total += (Double.parseDouble(shiftF[i][(ii*2)+1].getText())/100);
    					hoursNeeded[i][ii] = total;
    					//System.out.println("Line: " + i + " " + hoursNeeded[i][ii]);
    				
    			}
    		
    	}
    }
    public void createTimeSheet() {
    	border = new JLabel[TIMESLOTS+1];
    	timeB = new JRadioButton[TIMESLOTS];
		timeB[0] = new JRadioButton("6:00-7:00");
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
		timeB[16] = new JRadioButton("11:00-12:30");
		border[TIMESLOTS] = new JLabel("---------------------------------");
		p1.add(border[TIMESLOTS]);
		for(int i=0; i<TIMESLOTS; i++) {
			timeB[i].addActionListener(this);
			p1.add(timeB[i]);
			border[i] = new JLabel("---------------------------------");
			p1.add(border[i]);
		}
    }
	
    
    public void actionPerformed(ActionEvent event){
		if(event.getSource() == next) {
			if(counter <= -2) {
				if(counter == -6) {
					footQ.setText("  for Saturday");
					//for(int i=0; i<SHIFTS; i++) {
					//	shiftB[i].setSelected(false);
					//}
				}
				else if(counter == -5) {
					footQ.setText("  for Sunday");
					//for(int i=0; i<SHIFTS; i++) {
					//	shiftB[i].setSelected(false);
					//}
				}
				else if(counter == -4){
					footQ.setText("  for Friday");
					p2.setLayout(new GridBagLayout());
					removeShifts();
					//p1.remove(next);
					promptHours();
					//p1.add(next);
				}
				else if(counter == -3){
					footQ.setText("  for Saturday");
					removeShifts();
					//p1.remove(next);
					promptHours();
					//p1.add(next);
				}
				else{
					if(weekEnd)
						footQ.setText(" for Sunday");
					else
						footQ.setText("");
					p2.setLayout(new GridBagLayout());
					removeShifts();
					//p1.remove(next);
					promptHours();
					//Here
					//p1.add(next);
				}
				//p1.setLayout(new GridLayout(42, 1));
				counter++;
				f.setContentPane(c1);
			}
			else if(counter == -1) {
				if(JOptionPane.showConfirmDialog(f,"Are You Sure The Info Is Correct?\nNOTE: This Action Cannot Be Changed After Proceeding","Comfirm Action",JOptionPane.YES_NO_OPTION)
						== JOptionPane.YES_OPTION) {
					
					//System.out.println("First Text Box is" + shiftF[0][0].getText());
					//System.out.println("First Text Box is" + comboAMPM[0][0].getSelectedIndex());
					collectHoursNeeded();
					System.out.println("RESPONSE=" + response);
					
					if(2 == response) {
						System.out.println("QUICK BUILD");
						try{
				            @SuppressWarnings("resource")
							BufferedReader fr= new BufferedReader(new FileReader("save.txt"));
				            //worker.clear();
				            String tokens[];
			            	boolean cHours[] = new boolean[17];
				            while((current = fr.readLine()) != null){
				            	
				            	
				                tokens = current.split(":");
				                    a = tokens[0];
				                    b = tokens[1];
				                    c = tokens[2];
				                    for(int i=0; i<17; i++) {
				                    	if(tokens[3+i].equals("true"))
				                    		cHours[i] = true;
				                    	else
				                    		cHours[i] = false;
				                    }
				                    System.out.println("LOAD DATA");
				                    System.out.println(tokens[0]);
				                    System.out.println(tokens[1]);
				                    System.out.println(tokens[2]);
				                    for(int i=0; i<17; i++) {
				                    	System.out.println(cHours[i]);
				                    }
				                worker.add(new Worker(a,b,c,cHours));
				            }
				        }catch(Exception e ){System.out.print("ERROR!!!"+e);}
			    		genList(hoursNeeded, worker);
			    		System.exit(0);
			    	}
					try{
			            @SuppressWarnings("resource")
			            InputStream is = getClass().getResourceAsStream("input.txt");
			            InputStreamReader isr = new InputStreamReader(is);
						BufferedReader fr= new BufferedReader(isr);
			            while((current = fr.readLine()) != null){
			                tokens = current.split("\t");
			                    a = tokens[0];
			                    b = tokens[1];
			                    c = tokens[2];
			                    System.out.println(tokens[0]);
			                    System.out.println(tokens[1]);
			                    System.out.println(tokens[2]);
			                worker.add(new Worker(a,b,c));
			            }
			        }catch(Exception e ){System.out.print("ERROR!!!"+e);}
					f.setSize(225,850);
					p2.remove(next);
					p1.remove(sAll);
			    	c1.remove(p2);
			    	c1.add(p1);
					/*for(int i=0; i<TIMESLOTS; i++) {
						hoursNeeded[i] = timeB[i].isSelected();
					}*/
			    	createTimeSheet();
			    	
					counter++;
					headQ.setText("  Commitment Hours For");
					footQ.setText("  " + worker.get(counter).getName());
					for(int i=0; i<TIMESLOTS; i++) {
						timeB[i].setSelected(false);
					}
					p1.add(sAll);
					p1.add(next);
					f.setContentPane(c1);
				}
			}
			else {
				worker.get(counter).setComHours(timeB);
				while(worker.get(counter+1).getName().equals(worker.get(counter).getName())) {
		    		
		    		if(counter == 0) {
						p1.remove(next);
						p1.add(back);
						p1.add(next);
					}
		    		counter++;
		    		worker.get(counter).setComHours(timeB);
		    		if(counter == worker.size()-1) {
						p1.remove(next);
						p1.add(finish);
						atEnd = true;
						f.setContentPane(c1);
						counter--;
						break;
					}
		    	}
				if(counter == 0) {
					p1.remove(next);
					p1.add(back);
					p1.add(next);
				}
				counter++;
				footQ.setText("  " + worker.get(counter).getName());
				for(int i=0; i<TIMESLOTS; i++) {
					timeB[i].setSelected(false);
				}
				f.setContentPane(c1);
			}
			if(counter == worker.size()-1 && response!=2) {
				p1.remove(next);
				p1.add(finish);
				atEnd = true;
				for(int i=0; i<TIMESLOTS; i++) {
					timeB[i].setSelected(false);
				}
				f.setContentPane(c1);
			}
		}
		if(event.getSource() == sAll) {
			for(int i=0; i<TIMESLOTS; i++) {
				
				if(timeB[i].isSelected()) {
					timeB[i].setSelected(false);
				}
				else {
					timeB[i].setSelected(true);
				}
			}
			
		}
		if(event.getSource() == back) {
			if(counter == 1) {
				//counter--;
				p1.remove(back);
				footQ.setText("  " + worker.get(counter).getName());
				if(atEnd) {
					p1.remove(finish);
					p1.add(next);
					atEnd = false;
				}
				for(int i=0; i<TIMESLOTS; i++) {
					timeB[i].setSelected(false);
				}
				f.setContentPane(c1);
			}
			else {
				
				counter--;
				while(counter != 0 && worker.get(counter-1).getName().equals(worker.get(counter).getName())) {
					counter--;
		    		if(counter == 0) {
						p1.remove(back);
					}
		    		
		    	}
				footQ.setText("  " + worker.get(counter).getName());
				if(atEnd) {
					p1.remove(finish);
					p1.add(next);
					atEnd = false;
				}
				for(int i=0; i<TIMESLOTS; i++) {
					timeB[i].setSelected(false);
				}
				f.setContentPane(c1);
			}
		}
		if(event.getSource() == finish) {
			int response = JOptionPane.showConfirmDialog(null, "Do you want to submit? (You won't be able to change this info after submitting)", "Submit?",
	                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(response == JOptionPane.YES_OPTION) {
				worker.get(counter).setComHours(timeB);
				System.out.println("FINISH");
				try {
		    		FileWriter fstream = new FileWriter("save.txt");
		            BufferedWriter out = new BufferedWriter(fstream);
		            for(int i=0; i<worker.size(); i++) {
		            	
		            	worker.get(i).save(out);
		            }
		            out.close();
		    	}catch(Exception e){System.err.println("Error: " + e.getMessage());}
				genList(hoursNeeded, worker);
				System.exit(0);
			}
		}
	}
}