package ifes.fsa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Verifica se cadeias de caracteres são aceitas por um dado AFD. Os códigos de
 * erro do programa seguem o padrão adotado em Haskell:
 * https://hackage.haskell.org/package/exit-codes-1.0.0/docs/System-Exit-Codes.html
 *
 * @author Jefferson Andrade <jefferson.andrade@ifes.edu.br>
 */
public class CheckWordDsa implements Runnable {

    public static final int DATA_ERROR = 65;
    public static final int NO_INPUT_ERROR = 66;
    public static final int CANT_CREATE_ERROR = 73;
    public static final int IO_ERROR = 74;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CheckWordDsa chk = new CheckWordDsa(args);
        chk.run();
    }

    // Campos que codificam as opções de linha de comando
    private boolean _showUsage;
    private boolean _showVersion;
    private File _inputFile;
    private File _outputFile;

    // Entrada de dados do programa
    private BufferedReader _reader;

    // Saída de dados do programa
    private PrintWriter _writer;

    // Factory para criaçãode AFDs
    private FsaFactory _factory;

    private CheckWordDsa(String[] args) {
        parseArgs(args);
    }

    @Override
    public void run() {
        processArgs();
        _factory = new FsaFactory();
        Dfa<String, String> m;
        try {
            m = readDsa();
            String t = _reader.readLine();
            while (t != null) {
                List<String> w = Arrays.asList(t.split(" "));
                String ans = m.accept(w) ? "ACEITA" : "REJEITA";
                _writer.println(ans);
                t = _reader.readLine();
            }
        } catch (IOException ex) {
            System.err.printf("Erro de leitura de dados: %s\n", ex.getMessage());
            System.exit(IO_ERROR);
        } catch (DataFormatException ex) {
            System.err.printf("Erro de leitura de dados: %s\n", ex.getMessage());
            System.exit(DATA_ERROR);
        }
        _writer.flush();
    }

    /**
     * Analisa os argumentos de linha de comando e codifica seus significados
     * nos campos apropriados.
     *
     * @param args vetor com os argumentos de linha de comando
     */
    private void parseArgs(String[] args) {
        List<String> largs = Arrays.asList(args);
        int i = 0;
        while (i < largs.size()) {
            int inc = 1;
            switch (largs.get(i)) {
                case "-h":
                case "--help":
                    _showUsage = true;
                    break;
                case "-v":
                case "--version":
                    _showVersion = true;
                    break;
                case "-i":
                case "--input": {
                    String fname = largs.get(i + 1);
                    _inputFile = new File(fname);
                    inc = 2;
                    break;
                }
                case "-o":
                case "--output": {
                    String fname = largs.get(i + 1);
                    _outputFile = new File(fname);
                    inc = 2;
                    break;
                }
                default:
                    throw new IllegalArgumentException(
                            String.format("Parâmetro desconhecido: %s", largs.get(i)));
            }
            i += inc;
        }
    }

    /**
     * Executa as ações apropriadas de acordo com os argumentos de linha de
     * comando especificados.
     */
    private void processArgs() {
        if (_showUsage) {
            showUsageMessage();
            System.exit(0);
        }
        if (_showVersion) {
            showVersionMessage();
            System.exit(0);
        }
        try {
            _reader = makeReader();
        } catch (FileNotFoundException ex) {
            System.err.printf("Arquivo de entrada não encontrado: %s\n", _inputFile.getPath());
            System.exit(NO_INPUT_ERROR);
        }
        try {
            _writer = makeWriter();
        } catch (IOException ex) {
            System.err.printf("Sem permissão para gravar no arquivo: %s\n", _outputFile.getPath());
            System.exit(CANT_CREATE_ERROR);
        }
    }

    private final String USAGE_MESSAGE
            = "Modo de uso: java -jar CheckWordDsa.jar [OPÇÕES]\n"
            + "\n"
            + "OPÇÕES:\n"
            + " Se não for dada nenhuma opção de linha de comando, o programa vai\n"
            + " ler a definição do AFD e os casos de teste da entrada padrão, e\n"
            + " irá escrever osresultados na saída padrão. Veja a documentação do\n"
            + " programa para a descrição do formato dos dados.\n"
            + "-v|--version     : Imprime a versão do programa\n"
            + "-h|--help        : Imprime a forma de uso do programa\n"
            + "-i|--input FILE  : Faz a leitura dos dados do arquivo FILE e não\n"
            + "                   da entrada padrão\n"
            + "-o|--output FILE : Faz a escrita dos dados no arquivo FILE e não\n"
            + "                   na saída padrão\n";

    private void showUsageMessage() {
        showVersionMessage();
        System.out.println(USAGE_MESSAGE);
    }

    private final String VERSION_MESSAGE
            = "CheckWordDsa v. 0.1.0\n";

    private void showVersionMessage() {
        System.out.println(VERSION_MESSAGE);
    }

    /**
     * Cria um objeto `Reader` de acordo com os parâmetros do programa. Se tiver
     * sido especificado um nome de arquivo para entrada o reader apontará para
     * este arquivo, caso contrário o reader apontará para a entrada padrão.
     *
     * @return um reader construido de acordo com os parâmetros do programa
     * @throws FileNotFoundException
     */
    private BufferedReader makeReader() throws FileNotFoundException {
        BufferedReader rd;
        if (_inputFile != null) {
            rd = new BufferedReader(new FileReader(_inputFile));
        } else {
            rd = new BufferedReader(new InputStreamReader(System.in));
        }
        return rd;
    }

    /**
     * Cria um objeto `Writer` de acordo com os parâmetros do programa. Se tiver
     * sido especificado o nome de um arquivo de saída o writer apontará para
     * este arquivo, caso contrário, o writer apontará para a saída padrão.
     *
     * @return um writer construído de acordo com os parâmetros do programa
     * @throws IOException
     */
    private PrintWriter makeWriter() throws IOException {
        PrintWriter wr;
        if (_outputFile != null) {
            wr = new PrintWriter(new FileWriter(_outputFile));
        } else {
            wr = new PrintWriter(new OutputStreamWriter(System.out));
        }
        return wr;
    }

    /**
     * Cria um objeto `Dfa` lendo a definição do AFD à partir da entrada do
     * programa. Lê as linhas da entrada do programa até encontrar uma linha
     * contendo apenas `---`. Estas linhas são salvas em uma string que é usada
     * como argumento para o método que constrói o AFD no objeto
     * <em>factory<em>.
     *
     * @return um objeto `Dfa` construído à partir da definição lida da entrada
     * do programa
     * @throws IOException
     * @throws DataFormatException
     */
    private Dfa<String, String> readDsa() throws IOException, DataFormatException {
        StringBuilder sb = new StringBuilder();
        String t = _reader.readLine();
        while (t != null && !t.trim().equals("---")) {
            sb.append(t).append("\n");
            t = _reader.readLine();
        }
        Dfa<String, String> m = _factory.makeDfaFromString(sb.toString());
        return m;
    }

}
