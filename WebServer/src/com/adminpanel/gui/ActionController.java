package com.adminpanel.gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import com.database.mysql.DBCommunication;
import com.packetMem.shared.ActiveNodes;
import com.packetMem.shared.MPSMemory;
import com.packetMem.shared.PropertiesMemory;

public class ActionController implements ActionListener, MouseListener, WindowListener {

	private static ActionController actionController;
	
	private static final String IPV4PATTERN = 
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"; //ipv4 format check pattern
	/* the below explanation was taken from mkyong and stackoverflow */
	/*
	^		#start of the line
	 (		#  start of group #1
	   [01]?\\d\\d? #    Can be one or two digits. If three digits appear, it must start either 0 or 1
			#    e.g ([0-9], [0-9][0-9],[0-1][0-9][0-9])
	    |		#    ...or
	   2[0-4]\\d	#    start with 2, follow by 0-4 and end with any digit (2[0-4][0-9]) 
	    |           #    ...or
	   25[0-5]      #    start with 2, follow by 5 and ends with 0-5 (25[0-5]) 
	 )		#  end of group #2
	  \.            #  follow by a dot "."
	....            # repeat with 3 times (3x)
	$		#end of the line
	*/
	
	private ActionController(){
		
	}
	/*Creation of instance using Singletton pattern*/
	public static ActionController getInstance() {
		if (actionController == null) {
			synchronized (ActionController.class) {
				actionController = new ActionController();
			}
		}
		return actionController;
	}
	
	
	@Override
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent ae) {
		Panel panelIns = Panel.getInstance();
    	 
        Panel.getInstance();
		if (ae.getSource() == Panel.getButton1()) {
        	//let only the administrator to enter the system
        	
        	if(Panel.getUsername().getText().equals(PropertiesMemory.getInstance().getGuiUser()) && Panel.getPasswordField().getText().equals(PropertiesMemory.getInstance().getGuiPass()))
            {
        		 CardLayout cl = (CardLayout) (Panel.getCards().getLayout());
        		 // initialize every card/panel 
        		 panelIns.setInputPanel();
        		 panelIns.setMaliciousPatPanel();
        		 panelIns.setMaliciousIpPanel();
        		 panelIns.setStatsPanel();
        		 //shows the administrator the first panel with the options
                 cl.show(Panel.getCards(), "inputPanel");
                 
            }
        	else{
        		JOptionPane.showMessageDialog(Panel.getFrame1(), "Incorrect User Information","Message Log", JOptionPane.PLAIN_MESSAGE);
        	}
           

        } 
        else if (ae.getSource() == Panel.getPatButton()) {
        	// option for inserting the malicious patterns
            CardLayout cl = (CardLayout) (Panel.getCards().getLayout());
            cl.show(Panel.getCards(), "patternPanel");
            
        }
        else if (ae.getSource() == Panel.getIpButton()) {
        	// option for inserting the malicious ips
            CardLayout cl = (CardLayout) (Panel.getCards().getLayout());
            cl.show(Panel.getCards(), "ipPanel");
            
        }
        else if(ae.getSource() == Panel.getStatsButton() || ae.getSource() == Panel.getRefreshButton()){
        	// option for showing the statistics with the use of scrollbars
        	CardLayout cl = (CardLayout) (Panel.getCards().getLayout());
        	Panel.getStatslist().removeAll();
        	
        	DBCommunication con = new DBCommunication();
        	String nodeID = null;
        	ArrayList<String> templist = new ArrayList<String>(); //create a templist to get the result
        	// for every active node get it's malicious report an add it to the templist
        	for(int i = 0; i < ActiveNodes.getInstance().getNodes().size(); i++){
        		nodeID = ActiveNodes.getInstance().getNodeDataUID(i);
        		templist.addAll(con.getStatisticsList(nodeID));
        	}
        	// create a DefaultListModel to add the templist
        	DefaultListModel<String> model;
    		model = new DefaultListModel<String>();
    	    for(String p : templist){
    	         model.addElement(p);
    	    } 
        	//set the Statslist with this model
    	    Panel.getStatslist().setModel(model);
    		// add it to the scrollpanel and then show it...
    	    Panel.getStatsscrollpanel().setViewportView(Panel.getStatslist());
            cl.show(Panel.getCards(), "statsPanel");
        }
        else if(ae.getSource() == Panel.getPatbackButton() || ae.getSource() == Panel.getIpbackButton() || ae.getSource() == Panel.getStatsBackButton()){
        	// by pressing the "Back" button the user returns to the input Panel
        	CardLayout cl = (CardLayout) (Panel.getCards().getLayout());
            cl.show(Panel.getCards(), "inputPanel");
        }
        else if(ae.getSource() == Panel.getSubmitIPButton()){
        	//insert the malicious ip
        	//a little check for the ip format
        	Pattern pattern = Pattern.compile(IPV4PATTERN);
        	Matcher matcher = pattern.matcher(Panel.getInputIP().getText());
        	      
        	if(matcher.matches() == false)
        	{
        		JOptionPane.showMessageDialog(Panel.getFrame2(), "Incorrect Address","IP Log", JOptionPane.PLAIN_MESSAGE);
        	}
        	else
        	{
        		//pops a message for the user
        		if(MPSMemory.getInstance().insertPattern(Panel.getInputIP().getText(),1) == 0){
        			
        		JOptionPane.showMessageDialog(Panel.getFrame3(), "New Malicious IP Submitted","IP Log", JOptionPane.PLAIN_MESSAGE);
        		}
        		else{
        			JOptionPane.showMessageDialog(Panel.getFrame3(), "Malicious IP already exists, \n insert new IP","IP Log", JOptionPane.PLAIN_MESSAGE);
        		}
        	}
        	
        }
        else if(ae.getSource() == Panel.getSubmitPatButton()){
        	//insert the malicious pattern
        	if(MPSMemory.getInstance().insertPattern(Panel.getInputPat().getText(),0) == 0){
        		JOptionPane.showMessageDialog(Panel.getFrame4(), "New Malicious Pattern Submitted","Pattern Log", JOptionPane.PLAIN_MESSAGE);
        		}
        		else{
        			JOptionPane.showMessageDialog(Panel.getFrame4(), "Malicious Pattern already exists, \n insert new IP","Pattern Log", JOptionPane.PLAIN_MESSAGE);
        		}
        }
        else if(ae.getSource() == Panel.getExitServer()){
        	//the option where the user chooses to close the server along with the gui
        	int Answer = JOptionPane.showConfirmDialog(Panel.getframe6(), "You want to quit the Server?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (Answer == JOptionPane.YES_OPTION){
           	
           	 System.out.println("GUI is Closing the Server");
           	 System.exit(0);
            }
        }
        else if( ae.getSource() == Panel.getHelpabout()){
        	JOptionPane.showMessageDialog(Panel.getFrame5(), "Application v.1.0 \nCreated for DIT K23b. \n Project2","About", JOptionPane.PLAIN_MESSAGE);
        }
        
	}
	// some actions for the mouse events
	@Override
	public void mouseClicked(MouseEvent e) {
		Panel.getInstance();
		if(e.getSource() == Panel.getUsername()){
			Panel.getInstance();
			Panel.getUsername().setText("");
		}
		Panel.getInstance();
		if(e.getSource() == Panel.getPasswordField()){
			Panel.getInstance();
			Panel.getPasswordField().setText("");
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		Panel.getInstance();
		if(e.getSource() == Panel.getUsername()){
			Panel.getInstance();
			Panel.getUsername().setText("<Enter Username>");
		}
		Panel.getInstance();
		if(e.getSource() == Panel.getPasswordField()){
			Panel.getInstance();
			Panel.getPasswordField().setText("Enter Password");
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		//prints a confirmation for the administrator that the gui has closed
		System.out.println("GUI is Closing ");
	}
	@Override
	public void windowClosed(WindowEvent e) {

	}
	@Override
	public void windowIconified(WindowEvent e) {
	
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
	
	}
	@Override
	public void windowActivated(WindowEvent e) {
	
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
	
	}   
    

}
