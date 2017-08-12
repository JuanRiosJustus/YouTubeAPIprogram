package crawl.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.google.api.services.youtube.cmdline.data.Search;

import crawl.objects.Video;
import crawl.url.ProjectHandler;

public class SortPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static JTextArea textArea;
	private JScrollPane scrollPane;
	
	public SortPanel() {
		initComponents();
		setComponents();
		add(scrollPane);
	}

	/**
	 * Method which initializes the variables
	 */
	private void initComponents() {
		textArea = new JTextArea(ProjectHandler.getRawScreenHeight()/28,ProjectHandler.getRawScreenWidth()/20);
		scrollPane = new JScrollPane(textArea);
		System.out.print("Have any videos been collected?:  " + !Search.getVideoList().isEmpty());
		
	}
	/**
	 * Method to set and adjust certain properties of objects, etc.
	 */
	private void setComponents() {
		textArea.setCaretPosition(textArea.getDocument().getLength());
		
		textArea.setEditable(false);
		textArea.setVisible(true);
		textArea.setLineWrap (true);
	}
	
	/**
	 * 
	 * @return - returns the JTextArea of the SortPanel class particularly for appending 
	 */
	public static JTextArea getTextArea() {
		return textArea;
	}
}
