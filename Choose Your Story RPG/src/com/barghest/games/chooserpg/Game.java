package com.barghest.games.chooserpg;

import java.io.File;
import java.util.Scanner;

public class Game {

	private String title;
	private String story;
	
	public static String loadStory(String race, int chapter) {
        File file = new File("data/ChooseRPG-" + race + ".txt");
        String pattern = "-Chapter " + chapter + "-";
        int nextChap = chapter + 1;
        String endpattern = "-Chapter " + nextChap + "-";
        Boolean foundChap = false;
        
        StringBuilder story = new StringBuilder();
        try {
	        Scanner sc = new Scanner(file);
	        sc.reset();
	        while(sc.hasNextLine()) {
	        	String line = sc.nextLine();
	        	if (line.matches(pattern)) { foundChap = true; }
	        	else if (line.matches(endpattern)) { foundChap = false; }
	        	else if (foundChap) {
	        		//System.out.println(line);
	        		story.append(line + "\n");
	        	}
	        }
	        sc.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return story.toString();
    }
}
