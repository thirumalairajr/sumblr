/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumblr;

/**
 *
 * @author thiru
 */
import java.util.List;
import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

public class TwitterStreamData {
    
    final static String FILTER_TOPICS[] = {"trump"};
    final static int NO_OF_FILTERS = 1;
    private final static String LANGUAGE = "en";
    private final static String CONSUMER_KEY = "NtBdTUaaUG63tXPlkp9SdcRP6";
    private final static String CONSUMER_KEY_SECRET = "iQuz45ijY2iKXNHVViisCCzuUx4aEaz7q0goP8eruACjITHzCL";
    private final static String ACCESS_TOKEN = "776376101207613440-lTzo2MWpEEYyUdwBZP994lvm6p3cKhi";
    private final static String ACCESS_TOKEN_SECRET = "7KjqNQFZYuX29kNvTHVQ0VSQJlAXTnfxMhgBttpJ444Mg";
    private TwitterStream twitterStream;
    private AccessToken oathAccessToken;
    private StatusListener listener;
    private FilterQuery fq;
    private Preprocessor preprocessor;
    private final static int TRAINING_DATA_SIZE = 100;
    private static int tweetCount = 0;
    
    
    public TwitterStreamData()
    {
        this.oathAccessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
        this.twitterStream = new TwitterStreamFactory().getInstance();
        this.twitterStream.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        this.twitterStream.setOAuthAccessToken(oathAccessToken);
        this.fq = new FilterQuery();
        preprocessor = new Preprocessor();
        
    }
    
    private void setLanguage(String lang)
    {
        this.fq.language(lang);
    }
    
    private void addFilter()
    {
        this.fq.track(FILTER_TOPICS);
    }
            
            
    
    public void getData()
    {
        this.listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                String statusText = status.getText();
                statusText = statusText.toLowerCase();
                
                tweetCount++;
                if(statusText.substring(0,4).equals("rt @"))
                {
                    String str[] = statusText.split(":");
                    statusText = "";
                    for(int i=1;i<str.length;i++)
                    {
                        statusText += str[i];
                    }
                    
                    
                    preprocessor.preprocess(tweetCount,statusText);
                   
                    //System.out.println(statusText);
                    //System.out.println(preprocessor.preprocess(tweetCount));
                }
                else
                {
                    //System.out.println( statusText);
                    
                    preprocessor.preprocess(tweetCount,statusText);
                    //System.out.println(preprocessor.preprocess(tweetCount));
                }
                
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
               // System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            
        };
        this.twitterStream.addListener(listener);
        this.setLanguage(LANGUAGE);
        this.addFilter();
        this.twitterStream.filter(this.fq);
        
    }
    
    public void getSampleData()
    {
        this.listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
               
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
               
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        this.twitterStream.addListener(listener);
        this.twitterStream.sample();
    }
    
}
