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
public class TMNode {
    private int frequency;
    private String word;
    
    public TMNode(String word, int frequency){
        this.word = word;
        this.frequency = frequency;
    }
    
    public String getWord(){
        return this.word;
    }
    
    public int getFrequency(){
        return this.frequency;
    }
}
