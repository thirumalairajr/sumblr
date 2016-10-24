/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumblr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import weka.core.Stopwords;
import weka.core.stemmers.SnowballStemmer;

/**
 *
 * @author thiru
 */
public class Preprocessor {
    
    private Stopwords stopwords;
    private String UnprocessedText;
    private ArrayList TextArr;
    private SnowballStemmer stemmer;
    private final static int TRAINING_DATA_SIZE = 10;
    private final static int SUMMARY_GENARATION_POINT = 20;
    private HashMap idfValues;
    private static ArrayList TrainingData;
    private InformationRetrieval ir;
    private SummaryGenerator sg;
    
    public Preprocessor()
    {
        this.stopwords = new Stopwords();
        this.stemmer = new SnowballStemmer();
        
        idfValues = new HashMap();
        TrainingData = new ArrayList();
        sg = new SummaryGenerator();
        
    }
    
    private String removeUrl(String commentstr)
    {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\\\\\))+[\\\\w\\\\d:#@%/;$()~_?\\\\+-=\\\\\\\\\\\\.&]\\*)";
        //String urlPattern = "https.*\\s";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(m.group(i),"").trim();
            i++;
        }
        commentstr = commentstr.replaceAll(urlPattern, "");
        return commentstr;
    }
    
    private void convertToArrayList()
    {
        this.UnprocessedText = removeUrl(UnprocessedText);
        //System.out.println(UnprocessedText);
        this.UnprocessedText = UnprocessedText.replace(",", "");
        this.UnprocessedText = UnprocessedText.replace("!", "");
        this.UnprocessedText = UnprocessedText.replace("&", "");
        this.UnprocessedText = UnprocessedText.replace("?", "");
        this.UnprocessedText = UnprocessedText.replace(".", "");
        this.UnprocessedText = UnprocessedText.replace("'", "");
        this.UnprocessedText = UnprocessedText.replace(":", "");
        this.UnprocessedText = UnprocessedText.replace("*", "");
        this.UnprocessedText = UnprocessedText.replace("#", "");
        this.UnprocessedText = UnprocessedText.replace("@", "");
        this.TextArr = new ArrayList(Arrays.asList(UnprocessedText.split(" ")));
    }
    private void removeStopwords()
    {
        //String TextArr[] = UnprocessedText.split(" ");
        this.convertToArrayList();
        for(int i=0;i<TextArr.size();i++)
        {
            if(this.stopwords.is((String) TextArr.get(i)))
            {
                this.TextArr.remove(i);
                i--;
            }
        }
        
    }
    
    private void stemWords()
    {
        for(int i=0;i<TextArr.size();i++)
        {
            String stemmedWord = this.stemmer.stem((String) this.TextArr.get(i));
            this.TextArr.set(i, stemmedWord);
            
        }
    }
    private void train()
    {
        TrainingData.add(this.TextArr);
        sg.addTweet(this.TextArr);
    }
    public void preprocess(int tweetCount,String ipText)
    {
        this.UnprocessedText = ipText;
        this.removeStopwords();
        //this.stemWords();
        
        if(tweetCount<TRAINING_DATA_SIZE)
        {
            System.out.println("Recieved tweet no."+tweetCount);
            this.train();
        }
        else if(tweetCount == TRAINING_DATA_SIZE)
        {
            System.out.println("Recieved tweet no."+tweetCount);
            //System.out.println(TrainingData);
            this.train();
            ir = new InformationRetrieval();
            ir.tfIDF(TrainingData);
            TweetClusterVector[] tcv = ir.kmeans();
            System.out.println("PreMature Clustering Results");
            for(int i=0;i<tcv.length;i++)
            {
                System.out.println("Cluster "+i+" Data");
                tcv[i].displayCluster();
                System.out.println("********************************");
                System.out.println("********************************");
                System.out.println("********************************");
                System.out.println("Cluster "+i+" Centroid");
                System.out.println(tcv[i].getCentroid());
                System.out.println("********************************");
                System.out.println("********************************");
                System.out.println("********************************");
                System.out.println("End of Cluster");
                System.out.println("********************************");
                System.out.println("********************************");
                System.out.println("********************************");
                System.out.println("********************************");
                System.out.println("********************************");
                System.out.println("********************************");
            }
            
                
                
                
                
                    
        }
        else
        {
            HashMap tweet = ir.addTweet(TextArr);
            sg.addTweet(TextArr);
            System.out.println("Recieved tweet no."+tweetCount);
            TweetClusterVector[] tcv = ir.IncrementalCluster(tweet);
            
            if(tweetCount==SUMMARY_GENARATION_POINT)
            {
                sg.computeMatrix();
                sg.generatePriorityQueue();
                System.out.println("Cluster at the instance of "+SUMMARY_GENARATION_POINT+"th tweet");
                for(int i=0;i<tcv.length;i++)
                {
                    System.out.println("Cluster "+i+" Data");
                    tcv[i].displayCluster();
                    System.out.println("********************************");
                    System.out.println("********************************");
                    System.out.println("********************************");
                    System.out.println("Cluster "+i+" Centroid");
                    System.out.println(tcv[i].getCentroid());
                    System.out.println("********************************");
                    System.out.println("********************************");
                    System.out.println("********************************");
                    System.out.println("End of Cluster");
                    System.out.println("********************************");
                    System.out.println("********************************");
                    System.out.println("********************************");
                    System.out.println("********************************");
                    System.out.println("********************************");
                    System.out.println("********************************");
                    System.out.println("Summary"+i+":");
                    System.out.println(sg.generateSummary(tcv[i].getCentroid()));
                }
                
                
                
                
                
                
                
            }
            
        }
        //return this.TextArr;
    }
    
}

