/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.youtube.cmdline.data;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.cmdline.Auth;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import crawl.gui.FetchPanel;
import crawl.gui.SortPanel;
import crawl.objects.Video;
import crawl.url.MetaData;
import crawl.url.ProjectHandler;
import crawl.url.URLReader;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
public class Search {

    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
    private static final String PROPERTIES_FILENAME = "youtube.properties";
    private static ArrayList<Video> videoList = new ArrayList<Video>();
    private static Video currentVideo;
    private static int videosSearched = 0;
    
    public Search() {
    }

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     * @param videosToFind - amount of videos within the the query for user to seearch through
     * @param searchedTerm - Term to search
     */
    public static void locateVideoInformation (long videosToFind, String searchedTerm) {
    	
        // Read the developer key from the properties file.
    	StringBuilder stringbuilder = new StringBuilder();
    	stringbuilder.append(searchedTerm);
        Properties properties = new Properties();
        videosSearched = (int)videosToFind;
        try {
            InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-search").build();

            String queryTerm = stringbuilder.toString();

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. 
            String apiKey = properties.getProperty("youtube.apikey");
            search.setKey(apiKey);
            search.setQ(queryTerm);

            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(videosToFind);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                prettyPrint(searchResultList.iterator(), queryTerm, videosToFind);
            }
        } catch (GoogleJsonResponseException ex) {
            FetchPanel.getGenArea().append("There was a service error: " + ex.getDetails().getCode() + " : "
                    + ex.getDetails().getMessage());
        } catch (IOException ex) {
        	FetchPanel.getGenArea().append("There was an IO error: " + ex.getCause() + " : " + ex.getMessage());
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Prints out all results in the Iterator.
     *
     * @param iteratorSearchResults - Iterator of SearchResults to print
     * @param query - Term to be searched
     */
    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query, long videosToFind) {
    	if (videoList.size() > 0) {
    		videoList.clear();
    	}
    	FetchPanel.getGenArea().setText("");
    	
        System.out.println("\n=============================================================");
        System.out.println(
                "Finding the first " +videosToFind + " video(s) for search on \"" + query + "\".");
        System.out.println("=============================================================\n");
        FetchPanel.getGenArea().append("\n=============================================================");
        FetchPanel.getGenArea().append("\n  First " + videosToFind + " videos for search on \"" + query + "\".");
        FetchPanel.getGenArea().append("\n=============================================================");
        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }
        	while (iteratorSearchResults.hasNext()) {

            	SearchResult singleVideo = iteratorSearchResults.next();
            	ResourceId rId = singleVideo.getId();

            	// Confirm that the result represents a video. Otherwise, the
            	// item will not contain a video ID.
            	if (rId.getKind().equals("youtube#video")) {
                	Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                	
                try {
                	currentVideo = new Video();
            	   	FetchPanel.getGenArea().append(ProjectHandler.getGreatestDivide());
                   	FetchPanel.getGenArea().append("\n Video: " + ProjectHandler.getYouTubeURLdef() + rId.getVideoId());
                   	FetchPanel.getGenArea().append("\n Title: " + singleVideo.getSnippet().getTitle());
                   	FetchPanel.getGenArea().append("\n Views: " + MetaData.getViews(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
                   	FetchPanel.getGenArea().append("\n Likes: " + MetaData.getLikes(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId()))
                   	//MetaData.getLikeRatio(theVideo, isLikeRatio)
                   			);
                   	FetchPanel.getGenArea().append("\n Dislikes: " + MetaData.getDislikes(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
                   	FetchPanel.getGenArea().append("\n Channel: " + MetaData.getChannel(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
                   	
                   	FetchPanel.getGenArea().append("\n Genre: " +  MetaData.getType(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
                   	FetchPanel.getGenArea().append("\n Duration: " +  MetaData.getDuration(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
                   	
                   	FetchPanel.getGenArea().append("\n Subs: " +  MetaData.getChannelSubs(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
                   	FetchPanel.getGenArea().append("\n Thumbnail: " + thumbnail.getUrl());
                   	FetchPanel.getGenArea().append("\n Published: " + MetaData.getTime(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
                   	FetchPanel.getGenArea().append("\n Tags: " + MetaData.getTags(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
                   	if (FetchPanel.getShowDescription()) {
                   		System.out.println("Showing description");
                   		FetchPanel.getGenArea().append(ProjectHandler.getLesserDivide());
                       	FetchPanel.getGenArea().append("\n Description: " + MetaData.getDescript(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
                   	}
            	   	FetchPanel.getGenArea().append(ProjectHandler.getLesserDivide());
            	   	
            	   	currentVideo.setVideoId(rId.getVideoId());
            	   	currentVideo.setTitle(singleVideo.getSnippet().getTitle());
            	   	currentVideo.setViews(MetaData.getViews(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
            	   	currentVideo.setLikes(MetaData.getLikes(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
            	   	currentVideo.setDislikes(MetaData.getDislikes(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
            	   	currentVideo.setChannel(MetaData.getChannel(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
            	   	currentVideo.setDuration(MetaData.getDuration(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
            	   	currentVideo.setType(MetaData.getType(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
            	   	currentVideo.setChannelSubs(MetaData.getChannelSubs(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
            	   	currentVideo.setThumbnail(thumbnail.getUrl());
            	   	currentVideo.setDate(MetaData.getTime(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
            	   	currentVideo.setTags(MetaData.getTags(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
            	   	currentVideo.setDescription( MetaData.getDescript(URLReader.getUrlSource(ProjectHandler.getYouTubeURLdef() + rId.getVideoId())));
            	   	
            	   	videoList.add(currentVideo);
            	   	System.out.println("The current size of the query: "  +videoList.size());
            	   	
               } catch (IOException ex) {
					ex.printStackTrace();
				}
            }
        }
        	updateGUI(videoList);
    }
    public static ArrayList<Video> getVideoList() {
    	return videoList;
    }
    public static int getVideoAmount() {
    	return videosSearched;
    }
	private static void updateGUI(ArrayList<Video> listOfVideos) {
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
	
	/**
	 *  displays information of the first video found
	 * @param listOfVideos videos in the query
	 */
	private static void getTopVideoContents(ArrayList<Video> listOfVideos) {
		SortPanel.getTextArea().append("\n First video found: " + "\"" + listOfVideos.get(0).getTitle());
		SortPanel.getTextArea().append("\n Video author: " + listOfVideos.get(0).getChannel() + " (Subs:)" + listOfVideos.get(0).getChannelSubs());
		SortPanel.getTextArea().append("\n Video likes: " + listOfVideos.get(0).getLikes());
		SortPanel.getTextArea().append("\n Video dislikes: " + listOfVideos.get(0).getDislikes());
		SortPanel.getTextArea().append("\n Video views: " + listOfVideos.get(0).getViews());
		SortPanel.getTextArea().append("\n Video Tags: " + listOfVideos.get(0).getTags());
	}
	/**
	 *  Prints out the most liked video with priority to the first highests liked video
	 * @param listOfVideos videos that have been searched
	 */
	private static void getMostLikedVideo(ArrayList<Video> listOfVideos) {
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
	/**
	 * prints out the most disliked videos with priority with the ;east liked videos
	 * @param listOfVideosvideos tht have been found in the query
	 */
	private static void getMostDislikedVideo(ArrayList<Video> listOfVideos) {
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
		SortPanel.getTextArea().append("\n  Video Tags: " + listOfVideos.get(dislikeIndex).getTags());
	}
	/**
	 *  prints out the most viewed video with priority to the first
	 * @param listOfVideos videos found within the search
	 */
	private static void getMostViewedVideo(ArrayList<Video> listOfVideos) {
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
	
	/**
	 *  Determine the frequency of a youtube channel
	 * @param listOfVideos
	 */
	private static void getMostFrequantChannel(ArrayList<Video> listOfVideos) {
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
