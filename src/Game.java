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

//TODO: ADD BASE STATS TO DEVICES
//TODO: Timer and associated sprites
//TODO: Attach sprites
//TODO: Finish ui

public class Game {

	public final Dimension screenSize;
	private ArrayList<Device> devices;
	private Thread rngThread, statThread;
	private ArrayList<Upgrade> upgradeList = new ArrayList<Upgrade>();
	public static Player player;
	private Semaphore sem = new Semaphore(1);
	public statTracker tracker = new statTracker(sem);
	public RNG rng = new RNG(sem);
	private JLabel title, instructions, credits, statTitleLabel, deviceLabel, chanceLabel, periodLabel, valueLabel, GPULabel, CPULabel, RAMLabel, btcIcon, usdIcon, btcLabel, usdLabel, noMoreDevices;
	private JButton backToMainButton, tutorialButton, tutorialBackButton, resetDataButton, statsButton, gameMenuButton, statBackButton, exitButton, convertUSD, convertBTC, upgradeMarketButton, currencyMarketButton, blackMarketButton, deviceMarketButton, buyDeviceButton;
	public JFrame frame = new JFrame();
	private JPanel mainMenu,gameMenu,tutorialPanel, statsPanel;

	public Game() {

		mainMenu = new JPanel();
		mainMenu.setLayout(null);
		mainMenu.setSize(1000,700);

		gameMenu = new JPanel();
		gameMenu.setLayout(null);
		gameMenu.setSize(1000,700);

		tutorialPanel = new JPanel();
		tutorialPanel.setLayout(null);
		tutorialPanel.setSize(1000,700);

		statsPanel = new JPanel();
		statsPanel.setLayout(null);
		statsPanel.setSize(1000,700);

		devices = new ArrayList<Device>();
		devices.add(new Device("Arduino Nano", "It somehow works, it just does, don't question it.", "resources/Nano.png", 0, 0, 0, 0, 0));
		devices.add(new Device("Arduino Uno", "Marginally better, at least it's larger.", "resources/Uno.png", 0, 1, 0, 0, 0));
		devices.add(new Device("Casual Laptop", "Finally, something real although sketchy.", "resources/Casual.png", 0, 2, 0, 0, 0));
		devices.add(new Device("Business Laptop", "Wow! Actually capable of getting some BTC.", "resources/Business.png", 0, 3, 0, 0, 0));
		devices.add(new Device("Gaming Laptop", "A beast for it's size.", "resources/Gaming.png", 0, 4, 0, 0, 0));
		devices.add(new Device("Mini Desktop", "Marginally better than a laptop.", "resources/Mini.png", 0, 5, 0, 0, 0));
		devices.add(new Device("PC Desktop", "Wow! Can actually run Windows 10.", "resources/Pc.png", 0, 6, 0, 0, 0));
		devices.add(new Device("Gaming Desktop", "Amazing top of the line performance.", "resources/GamingPC.png", 0, 7, 0, 0, 0));
		devices.add(new Device("Mom's Basement", "First server finally.", "none", 0, 8, 0, 0, 0));
		devices.add(new Device("Ubisoft Server", "Practically non-existent", "none", 0, 9, 0, 0, 0));
		devices.add(new Device("Server Farm", "Finally, you've reached the end-game", "none", 0, 10, 0, 0, 0));
		Collections.sort(devices, new deviceByTier());
		Upgrade.setDefaultPrices();
		player = new Player();
		screenSize = new Dimension(1000,700);

		tutorialButton = new JButton("How To Play");				//Done
		tutorialButton.setBackground(Color.gray);
		tutorialButton.setBounds(425,400,150,50);
		tutorialButton.setActionCommand("tutorial");
		tutorialButton.addActionListener(new ButtonActions());

		backToMainButton = new JButton("Back");						//Done
		backToMainButton.setBackground (Color.gray);
		backToMainButton.setBounds(50,570,100,50);
		backToMainButton.setActionCommand("backToMain");
		backToMainButton.addActionListener(new ButtonActions());

		statsButton = new JButton("Player Stats");					//Done
		statsButton.setBackground (Color.gray);
		statsButton.setBounds(175,570,150,50);
		statsButton.setActionCommand("displayStats");
		statsButton.addActionListener(new ButtonActions());

		gameMenuButton = new JButton("Play");						//Done
		gameMenuButton.setBackground (Color.gray);
		gameMenuButton.setBounds(425,300,150,50);
		gameMenuButton.setActionCommand("playGame");
		gameMenuButton.addActionListener(new ButtonActions());

		statBackButton = new JButton("Back");						//Done
		statBackButton.setBackground (Color.gray);
		statBackButton.setBounds(250,50,200,50);
		statBackButton.setActionCommand("stat-back");
		statBackButton.addActionListener(new ButtonActions());

		tutorialBackButton = new JButton("Back");					//Done
		tutorialBackButton.setBackground (Color.gray);
		tutorialBackButton.setBounds(50,570,100,50);
		tutorialBackButton.setActionCommand("toMain");
		tutorialBackButton.addActionListener(new ButtonActions());
		
		resetDataButton =  new JButton("Reset Data");
		resetDataButton.setBackground (Color.gray);
		resetDataButton.setBounds (425,500,150,50);
		resetDataButton.setActionCommand("reset");
		resetDataButton.addActionListener(new ButtonActions());
		

		exitButton = new JButton("Exit");							//Done
		exitButton.setBackground (Color.gray);
		exitButton.setBounds(775,550,150,50);
		exitButton.setActionCommand("quit");
		exitButton.addActionListener(new ButtonActions());

		convertUSD = new JButton("Convert USD to BTC");				//
		convertUSD.setBackground(Color.gray);
		convertUSD.setBounds(500,300,100,50);
		convertUSD.setActionCommand("usdToBTC");
		convertUSD.addActionListener(new MarketButtons());
		convertUSD.setVisible(false);

		convertBTC = new JButton("Convert BTC to USD");				//
		convertBTC.setBackground(Color.gray);
		convertBTC.setBounds(500,400,100,50);
		convertBTC.setActionCommand("btcToUSD");
		convertBTC.addActionListener(new MarketButtons());
		convertBTC.setVisible(false);

		upgradeMarketButton = new JButton("Upgrades");
		upgradeMarketButton.setBackground(Color.gray);
		upgradeMarketButton.setBounds(400,400,100,50);
		upgradeMarketButton.setActionCommand("upgradeMarket");
		upgradeMarketButton.addActionListener(new MarketButtons());
		
		currencyMarketButton = new JButton("Currency");
		currencyMarketButton.setBackground(Color.gray);
		currencyMarketButton.setBounds(400,330,100,50);
		currencyMarketButton.setActionCommand("currencyMarket");
		currencyMarketButton.addActionListener(new MarketButtons());

		deviceMarketButton = new JButton("Devices");
		deviceMarketButton.setBackground(Color.gray);
		deviceMarketButton.setBounds(400,260,100,50);
		deviceMarketButton.setActionCommand("deviceMarket");
		deviceMarketButton.addActionListener(new MarketButtons());

		blackMarketButton = new JButton("");
		blackMarketButton.setBackground(Color.gray);
		blackMarketButton.setBounds(400,190,100,50);
		blackMarketButton.setActionCommand("blackMarket");
		blackMarketButton.addActionListener(new MarketButtons());

		buyDeviceButton = new JButton("Buy Next Device");
		buyDeviceButton.setBackground(Color.gray);
		buyDeviceButton.setBounds(550,300,100,50);
		buyDeviceButton.setActionCommand("nextDevice");
		buyDeviceButton.addActionListener(new MarketButtons());
		buyDeviceButton.setVisible(false);

		btcIcon = new JLabel(new ImageIcon("resources/BTC.png"));
		btcIcon.setBounds(500,300,75,75);
		btcIcon.setVisible(false);

		usdIcon = new JLabel(new ImageIcon("resources/USD.png"));
		usdIcon.setBounds(500,400,75,75);
		usdIcon.setVisible(false);

		btcLabel = new JLabel("");
		btcLabel.setBounds(50,50,200,50);
		btcLabel.setBackground(Color.lightGray);
		
		usdLabel = new JLabel("");
		usdLabel.setBounds(250,50,200,50);
		usdLabel.setBackground(Color.lightGray);

		title = new JLabel("Bitcoin Tycoon", SwingConstants.CENTER);
		title.setBounds(0,75,1000,200);
		title.setFont(new Font("Serif", Font.PLAIN, 48));

		noMoreDevices = new JLabel("<html>No More Devices to Buy</html>", SwingConstants.CENTER);
		noMoreDevices.setBounds(300,300,200,200);
		noMoreDevices.setFont(new Font("Serif", Font.BOLD, 20));
		noMoreDevices.setVisible(false);

		instructions = new JLabel("<html>You are a poor computer whiz in need of some quick cash. Luckily, your Arduino Nano is capable of mining Bitcoin. Upgrade your device by buying more RAM, CPU, and GPU. When the market gets too expensive, buy another device and start over. Soon, you will be able to turn your mom's basement into a money making machine. And if you have enough money, you may be able to attract a mysterious company.</html>", SwingConstants.LEFT);
		instructions.setBounds(50,50,600,600);
		instructions.setFont(new Font("Serif", Font.PLAIN, 24));

		credits = new JLabel("<html> Created by Shawn and David </html>", SwingConstants.LEFT);
		credits.setBounds(500,500,100,100);
		credits.setFont(new Font("Serif", Font.PLAIN, 14));

		statTitleLabel = new JLabel("Stats:", SwingConstants.LEFT);				//Done
		statTitleLabel.setBounds(100,100,500,25);
		statTitleLabel.setFont(new Font("Serif", Font.BOLD, 14));

		deviceLabel = new JLabel();												//Done
		deviceLabel.setBounds(100,200,500,25);
		deviceLabel.setFont(new Font("Serif", Font.BOLD, 14));
		
		chanceLabel = new JLabel();												//Done
		chanceLabel.setBounds(100,225,500,25);
		deviceLabel.setFont(new Font("Serif", Font.BOLD, 14));
		
		valueLabel = new JLabel();												//Done
		valueLabel.setBounds(100,250,500,25);
		valueLabel.setFont(new Font("Serif", Font.BOLD, 14));

		periodLabel = new JLabel();												//Done
		periodLabel.setBounds(100,275,500,25);
		periodLabel.setFont(new Font("Serif", Font.BOLD, 14));

		CPULabel = new JLabel(String.format("CPU Upgrades: "), SwingConstants.LEFT);
		CPULabel.setBounds(100,325,100,25);
		CPULabel.setFont(new Font("Serif", Font.BOLD, 14));

		GPULabel = new JLabel(String.format("GPU Upgrades:"), SwingConstants.LEFT);
		GPULabel.setBounds(100,350,100,25);
		GPULabel.setFont(new Font("Serif", Font.BOLD, 14));
		
		RAMLabel = new JLabel(String.format("RAM Upgrades:"), SwingConstants.LEFT); 
		RAMLabel.setBounds(100,375,100,25);
		RAMLabel.setFont(new Font("Serif", Font.BOLD, 14));
		
		mainMenu.add(title);
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

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if (JOptionPane.showConfirmDialog(frame,"Are you sure you want to close the game?","Close Game?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					saveGame();
					stop();
					System.exit(0);
				}
		    }
		});

		frame.setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
		frame.setPreferredSize(screenSize);
		frame.setResizable(false);
		frame.setTitle("Bitcoin Tycoon");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		gameMenu.setVisible(false);
		mainMenu.setVisible(true);
		tutorialPanel.setVisible(false);
		statsPanel.setVisible(false);

		frame.add(mainMenu);
		frame.add(gameMenu);
		frame.add(tutorialPanel);
		frame.add(statsPanel);

		statThread = new Thread(tracker);
		rngThread = new Thread(rng);
		readSaveFile();
		if(player.blackMarketStatus()) {
			blackMarketButton.setText("Black Market");
		} else {
			blackMarketButton.setText("Locked");
		}
		for(Upgrade i : upgradeList) {
			gameMenu.add(i.image);
		}
	}
	public void displayUpgradeMarket() {
		for(Upgrade i : upgradeList) {
			if(!i.blackMarketItem && player.getDevice().availableUpgrades.contains(i)) {
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
	}
	public void displayCurrencyMarket() {
		btcIcon.setVisible(true);
		usdIcon.setVisible(true);
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
		convertUSD.setVisible(true);
		convertBTC.setVisible(true);
	}
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
		}
	}
	public void displayDeviceMarket() {
		for(Upgrade i : upgradeList) {
			i.image.setVisible(false);
		}
		btcIcon.setVisible(false);
		usdIcon.setVisible(false);
		if(player.getDevice().getTier()+1 >= devices.size()) {
			noMoreDevices.setVisible(true);
			buyDeviceButton.setVisible(false);
		} else {
			devices.get(devices.indexOf(player.getDevice())+1).image2.setVisible(true);
			buyDeviceButton.setVisible(true);
		}
		convertUSD.setVisible(false);
		convertBTC.setVisible(false);
	}
	public void displayMainMenu() {
		mainMenu.setVisible(true);
		gameMenu.setVisible(false);
		tutorialPanel.setVisible(false);
		statsPanel.setVisible(false);
		player.getDevice().image.setVisible(false);
		stop();
	}
	public void displayGameMenu() {
		gameMenu.setVisible(true);
		mainMenu.setVisible(false);
		tutorialPanel.setVisible(false);
		statsPanel.setVisible(false);
		player.getDevice().image.setVisible(true);
		start();
	}
	public void displayTutorial() {
		tutorialPanel.setVisible(true);
		gameMenu.setVisible(false);
		mainMenu.setVisible(false);
		statsPanel.setVisible(false);
	}
	public void backToMain() {
		mainMenu.setVisible(true);
		gameMenu.setVisible(false);
		tutorialPanel.setVisible(false);
		statsPanel.setVisible(false);
	}
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
	public synchronized void start() {
		statThread = new Thread(tracker);
		rngThread = new Thread(rng);
		rng.running = true;
		tracker.running = true;
		statThread.start();
		rngThread.start();
	}
	public synchronized void stop() {
		rng.running = false;
		tracker.running = false;
		try {
			statThread.join();
			rngThread.join();
		} catch(InterruptedException e) {
			System.out.print("Error 10: Unable to join threads");
			System.exit(0);
		}
		System.gc();
	}
	public void readSaveFile() {
		try {
			Scanner scan = new Scanner(new File("upgrades.ini"));
			parseUpgradeFile(scan);
			scan.close();
			Scanner cin = new Scanner(new File("presets.ini"));
			parsePresetFile(cin);
			cin.close();
			cin = new Scanner(System.in);
			File saveFile = new File("save.ini");
			parseSaveFile(cin, saveFile);
			cin.close();
		} catch(IOException e) {
			System.out.println("Error 7: Read/write error - save.ini");
			System.exit(0);
		}
	}
	public void parseUpgradeFile(Scanner scan) {
		while(scan.hasNextLine()) {
			String input = scan.nextLine();
			StringTokenizer splitter = new StringTokenizer(input, "=");
			Upgrade u;
			if(splitter.countTokens() > 1) {
				String[] effect = new String[3];
				String temp = splitter.nextToken();
				if(temp.equalsIgnoreCase("CPU")) {
					effect[0] = "period";
					effect[1] = "multiply";
					effect[2] = "5";
					u = new Upgrade("CPU Upgrade", "ProcessorUpgrade", Upgrade.USD, false, effect, new StringTokenizer(splitter.nextToken(),","));
					u.image.setBounds(300,300,75,75);
					u.image.setActionCommand("buyUpgrade-CPU");
					u.image.addActionListener(new MarketButtons());
					upgradeList.add(u);
				} else if(temp.equalsIgnoreCase("GPU")) {
					effect[0] = "value";
					effect[1] = "multiply";
					effect[2] = "3";
					u = new Upgrade("Graphics Card Upgrade", "GraphicsUpgrade", Upgrade.USD, false, effect, new StringTokenizer(splitter.nextToken(),","));
					u.image.setBounds(300,400,75,75);
					u.image.setActionCommand("buyUpgrade-GPU");
					u.image.addActionListener(new MarketButtons());
					upgradeList.add(u);
				} else if(temp.equalsIgnoreCase("RAM")) {
					effect[0] = "chance";
					effect[1] = "multiply";
					effect[2] = "2";
					u = new Upgrade("Memory Upgrade", "RAMUpgrade", Upgrade.USD, false, effect, new StringTokenizer(splitter.nextToken(),","));
					u.image.setBounds(300,300,75,75);
					u.image.setActionCommand("buyUpgrade-RAM");
					u.image.addActionListener(new MarketButtons());
					upgradeList.add(u);
				} else if(temp.equalsIgnoreCase("BlackMarket")) {
					u = new BlackMarketItem("Black Market Item", effect, new StringTokenizer(splitter.nextToken(),","));
					u.image.setBounds(300,300,75,75);
					u.image.setActionCommand("buyUpgrade-BlackMarket");
					u.image.addActionListener(new MarketButtons());
					upgradeList.add(u);
				}
			}
		}
		Collections.sort(upgradeList, new upgradeByType());
	}
	public void parseSaveFile(Scanner cin, File saveFile) {
		try {
			if(!saveFile.createNewFile()) {
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
					if(temp.equalsIgnoreCase("Device")) {
						temp = splitter.nextToken();
						if(temp.equalsIgnoreCase("None")) {}
						else {
							player.setDevice(devices.get(Collections.binarySearch(devices, new Device(Integer.parseInt(temp)), new deviceByTier())));
						}
					} else if(temp.equalsIgnoreCase("Upgrades")) {
						temp = splitter.nextToken();
						if(temp.equalsIgnoreCase("None")) {
						} else {
							splitter = new StringTokenizer(temp,",");
							while(splitter.hasMoreTokens())
								player.getDevice().addUpgrade(upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade(splitter.nextToken()), new upgradeByType())));
						}
					}
					else if(temp.equalsIgnoreCase("USD"))		player.setUSD(Double.parseDouble(splitter.nextToken()));
					else if(temp.equalsIgnoreCase("BTC"))		player.setUpBTC(Double.parseDouble(splitter.nextToken()));
					else if(temp.equalsIgnoreCase("BlackMarket")) {
						if(Boolean.parseBoolean(splitter.nextToken())) player.unlockBlackMarket();
						else player.lockBlackMarket();
					}
					else if(temp.equalsIgnoreCase("chance"))	Player.chance = Double.parseDouble(splitter.nextToken());
					else if(temp.equalsIgnoreCase("period"))	Player.period = Double.parseDouble(splitter.nextToken());
					else if(temp.equalsIgnoreCase("value"))		Player.value = Double.parseDouble(splitter.nextToken());
					else if(temp.equalsIgnoreCase("LifeTimeBTC")) Player.totalBTC = Double.parseDouble(splitter.nextToken());
				}
			} else {
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
	public void parsePresetFile(Scanner scan) {
		String input;
		StringTokenizer splitter;
		for(Device i : devices) {
			input = scan.nextLine();
			splitter = new StringTokenizer(input,"=");
			input = splitter.nextToken();
			splitter = new StringTokenizer(splitter.nextToken(),",");
			while(splitter.hasMoreTokens()) {
				i.addAvailableUpgrade(upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade(splitter.nextToken()), new upgradeByType())));
			}
		}
	}
	public void saveGame() {
		writeToSaveFile();
		writeToUpgradeFile();
	}
	public void writeToSaveFile() {
		try {
			BufferedWriter cout = new BufferedWriter(new FileWriter("save.ini"));
			if(player.getDevice()==null) {
				cout.write(String.format("Device=None%n"));
			} else {
				cout.write(String.format("Device=%d%n",player.getDevice().getTier()));
			}
			String temp = "";
			if(player.getDevice() == null || player.getDevice().getUpgrades().isEmpty()) {
				cout.write("Upgrades=None\n");
			} else {
				for(Upgrade i : player.getDevice().getUpgrades()) {
					temp += i.type + ",";
				}
				temp = temp.substring(0,temp.length()-1);
				cout.write(String.format("Upgrades=%s%n", temp));
			}
			
			cout.write(String.format("USD=%f%n", player.getUSD()));
			cout.write(String.format("BTC=%f%n", player.getBTC()));
			cout.write(String.format("BlackMarket=%b%n", player.blackMarketStatus()));
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
	public void writeToUpgradeFile() {
		try {
			BufferedWriter cout = new BufferedWriter(new FileWriter("upgrades.ini"));
			Upgrade temp;
			cout.write("[Normal Upgrades]\n");
			temp = upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade("ProcessorUpgrade"), new upgradeByType()));
			cout.write(String.format("CPU=%f,%d,resources/CPU.png%n", temp.price, temp.numOf));
			temp = upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade("GraphicsUpgrade"), new upgradeByType()));
			cout.write(String.format("GPU=%f,%d,resources/GPU.png%n", temp.price, temp.numOf));
			temp = upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade("RAMUpgrade"), new upgradeByType()));
			cout.write(String.format("RAM=%f,%d,resources/RAM.png%n", temp.price, temp.numOf));
			cout.write("[Black Market Upgrades]\n");
			temp = upgradeList.get(Collections.binarySearch(upgradeList, new Upgrade("BlackMarket"), new upgradeByType()));
			cout.write(String.format("BlackMarket=%f,%d,resources/BlackMarket.png%n", temp.price, temp.numOf));
			cout.close();
		} catch(IOException e) {
			System.out.println("Error 13: error writing to upgrades.ini");
			System.exit(0);
		}
	}
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
			} else if(action.equalsIgnoreCase("usdToBTC")) {
				boolean correct = false;
				do {
					String input = JOptionPane.showInputDialog(gameMenu, "How much USD do you want to convert?");
					if(input == null) {
						return;
					}
					try {
						double usdToConvert = Double.parseDouble(input);
						if(usdToConvert > player.getUSD()) {
							throw new NumberFormatException();
						}
						player.setUSD(player.getUSD()-usdToConvert);
						player.addBTC(usdToConvert/Player.conversionRate);
						correct = true;
					} catch(NumberFormatException e) {
						JOptionPane.showMessageDialog(gameMenu, "Not a valid number", "Error", JOptionPane.WARNING_MESSAGE);
					}
					if(correct) {
						break;
					}
				} while(true);
			} else if(action.equalsIgnoreCase("btcToUSD")) {
				boolean correct = false;
				do {
					String input = JOptionPane.showInputDialog(gameMenu, "How much BTC do you want to convert?");
					if(input == null) {
						return;
					}
					try {
						double btcToConvert = Double.parseDouble(input);
						if(btcToConvert > player.getBTC()) {
							throw new NumberFormatException();
						}
						player.setBTC(player.getBTC()-btcToConvert);
						player.addUSD(btcToConvert*Player.conversionRate);
						correct = true;
					} catch(NumberFormatException e) {
						JOptionPane.showMessageDialog(gameMenu, "Not a valid number", "Error", JOptionPane.WARNING_MESSAGE);
					}
					if(correct) {
						break;
					}
				} while(true);
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
				System.out.println("reached");
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
				if(devices.get(devices.indexOf(player.getDevice())+1).buy(player)) {
					devices.get(devices.indexOf(player.getDevice())-1).image.setVisible(false);
					JOptionPane.showMessageDialog(gameMenu, "Success", "Purchase", JOptionPane.INFORMATION_MESSAGE);
					devices.get(devices.indexOf(player.getDevice())).image2.setVisible(false);
					devices.get(devices.indexOf(player.getDevice())).image.setVisible(true);
					for(Upgrade i : upgradeList) {
						i.resetPrices();
					}
					Player.chance = player.getDevice().getBaseChance();
					Player.period = player.getDevice().getBasePeriod();
					Player.value = player.getDevice().getBaseValue();
					displayUpgradeMarket();
				} else {
					JOptionPane.showMessageDialog(gameMenu, "Insufficient Funds", "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
	class statTracker implements Runnable {
		private Semaphore sem;
		public statTracker(Semaphore s) {
			sem = s;
		}
		public boolean running = false;
		public void run() {
			long lastTime = System.nanoTime();
			final double ns = 1000000000.0 / 30.0;	//30 times per second update
			double delta = 0;
			boolean prevStatus = false;
			while(running) {
				long now = System.nanoTime();
				delta = delta + ((now-lastTime) / ns);
				lastTime = now;
				while (delta >= 1)	//Make sure update is only happening 30 times a second
				{
					try {
						sem.acquire();
					} catch (InterruptedException e) {
						System.out.println("Error 14: Unable to acquire semaphore permit");
						System.exit(0);
					}
					updateElements();
					if(!prevStatus && Player.totalBTC > 10) {
						System.out.println("yes");
						player.unlockBlackMarket();
						blackMarketButton.setText("Black Market");
					}
					prevStatus = player.blackMarketStatus();
					sem.release();
					delta--;
				}
			}
		}
		public void updateElements() {
			btcLabel.setText(String.format("BTC: %f", player.getBTC()));
			usdLabel.setText(String.format("USD: %.2f", player.getUSD()));
			deviceLabel.setText(String.format("Device: %s", player.getDevice().name));
			chanceLabel.setText(String.format("Chance: %.2f%% chance for a successful mine", Player.chance));
			valueLabel.setText(String.format("Value: %f BTC added on successful mine", Player.value));
			periodLabel.setText(String.format("Period: Mines once every %.2f seconds", Player.period));
		}
	}
	public static void main(String[] args) {
		new Game();
	}
}