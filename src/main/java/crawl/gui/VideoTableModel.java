package crawl.gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import crawl.objects.Video;

public class VideoTableModel extends AbstractTableModel{

	private ArrayList<Video> videoList;
	private String[] columns;
	
	public VideoTableModel(ArrayList<Video> videoList) {
		super();
		this.videoList = videoList;
		columns = new String[] {"Channel","Subs","New row"};
	}
	@Override
	public int getRowCount() {
		return videoList.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Video listOfVideos = videoList.get(rowIndex);
		switch(columnIndex) {
		case 0: return listOfVideos.getVideoId();
		case 1: return listOfVideos.getChannel();
		case 2: return null;
		default: return null;
		}
	}

}
