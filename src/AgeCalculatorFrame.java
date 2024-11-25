// All import statements.
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.stream.IntStream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// A class that extends JFrame, implements ActionListener, and is used to calculate the age of someone based on the birthday entered.
public class AgeCalculatorFrame extends JFrame implements ActionListener {
	// All private class properties.
	private static final long serialVersionUID = 1L;
	private JComboBox<Integer> yearBox;  
	private JComboBox<String> monthBox;
	private JComboBox<Integer> dayBox;
	private JLabel yearLabel; 
	private JLabel monthLabel; 
	private JLabel dayLabel;
	private JLabel ageLabel;
	private JButton calcButton;
	private JPanel panel;
	private ArrayList<Integer> days = new ArrayList<Integer>();
	private ArrayList<Integer> years = new ArrayList<Integer>();
	
	// Constructor
	AgeCalculatorFrame() {
		// Set up initial year to 1900 since no on is still alive from before that and set current year based off of current date.
		int initalYear = 1900;
		int currentYear = Year.now().getValue();
		
		// Create a stream of integers of all possible years and add them to the ArrayList.
		IntStream.rangeClosed(initalYear, currentYear).forEach(years::add);
		
		// Create an array of all the possible months.
		String[] monthStrings = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
		
		// Since January is the default month, set first day to 1 and last to 31 due to 31 days being in January.
		int initialDay = 1;
		int lastDay = 31;
		
		// Create an in stream of all possible day options and add them to the ArrayList.
		IntStream.rangeClosed(initialDay, lastDay).forEach(days::add);
		
		// Set up the frame title.
		setTitle("Calculate Age");
		
		// Set up the day, month, and year labels along with their title values.
		yearLabel = new JLabel("Please select your birth year:");
		monthLabel = new JLabel("Please select your birth month:");
		dayLabel = new JLabel("Please select your birth day:");
		ageLabel = new JLabel("Current Age: 0");
		
		// Set up all the JCombo boxes with their content and action listeners.
		yearBox = new JComboBox<Integer>(years.reversed().toArray(new Integer[0]));
		yearBox.addActionListener(this);
		monthBox = new JComboBox<String>(monthStrings);
		monthBox.addActionListener(this);
		dayBox = new JComboBox<Integer>(days.toArray(new Integer[0]));
		
		// Create the main panel.
		panel = new JPanel(new GridBagLayout());
		
		// Set up the button and add the action listener.
		calcButton = new JButton("Calculate Age");
		calcButton.addActionListener(this);
		
		// Set up the day label and its constraints before adding it to the panel.
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(15, 15, 15, 10);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(dayLabel, constraints);
		
		// Set up the day box and its constraints before adding it to the panel.
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(15, 0, 15, 15);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(dayBox, constraints);
		
		// Set up the month label and its constraints before adding it to the panel.
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(15, 15, 15, 10);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(monthLabel, constraints);
		
		// Set up the month box and its constraints before adding it to the panel.
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(15, 0, 15, 15);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(monthBox, constraints);
		
		// Set up the year label and its constraints before adding it to the panel.
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(15, 15, 15, 10);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(yearLabel, constraints);
		
		// Set up the year box and its constraints before adding it to the panel.
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(15, 0, 15, 15);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 2;
		panel.add(yearBox, constraints);
		
		// Set up the age label and its constraints before adding it to the panel.
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(15, 15, 15, 15);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridx = 0;
		constraints.gridy = 3;
		panel.add(ageLabel, constraints);
		
		// Set up the calculate button and its constraints before adding it to the panel.
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(15, 0, 15, 15);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 3;
		panel.add(calcButton, constraints);
		
		// Add the main panel to the frame.
		add(panel);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// Get the source of the event.
		Object source = event.getSource();
		
		// Check the source and handle it based on where the event came from.
		if (source == calcButton) {
			calculateAge();
		} else if ((source == monthBox) || source == yearBox) {
			handleYearOrMonthUpdated();
		}
	}
	
	// A method to use the GUI components to calculate the currently displayed age.
	private void calculateAge() {
		int year = yearBox.getItemAt(yearBox.getSelectedIndex()).intValue();
		int month = (monthBox.getSelectedIndex() + 1);
		int day = dayBox.getItemAt(dayBox.getSelectedIndex()).intValue();
		
		try {
			LocalDate birthDate = LocalDate.of(year, month, day);
			int age = Period.between(birthDate, LocalDate.now()).getYears();
			ageLabel.setText(String.format("Current Age: %d", age));
		} 
		
		// Catching DateTimeException error that can be thrown from the LocalDate creation to inform the user something went wrong and log to console.
		catch (DateTimeException e) {
			System.out.println("There was a problem creating LocalDate");
			showErrorMessage();
		}
	}
	
	// A method to use current values in month and year to update the number of days in the selected month. 
	// This will display and error to the user is something goes wrong and output a debug statement to the console.
	private void handleYearOrMonthUpdated() {
		int dayIndex = dayBox.getSelectedIndex();
		int year = yearBox.getItemAt(yearBox.getSelectedIndex()).intValue();
		int month = monthBox.getSelectedIndex() + 1;
		try {
			
			// Update the days of the month based on the selected year and month.
			YearMonth yearMonthObject = YearMonth.of(year, month);
			int daysInMonth = yearMonthObject.lengthOfMonth();
			days.removeAll(days);
			IntStream.rangeClosed(1, daysInMonth).forEach(days::add);
			dayBox.setModel(new DefaultComboBoxModel<Integer>(days.toArray(new Integer[0])));
			
			// If the previously selected day no longer exists because there are less days, set the selected day to the last day of the current month. Otherwise persist the selected day.
			if (dayIndex >= days.size() - 1) {
				dayBox.setSelectedIndex(days.size() - 1);
			} else {
				dayBox.setSelectedIndex(dayIndex);
			}
	
		} catch (DateTimeException e) {
			System.out.println("There was a problem creating YearMonth object from day, month, or year.");
			showErrorMessage();
		} catch (IllegalArgumentException e) {
			System.out.println("There was a problem setting the selected day index.");
			showErrorMessage();
		} catch (ClassCastException | NullPointerException e) {
			System.out.println("There was a problem removing days from the ArrayList.");
			showErrorMessage();
		}
	}
	
	// Show an error message to the user.
	private void showErrorMessage() {
		// Show error message
        JOptionPane.showMessageDialog(this, "Something went wrong. Please try again.");
	}
	
	
	public static void main(String[] args) {
		// Creates an AgeCalculatorFrame and all of its components from the init.
		AgeCalculatorFrame mainFrame = new AgeCalculatorFrame();

		// Set the default close operation.
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Resize the frame to fit all of its contents.
		mainFrame.pack();
		
		// Make the main frame visible.
		mainFrame.setVisible(true);
	}
}
