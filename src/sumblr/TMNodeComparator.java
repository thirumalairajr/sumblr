/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumblr;

import java.util.Comparator;

/**
 *
 * @author thiru
 */
public class TMNodeComparator implements Comparator<TMNode>{

    @Override
    public int compare(TMNode n1, TMNode n2) {
        
         return Integer.compare(n1.getFrequency(), n2.getFrequency());
    }
    
}
