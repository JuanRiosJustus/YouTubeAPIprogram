package crawl.gui;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import crawl.url.ProjectHandler;

public class Display {
	
	private JFrame mainFrame, fetchFrame, sortFrame;
	private JPanel mainPanel;
	private JButton buttonZero, buttonOne, buttonTwo;
	private FetchPanel fetchPanel;
	private SortPanel sortPanel;
	private boolean buttonZeroClicked = false, buttonOneClicked = false, buttonTwoClicked = false;
	
	public Display() {
	}
	
	public void main() {
		initComponents();
		setComponents();
	}

	/**
	 * Method to initialize variables
	 */
	private void initComponents() {
		mainFrame = new JFrame("YouTube Assistance Suite");
		fetchFrame = new JFrame("Youtube Crawler");
		sortFrame =  new JFrame("Sort things");
		mainPanel = new JPanel();
		buttonZero = new JButton("YOUTUBE SCRAPER");
		buttonOne = new JButton("ANALYSIS");
		
		sortPanel = new SortPanel();
		fetchPanel = new FetchPanel();
	}
	
	/**
	 * Method to set and or adjust properties of objects
	 */
	private void setComponents() {
		mainFrame.setSize(ProjectHandler.screenWidth(), ProjectHandler.screenHeight());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setLocationRelativeTo(null);
		
        fetchFrame.setResizable(false);
        fetchFrame.setVisible(false);
        fetchFrame.setSize(ProjectHandler.screenWidth(), ProjectHandler.screenHeight());
        fetchFrame.setLocationRelativeTo(mainFrame);
        fetchFrame.setLocationRelativeTo(null);
        fetchFrame.getContentPane().add(fetchPanel);
        fetchFrame.setAlwaysOnTop(true);
        fetchFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
            	mainFrame.setVisible(true);
            }
        });
        
        sortFrame.setResizable(false);
        sortFrame.setVisible(false);
        sortFrame.setSize(ProjectHandler.screenWidth(), ProjectHandler.screenHeight());
        sortFrame.setLocationRelativeTo(mainFrame);
        sortFrame.setLocationRelativeTo(null);
        sortFrame.getContentPane().add(sortPanel);
        sortFrame.setAlwaysOnTop(true);
        sortFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
            	mainFrame.setVisible(true);
            }
        });
		
		buttonZero.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonZero.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (buttonZeroClicked) {
            		buttonZeroClicked = false;
            	} else {
            		buttonZeroClicked = true;
            	}
            	fetchFrame.setVisible(true);
            	mainFrame.setVisible(false);
            }
            });
		
		buttonOne.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (buttonOneClicked) {
            		buttonOneClicked = false;
            	} else {
            		buttonOneClicked = true;
            	}
            	sortFrame.setVisible(true);
            	mainFrame.setVisible(false);
            }
            });
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(ProjectHandler.getRawScreenWidth()/10,ProjectHandler.screenHeight()/3)));
		mainPanel.add(buttonZero);
		mainPanel.add(Box.createRigidArea(new Dimension(0,ProjectHandler.screenHeight()/9)));
		mainPanel.add(buttonOne);
		mainPanel.add(Box.createRigidArea(new Dimension(0,ProjectHandler.screenHeight()/9)));
		
		mainFrame.getContentPane().add(mainPanel);
		mainFrame.setVisible(true);
	}
	
	/**
	 * 
	 * @return - JPanel component to add various other components to it
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}
}
