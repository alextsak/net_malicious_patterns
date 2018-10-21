package com.adminpanel.gui;


import java.awt.*;

import javax.swing.*;

public class Panel extends JFrame {
	
	
	
	private static final long serialVersionUID = 1L;
	private static JPanel cards, card1, inputPanel, patternPanel, ipPanel, statsPanel;
    private static JButton button1, submitIPButton, submitPatButton, patbackButton, ipbackButton, statsBackButton;
    private static JButton ipButton, patButton, statsButton, refreshButton;
	private static Component frame1, frame2, frame3, frame4, frame5, frame6;
    private static JTextField username, inputPat, inputIP;
    private static JPasswordField passwordField;
    private static JScrollPane statsscrollpanel;
    private static JList<String> statslist;
    private static JMenuItem helpabout, exitServer;
    private static JMenuBar menubar;							
    private static JMenu filemenu ;
    private static JMenu helpmenu;
		
	private static Panel panel;
	
    private Panel() {
    	
    }
    // Creates an instatnce using singletton pattern
    public static Panel getInstance() {
		if (panel == null) {
			synchronized (Panel.class) {
				panel = new Panel();
			}
		}
		return panel;
	}
	/*Setters-getters for the fields*/
	public static JMenuItem getHelpabout() {
		return helpabout;
	}
	public static void setHelpabout(JMenuItem helpabout) {
		Panel.helpabout = helpabout;
	}
	public static JMenuItem getExitServer() {
		return exitServer;
	}
	public static void setExitServer(JMenuItem exitServer) {
		Panel.exitServer = exitServer;
	}
	public static JMenuBar getMenubar() {
		return menubar;
	}
	public static void setMenubar(JMenuBar menubar) {
		Panel.menubar = menubar;
	}
	public static JMenu getFilemenu() {
		return filemenu;
	}
	public static void setFilemenu(JMenu filemenu) {
		Panel.filemenu = filemenu;
	}
	public static JMenu getHelpmenu() {
		return helpmenu;
	}
	public static void setHelpmenu(JMenu helpmenu) {
		Panel.helpmenu = helpmenu;
	}
	public static JPanel getCards() {
		return cards;
	}
	public static JPanel getCard1() {
		return card1;
	}
	public static JPanel getInputPanel() {
		return inputPanel;
	}
	public static JPanel getPatternPanel() {
		return patternPanel;
	}
	public static JPanel getIpPanel() {
		return ipPanel;
	}
	public static JPanel getStatsPanel() {
		return statsPanel;
	}
	public static JButton getButton1() {
		return button1;
	}
	public static JButton getSubmitIPButton() {
		return submitIPButton;
	}
	public static JButton getSubmitPatButton() {
		return submitPatButton;
	}
	public static JButton getPatbackButton() {
		return patbackButton;
	}
	public static JButton getIpbackButton() {
		return ipbackButton;
	}
	public static JButton getStatsBackButton() {
		return statsBackButton;
	}
	public static JButton getIpButton() {
		return ipButton;
	}
	public static JButton getPatButton() {
		return patButton;
	}
	public static JButton getStatsButton() {
		return statsButton;
	}
	public static JButton getRefreshButton() {
		return refreshButton;
	}
	public static Component getFrame1() {
		return frame1;
	}
	public static Component getFrame2() {
		return frame2;
	}
	public static Component getFrame3() {
		return frame3;
	}
	public static Component getFrame4() {
		return frame4;
	}
	public static Component getframe6() {
		return frame6;
	}
	public static void setframe6(Component frame6) {
		Panel.frame6 = frame6;
	}
	public static Component getFrame5() {
		return frame5;
	}
	public static void setFrame5(Component frame5) {
		Panel.frame5 = frame5;
	}
	public static JTextField getUsername() {
		return username;
	}
	public static JTextField getInputPat() {
		return inputPat;
	}
	public static JTextField getInputIP() {
		return inputIP;
	}	
	public static JPasswordField getPasswordField() {
		return passwordField;
	}
	public static JScrollPane getStatsscrollpanel() {
		return statsscrollpanel;
	}
	public static JList<String> getStatslist() {
		return statslist;
	}
	public static void setFrame4(Component frame4) {
		Panel.frame4 = frame4;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	// This method is used by the GridBagLayout manager to set the components for the given panel
	public void ManagePos(Component component, JPanel panel, GridBagConstraints constraints, GridBagLayout layout, int row, int col, int width, int height){
		constraints.gridx = col; 
		constraints.gridy = row;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		layout.setConstraints(component, constraints); 
		panel.add(component); // adds the component to the given panel
		
	}
	
	public void addComponentToPane(Container pane, JFrame frame) {
        // create panel using BorderLayout for each one
        card1 = new JPanel(new BorderLayout(6,6));
        inputPanel = new JPanel(new BorderLayout(6,6));
        patternPanel = new JPanel(new BorderLayout(6,6));
        ipPanel = new JPanel(new BorderLayout(6,6));
        statsPanel = new JPanel(new BorderLayout(8,8));
        
        // Set topic panel for the first card
        JPanel topPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout(); //set a GridBagLayout Manager for this panel
        topPanel.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints(); //set some constraints
        
      //creates a field for the administrators username
        username = new JTextField("<Enter Username>", 20);
        username.addMouseListener(ActionController.getInstance());
        
        //creates a field for the administrators password
        passwordField = new JPasswordField("<Enter Password>", 20);
        passwordField.addMouseListener(ActionController.getInstance());
        
        JLabel userLabel = new JLabel("Enter Username:"); 
        userLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JLabel passLabel = new JLabel("Enter Password:"); 
        passLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        //topic panel for the first button using a simple flowlayout manager
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        button1 = new JButton("Submit"); 
        centerPanel.add(button1);
        
        //adds action listeners for catching events in these two components
        button1.addActionListener(ActionController.getInstance());
        
        // manage the position of every component using some constraints
        constraints.fill = GridBagConstraints.BOTH;
        ManagePos(userLabel, topPanel,constraints,layout, 0, 0,1,1);
        
        constraints.fill = GridBagConstraints.BOTH;
        ManagePos(username,topPanel, constraints,layout, 0, 1,1,1);
        
        constraints.fill = GridBagConstraints.BOTH;
        ManagePos(passLabel,topPanel,constraints,layout, 1, 0,1,1);
        
        constraints.fill = GridBagConstraints.BOTH;
        ManagePos(passwordField,topPanel,constraints,layout, 1, 1,1,1);
        
        //add to the bigger panel(card1) the small ones
        card1.add(topPanel,BorderLayout.NORTH);
        card1.add(centerPanel,BorderLayout.CENTER);
        
        //adds action listeners for these two components
        username.addActionListener(ActionController.getInstance());
        passwordField.addActionListener(ActionController.getInstance());
             
        // create panel that contains the other panel(cards) using the CardLayout
        cards = new JPanel(new CardLayout());
        cards.add(card1, "Card 1");
        cards.add(inputPanel, "inputPanel");
        cards.add(patternPanel,"patternPanel");
        cards.add(ipPanel,"ipPanel");
        cards.add(statsPanel,"statsPanel");
        // adds the pane "cards" to the pane(frame)
        pane.add(cards, BorderLayout.CENTER);
        
    }

    
    public static void createAndShowGUI() {
        // create and setup window
        JFrame frame = new JFrame("Administrator Interface");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(ActionController.getInstance());
        Panel main = new Panel();
        
        //adds everything to the main panel and show it
        main.addComponentToPane(frame.getContentPane(), frame);

        // display window
        frame.setSize(800,600);
        
        //sets some attributes to the frame
        frame.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        
        // creates a menu bar
        menubar = new JMenuBar();							
        filemenu = new JMenu("File");
		exitServer= new JMenuItem("Exit Server");
		helpmenu = new JMenu("Help");
		helpabout = new JMenuItem("About...");
	
		//add action listener to some menu items
		exitServer.addActionListener(ActionController.getInstance());				
		helpabout.addActionListener(ActionController.getInstance());
		
		filemenu.add(exitServer);
		helpmenu.add(helpabout);
		menubar.add(filemenu);
		menubar.add(helpmenu);
		//sets the menu bar to the frame
		frame.setJMenuBar(menubar);
    }
    

    public void setStatsPanel(){
    	//creates the Panel to display the statistics
    	
    	JLabel statsLabel = new JLabel("Statistics Table");
    	statsLabel.setFont(statsLabel.getFont().deriveFont(Font.BOLD));
    	//creates a topic panel using flowlayout manager
    	JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	topPanel.add(statsLabel);
    	
    	//creates the statlist in order to display the statistics
    	statslist = new JList<String>();
    	statslist.removeAll();
    	
    	//creates a scrollpanel for adding scrollbars when needed
		statsscrollpanel = new JScrollPane(statslist);
		statsscrollpanel.setViewportView(statslist);
		
		refreshButton = new JButton("Refresh");
        statsBackButton = new JButton("Back");
        //creates a topic panel for adding the buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(statsBackButton);
        bottomPanel.add(refreshButton);
        //add everything to the statspanel
        statsPanel.add(topPanel, BorderLayout.NORTH);
        statsPanel.add(statsscrollpanel, BorderLayout.CENTER);
        statsPanel.add(bottomPanel, BorderLayout.AFTER_LAST_LINE);
 
        //add action listeners for the two buttons
        statsBackButton.addActionListener(ActionController.getInstance());
        refreshButton.addActionListener(ActionController.getInstance());
 	
    	
    }
    
    
    
    public void setInputPanel(){
    	//creates and displays the panel with the options for the administrator
    	//creates a topic panel with the GridBagLayout manager
    	JPanel topPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        topPanel.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
    	
    	JLabel ipLabel = new JLabel("Enter Malicious IP:");
    	JLabel patLabel = new JLabel("Enter Malicious Pattern:");
    	JLabel statsLabel = new JLabel("Show Statistics:");
        
    	//add some gap between the components
    	constraints.insets = new Insets(10,30,5,30);
        ipButton = new JButton("Enter IP");
        patButton = new JButton("Enter Pattern");
        statsButton = new JButton("Show Statistics");

        // manage the position of the components and set some constraints
        constraints.fill = GridBagConstraints.BOTH;
        ManagePos(ipLabel,topPanel,constraints,layout, 0, 1,1,1);
        
        ManagePos(patLabel,topPanel,constraints,layout, 1, 1,1,1);
        
        ManagePos(statsLabel,topPanel,constraints,layout, 2, 1,1,1);
        
        ManagePos(ipButton,topPanel,constraints,layout, 0, 2,1,1);
        
        ManagePos(patButton,topPanel,constraints,layout, 1, 2,1,1);
        
        ManagePos(statsButton,topPanel,constraints,layout, 2, 2,1,1);
        
        //add everything to the inputPanel
        inputPanel.add(topPanel, BorderLayout.NORTH);
        
        //add action listeners for these 3 buttons
        ipButton.addActionListener(ActionController.getInstance());
        patButton.addActionListener(ActionController.getInstance());
        statsButton.addActionListener(ActionController.getInstance());
    	
    }
    
    public void setMaliciousPatPanel(){
    	//creates and displays the panel for entering the malicious patterns
    	//creates a topic panel with the GridBagLayout manager
    	JPanel topPanel = new JPanel();
    	
    	//creates a topic panel using flowlayout manager
    	JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        GridBagLayout layout = new GridBagLayout();
        topPanel.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
    	
    	JLabel patInputLabel = new JLabel("Set New Malicious Pattern(Name):");
    	patInputLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    	inputPat = new JTextField(20);
    	
    	patbackButton = new JButton("Back");
    	submitPatButton = new JButton("Submit");
    	
    	// manage the position of the components and set some constraints
    	constraints.fill = GridBagConstraints.BOTH;
    	ManagePos(patInputLabel, topPanel,constraints,layout, 0, 1, 1,1);
    	
    	ManagePos(inputPat, topPanel,constraints,layout, 0, 2, 1,1);
    	
    	ManagePos(patbackButton, centerPanel,constraints,layout, 1, 1, 1,1);
    	
    	ManagePos(submitPatButton, centerPanel,constraints,layout, 1, 2, 1,1);
    	
    	//add everything to the patternPanel 
    	patternPanel.add(topPanel, BorderLayout.NORTH);
    	patternPanel.add(centerPanel, BorderLayout.CENTER);
    	
    	//add action listeners for these 2 buttons
    	submitPatButton.addActionListener(ActionController.getInstance());
    	patbackButton.addActionListener(ActionController.getInstance());
        
    }
    
    public void setMaliciousIpPanel(){
    	//creates and displays the panel for entering the malicious ips
    	//creates a topic panel with the GridBagLayout manager
    	JPanel topPanel = new JPanel();
    	
    	//creates a topic panel using flowlayout manager
    	JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        GridBagLayout layout = new GridBagLayout();
        topPanel.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
    	
        JLabel ipInputLabel = new JLabel("Set New Malicious  IP(Address):");
        ipInputLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    	inputIP = new JTextField(20);
    	
    	ipbackButton = new JButton("Back");
    	submitIPButton = new JButton("Submit");
    	
    	// manage the position of the components and set some constraints
    	constraints.fill = GridBagConstraints.BOTH;
    	ManagePos(ipInputLabel, topPanel,constraints,layout, 0, 1, 1,1);
    	
    	ManagePos(inputIP, topPanel,constraints,layout, 0, 2, 1,1);
    	
    	ManagePos(ipbackButton, centerPanel,constraints,layout, 1, 1, 1,1);
    	
    	ManagePos(submitIPButton, centerPanel,constraints,layout, 1, 2, 1,1);
    	
    	//add everything to the ipPanel 
    	ipPanel.add(topPanel, BorderLayout.NORTH);
    	ipPanel.add(centerPanel, BorderLayout.CENTER);
    	
    	//add action listeners for these 2 buttons
    	submitIPButton.addActionListener(ActionController.getInstance());
    	ipbackButton.addActionListener(ActionController.getInstance());
    
    }



	
	public void StartPanel(){
		//starts the gui
		
		//use the UIManager to set the look and feel because of the changing cards
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        // turn off metal's bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        // schedules job for the event dispatch thread creating and showing GUI 
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {   	
                createAndShowGUI();
	
            }
        });
    }

}