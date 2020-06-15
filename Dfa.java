package ifes.fsa;

import java.util.List;
import java.util.Set;

/**
 * Interface que define as operações de um AFD.
 * 
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 * @param <S> tipo de objetos que representarão os estados do AFD
 * @param <A> tipo de objetos que representarão os símbolos do alfabeto
 */
public interface Dfa<S,A> {
    
    public Set<A> getAlphabet();
    
    public Set<S> getStates();
    
    public Set<S> getFinalStates();

    public S trans(S si, A x);
    
    public S extTrans(S si, List<A> w);
    
    public boolean accept(List<A> w);
    
}
