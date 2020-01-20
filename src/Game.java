import java.awt.Color;
import java.io.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* 
 * Names: David Wu and Shawn Hu
 * Purpose: To create game that fulfills the requirements of the project and demonstrates our learning in the course
 * Description: Bitcoin Tycoon, a idle game bitcoin mining simulator where your a broke computer scientist with an arduino nano and because you're such a genius you decide
 * 				to code a bitcoin miner onto your arduino nano, thereby allowing you to make money again. Ascend through the different devices you can purchase and achieve the
 * 				ultimate bitcoin mining setup!
 * How the game works:
 * 		The game class is initially called to start everything.
 * 		A thread (RNG) is continuously running during the game, this thread is a timer that generates a random double on the timer trigger.
 * 		Based on the value returned and the player chance, the player may receive some BTC of x value.
 * 		A second thread (statTracker) is continuously running and loops 30 times a second in order to continuously update the values posted on the labels
 * 		during gameplay.
 * 		Players can purchase upgrades for their device which will allow them to boost their mining period, chance, and value
 * 		Period
 * 		- The amount of time in between each timer trigger, can be improved by buying CPU upgrades
 * 		Chance
 * 		- The chance that a timer trigger will provide a BTC for the player, can be improved by buying RAM upgrades
 * 		Value
 * 		- The amount of BTC awarded on a successful timer trigger, can be improved by buying GPU upgrades
 * 
 * 		Special Item - Black Market:
 * 		- A special upgrade that has a 50/50 chance of a successful purchase.
 * 		- The effects of this upgrade are randomly generated are drastically better than the basic upgrades, however, black market upgrades will increase one attribute (see above)
 * 			and decrease another
 * 
 * 		Different markets
 * 		Upgrade market
 * 		- market for the three basic uprgaeds
 * 		Currency market
 * 		- market to make conversions between usd -> btc and btc -> usd
 * 		Device market
 * 		- market to allow the player to ascend to the next device
 * 		Black market
 * 		- market for the special item
 * 
 * If you wanna skip ahead and get lots of money, change the BTC field in save.ini to some large positive integer value. Please don't do negatives because the system is not designed
 * to accomodate human fiddling and will not take edge cases because it's not designed for those.
*/

public class Game {
	
	private Semaphore sem = new Semaphore(1);
	private ArrayList<Device> devices;
	private Thread rngThread, statThread;
	private ArrayList<Upgrade> upgradeList = new ArrayList<Upgrade>();
	private JLabel blackMarketPrice, CPUPrice, GPUPrice, RAMPrice, title, instructions, credits, statTitleLabel, deviceLabel, chanceLabel, periodLabel, valueLabel, GPULabel, CPULabel, RAMLabel, btcIcon, usdIcon, btcLabel, usdLabel, noMoreDevices, timer, currentDeviceLabel, devicePrice;
	private JButton backToMainButton, tutorialButton, tutorialBackButton, resetDataButton, statsButton, gameMenuButton, statBackButton, exitButton, convertUSD, convertBTC, upgradeMarketButton, currencyMarketButton, blackMarketButton, deviceMarketButton, buyDeviceButton;
	private JPanel mainMenu,gameMenu,tutorialPanel, statsPanel;

	public static int timerFrame;
	public final Dimension screenSize;
	public static Player player;
	public statTracker tracker = new statTracker(sem);
	public RNG rng = new RNG(sem);
	public JFrame frame;
	
	//Constructor
	public Game() {

		frame = new JFrame();	//init frame

		mainMenu = new JPanel();			//Init all the panels
		mainMenu.setLayout(null);
		mainMenu.setSize(1000,700);			//set all to same size

		gameMenu = new JPanel();
		gameMenu.setLayout(null);
		gameMenu.setSize(1000,700);
		
		tutorialPanel = new JPanel();
		tutorialPanel.setLayout(null);
		tutorialPanel.setSize(1000,700);
		
		statsPanel = new JPanel();
		statsPanel.setLayout(null);
		statsPanel.setSize(1000,700);
		
		//init array of all devices available
		devices = new ArrayList<Device>();
		devices.add(new Device("Arduino Nano", "resources/Nano.png", 0, 0, 20, 10, 0.01));
		devices.add(new Device("Arduino Uno", "resources/Uno.png", 600, 1, 25, 10, 0.10));
		devices.add(new Device("Casual Laptop", "resources/Casual.png", 1000, 2, 30, 8, 0.30));
		devices.add(new Device("Business Laptop", "resources/Business.png", 2001, 3, 35, 8, 0.50));
		devices.add(new Device("Gaming Laptop", "resources/Gaming.png", 5000, 4, 40, 6, 1.00));
		devices.add(new Device("Mini Desktop", "resources/Mini.png", 4000, 5, 45, 5, 1.50));
		devices.add(new Device("PC Desktop", "resources/Pc.png", 6000, 6, 50, 4, 2.00));
		devices.add(new Device("Gaming Desktop", "resources/GamingPC.png", 9999, 7, 55, 3, 2.20));
		devices.add(new Device("Mom's Basement", "resources/Basement.png", 100000, 8, 60, 2, 2.6));
		devices.add(new Device("Ubisoft Server", "resources/Ubisoft.png", 500000, 9, 65, 2, 3.33));
		devices.add(new Device("Server Farm", "resources/ServerFarm.png", 9999999, 10, 70, 1, 4.20));
		Collections.sort(devices, new deviceByTier());	//sort by tier

		Upgrade.setDefaultPrices();	//set default prices
		player = new Player();	//create player
		screenSize = new Dimension(1000,700);	//set screensize dimension

		//Button creation
		tutorialButton = new JButton("How To Play");	//Note: tutorial is not an actual tutorial, tutorial is the how to play menu
		tutorialButton.setBackground(Color.gray);
		tutorialButton.setBounds(425,400,150,50);
		tutorialButton.setActionCommand("tutorial");	//Navigate from main menu to how to play
		tutorialButton.addActionListener(new ButtonActions());

		backToMainButton = new JButton("Back");
		backToMainButton.setBackground (Color.gray);
		backToMainButton.setBounds(50,570,100,50);
		backToMainButton.setActionCommand("backToMain");	//Navigate from game play menu to main menu
		backToMainButton.addActionListener(new ButtonActions());

		statsButton = new JButton("Player Stats");
		statsButton.setBackground (Color.gray);
		statsButton.setBounds(175,570,150,50);
		statsButton.setActionCommand("displayStats");	//Navigate from game play meny to player stats menu
		statsButton.addActionListener(new ButtonActions());

		gameMenuButton = new JButton("Play");
		gameMenuButton.setBackground (Color.gray);
		gameMenuButton.setBounds(425,300,150,50);
		gameMenuButton.setActionCommand("playGame");	//Navigate from main menu to game play menu
		gameMenuButton.addActionListener(new ButtonActions());

		statBackButton = new JButton("Back");
		statBackButton.setBackground (Color.gray);
		statBackButton.setBounds(250,50,200,50);
		statBackButton.setActionCommand("stat-back");	//Navigate from player stats menu to game play menu
		statBackButton.addActionListener(new ButtonActions());

		tutorialBackButton = new JButton("Back");
		tutorialBackButton.setBackground (Color.gray);
		tutorialBackButton.setBounds(50,570,100,50);
		tutorialBackButton.setActionCommand("toMain");	//Navigate from how to play menu to main menu
		tutorialBackButton.addActionListener(new ButtonActions());
		
		resetDataButton =  new JButton("Reset Data");
		resetDataButton.setBackground (Color.gray);
		resetDataButton.setBounds (425,500,150,50);
		resetDataButton.setActionCommand("reset");	//Resets the player data back to the beginning
		resetDataButton.addActionListener(new ButtonActions());
		
		exitButton = new JButton("Exit");
		exitButton.setBackground (Color.gray);
		exitButton.setBounds(775,550,150,50);
		exitButton.setActionCommand("quit");	//Exits from the application
		exitButton.addActionListener(new ButtonActions());

		convertUSD = new JButton("Convert USD to BTC");
		convertUSD.setBackground(Color.gray);
		convertUSD.setBounds(650,250,200,50);
		convertUSD.setActionCommand("usdToBTC");	//Button that converts player's usd to btc
		convertUSD.addActionListener(new MarketButtons());
		convertUSD.setVisible(false);

		convertBTC = new JButton("Convert BTC to USD");
		convertBTC.setBackground(Color.gray);
		convertBTC.setBounds(650,400,200,50);
		convertBTC.setActionCommand("btcToUSD");	//Button that converts player's btc to usd
		convertBTC.addActionListener(new MarketButtons());
		convertBTC.setVisible(false);

		upgradeMarketButton = new JButton("Upgrades");
		upgradeMarketButton.setBackground(Color.gray);
		upgradeMarketButton.setBounds(400,400,100,50);
		upgradeMarketButton.setActionCommand("upgradeMarket");	//Button that displays the upgrade market
		upgradeMarketButton.addActionListener(new MarketButtons());
		
		currencyMarketButton = new JButton("Currency");
		currencyMarketButton.setBackground(Color.gray);
		currencyMarketButton.setBounds(400,330,100,50);
		currencyMarketButton.setActionCommand("currencyMarket");	//Button that displays the currency market
		currencyMarketButton.addActionListener(new MarketButtons());

		deviceMarketButton = new JButton("Devices");
		deviceMarketButton.setBackground(Color.gray);
		deviceMarketButton.setBounds(400,260,100,50);
		deviceMarketButton.setActionCommand("deviceMarket");	//Button that displays the device market
		deviceMarketButton.addActionListener(new MarketButtons());

		blackMarketButton = new JButton("");
		blackMarketButton.setBackground(Color.gray);
		blackMarketButton.setBounds(400,190,100,50);
		blackMarketButton.setActionCommand("blackMarket");	//Button that displays the black market
		blackMarketButton.addActionListener(new MarketButtons());

		buyDeviceButton = new JButton("Buy Next Device");
		buyDeviceButton.setBackground(Color.gray);
		buyDeviceButton.setBounds(650,500,200,50);
		buyDeviceButton.setActionCommand("nextDevice");		//Button that purchases the next device
		buyDeviceButton.addActionListener(new MarketButtons());
		buyDeviceButton.setVisible(false);

// 		Label Creation:

		btcIcon = new JLabel(new ImageIcon("resources/BTC.png"));
		btcIcon.setBounds(710,320,75,75);
		btcIcon.setVisible(false); 

		usdIcon = new JLabel(new ImageIcon("resources/USD.png")); 
		usdIcon.setBounds(710,170,75,75);
		usdIcon.setVisible(false);

		btcLabel = new JLabel();
		btcLabel.setBounds(50,50,200,50);
		btcLabel.setBackground(Color.lightGray);				//Note: any JLabels that have empty or seemingly useless content are going to be assigned text/images on runtime
		
		usdLabel = new JLabel();
		usdLabel.setBounds(250,50,200,50);
		usdLabel.setBackground(Color.lightGray);

		currentDeviceLabel = new JLabel();
		currentDeviceLabel.setBounds(400,50,300,50);

		devicePrice = new JLabel("", SwingConstants.CENTER);
		devicePrice.setBounds(650,440,200,50);
		devicePrice.setFont(new Font("Serif", Font.PLAIN, 16));
		devicePrice.setVisible(false);

		CPUPrice = new JLabel("", SwingConstants.LEFT);
		CPUPrice.setBounds(650,200,200,50);
		CPUPrice.setFont(new Font("Serif", Font.PLAIN, 16));
		CPUPrice.setVisible(false);

		GPUPrice = new JLabel("", SwingConstants.LEFT);
		GPUPrice.setBounds(650,400,200,50);
		GPUPrice.setFont(new Font("Serif", Font.PLAIN, 16));
		GPUPrice.setVisible(false);

		RAMPrice = new JLabel("", SwingConstants.LEFT);
		RAMPrice.setBounds(650,300,200,50);
		RAMPrice.setFont(new Font("Serif", Font.PLAIN, 16));
		RAMPrice.setVisible(false);

		blackMarketPrice = new JLabel("", SwingConstants.LEFT);
		blackMarketPrice.setBounds(650,200,200,50);
		blackMarketPrice.setFont(new Font("Serif", Font.PLAIN, 16));
		blackMarketPrice.setVisible(false);

		title = new JLabel("Bitcoin Tycoon", SwingConstants.CENTER);
		title.setBounds(0,75,1000,200);
		title.setFont(new Font("Serif", Font.PLAIN, 48));

		noMoreDevices = new JLabel("<html>No More Devices to Buy</html>", SwingConstants.CENTER);	//HTML formatting to make it look nicer
		noMoreDevices.setBounds(300,300,200,200);
		noMoreDevices.setFont(new Font("Serif", Font.BOLD, 20));
		noMoreDevices.setVisible(false);

		instructions = new JLabel("<html>You are a poor computer whiz in need of some quick cash. Luckily, your Arduino Nano is capable of mining Bitcoin. Upgrade your device by buying more RAM, CPU, and GPU. When the market gets too expensive, buy another device and start over. Soon, you will be able to turn your mom's basement into a money making machine. And if you have enough money, you may be able to attract a mysterious company. Remember, any upgrades you buy are non-refundable.</html>", SwingConstants.LEFT);
		instructions.setBounds(50,50,600,600);
		instructions.setFont(new Font("Serif", Font.PLAIN, 24));

		credits = new JLabel("<html> Created by Shawn and David </html>", SwingConstants.LEFT);
		credits.setBounds(500,500,100,100);
		credits.setFont(new Font("Serif", Font.PLAIN, 14));

		statTitleLabel = new JLabel("Stats:", SwingConstants.LEFT);
		statTitleLabel.setBounds(100,100,500,25);
		statTitleLabel.setFont(new Font("Serif", Font.BOLD, 14));

		deviceLabel = new JLabel();
		deviceLabel.setBounds(100,200,500,25);
		deviceLabel.setFont(new Font("Serif", Font.BOLD, 14));
		
		chanceLabel = new JLabel();
		chanceLabel.setBounds(100,225,500,25);
		deviceLabel.setFont(new Font("Serif", Font.BOLD, 14));
		
		valueLabel = new JLabel();
		valueLabel.setBounds(100,250,500,25);
		valueLabel.setFont(new Font("Serif", Font.BOLD, 14));

		periodLabel = new JLabel();
		periodLabel.setBounds(100,275,500,25);
		periodLabel.setFont(new Font("Serif", Font.BOLD, 14));

		CPULabel = new JLabel("", SwingConstants.LEFT);
		CPULabel.setBounds(100,325,600,25);
		CPULabel.setFont(new Font("Serif", Font.BOLD, 14));

		GPULabel = new JLabel("", SwingConstants.LEFT);
		GPULabel.setBounds(100,350,600,25);
		GPULabel.setFont(new Font("Serif", Font.BOLD, 14));
		
		RAMLabel = new JLabel("", SwingConstants.LEFT); 
		RAMLabel.setBounds(100,375,600,25);
		RAMLabel.setFont(new Font("Serif", Font.BOLD, 14));

		timer = new JLabel();
		timer.setBounds(150,440,112,112);
		
		mainMenu.add(title); // adds the buttons to the correct JPanels
		mainMenu.add(gameMenuButton);
		mainMenu.add(exitButton);
		mainMenu.add(tutorialButton);
		mainMenu.add(resetDataButton);

		gameMenu.add(backToMainButton); 
		for(Device i : devices) {
			i.image.setVisible(false);
			i.image2.setVisible(false);
			gameMenu.add(i.image);
			gameMenu.add(i.image2);
		}
		gameMenu.add(currentDeviceLabel);
		gameMenu.add(statsButton);
		gameMenu.add(convertUSD);
		gameMenu.add(convertBTC);
		gameMenu.add(upgradeMarketButton);
		gameMenu.add(currencyMarketButton);
		gameMenu.add(deviceMarketButton);
		gameMenu.add(blackMarketButton);
		gameMenu.add(btcIcon);
		gameMenu.add(usdIcon);
		gameMenu.add(btcLabel);
		gameMenu.add(usdLabel);
		gameMenu.add(noMoreDevices);
		gameMenu.add(buyDeviceButton);
		gameMenu.add(timer);
		gameMenu.add(blackMarketPrice);
		gameMenu.add(CPUPrice);
		gameMenu.add(GPUPrice);
		gameMenu.add(RAMPrice);
		gameMenu.add(devicePrice);

		tutorialPanel.add(instructions);
		tutorialPanel.add(tutorialBackButton);
		tutorialPanel.add(credits);

		statsPanel.add(statTitleLabel);
		statsPanel.add(statBackButton);
		statsPanel.add(deviceLabel);
		statsPanel.add(chanceLabel);
		statsPanel.add(periodLabel);
		statsPanel.add(valueLabel);
		statsPanel.add(GPULabel);
		statsPanel.add(CPULabel);
		statsPanel.add(RAMLabel);

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Asks the user if the want to confirm when they press exit buttons
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent windowEvent) {
		    	if (JOptionPane.showConfirmDialog(frame,"Are you sure you want to close the game?","Close Game?",0,3) == JOptionPane.YES_OPTION) {
					System.out.println("yes");
					saveGame();
					stop();
					System.exit(0);
				}
		    }
		});

		frame.setSize((int)screenSize.getWidth(),(int)screenSize.getHeight()); // setting up the JFrame
		frame.setPreferredSize(screenSize);	//set pereferred size so that setLayout null will work properly, otherwise it'll do some wonky stuff and bring it down to the smallest size possible
		frame.setResizable(false);	//Not allowed to resize :P
		frame.setTitle("Bitcoin Tycoon");
		frame.setBackground(Color.WHITE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		gameMenu.setVisible(false);	//Set mainMenu panel visible first so that the main menu will display first
		mainMenu.setVisible(true);
		tutorialPanel.setVisible(false);
		statsPanel.setVisible(false);

		frame.add(mainMenu); // add the JPanels
		frame.add(gameMenu);
		frame.add(tutorialPanel);
		frame.add(statsPanel);

		statThread = new Thread(tracker); // start threads
		rngThread = new Thread(rng);
		readSaveFile(); // get info from the save file
		if(player.blackMarketStatus()) {	//Set the black market label in case the black market is already unlocked
			blackMarketButton.setText("???");
		} else {
			blackMarketButton.setText("Locked");
		}
		for(Upgrade i : upgradeList) {	//adding the images from upgrades into the menu, this is done after readSaveFile because readSaveFile is where the Upgrade classes are created
			gameMenu.add(i.image);
		}
	}

	// This method displays the upgrades tab and the associated information regarding the purchase of upgrades
	// Returns nothing,
	// No parameters
	public void displayUpgradeMarket() {
		for(Upgrade i : upgradeList) {
			if(!i.blackMarketItem && player.getDevice().availableUpgrades.contains(i)) {	//Loops through the upgrades and makes comparisons to check which labels to display
				i.image.setVisible(true);
				if(i.type.equalsIgnoreCase("ProcessorUpgrade")) {
					CPUPrice.setVisible(true);
				} else if(i.type.equalsIgnoreCase("GraphicsUpgrade")) {
					GPUPrice.setVisible(true);
				} else if(i.type.equalsIgnoreCase("RAMUpgrade")) {
					RAMPrice.setVisible(true);
				} else if(i.type.equalsIgnoreCase("BlackMarket")) {
					blackMarketPrice.setVisible(false);
				}
			} else {
				i.image.setVisible(false);
			}
		}
		btcIcon.setVisible(false);		//Sets all the other labels and buttons from other markets to invisible
		usdIcon.setVisible(false);
		if(player.getDevice().getTier()+1 >= devices.size()) {
			noMoreDevices.setVisible(false);
			buyDeviceButton.setVisible(false);
		} else {
			devices.get(devices.indexOf(player.getDevice())+1).image2.setVisible(false);
			buyDeviceButton.setVisible(false);
		}
		convertUSD.setVisible(false);
		convertBTC.setVisible(false);
		devicePrice.setVisible(false);
	}
	// This method displays the currency tab and the associated information regarding the exchange of money.
	// Returns nothing,
	// No parameters
	public void displayCurrencyMarket() {
		btcIcon.setVisible(true);
		usdIcon.setVisible(true);
		convertUSD.setVisible(true);
		convertBTC.setVisible(true);
		for(Upgrade i : upgradeList) {
			i.image.setVisible(false);
		}
		if(player.getDevice().getTier()+1 >= devices.size()) {
			noMoreDevices.setVisible(false);
			buyDeviceButton.setVisible(false);
		} else {
			devices.get(devices.indexOf(player.getDevice())+1).image2.setVisible(false);
			buyDeviceButton.setVisible(false);
		}
		CPUPrice.setVisible(false);
		GPUPrice.setVisible(false);
		RAMPrice.setVisible(false);
		blackMarketPrice.setVisible(false);
		devicePrice.setVisible(false);
	}
	// This method displays the black market tab and the associated information regarding the black market upgrades. 
	// Returns nothing,
	// No parameters
	public void displayBlackMarket() {
		if(player.blackMarketStatus()) {
			for(Upgrade i : upgradeList) {
				if(i.blackMarketItem && player.getDevice().availableUpgrades.contains(i)) {
					i.image.setVisible(true);
				} else {
					i.image.setVisible(false);
				}
			}
			btcIcon.setVisible(false);
			usdIcon.setVisible(false);
			if(player.getDevice().getTier()+1 >= devices.size()) {
				noMoreDevices.setVisible(false);
				buyDeviceButton.setVisible(false);
			} else {
				devices.get(devices.indexOf(player.getDevice())+1).image2.setVisible(false);
				buyDeviceButton.setVisible(false);
			}
			convertUSD.setVisible(false);
			convertBTC.setVisible(false);
			CPUPrice.setVisible(false);
			GPUPrice.setVisible(false);
			RAMPrice.setVisible(false);
			blackMarketPrice.setVisible(false);
			devicePrice.setVisible(false);
		}
	}
	// This method displays the devices tab and the associated information regarding the purchase of devices
	// Returns nothing,
	// No parameters
	public void displayDeviceMarket() {
		for(Upgrade i : upgradeList) {
			i.image.setVisible(false);
		}
		btcIcon.setVisible(false);
		usdIcon.setVisible(false);
		if(player.getDevice().getTier()+1 >= devices.size()) {
			devicePrice.setText("");
			noMoreDevices.setVisible(true);
			buyDeviceButton.setVisible(false);
		} else {
			devicePrice.setText(String.format("Price: $%.2f USD", devices.get(devices.indexOf(player.getDevice())+1).price));
			devices.get(devices.indexOf(player.getDevice())+1).image2.setVisible(true);
			buyDeviceButton.setVisible(true);
		}
		devicePrice.setVisible(true);
		convertUSD.setVisible(false);
		convertBTC.setVisible(false);
		CPUPrice.setVisible(false);
		GPUPrice.setVisible(false);
		RAMPrice.setVisible(false);
		blackMarketPrice.setVisible(false);
	}
	// This method displays the main menu.
	// Returns nothing,
	// No parameters
	public void displayMainMenu() {
		mainMenu.setVisible(true);
		gameMenu.setVisible(false);
		tutorialPanel.setVisible(false);
		statsPanel.setVisible(false);
		player.getDevice().image.setVisible(false);
		stop();
	}
	// This method displays the game screen
	// Returns nothing,
	// No parameters
	public void displayGameMenu() {
		gameMenu.setVisible(true);
		mainMenu.setVisible(false);
		tutorialPanel.setVisible(false);
		statsPanel.setVisible(false);
		player.getDevice().image.setVisible(true);
		start();
	}
	// This method displays the "how to play screen"
	// Returns nothing,
	// No parameters
	public void displayTutorial() {
		tutorialPanel.setVisible(true);
		gameMenu.setVisible(false);
		mainMenu.setVisible(false);
		statsPanel.setVisible(false);
	}
	// This method allows the player to go back to main men from a different screen
	// Returns nothing,
	// No parameters
	public void backToMain() {
		mainMenu.setVisible(true);
		gameMenu.setVisible(false);
		tutorialPanel.setVisible(false);
		statsPanel.setVisible(false);
	}
	// Rest of the methods essentially do the same thing but just displays different screens
	public void displayPlayerStats() {
		mainMenu.setVisible(false);
		gameMenu.setVisible(false);
		tutorialPanel.setVisible(false);
		statsPanel.setVisible(true);
	}
	public void backToGame() {
		mainMenu.setVisible(false);
		gameMenu.setVisible(true);
		tutorialPanel.setVisible(false);
		statsPanel.setVisible(false);
	}
	// This method starts both threads at the same time. 
	public synchronized void start() {
		statThread = new Thread(tracker);	//New threads are created everytime the play game button is pressed
		rngThread = new Thread(rng);
		rng.running = true;
		tracker.running = true;
		statThread.start();
		rngThread.start();
	}
	// This method stops both threads at the same time. 
	public synchronized void stop() {
		rng.running = false;		//Upon exiting to main menu or quitting the application, it'll join the threads
		tracker.running = false;
		try {
			statThread.join();
			rngThread.join();
		} catch(InterruptedException e) {
			System.out.print("Error 10: Unable to join threads");
			System.exit(0);
		}
		System.gc();		//garbage collector is run to recycle memory
	}
	// This method reads in the save file and gets information from it.
	public void readSaveFile() {
		try {
			Scanner scan = new Scanner(new File("upgrades.ini"));
			parseUpgradeFile(scan);	//parses upgrade file
			scan.close();
			Scanner cin = new Scanner(new File("presets.ini"));
			parsePresetFile(cin);	//parses presets file
			cin.close();
			cin = new Scanner(System.in);
			File saveFile = new File("save.ini");
			parseSaveFile(cin, saveFile);	//parses savefile
			cin.close();
		} catch(IOException e) {
			System.out.println("Error 7: Read/write error");
			System.exit(0);
		}
	}
	// This parses the info from the Upgrade file into the right type to be used. 
	public void parseUpgradeFile(Scanner scan) {
		while(scan.hasNextLine()) {
			String input = scan.nextLine();
			StringTokenizer splitter = new StringTokenizer(input, "=");
			Upgrade u;
			if(splitter.countTokens() > 1) {	//if the line actually contains information
				String[] effect = new String[3];
				String temp = splitter.nextToken();	//grabs the next token
				if(temp.equalsIgnoreCase("CPU")) {	//if the selected field is CPU
					effect[0] = "period";	//Set the effect
					effect[1] = "multiply";
					effect[2] = "5";
					u = new Upgrade("CPU Upgrade", "ProcessorUpgrade", Upgrade.USD, false, effect, new StringTokenizer(splitter.nextToken(),","));	//Create object
					u.image.setBounds(550,200,75,75);	//Set button location
					u.image.setActionCommand("buyUpgrade-CPU");	//Set command
					u.image.addActionListener(new MarketButtons());	//Add listener
					upgradeList.add(u);	//Add it to the list
				} else if(temp.equalsIgnoreCase("GPU")) {	//If the selected field is GPU
					effect[0] = "value";	//Set the effect
					effect[1] = "multiply";
					effect[2] = "3";
					u = new Upgrade("Graphics Card Upgrade", "GraphicsUpgrade", Upgrade.USD, false, effect, new StringTokenizer(splitter.nextToken(),","));	//Create object
					u.image.setBounds(550,400,75,75);	//Set button location
					u.image.setActionCommand("buyUpgrade-GPU");	//Set command
					u.image.addActionListener(new MarketButtons());	//Add listener
					upgradeList.add(u);	//Add it to the list
				} else if(temp.equalsIgnoreCase("RAM")) {	//If the selected field is RAM
					effect[0] = "chance";	//Set the effect
					effect[1] = "multiply";
					effect[2] = "2";
					u = new Upgrade("Memory Upgrade", "RAMUpgrade", Upgrade.USD, false, effect, new StringTokenizer(splitter.nextToken(),","));	//create object
					u.image.setBounds(550,300,75,75);	//Set button location
					u.image.setActionCommand("buyUpgrade-RAM");	//Set command
					u.image.addActionListener(new MarketButtons());	//Add listener
					upgradeList.add(u);	//Add it to the list
				} else if(temp.equalsIgnoreCase("BlackMarket")) {	//If the selected field is Black Market
					u = new BlackMarketItem("Black Market Item", effect, new StringTokenizer(splitter.nextToken(),","));	//create object
					u.image.setBounds(550,300,75,75);	//Set button location
					u.image.setActionCommand("buyUpgrade-BlackMarket");	//Set command
					u.image.addActionListener(new MarketButtons());	//Add listener
					upgradeList.add(u);	//Add it to the list
				}
			}
		}
		Collections.sort(upgradeList, new upgradeByType());	//Sort by type so binary search can be used
	}
	// This method parses the Save file info into the right types for processing.
	public void parseSaveFile(Scanner cin, File saveFile) {
		try {
			if(!saveFile.createNewFile()) {	//If the file already exists then parse it
				try {
					cin = new Scanner(new File("save.ini"));
				} catch (FileNotFoundException e) {
					System.out.println("Error 12: save.ini not found");
					System.exit(0);
				}
				while(cin.hasNextLine()) {
					String input = cin.nextLine();
					StringTokenizer splitter = new StringTokenizer(input,"=");
					String temp = splitter.nextToken();
					if(temp.equalsIgnoreCase("Device")) {	//Device field
						temp = splitter.nextToken();
						if(temp.equalsIgnoreCase("None")) {}
						else {
							player.setDevice(devices.get(Collections.binarySearch(devices, new Device(Integer.parseInt(temp)), new deviceByTier())));
						}
					} else if(temp.equalsIgnoreCase("Upgrades")) {	//Upgrades field
						temp = splitter.nextToken();
						if(temp.equalsIgnoreCase("None")) {
						} else {
							splitter = new StringTokenizer(temp,",");
							while(splitter.hasMoreTokens())
								player.getDevice().addUpgrade(upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade(splitter.nextToken()), new upgradeByType())));
						}
					}
					else if(temp.equalsIgnoreCase("USD"))		player.setUSD(Double.parseDouble(splitter.nextToken()));	//CPU field
					else if(temp.equalsIgnoreCase("BTC"))		player.setUpBTC(Double.parseDouble(splitter.nextToken()));	//BTC field
					else if(temp.equalsIgnoreCase("BlackMarket")) {		//Black Market field
						if(Boolean.parseBoolean(splitter.nextToken())) player.unlockBlackMarket();	//Doing this because i don't have a setted for blackMarketStatus in the Player class
						else player.lockBlackMarket();
					}
					else if(temp.equalsIgnoreCase("chance"))	Player.chance = Double.parseDouble(splitter.nextToken());	//chance field
					else if(temp.equalsIgnoreCase("period"))	Player.period = Double.parseDouble(splitter.nextToken());	//period field
					else if(temp.equalsIgnoreCase("value"))		Player.value = Double.parseDouble(splitter.nextToken());	//value field
					else if(temp.equalsIgnoreCase("LifeTimeBTC")) Player.totalBTC = Double.parseDouble(splitter.nextToken());	//LifeTimeBTC (used to determine if black market is unlocked or not)
				}
			} else {	//File does not exist so create a new one and write the default values into it
				BufferedWriter b = new BufferedWriter(new FileWriter("save.ini"));
				b.write("Device=0\n");
				b.write("Upgrades=None\n");
				b.write("USD=0\n");
				b.write("BTC=0.5\n");
				b.write("BlackMarket=false\n");
				b.write("chance=20\n");
				b.write("period=10\n");
				b.write("value=0.01\n");
				b.write("LifeTimeBTC=0.5\n");
				b.close();
				player.setDevice(devices.get(Collections.binarySearch(devices, new Device(0), new deviceByTier())));
				player.setUSD(0);
				player.setUpBTC(0.5);
				Player.chance = 20;
				Player.period = 10;
				Player.value= 0.01;
				Player.totalBTC = 0.5;
				player.lockBlackMarket();
			}
		} catch (NumberFormatException | IOException e) {
			System.out.println("Error 7: Read/write error - save.ini");
			System.exit(0);
		}
	}
	//This method parses the presets.ini file for information regarding which upgrades are available to which devices
	public void parsePresetFile(Scanner scan) {
		String input;
		StringTokenizer splitter;
		for(Device i : devices) {
			input = scan.nextLine();	//Reads in the input
			splitter = new StringTokenizer(input,"=");	//Creates splitter
			input = splitter.nextToken();	//Grabs the first token
			splitter = new StringTokenizer(splitter.nextToken(),",");	//Then makes a new splitter on the second token
			while(splitter.hasMoreTokens()) {	//Goes through the second splitter and adds the upgrades
				i.addAvailableUpgrade(upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade(splitter.nextToken()), new upgradeByType())));
			}
		}
	}
	//This method is called when the player wishes to properly exit, it will write to the save.ini and upgrades.ini files
	public void saveGame() {
		writeToSaveFile();
		writeToUpgradeFile();
	}
	//This method writes everything to the save file
	public void writeToSaveFile() {
		try {
			BufferedWriter cout = new BufferedWriter(new FileWriter("save.ini"));
			cout.write(String.format("Device=%d%n",player.getDevice().getTier()));	//Writes the tier in order to better smooth the read/write operation and parsing data (i.e. i got lazy)
			String temp = "";
			if(player.getDevice().getUpgrades().isEmpty()) {	//If no upgrades are purchased, then write none, otherwise write which ones have been purchased
				cout.write("Upgrades=None\n");
			} else {
				for(Upgrade i : player.getDevice().getUpgrades()) {
					temp += i.type + ",";
				}
				temp = temp.substring(0,temp.length()-1);
				cout.write(String.format("Upgrades=%s%n", temp));
			}
			cout.write(String.format("USD=%f%n", player.getUSD()));						//Write the # of USD gained
			cout.write(String.format("BTC=%f%n", player.getBTC()));						//Write the # of BTC
			cout.write(String.format("BlackMarket=%b%n", player.blackMarketStatus()));	//e.t.c.
			cout.write(String.format("chance=%f%n", Player.chance));
			cout.write(String.format("period=%f%n", Player.period));
			cout.write(String.format("value=%f%n", Player.value));
			cout.write(String.format("LifeTimeBTC=%f%n", Player.totalBTC));
			cout.close();
		} catch (IOException e) {
			System.out.println("Error 7: error writing to save.ini");
			System.exit(0);
		}
	}
	//This method writes to the upgrades.ini file
	public void writeToUpgradeFile() {
		try {
			BufferedWriter cout = new BufferedWriter(new FileWriter("upgrades.ini"));
			Upgrade temp;
			cout.write("[Normal Upgrades]\n");	//Formatting thing
			temp = upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade("ProcessorUpgrade"), new upgradeByType()));	//Writes some basic info about the upgrade as it is right now
			cout.write(String.format("CPU=%f,%d,resources/CPU.png%n", temp.price, temp.numOf));	//Writes the current price, numOf, and then the imageLoc
			temp = upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade("GraphicsUpgrade"), new upgradeByType()));
			cout.write(String.format("GPU=%f,%d,resources/GPU.png%n", temp.price, temp.numOf));
			temp = upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade("RAMUpgrade"), new upgradeByType()));
			cout.write(String.format("RAM=%f,%d,resources/RAM.png%n", temp.price, temp.numOf));
			cout.write("[Black Market Upgrades]\n");	//Formatting thing
			temp = upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade("BlackMarket"), new upgradeByType()));
			cout.write(String.format("BlackMarket=%f,%d,resources/BlackMarket.png%n", temp.price, temp.numOf));
			cout.close();
		} catch(IOException e) {
			System.out.println("Error 13: error writing to upgrades.ini");
			System.exit(0);
		}
	}
	//This class is for navigating screens
	class ButtonActions implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			if(action.equalsIgnoreCase("playGame")) {
				displayGameMenu();
			} else if(action.equalsIgnoreCase("backToMain")) {
				displayMainMenu();
			} else if(action.equalsIgnoreCase("quit")) {
				if (JOptionPane.showConfirmDialog(frame,"Are you sure you want to close the game?","Close Game?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					saveGame();
					stop();
					System.exit(0);
				}
			} else if(action.equalsIgnoreCase("displayStats")) {
				displayPlayerStats();
			} else if(action.equalsIgnoreCase("tutorial")) {
				displayTutorial();
			} else if(action.equalsIgnoreCase("toMain")) {
				backToMain();
			} else if(action.equalsIgnoreCase("stat-back")) {
				backToGame();
			} else if(action.equalsIgnoreCase("reset")) {
				if(JOptionPane.showConfirmDialog(frame,"Are you sure you want to reset your data?","Reset?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					player.reset();
					for(Upgrade i : upgradeList) {
						i.resetPrices();
						i.numOf = 0;
					}
					player.setDevice(devices.get(0));
				}
			}
		}
	}
	//This class is used for navigating the different markets as well as their associated actions/events
	class MarketButtons implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			String action = a.getActionCommand();
			if(action.equalsIgnoreCase("upgradeMarket")) {
				displayUpgradeMarket();
			} else if(action.equalsIgnoreCase("currencyMarket")) {
				displayCurrencyMarket();
			} else if(action.equalsIgnoreCase("deviceMarket")) {
				displayDeviceMarket();
			} else if(action.equalsIgnoreCase("blackMarket")) {
				displayBlackMarket();
			} else if(action.equalsIgnoreCase("usdToBTC")) {	//Performs the usdToBTC conversion
				boolean correct = false;
				do {
					String input = JOptionPane.showInputDialog(gameMenu, "How much USD do you want to convert?");
					if(input == null) return;	//no input and the person has directly hit cancel
					try {
						double usdToConvert = Double.parseDouble(input);	//tries to convert number, will produce exception if invalid
						if(usdToConvert > player.getUSD()) throw new NumberFormatException();	//throws exception for easy handling if the value exceeds the persons current USD
						player.setUSD(player.getUSD()-usdToConvert);	//Sets the new amount of USD
						player.addBTC(usdToConvert/Player.conversionRate);	//Converts to btc
						correct = true;	//exit loop flag
					} catch(NumberFormatException e) {
						JOptionPane.showMessageDialog(gameMenu, "Not a valid number", "Error", JOptionPane.WARNING_MESSAGE);
					}
					if(correct) break;
				} while(true);	//safe loop to prevent invalid numbers
			} else if(action.equalsIgnoreCase("btcToUSD")) {	//Performs the btcToUSD conversion
				boolean correct = false;
				do {
					String input = JOptionPane.showInputDialog(gameMenu, "How much BTC do you want to convert?");
					if(input == null) return;	//no input and the person has directly hit cancel
					try {
						double btcToConvert = Double.parseDouble(input);	//tries to convert number, will produce exception if invalid
						if(btcToConvert > player.getBTC()) throw new NumberFormatException();	//throws exception for easy handling if the value exceeds the persons current BTC
						player.setBTC(player.getBTC()-btcToConvert);	//Sets the new amount of BTC
						player.addUSD(btcToConvert*Player.conversionRate);	//converts to USD
						correct = true;	//exit loop flag
					} catch(NumberFormatException e) {
						JOptionPane.showMessageDialog(gameMenu, "Not a valid number", "Error", JOptionPane.WARNING_MESSAGE);
					}
					if(correct) break;
				} while(true);	//safe loop to prevent invalid values
			} else if(action.equalsIgnoreCase("buyUpgrade-CPU")) {
				for(Upgrade i : upgradeList) {
					if(i.type.equalsIgnoreCase("ProcessorUpgrade")) {
						if(i.buy(player)) {
							JOptionPane.showMessageDialog(gameMenu, "Success", "Purchase", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(gameMenu, "Insufficient Funds", "Error", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			} else if(action.equalsIgnoreCase("buyUpgrade-GPU")) {
				for(Upgrade i : upgradeList) {
					if(i.type.equalsIgnoreCase("GraphicsUpgrade")) {
						if(i.buy(player)) {
							JOptionPane.showMessageDialog(gameMenu, "Success", "Purchase", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(gameMenu, "Insufficient Funds", "Error", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			} else if(action.equalsIgnoreCase("buyUpgrade-RAM")) {
				for(Upgrade i : upgradeList) {
					if(i.type.equalsIgnoreCase("RAMUpgrade")) {
						if(i.buy(player)) {
							JOptionPane.showMessageDialog(gameMenu, "Success", "Purchase", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(gameMenu, "Insufficient Funds", "Error", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			} else if(action.equalsIgnoreCase("buyUpgrade-BlackMarket")) {
				for(Upgrade i : upgradeList) {
					if(i.type.equalsIgnoreCase("BlackMarket")) {
						if(i.buy(player)) {
							JOptionPane.showMessageDialog(gameMenu, "Success", "Purchase", JOptionPane.INFORMATION_MESSAGE);
						} else if(player.getBTC() > i.price) {
							JOptionPane.showMessageDialog(gameMenu, "Failure", "Purchase", JOptionPane.WARNING_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(gameMenu, "Insufficient Funds", "Error", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			} else if(action.equalsIgnoreCase("nextDevice")) {
				if(devices.get(devices.indexOf(player.getDevice())+1).buy(player)) {	//if the device is successfully bought then the device is switched to the next one
					JOptionPane.showMessageDialog(gameMenu, "Success", "Purchase", JOptionPane.INFORMATION_MESSAGE);	//display success message
					devices.get(devices.indexOf(player.getDevice())-1).image.setVisible(false);	//set the previous device's image to invisible
					devices.get(devices.indexOf(player.getDevice())).image2.setVisible(false);	//set the second image for the current one to invisible
					devices.get(devices.indexOf(player.getDevice())).image.setVisible(true);	//set the primary image for the current one to visible
					for(Upgrade i : upgradeList) {	//reset the prices and numOf
						i.resetPrices();
						i.numOf = 0;
					}
					Player.chance = player.getDevice().getBaseChance();	//Reset stats to the new base stats
					Player.period = player.getDevice().getBasePeriod();
					Player.value = player.getDevice().getBaseValue();
					displayUpgradeMarket();	//switch the market
				} else {
					JOptionPane.showMessageDialog(gameMenu, "Insufficient Funds", "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
	//This class is a thread dedicated to updating the content on the screen during the game
	class statTracker implements Runnable {
		private Semaphore sem;	//Semaphore to prevent concurrent modification error
		public boolean running = false;
		public statTracker(Semaphore s) {
			sem = s;
		}
		//This method is the method that is called when thread.start is called
		public void run() {
			long lastTime = System.nanoTime();
			final double ns = 1000000000.0 / 30.0;	//30 times per second update
			double delta = 0;
			boolean prevStatus = false;	//previous status flag to dedicate rising edge, transition from blackmarket locked to blackmarket unlocked
			while(running) {
				long now = System.nanoTime();
				delta = delta + ((now-lastTime) / ns);
				lastTime = now;
				while (delta >= 1)	//Make sure update is only happening 30 times a second
				{
					try {
						sem.acquire();	//acquires the permit
					} catch (InterruptedException e) {
						System.out.println("Error 14: Unable to acquire semaphore permit");
						System.exit(0);
					}
					updateElements();	//updates some ui elements
					if(!prevStatus && Player.totalBTC > 10) {	//checks for rising edge as well as player's lifetime btc, then unlocks the black market
						player.unlockBlackMarket();
						blackMarketButton.setText("???");
					}
					prevStatus = player.blackMarketStatus();	//sets the previous black market status to dedicate changes from previous frame
					currentDeviceLabel.setText("Current Device: " + player.getDevice().name);	//Sets the device label
					timer.setIcon(new ImageIcon("resources/timer" + timerFrame + ".png"));	//Sets the timer image
					for(Upgrade i : upgradeList) {	//Sets the upgrade labels
						if(i.type.equalsIgnoreCase("ProcessorUpgrade")) {
							CPUPrice.setText(String.format("Price: $%.2f USD", i.price));
							CPULabel.setText(String.format("CPU Upgrades: %d", i.numOf));
						} else if(i.type.equalsIgnoreCase("GraphicsUpgrade")) {
							GPUPrice.setText(String.format("Price: $%.2f USD", i.price));
							GPULabel.setText(String.format("GPU Upgrades: %d", i.numOf));
						} else if(i.type.equalsIgnoreCase("RAMUpgrade")) {
							RAMPrice.setText(String.format("Price: $%.2f USD", i.price));
							RAMLabel.setText(String.format("RAM Upgrades: %d", i.numOf));
						} else if(i.type.equalsIgnoreCase("BlackMarket")) {
							blackMarketPrice.setText(String.format("Price: %f BTC", i.price));
						}
					}
					sem.release();	//release permit back into the wild
					delta--;	//To ensure that it is only updated 30 times a second, unrestricted time
				}
			}
		}
		//Method that updates some UI elements
		public void updateElements() {
			btcLabel.setText(String.format("BTC: %f", player.getBTC()));
			usdLabel.setText(String.format("USD: %.2f", player.getUSD()));
			deviceLabel.setText(String.format("Device: %s", player.getDevice().name));
			chanceLabel.setText(String.format("Chance: %.2f%% chance for a successful mine", Player.chance));
			valueLabel.setText(String.format("Value: %f BTC added on successful mine", Player.value));
			periodLabel.setText(String.format("Period: Mines once every %.2f seconds", Player.period));
		}
	}
	//Main method
	public static void main(String[] args) {
		new Game();
	}
}