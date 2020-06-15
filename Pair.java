package ifes.fsa;

import java.util.Objects;

/**
 * Um par ordenado.A linguagem Java não representa n-uplas nativamente.Por
 isso esta classe é necessária.
 *
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 * @param <T1> tipo do primeiro componente do par ordenado
 * @param <T2> tipo do segundo componente do par ordenado
 */
public class Pair<T1, T2> implements Cloneable {
    
    /**
     * Retorna um par ordenado.
     * 
     * @param <V1> tipo do primeiro componente do par ordenado
     * @param <V2> tipo do segundo componente do par ordenado
     * @param u1 primeiro componente do par ordenado
     * @param u2 segundo componente do par ordenado
     * @return um par ordenado com componentes `u1` e `u2`
     */
    public static <V1,V2> Pair<V1,V2> p(V1 u1, V2 u2) {
        return new Pair(u1,u2);
    }

    /**
     * Primeiro componente do par ordenado. Campo imutável e público, evitando a
     * necessidade de um método "getter".
     */
    public final T1 _1;
    
    /**
     * Segundo componente do par ordenado. Campo imutável e público, evitando a
     * necessidade de um método "getter".
     */
    public final T2 _2;
    
    /**
     * Constroi uma instância de par ordenado dados os dois componentes.
     *
     * @param x1 primeiro componente do par ordenado
     * @param x2 segundo componente do par ordenado
     */
    public Pair(T1 x1, T2 x2) {
        this._1 = x1;
        this._2 = x2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash += 11 * _1.hashCode();
        hash += 11 * _2.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (!Objects.equals(this._1, other._1)) {
            return false;
        }
        if (!Objects.equals(this._2, other._2)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ')';
    }

}
