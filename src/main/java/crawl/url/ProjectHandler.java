package crawl.url;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ProjectHandler {
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int widthDimension = (int) screenSize.getWidth();
    private static int heightDimension = (int) screenSize.getHeight();
    private static String youtubeURLdef = "https://www.youtube.com/watch?v=";
    private static String lesserDivide = "\n-------------------------------------------------------------";
    private static String greaterDivide = "\n=============================================================";
    private static String greatestDivide = "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
    private static final int ALLOWED_TAGS = 38;
    private static Random randomNumber = new Random();
    
	public static int screenWidth() {
		return (widthDimension/2) + 100;
	}
	public static int getRawScreenWidth() {
		return widthDimension;
	}
	public static int screenHeight () {
		return (heightDimension/2) + 100;
	}
	public static int getRawScreenHeight() {
		return heightDimension;
	}
	public static String getYouTubeURLdef() {
		return youtubeURLdef;
	}
	public static int getAllowedTags() {
		return ALLOWED_TAGS;
	}
	public static int getRandomNumber(int maxNum) {
		return randomNumber.nextInt(maxNum + 1);
	}
	public static String getGreaterDivide() {
		return greaterDivide;
	}
	public static String getLesserDivide() {
		return lesserDivide;
	}
	public static String getGreatestDivide() {
		return greatestDivide;
	}
	public static ArrayList<Integer> searchSize() {
		ArrayList<Integer> searchSize = new ArrayList<Integer>();
		for (int count = 0; count < 100; count++) {
			searchSize.add(count);
		}
		return searchSize;
	}
	public static void projectProps() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("Computer resolution: "  + widthDimension + "x" + heightDimension);
		System.out.println("-------------------------------------------------------------");
	}
}
