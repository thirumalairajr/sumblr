/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumblr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author thiru
 */
public class InformationRetrieval {
    private static ArrayList data;
    private static HashMap docCount;
    private static HashMap idf;
    private static ArrayList tfarr;
    private static int NoofDocs;
    private static ArrayList tfidfarr;
    private static TweetClusterVector tcv[];
    public InformationRetrieval()
    {
        this.tfarr = new ArrayList();
        this.docCount = new HashMap();
        this.idf = new HashMap();
        this.NoofDocs = 0;
        this.tfidfarr = new ArrayList();
        
    }
    
    public void tfIDF(ArrayList givenData)
    {
        this.data = givenData;
        this.NoofDocs += data.size();
        for(int i=0;i<data.size();i++)
        {
            ArrayList tweet = (ArrayList) data.get(i);
            HashMap tf = new HashMap();
            for(int j=0;j<tweet.size();j++)
            {
                if(!tf.containsKey((String) tweet.get(j)))
                {
                    tf.put(tweet.get(j),1);
                }
                else
                {
                    tf.put(tweet.get(j),(int) tf.get(tweet.get(j))+1);
                }
            }
            for(Object key: tf.keySet())
            {
                String keyString = (String) key;
                if(!docCount.containsKey(keyString))
                {
                    docCount.put(keyString, 1);
                }
                else
                {
                    docCount.put(keyString,(int) docCount.get(keyString)+1);
                }
            }

            
            tfarr.add(tf);
            
        }
        for(Object key: docCount.keySet())
        {
            //System.out.println((String) key +":"+((double)this.NoofDocs)/(int)docCount.get((String) key));
            idf.put((String)key,((double)this.NoofDocs)/(int)docCount.get((String) key) );
        }
        for(int i=0;i<tfarr.size();i++)
        {
            HashMap tf =  (HashMap) tfarr.get(i);
            HashMap tfidf = new HashMap();
            for(Object key: tf.keySet())
            {
                double value;
                if(idf.containsKey((String) key))
                {
                    value = (1+Math.log((int) tf.get((String)key)))*Math.log((double) idf.get((String) key));
                }
                else
                {
                    value = 1+Math.log((double) tf.get((String)key));
                }
                tfidf.put((String)key, value);
            }
            tfidfarr.add(tfidf);
        }
        //System.out.println(tfidfarr);
    }
    
    public HashMap addTweet(ArrayList tweet)
    {
        HashMap tf = new HashMap();
            for(int j=0;j<tweet.size();j++)
            {
                if(!tf.containsKey((String) tweet.get(j)))
                {
                    tf.put(tweet.get(j),1);
                }
                else
                {
                    tf.put(tweet.get(j),(int) tf.get(tweet.get(j))+1);
                }
            }
            for(Object key: tf.keySet())
            {
                String keyString = (String) key;
                if(!docCount.containsKey(keyString))
                {
                    docCount.put(keyString, 1);
                }
                else
                {
                    docCount.put(keyString,(int) docCount.get(keyString)+1);
                }
            }

            
            tfarr.add(tf);
            for(Object key: docCount.keySet())
            {
                //System.out.println((String) key +":"+((double)this.NoofDocs)/(int)docCount.get((String) key));
                idf.put((String)key,((double)this.NoofDocs)/(int)docCount.get((String) key) );
            }
            HashMap tfidf = new HashMap();
            for(Object key: tf.keySet())
            {
                double value;
                if(idf.containsKey((String) key))
                {
                    value = (1+Math.log((int) tf.get((String)key)))*Math.log((double) idf.get((String) key));
                }
                else
                {
                    value = 1+Math.log((double) tf.get((String)key));
                }
                tfidf.put((String)key, value);
            }
            tfidfarr.add(tfidf);
            return tfidf;
    }
        
        
    public TweetClusterVector[] kmeans()
    {
        tcv = new TweetClusterVector[TwitterStreamData.NO_OF_FILTERS];
        int i;
        for(i=0;i<TwitterStreamData.NO_OF_FILTERS;i++)
        {
            TreeMap h = new TreeMap();
            h.put(TwitterStreamData.FILTER_TOPICS[i], 0.3010);
            //tcv[i] = new TweetClusterVector((HashMap) tfidfarr.get(i));
            tcv[i] = new TweetClusterVector(h);
            //tcv[i].addTweet((HashMap) tfidfarr.get(i));
        }
        TreeMap prevCentroid[] = new TreeMap[TwitterStreamData.NO_OF_FILTERS];
        for(i=0;i<TwitterStreamData.NO_OF_FILTERS;i++)
        {
            prevCentroid[i] = tcv[i].getCentroid();
            
        }
        /*for(i=0;i<tfidfarr.size();i++)
        {
            double min = Float.MAX_VALUE;
            int index = 0;
            for(int j=0;j<TwitterStreamData.NO_OF_FILTERS;j++)
            {
                double distance = cosineDistance(tcv[j].getCentroid(), (HashMap) tfidfarr.get(i));
                if(distance<min)
                {
                    min = distance;
                    index = j;
                }
            }
            tcv[index].addTweet((HashMap) tfidfarr.get(i));
        }
        HashMap prevCentroid[] = new HashMap[TwitterStreamData.NO_OF_FILTERS];
        for(i=0;i<TwitterStreamData.NO_OF_FILTERS;i++)
        {
            prevCentroid[i] = tcv[i].CalculateCentroid();
            
        }*/
        for(int k=0;k<1000;k++)
        {
            System.out.println("Running iteration "+k);
            for(i=0;i<TwitterStreamData.NO_OF_FILTERS;i++)
            {
                tcv[i] = new TweetClusterVector(prevCentroid[i]);
                
            }
            for(i=0;i<tfidfarr.size();i++)
            {
                double min = Float.MAX_VALUE;
                int index = 0;
                for(int j=0;j<TwitterStreamData.NO_OF_FILTERS;j++)
                {
                    double distance = cosineDistance(tcv[j].getCentroid(), (HashMap) tfidfarr.get(i));
                    if(distance<min)
                    {
                        min = distance;
                        index = j;
                    }
                }
                tcv[index].addTweet((HashMap) tfidfarr.get(i));
            }
            
            for(i=0;i<TwitterStreamData.NO_OF_FILTERS;i++)
            {
                prevCentroid[i] = tcv[i].CalculateCentroid();
            
            }
        }
        return tcv;
    }
    
    public double cosineDistance(TreeMap tweet1, HashMap tweet2)
    {
        
        double distance = 0;
        for(Object str: tweet1.keySet())
        {
            
            if(tweet2.containsKey(str))
            {
                distance += (double) tweet1.get(str) * (double) tweet2.get(str);
            }
        }
        double denominator1 = 0;
        for(Object str: tweet1.keySet())
        {
            denominator1 += (double) tweet1.get(str) * (double) tweet1.get(str);
        }
        denominator1 = (double) Math.sqrt(denominator1);
        
        double denominator2 = 0;
        for(Object str: tweet2.keySet())
        {
            denominator2 += (double) tweet2.get(str) * (double) tweet2.get(str);
        }
        denominator2 = (double) Math.sqrt(denominator2);
            
        distance = distance / (denominator1 + denominator2);
        distance = 1 - distance;
        return (double) distance;
    }
    
    public TweetClusterVector[] IncrementalCluster(HashMap tweet)
    {
        int index = 0;
        double min = Double.MAX_VALUE;
        for(int i=0;i<tcv.length;i++){
            double distance = cosineDistance(tcv[i].getCentroid(), tweet);
            if(distance<min){
                min = distance;
                index = i;
            }            
        }
       
        tcv[index].IncrementalClusterCentroidComputation(tweet);
        return tcv;
                        
        
        
    }
}
