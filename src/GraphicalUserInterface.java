
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;


public class GraphicalUserInterface {
	
	private JFrame mainFrame;
	private JPanel mainPanel;
	private AskingToHostPanel askingToHostPanel;// = new AskingToHostPanel();
	private boolean userIsHost;
	private JPanel welcomePanel;

	public GraphicalUserInterface() { 
		this.mainFrame = new JFrame("One-Two-Three Pass");
			// Titled "One-Two-Three Pass"
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setSize(600,600);
		this.centerWindowOnRun(this.mainFrame);
		this.mainFrame.setVisible(true);

		this.mainPanel = new JPanel(new CardLayout());
		this.mainFrame.add(this.mainPanel);

		this.welcomePanel = new JPanel();
		welcomePanel.add(new JLabel("One-Two-Three Pass!"));

		this.askingToHostPanel = new AskingToHostPanel();
		this.mainPanel.add(this.askingToHostPanel, "askingToHostPanel");


		// this.mainFrame.pack();
		// this.setLocationRelativeTo(null);
	}


	/* This block just centers the GUI window */
	private void centerWindowOnRun(JFrame mainFrame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setLocation(
			(dimension.width) / 2 - mainFrame.getSize().width / 2,
			(dimension.height) / 2 - mainFrame.getSize().height / 2);
	}
	

	public boolean askToBeHost() {
		this.askingToHostPanel.resetAnswer();

		CardLayout cardLayout = new CardLayout();
		cardLayout.show(this.mainPanel, "askingToHostPanel");

		System.out.print("Show asking panel.");


		// wait until the user interacts to the GUI
		while(this.askingToHostPanel.isAnswered() == false) {
			// waiting
			System.out.print(".");
		}
		// then the value may be returned
		return this.askingToHostPanel.getAnswer();
	}

}