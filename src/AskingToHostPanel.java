
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AskingToHostPanel extends JPanel {

	private JLabel label = new JLabel("Would you like to host a game?");
	private JPanel buttonsPanel = new JPanel();
	private JButton yesButton = new JButton("Yes");
	private JButton noButton = new JButton("No");

	private boolean answered = false;
	private boolean userIsHost;

	public AskingToHostPanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			// layout components vertically
		this.add(this.label); // stacking vertically

		// Create a new panel to stack buttons horizontally
		this.buttonsPanel.setLayout(
			new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		this.add(this.buttonsPanel);

		this.yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Clicked yes.");
				AskingToHostPanel.this.userIsHost = true;
				AskingToHostPanel.this.answered = true;
			}
		});
		this.buttonsPanel.add(yesButton);

		this.noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Clicked no.");
				AskingToHostPanel.this.userIsHost = false;
				AskingToHostPanel.this.answered = true;
			}
		});
		this.buttonsPanel.add(noButton);
	}

	public void resetAnswer() {
		this.answered = false;
	}

	public boolean isAnswered() {
		return this.answered;
	}

	public boolean getAnswer() {
		return this.userIsHost;
	}

}