package crawl.url;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawl.objects.Video;

// TODO create a repeatable-on-click tag generator with ArrayLists

public class MetaData {

	/** Global variables to be used*/
	private static String youtubeMetaTag = "<meta property=\"og:video:tag\" content=\"";
	private static String lookUntil = "<meta property=\"fb:app_id\"";
	private static String tagStart = "t=\"";
	private static String tagEnd = "\">";
	private static String idStart = "href=\"/user/";
	private static String idEnd = "\" class=\"yt-user-photo g-hovercard yt-uix-sessionlink";
	private static String viewStart = "class=\"watch-view-count\">";
	private static String viewEnd = "views</div>";
	private static String descStart = "<p id=\"eow-description\" class=\"\" >";
	private static String descEnd = "</p></div>  <div id=\"watch-description-extras\">";
	private static String likeStart = "aria-label=\"like this video along with ";
	private static String likeEnd1 = " other people\" title=\"";
	private static String likeEnd2 =  " other person\" title=\"";
	private static String likeEnd3 =  " other people\" data";
	private static String dislikeStart = "aria-label=\"dislike this video along with ";
	private static String dislikeEnd1 = " other people\" title=\"";
	private static String dislikeEnd2 = " other person\" title=\"";
	private static String dislikeEnd3 = " other person\" data-orientation=\"";
	private static String channelStart = "<script type=\"application/ld+json\" >";
	private static String channelEnd = "<link itemprop=\"thumbnailUrl\" href";
	private static String subStart = "<span class=\"yt-subscription-button-subscriber-count-branded-horizontal yt-subscriber-count\" title=\"";
	private static String subEnd = "\" aria-label=\"";
	private static String genreStart = "<meta itemprop=\"genre\" content=\"";
	private static String genreEnd = "<div id=\"watch7-speedyg-area\">";
	private static String durStart = "<meta itemprop=\"duration\" content=\"";
	private static String durEnd = "<meta itemprop=\"unlisted\" content=\"";
	private static String pubStart = "<meta itemprop=\"datePublished\" content=\"";
	private static String replacement = "\n";
	private static String regexTagRemoval = "<[^>]*>";
	
	/**
	 *  Gets the type of video under its category
	 * @param URLSourceCode the URLsource code
	 * @return the type of video
	 */
	public static String getType(String URLSourceCode) {
		StringBuilder builtString = new StringBuilder();
		builtString.append(URLSourceCode.substring(URLSourceCode.lastIndexOf(genreStart) + genreStart.length(),URLSourceCode.indexOf(genreEnd)).replace("\">", "").replaceAll("amp;", ""));
		if (builtString.toString().length() > 20) {
			return builtString.toString().substring(0, builtString.toString().indexOf("   "));
		}
		return builtString.toString();
	}
	/**
	 *  Gets the duration of the video
	 * @param URLSourceCode the URL's source code
	 * @return duration of the video
	 */
	public static String getDuration(String URLSourceCode) {
		StringBuilder builtString = new StringBuilder();
		builtString.append(URLSourceCode.substring(URLSourceCode.indexOf(durStart) + durStart.length(),URLSourceCode.indexOf(durEnd)).replace("\">", "").replaceAll("PT", "").replaceAll("M", "(Minutes) ").replaceAll("S", "(Seconds) "));
		return builtString.toString();
	}
	
	/**
	 *  Returns the name of the channel
	 * @param URLSourceCode
	 * @return  channel name
	 */
	public static String getChannel(String URLSourceCode) {
		StringBuilder builtString = new StringBuilder();
		builtString.append(URLSourceCode.substring(URLSourceCode.lastIndexOf(idStart) + 5, URLSourceCode.lastIndexOf(idEnd)).substring(7));
		if (builtString.toString().length() > 100) {
			builtString.delete(0, builtString.length());
			System.out.println("Going through the enhanced parser");
			String newURLSourceCode = URLSourceCode.substring(URLSourceCode.indexOf(channelStart), URLSourceCode.indexOf(channelEnd));
			String startName = "\"name\": ";
			builtString.append(newURLSourceCode.substring(newURLSourceCode.indexOf(startName) + startName.length(), nthOccurrenceOf(newURLSourceCode, "}",1)).replaceAll("[^a-zA-Z]", ""));
			if (builtString.toString().length() > 100) {
				return "Unknown Channel";
			}
		}
		return builtString.toString();
	}
	
	/**
	 *  Gathers the subscribers from a channel given its video
	 * @param URLSourceCode urls source code
	 * @return the amount of subs on authors channel.
	 */
	public static String getChannelSubs(String URLSourceCode) {
		StringBuilder builtString = new StringBuilder();
		try {
			builtString.append(URLSourceCode.substring(URLSourceCode.indexOf(subStart) + subStart.length(), URLSourceCode.indexOf(subStart) + subStart.length() + 5).replaceAll("\"", "").replaceAll("(\\s+[a-z](?=\\s))", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builtString.toString();
	}
	
	/**
	 *  Determines the liking of a video
	 * @param theVideo video to be looked at
	 * @param isLikeRatio if were looking at the likes or dislikes
	 * @return respective ratio
	 */
	public static int getLikeRatio(Video theVideo, boolean isLikeRatio) {

			int likeRatio = Integer.valueOf(theVideo.getLikes().replace("[^0-9]", "").replaceAll(",", ""));
			int dislikeRatio = Integer.valueOf(theVideo.getDislikes().replace("[^0-9]", "").replaceAll(",", ""));
			int total = Integer.valueOf(likeRatio) + Integer.valueOf(dislikeRatio);
			if (isLikeRatio) {
				int likeAmount = likeRatio* 100 / total;
				return likeAmount;
			} else {
				int dislikeAmount = dislikeRatio * 100 / total;
				return dislikeAmount;
			}
	}
	
	/**
	 *  returns the length of the video
	 * @param URLSourceCode the video to be looked at
	 * @return the video length
	 */
	public static String getTime(String URLSourceCode) {
		StringBuilder builtString = new StringBuilder();
		String result = "";
		String day = URLSourceCode.substring(URLSourceCode.indexOf(pubStart) + pubStart.length(), URLSourceCode.indexOf(pubStart) + pubStart.length() + 10).substring(8, 10);
		String month = URLSourceCode.substring(URLSourceCode.indexOf(pubStart) + pubStart.length(), URLSourceCode.indexOf(pubStart) + pubStart.length() + 10).substring(5,7);
		String year = URLSourceCode.substring(URLSourceCode.indexOf(pubStart) + pubStart.length(), URLSourceCode.indexOf(pubStart) + pubStart.length() + 10).substring(0, 4);
		builtString.append(URLSourceCode.substring(URLSourceCode.indexOf(pubStart) + pubStart.length(), URLSourceCode.indexOf(pubStart) + pubStart.length() + 10));
		result = month + "(Month) /" + day + "(Day) /" + year + "(Year)";
		return result;
	}
	
	/**
	 * The description of the video
	 * @param URLSourceCode - the video to be looked at
	 * @return the description of the video
	 */
	public static String getDescript(String URLSourceCode) {
		StringBuilder builtString = new StringBuilder();
		builtString.append(URLSourceCode.substring(URLSourceCode.lastIndexOf(descStart) + descStart.length(), URLSourceCode.lastIndexOf(descEnd)));
		return builtString.toString().replaceAll(regexTagRemoval, replacement);
	}
	
	/**
	 * The tags of the video
	 * @param URLSourceCode - the video to be looked at 
	 * @return the tags found within the video
	 */
	public static String getTags(String URLSourceCode) {
		ArrayList<String> videoTags = new ArrayList<String>();
		try {
			String tags = URLSourceCode.substring(URLSourceCode.indexOf(youtubeMetaTag), URLSourceCode.indexOf(lookUntil));
			for (int count = 0; count <getStringFrequency(tags, youtubeMetaTag); count++) {
				videoTags.add(new String(tags.substring(nthOccurrenceOf(tags,tagStart,count + 1) + 3, nthOccurrenceOf(tags,tagEnd, count + 1))));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return Arrays.toString(videoTags.toArray());
	}
	
	/**
	 * Get the video views of the video
	 * @param URLSourceCode - the video to be looked at
	 * @return views of the video
	 */
	public static String getViews(String URLSourceCode) {
		StringBuilder builtString = new StringBuilder();
		try {
			builtString.append(URLSourceCode.substring(URLSourceCode.lastIndexOf(viewStart) + viewStart.length(), URLSourceCode.lastIndexOf(viewEnd)));
		} catch (Exception ex) {
			builtString.append("");
		}
		return builtString.toString();
	}
	
	/**
	 * The amount of likes to be found within the video
	 * @param URLSourceCode - the video to be looked at 
	 * @return the amount of likes found within the video
	 */
	public static String getLikes(String URLSourceCode) {
		StringBuilder builtString = new StringBuilder();
		try {
			if(URLSourceCode.contains(likeEnd1)) {
				//System.out.println("Number 1");
				builtString.append(URLSourceCode.substring(URLSourceCode.indexOf(likeStart) + likeStart.length(), nthOccurrenceOf(URLSourceCode,likeEnd1,1)));
			} else if (URLSourceCode.contains(likeEnd2)) {
				//System.out.println("Number 2");
				builtString.append(URLSourceCode.substring(URLSourceCode.indexOf(likeStart) + likeStart.length(), nthOccurrenceOf(URLSourceCode,likeEnd2,1)));
			} else if (URLSourceCode.contains(likeEnd3)) {
				//System.out.println("Number 3");
				builtString.append(URLSourceCode.substring(URLSourceCode.indexOf(likeStart) + likeStart.length(), nthOccurrenceOf(URLSourceCode,likeEnd3,1)));
			}
		} catch (Exception ex) {
			builtString.append("");
		}
		
		if (builtString.toString().equals("0")) {
			return "0";
		}
		return builtString.toString();
	}
	
	/**
	 * get the dislikes of a video based off of its link
	 * @param URLSourceCode - the video to be looked at
	 * @return the amount of dislikes found within the video
	 */
	public static String getDislikes(String URLSourceCode) {
		StringBuilder builtString = new StringBuilder();
		try {
			if(URLSourceCode.contains(dislikeEnd1)) {
				builtString.append(URLSourceCode.substring(URLSourceCode.indexOf(dislikeStart) + dislikeStart.length(),  nthOccurrenceOf(URLSourceCode,dislikeEnd1,3)));
				//System.out.println("The first one");
			} else if (URLSourceCode.contains(dislikeEnd2)) {
				builtString.append(URLSourceCode.substring(URLSourceCode.indexOf(dislikeStart) + dislikeStart.length(),  nthOccurrenceOf(URLSourceCode,dislikeEnd2,3)));
				//System.out.println("The second one");
			} else if (URLSourceCode.contains(dislikeEnd3)) {
				//System.out.println("The third one");
				builtString.append(URLSourceCode.substring(URLSourceCode.indexOf(dislikeStart) + dislikeStart.length(), nthOccurrenceOf(URLSourceCode,dislikeEnd3,3)));

			}
		} catch (Exception ex) {
			builtString.append("");
		}
		
		if (builtString.toString().equals("0")) {
			return "0";
		}
		return builtString.toString();
	}
	/**
	 * Find the specific occurrence of a string within a string
	 * @param str The string to be looked at
	 * @param substr The String occurrence we're looking for
	 * @param n the occurrence of the substring
	 * @return the specific occurrence of a string
	 */
	private static int nthOccurrenceOf(String str, String substr, int n) {
	    int pos = str.indexOf(substr);
	    while (--n > 0 && pos != -1) {
	        pos = str.indexOf(substr, pos + 1);
	    }
	    return pos;
	}
	
	/**
	 *  Find the frequency of a string 
	 * @param string the string to be looked at
	 * @param substring the substring to be found within the string
	 * @return the amount of times the substring appears
	 */
	private static int getStringFrequency(String string, String substring) {
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){
		    lastIndex = string.indexOf(substring,lastIndex);
		    if(lastIndex != -1){
		        count ++;
		        lastIndex += substring.length();
		    }
		}
		return count;
	}
}
