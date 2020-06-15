package ifes.fsa;

import static ifes.fsa.Pair.p;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Uma fábrica utilizada para crianção de instâncias de <em>autômatos
 * finitos</em>.
 *
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 */
public class FsaFactory {

    /**
     * Converte o conteúdo do arquivo indicado, representando um AFD, em uma
     * instância de `Dsa`.Veja `makeDfaFromString` para a descrição do formato
     * da definição do AFD.
     * @param fname nome e caminho do arquivo contendo a definição do AFD
     */
    public Dfa<String, String> makeDfaFromFile(String fname) throws DataFormatException, IOException {
        byte[] data = Files.readAllBytes(Path.of(fname));
        String contents = new String(data);
        return makeDfaFromString(contents);
    }

    /**
     * Converte a string dada, representando um AFD, em uma instância de `Dsa`.
     * A definição do AFD é segue a forma esquemática dada a seguir, por linha
     * de texto:
     * <ol>
     * <li>A string `dfa`.</li>
     * <li>Os símbolos do alfabeto, separados por espaço.</li>
     * <li>Os estados do autômato, separados por espaço.</li>
     * <li>O estados inicial.</li>
     * <li>Os estados finais, separados por espaço, ou `!!` se não houve estados
     * finais.</li>
     * <li>Desta linha em diante, até o final, cada linha representa uma
     * transição. Cada linha terá:<br>
     * `estado-origem` `símbolo` `estado-destino`.</li>
     * </ol>
     *
     * @param t a string contendo a definição do autômato
     * @return Uma instância de uma classe que implementa a interface `Dsa`
     */
    public Dfa<String, String> makeDfaFromString(String t) throws DataFormatException {
        String[] lines = t.split("\n");
        if (lines.length < 5) {
            throw new DataFormatException("Número de linhas insuficiente.");
        }

        if (!lines[0].equals("dfa")) {
            throw new DataFormatException("Definição não corresponde a AFD");
        }

        String[] ab = lines[1].split(" ");
        if (ab.length < 1) {
            throw new DataFormatException("Não foi definido alfabeto.");
        }
        Set<String> alphabet = new HashSet<>(Arrays.asList(ab));

        String[] qs = lines[2].split(" ");
        if (qs.length < 1) {
            throw new DataFormatException("Não foram definidos estados.");
        }
        Set<String> states = new HashSet<>(Arrays.asList(qs));

        if (lines[3].trim().length() < 1) {
            throw new DataFormatException("Não foi definido estado inicial.");
        }
        String init = lines[3].trim();

        String[] fs = lines[4].split(" ");
        if (fs.length < 1) {
            throw new DataFormatException("Não foi dada definição de estados finais.");
        }
        Set<String> finalStates = new HashSet<>();
        if (!fs[0].trim().equals("!!")) {
            finalStates.addAll(Arrays.asList(fs));
        }

        Map<Pair<String, String>, String> trans = new HashMap<>();
        int i = 5;
        while (i < lines.length) {
            String[] tr = lines[i].split(" ");
            if (tr.length != 3) {
                throw new DataFormatException(String.format(
                        "Erro na definição da transição. linha: %d [%s]", i + 1, lines[i]));
            }
            trans.put(p(tr[0], tr[1]), tr[2]);

            i += 1;
        }

        return new DfaConcrete<>(alphabet, states, trans, init, finalStates);
    }

}
