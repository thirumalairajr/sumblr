/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumblr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author thiru
 */
public class TweetClusterVector {
    private ArrayList tweetsTFIDF;
    private TreeMap centroid;

    public TweetClusterVector(TreeMap centroid) {
        this.tweetsTFIDF = new ArrayList();
        this.centroid = centroid;
        
    }
    
    public void addTweet(HashMap tweet)
    {
        this.tweetsTFIDF.add(tweet);
    }
    
    public TreeMap getCentroid()
    {
        return this.centroid;
        
    }
    
    public void removeTweet(HashMap tweet)
    {
        this.tweetsTFIDF.remove(tweet);
    }
    
    public ArrayList getTweetCLusterVector()
    {
        return this.tweetsTFIDF;
    }
    
    public TreeMap CalculateCentroid()
    {
        this.centroid = new TreeMap();
        for(int i=0;i<tweetsTFIDF.size();i++)
        {
            HashMap tweet = (HashMap) tweetsTFIDF.get(i);
            for(Object str :  tweet.keySet())
            {
                if(this.centroid.containsKey(str))
                {
                    this.centroid.put(str,(double) this.centroid.get(str)+(double) tweet.get(str));
                }
                else
                {
                    this.centroid.put(str, tweet.get(str));
                }
            }
        }
        for(Object str : this.centroid.keySet())
        {
            this.centroid.put(str, (double) this.centroid.get(str) / (double) tweetsTFIDF.size());
        }
        return this.centroid;
    }
    
    public void displayCluster()
    {
        for(int i=0;i<this.tweetsTFIDF.size();i++)
        {
            System.out.println(tweetsTFIDF.get(i));
        }
    }
            
    public void IncrementalClusterCentroidComputation(HashMap tweet){
        this.addTweet(tweet);
        for(Object str :  tweet.keySet())
            {
                if(this.centroid.containsKey(str))
                {
                    this.centroid.put(str,(double) this.centroid.get(str) * (double) tweetsTFIDF.size()+(double) tweet.get(str));
                }
                else
                {
                    this.centroid.put(str, tweet.get(str));
                }
            }
        
        for(Object str : this.centroid.keySet())
        {
            this.centroid.put(str, (double) this.centroid.get(str) / (double) tweetsTFIDF.size());
        }
    }
    
}
