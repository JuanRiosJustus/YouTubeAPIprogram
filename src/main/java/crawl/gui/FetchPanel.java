package crawl.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.List;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.mortbay.thread.Timeout.Task;

import com.google.api.services.youtube.cmdline.data.Search;

import crawl.url.MetaData;
import crawl.url.ProjectHandler;
import crawl.url.URLReader;

public class FetchPanel extends JPanel {
	/**
	 * 
	 */
	private JComboBox jComboBox;
	private static final long serialVersionUID = -6632517867850662235L;
	private static JTextField textfield;
	private JButton genButton, descButton;
	private static JTextArea genArea;
	private static JScrollPane scroll;
	private static boolean showDescription;

	public FetchPanel() {

		initComponents();
		setComponents();
		add(Box.createRigidArea(new Dimension(0,ProjectHandler.getRawScreenHeight()/6)));
		add(genButton);
		add(descButton);
		add(jComboBox);
		add(textfield);
		add(Box.createRigidArea(new Dimension(0,ProjectHandler.getRawScreenHeight()/30)));
		add(scroll);

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initComponents() {
		jComboBox = new JComboBox(numberList());
		textfield = new JTextField("Enter a term to search...");
		descButton = new JButton("DESC: OFF");
		genButton = new JButton("Fetch");
		genArea = new JTextArea(ProjectHandler.getRawScreenHeight()/40,ProjectHandler.getRawScreenWidth()/20);
		scroll = new JScrollPane(genArea);
		showDescription = false;
		 
	}
	private void setComponents() {
		
		genArea.setCaretPosition(genArea.getDocument().getLength());
		
		genArea.setEditable(false);
		genArea.setVisible(true);
		genArea.setLineWrap (true);
		
		textfield.setEditable(true);
		textfield.setColumns(ProjectHandler.getRawScreenWidth()/30);
		textfield.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				textfield.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		
		descButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (showDescription) {
					showDescription = false;
					descButton.setText("DESC: OFF");
					genArea.setText("The description has been turned off");
				} else {
					showDescription = true;
					descButton.setText("DESC: ON");
					genArea.setText("The description has been turned on");
				}
			}
		});
		
		genButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				genButton.setEnabled(false);
				descButton.setEnabled(false);
				jComboBox.setEnabled(false);
				new Thread(new Runnable() {
	                @Override
	                public void run() {
	                	genButton.setText("Fetching...");
	                requestData();
	                textfield.setText("Enter  term to search...");
	                genButton.setText("Fetch");
	                genButton.setEnabled(true);
	                descButton.setEnabled(true);
	                jComboBox.setEnabled(true);
	                }
	            }).start();
			}
		});
	}
	/**
	 * Finds the strings to be placed within the JTextArea
	 */
	private void requestData() {
		Search.locateVideoInformation(jComboBox.getSelectedIndex(), textfield.getText().toString());
	}
	
	/**
	 *  Get the text area to print to 
	 * @return the text area
	 */
	public static JTextArea getGenArea() {
		genArea.setCaretPosition(genArea.getDocument().getLength());
		return genArea;
	}
	/*
	 * return the list of numberrs to be placed i the jcombo box
	 */
	private String[] numberList() {
		String[] initString = new String[51];
		for (int count = 0; count < 51; count++) {
			initString[count] = Integer.toString(count);
		}
		return initString;
	}
	/*
	 * return the boolean value to determine whether or not to show the description
	 */
	public static boolean getShowDescription() {
		return showDescription;
	}
}
