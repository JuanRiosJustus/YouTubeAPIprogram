package com.google.api.services.youtube.cmdline.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import crawl.gui.SortPanel;
import crawl.objects.Video;
import crawl.url.MetaData;
import crawl.url.ProjectHandler;

public class Connector {
	
	public Connector()
	{
		
	}
	
	public static void toGUI(List<Video> listOfVideos)
	{
		SortPanel.getTextArea().append("\n" + ProjectHandler.getGreatestDivide());
		getTopVideoContents(listOfVideos);
		SortPanel.getTextArea().append("\n" + ProjectHandler.getGreaterDivide());
		getMostDislikedVideo(listOfVideos);
		SortPanel.getTextArea().append("\n" + ProjectHandler.getGreaterDivide());
		getMostLikedVideo(listOfVideos);
		SortPanel.getTextArea().append("\n" + ProjectHandler.getLesserDivide());
		getMostViewedVideo(listOfVideos);
		SortPanel.getTextArea().append("\n" + ProjectHandler.getLesserDivide());
		getMostFrequantChannel(listOfVideos);
		SortPanel.getTextArea().append("\n" + ProjectHandler.getLesserDivide());
	}
	
	// print the top videos
	private static void getTopVideoContents(List<Video> listOfVideos) {
		SortPanel.getTextArea().append("\n First video found: " + "\"" + listOfVideos.get(0).getTitle());
		SortPanel.getTextArea().append("\n Video author: " + listOfVideos.get(0).getChannel() 
				+ " (Subs:)" + listOfVideos.get(0).getChannelSubs());
		SortPanel.getTextArea().append("\n Video likes: " + listOfVideos.get(0).getLikes());
		SortPanel.getTextArea().append("\n Video dislikes: " + listOfVideos.get(0).getDislikes());
		SortPanel.getTextArea().append("\n Video views: " + listOfVideos.get(0).getViews());
		SortPanel.getTextArea().append("\n Video Tags: " + listOfVideos.get(0).getTags());
	}
	
	private static void getMostDislikedVideo(List<Video> listOfVideos) {
		int dislikeResult = 0;
		int dislikeIndex = 0;
		int mostDisliked = 0;
		for (int count = 0; count < listOfVideos.size(); count ++) {
			try {
				while (listOfVideos.get(count).getDislikes().replaceAll(",", "").replaceAll(" ", "").equals("No") ||
						listOfVideos.get(count).getDislikes().replaceAll(",", "").replaceAll(" ", "").equals("")) {
					count++;
				}
				if(MetaData.getLikeRatio(listOfVideos.get(count), false) > mostDisliked) {
					mostDisliked = MetaData.getLikeRatio(listOfVideos.get(count), false);
					dislikeResult = Integer.valueOf(listOfVideos.get(count).getDislikes().replaceAll("[^0-9]", ""));
					dislikeIndex = count;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("DONE WITH DISLIKES");
		SortPanel.getTextArea().append("\n Most hated video: " + listOfVideos.get(dislikeIndex).getTitle());
		SortPanel.getTextArea().append("\n Rough precentage of dislikes: " + String.valueOf(mostDisliked) +  "%");
		SortPanel.getTextArea().append("\n Amount of dislikes: " + dislikeResult);
		SortPanel.getTextArea().append("\n Amount of views: " + listOfVideos.get(dislikeIndex).getViews());
		SortPanel.getTextArea().append("\n Video Tags: " + listOfVideos.get(dislikeIndex).getTags());
	}
	
	private static void getMostLikedVideo(List<Video> listOfVideos) {
		int likeResult = 0;
		int likeIndex = 0;
		int mostLiked = 0;
		for (int count = 0; count < listOfVideos.size(); count ++) {
			try {
				while (listOfVideos.get(count).getLikes().replaceAll(",", "").replaceAll(" ", "").equals("No") ||
						listOfVideos.get(count).getLikes().replaceAll(",", "").replaceAll(" ", "").equals("")) {
					count++;
				}
				if(MetaData.getLikeRatio(listOfVideos.get(count), true) > mostLiked) {
					mostLiked = MetaData.getLikeRatio(listOfVideos.get(count), true);
					likeResult = Integer.valueOf(listOfVideos.get(count).getLikes().replaceAll("[^0-9]", ""));
					likeIndex = count;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("DONE WITH LIKES");
		SortPanel.getTextArea().append("\n Most liked video: " + listOfVideos.get(likeIndex).getTitle());
		SortPanel.getTextArea().append("\n Rough precentage of likes: " + String.valueOf(mostLiked) + "%" );
		SortPanel.getTextArea().append("\n Amount of likes: " + likeResult);
		SortPanel.getTextArea().append("\n Amount of views: " + listOfVideos.get(likeIndex).getViews());
		SortPanel.getTextArea().append("\n  Video Tags: " + listOfVideos.get(likeIndex).getTags());
	}
	
	private static void getMostViewedVideo(List<Video> listOfVideos) {
		int views = 0;
		int index =  0;
		for (int count = 0; count < listOfVideos.size(); count ++) {
			try {

				if (Integer.valueOf(listOfVideos.get(count).getViews().replaceAll(",", "").replaceAll(" ", "")) > views) {
					views = Integer.valueOf(listOfVideos.get(count).getViews().replaceAll(",", "").replaceAll(" ", ""));
					index = count;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("DONE WITH VIEWS");
		SortPanel.getTextArea().append("\n Video with most views " + listOfVideos.get(index).getTitle());
		SortPanel.getTextArea().append("\n Amount of views: " +listOfVideos.get(index).getViews());
		SortPanel.getTextArea().append("\n Amount of dislikes: " +listOfVideos.get(index).getLikes());
		SortPanel.getTextArea().append("\n Amount of likes: " + listOfVideos.get(index).getDislikes());
		SortPanel.getTextArea().append("\n Tags: " +listOfVideos.get(index).getTags());
	}
	
	private static void getMostFrequantChannel(List<Video> listOfVideos) {
		try {
			HashMap <String,Integer> map = new HashMap<String, Integer>();
			StringBuilder string = new StringBuilder();
			int maxValue = Integer.MIN_VALUE;
			int temp = 0;
			for (int index = 0; index < listOfVideos.size(); index++) {
				if (map.containsKey(listOfVideos.get(index).getChannel())) {
					temp = map.get(listOfVideos.get(index).getChannel()) + 1;
					map.put(listOfVideos.get(index).getChannel(), temp);
					System.out.println("Value for " + listOfVideos.get(index).getChannel() + " Incremented to " + temp);
					System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
					//map.
				} else {
					map.put(listOfVideos.get(index).getChannel(), 1);
					System.out.println(listOfVideos.get(index).getChannel() + " Was added to the map");
					System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
				}
				temp = 1;
			}
			for (Entry<String,Integer> entry : map.entrySet()) {
				if (maxValue < entry.getValue() ) {
					if (string.length() > 1) {
						string.delete(0, string.length());
					}
					maxValue = entry.getValue();
					string.append(entry.getKey());
				}
			}
			System.out.println("The most frequent channel is " + string.toString() + " appearing " + maxValue + " times.");
			SortPanel.getTextArea().append("\n The most frequent channel is " + string.toString() + " appearing " + maxValue + " times.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
