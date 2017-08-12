package crawl.url;

import java.net.*;
import java.io.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLReader {
	 public static String getUrlSource(String url) throws IOException {
         URL someURL = new URL(url);
         URLConnection yc = someURL.openConnection();
         BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
         String inputLine;
         StringBuilder builtString = new StringBuilder();
         while ((inputLine = in.readLine()) != null) {
        	 builtString.append(inputLine);
         }
         in.close();

         return builtString.toString();
     }
	 public static void main(String[] args) {
	 }
}