
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class GraphicalUserInterface {
	
	private JFrame mainFrame;
	private CardLayout mainCardLayout = new CardLayout();
	private JPanel mainPanel = new JPanel(this.mainCardLayout);
	private JPanel gamePanel = new JPanel();
	private JPanel menuPanel = new JPanel(new BorderLayout());
	private CardLayout cardLayout = new CardLayout();
	private JPanel cardLayoutPanel = new JPanel(this.cardLayout);
	private AskingToHostPanel askingToHostPanel = new AskingToHostPanel();
	private JPanel bottomPanel = new JPanel();
	private JPanel topPanel = new JPanel();
	private JButton startButton = new JButton("Start Game");

	private MainMenuPanel mainMenuPanel;
	
	private boolean userIsHost;

	public GraphicalUserInterface() { 
		this.mainFrame = new JFrame("One-Two-Three Pass");
		this.mainFrame.setSize(600,600);
			// do not use frame.pack() together with frame.setSize()
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.centerWindowOnRun(this.mainFrame);
		this.mainFrame.setVisible(true); // the outmost frame

		this.mainFrame.add(this.mainPanel);
			// the outmost frame has the biggest panel
		// this.mainPanel.add(this.menuPanel);
		this.mainPanel.add(this.menuPanel, "menu");
			// the biggest panel may show the menuPanel in BorderLayout
		// this.mainPanel.add(this.gamePanel, "game");
			// the biggest panel may show the gamePanel in BorderLayout
		// this.gamePanel.add(new JLabel("This is the game panel."));

		/* Top */
		this.menuPanel.add(this.topPanel); // on top of menu is a panel
		JLabel welcomeLabel = new JLabel("Welcome, and 1-2-3 Pass!");
		this.menuPanel.add(welcomeLabel, BorderLayout.NORTH);
			// It will contain a welcoming message.

		/* CardLayoutPanel is placed in the center */
		this.menuPanel.add(this.cardLayoutPanel, BorderLayout.CENTER);
			// The center of the borderd menuPanel will have a cardLayoutPanel
		this.cardLayoutPanel.add(this.askingToHostPanel, "askingToHostPanel");
			// The center of the menu will may show an asking Panel
		JPanel waitingPlayersPanel = new JPanel();
		JLabel waitingPlayersLabel = new JLabel("Waiting for other players...");
		waitingPlayersPanel.add(waitingPlayersLabel);
		this.cardLayoutPanel.add(waitingPlayersLabel, "waiting");
			// The center of the menu will may show an asking Panel

		/* Bottom of the window */

		this.menuPanel.add(this.bottomPanel, BorderLayout.SOUTH);
		this.startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Start button has been clicked");
				// Hide this button or show the game panel
				GraphicalUserInterface.this
					.mainCardLayout.show(
						GraphicalUserInterface.this.mainPanel,"game");

			}
		});
		this.startButton.setEnabled(false);
		this.bottomPanel.add(this.startButton);

		this.mainFrame.add(this.mainPanel);

		// after all this, show on top first the menu panel
		this.mainCardLayout.show(this.mainPanel,"menu");

		System.out.println("GUI initialized...");		
	}


	/* This block just centers the GUI window */
	private void centerWindowOnRun(JFrame mainFrame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setLocation(
			dimension.width / 2 - mainFrame.getSize().width / 2,
			dimension.height / 2 - mainFrame.getSize().height / 2);
	}

	private void showCenterMenu(String name) {
		this.cardLayout.show(this.cardLayoutPanel, name);
	}
	

	public boolean askToBeHost() {
		this.askingToHostPanel.resetAnswer();
		this.showCenterMenu("askingToHostPanel");
		System.out.println("Showing askingToHostPanel...");
		// wait until the user interacts to the GUI
		
		while(this.askingToHostPanel.answered() == false) {
			// waiting
			System.out.print("");
			/*
			* Unexpected behavior happens without
			* this print nothing statement,
			* this doesn't return to the main
			* function calling.
			*/ 
		}

		System.out.println("this.askingToHostPanel.isAnswered(): "
			+ this.askingToHostPanel.answered());

		System.out.println("User responded.");
		this.showCenterMenu("waiting");
		// then the value may be returned
		return this.askingToHostPanel.ask();
	}

	public void allowStart() {
		// Activate start button
		this.startButton.setEnabled(true);
	}

}