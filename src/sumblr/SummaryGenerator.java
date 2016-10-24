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
import java.util.Map;
import java.util.NavigableMap;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author thiru
 */
public class SummaryGenerator {
    private ArrayList TweetArray;
    private String summary;
    private TreeSet words;
    private int transitionMatrix[][];
    private PriorityQueue<TMNode> TMqueue[];
    private TMNodeComparator comp;

    public SummaryGenerator() {
        TweetArray = new ArrayList();
        comp = new TMNodeComparator();
        words = new TreeSet();
        
    }
    
    public void addTweet(ArrayList tweet){
        TweetArray.add(tweet);
        for(int i=0;i<tweet.size();i++){
            words.add(tweet.get(i));
        }
    }
    
    public void computeMatrix(){
        this.transitionMatrix = new int[this.words.size()][this.words.size()];
        for(int i=0;i<TweetArray.size();i++){
            ArrayList tweet = (ArrayList) TweetArray.get(i);
            for(int j=0;j<tweet.size()-1;j++){
                String word1 = (String) tweet.get(j);
                String word2 = (String) tweet.get(j+1);
                int index1 = words.headSet(word1).size();
                int index2 = words.headSet(word2).size();
                this.transitionMatrix[index1][index2]++;
            }
                
        }  
    }
    
    public void generatePriorityQueue(){
        TMqueue = new PriorityQueue[this.words.size()];
        for(int i=0;i<TMqueue.length;i++){
            TMqueue[i] = new PriorityQueue<TMNode>(comp);
            Iterator iterator = words.iterator();
            int j = 0;
            while(iterator.hasNext()){
                String word = (String) iterator.next();
                int frequency = transitionMatrix[i][j];
                TMNode node = new TMNode(word, frequency);
                TMqueue[i].add(node);
                j++;
            }
        }
        
    }
    
    public String generateSummary(TreeMap c){
        NavigableMap centroid = c.descendingMap();
        Set set = centroid.entrySet();
        HashSet read = new HashSet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            Map.Entry CentroidEntry = (Map.Entry) iterator.next();
            String CentroidWord = (String) CentroidEntry.getKey();
            summary += CentroidWord;
            summary += " - ";
            int index = words.headSet(CentroidWord).size();
            boolean found = false;
            do{
                TMNode node = TMqueue[index].poll();
                if(node == null)
                {
                    break;
                }
                if(c.containsKey(node.getWord()) && !read.contains(node.getWord()))
                {
                    found = true;
                    summary += node.getWord();
                    summary += " - ";
                    read.add(node.getWord());
                }
                
            }while(!found);
            
            
        }
        return summary;
        
    }
    
}
