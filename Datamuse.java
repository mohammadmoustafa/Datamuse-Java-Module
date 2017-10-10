package com.module.datamuse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import com.google.gson.Gson;

public class Datamuse {
	
	private String url = "";
    
    /**
     * Starts the first step in the builder process.
     * 
     * @return Datamuse
     */
    public Datamuse QueryBuilder() {
    	this.url = "http://api.datamuse.com/words?";
    	return this;
    }
    
    /**
     * Adds the 'ml' (means like) endpoint to the query.
     * 
     * @param word The word to query, as a String
     * @return Datamuse
     */
    public Datamuse meansLike(String word) {
    	word = word.replaceAll(" ", "+");
    	if (this.url.equals("http://api.datamuse.com/words?")) {
    		this.url += "ml=" + word;
    	} else {
    		this.url += "&ml=" + word;
    	}
    	return this;
    }
    
    /**
     * Adds a max word limit endpoint to the query.
     * 
     * @param limit The maximum number of words to be returned, as an int
     * @return Datamuse
     */
    public Datamuse max(int limit) {
    	if (this.url.equals("http://api.datamuse.com/words?")) {
    		this.url += "max=" + limit;
    	} else {
    		this.url += "&max=" + limit;
    	}
    	return this;
    }
    
    /**
     * Build the query and retrieve the array of JSON objects the is resulted.
     * 
     * @return DatamuseObject[]
     */
    public DatamuseObject[] build() {
    	return parseJson(url);
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
    
    public static class DatamuseObject {
        public String word;
        public int score;
        public List<String> tags;
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
