/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumblr;

import java.util.ArrayList;
import java.util.List;
import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

/**
 *
 * @author thiru
 */
public class Sumblr {

    /**
     * @param args the command line arguments
     */
   
    public static void main(String[] args) throws TwitterException {
        // TODO code application logic here
        TwitterStreamData twitterstreamdata = new TwitterStreamData();
        twitterstreamdata.getData();
        
        
    }
    
}
