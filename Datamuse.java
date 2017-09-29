package com.module.datamuse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class Datamuse {
	
	private String url = "";
	
    public Datamuse(){}
    
    public Datamuse QueryBuilder() {
    	this.url = "http://api.datamuse.com/words?";
    	return this;
    }
    
    public Datamuse meansLike(String word) {
    	word = word.replaceAll(" ", "+");
    	if (this.url.equals("http://api.datamuse.com/words?")) {
    		this.url += "ml=" + word;
    	} else {
    		this.url += "&ml=" + word;
    	}
    	System.out.println(this.url);
    	return this;
    }
    
    public Datamuse max(int limit) {
    	if (this.url.equals("http://api.datamuse.com/words?")) {
    		this.url += "max=" + limit;
    	} else {
    		this.url += "&max=" + limit;
    	}
    	System.out.println(this.url);
    	return this;
    }
    
    public DatamuseObject[] query() {
    	String pattern = "(http://api.datamuse.com/words\\?max=)[0-9]+";
    	Pattern r = Pattern.compile(pattern);
    	Matcher m = r.matcher(this.url);
    	if (m.find()) {
    		System.out.println("'max' is the only endpoint in the builder. Will return an empty array.");
    	}
    	return parseJson(url);
    }

    /**
     * Returns words that mean like the input, with a max number of words.
     * 
     * @param user_in word to be queried as a string
     * @param sensitivity max number of words to return as an int
     * @return Array of DatamuseObjects that contain the resulting words
     */
    public DatamuseObject[] meansLikeMax(String user_in, int sensitivity) {
        String s = user_in.replaceAll(" ", "+");
        return parseJson("http://api.datamuse.com/words?ml=" + s + "&max=" + sensitivity);
    }
    
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read); 

            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    
    static class DatamuseObject {
        String word;
        int score;
        List<String> tags;
    }

    private DatamuseObject[] parseJson(String url) {
        String json = null;
		try {
			json = readUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Gson gson = new Gson();        
        DatamuseObject[] page = gson.fromJson(json, DatamuseObject[].class);
        return page;
    }
}
