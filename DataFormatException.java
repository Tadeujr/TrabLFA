package ifes.fsa;

/**
 * Um erro na leitura da definição de um autômato finito.
 * 
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 */
public class DataFormatException extends Exception {
    
    public DataFormatException() {
        super();
    }
    
    public DataFormatException(String message) {
        super(message);
    }
}
