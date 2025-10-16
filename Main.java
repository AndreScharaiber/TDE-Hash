import java.util.Random;
import java.util.ArrayList;

public class Main {

    // Aqui foram definidas as variáveis globais, que definem o tamanho dos dados e do vetor
    private static final long SEED = 999;
    private static final int[] TAMANHOS_VETOR = {1000, 10000, 100000};
    private static final int[] TAMANHOS_DADOS = {100_000, 1_000_000, 10_000_000};

    // - Lista as três funções hash alternativas escolhidas para o trabalho -
    private static final HashFunction[] funcoesHash = {
            FuncoesHash.HASH_MULTIPLICACAO,
            FuncoesHash.HASH_DOBRAMENTO,
            FuncoesHash.HASH_QUADRADO_CENTRAL
    };
    private static final String[] nomesHash = {
            "Multiplicacao (h1)", "Dobramento (h2)", "Quadrado Central (h3)"
    };

    // Gera um ArrayList de objetos Registro, simulando os códigos de 9 dígitos
    // Aqui foi gerado o código de 9 dígitos
    // Aqui foi também usada a seed, solicitado para que os dados sempre sejam idênticos
    public static ArrayList<Registro> gerarDados(int N) {
        ArrayList<Registro> dados = new ArrayList<>(N);
        Random rand = new Random(SEED); // Uso da SEED obrigatória

        for (int i = 0; i < N; i++) {
            long codigo = 1 + (long) (rand.nextDouble() * 999_999_999L);
            dados.add(new Registro(codigo));
        }
        return dados;
    }

    public static void main(String[] args) {
        System.out.println(" TESTE DE HASH! ");

        // Aqui é definido um padrão estético, um header para facilitar a visualização
        System.out.printf("%-15s | %-20s | %-10s | %-15s | %-15s | %-15s | %-15s | %-45s%n",
                "Estrategia", "Funcao Hash", "Tamanho M", "Tamanho N", "Tempo Ins (ms)", "Colisoes", "Tempo Busca (ms)", "Analise Extra (Max Lista / Gaps)");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        // Loop externo: Aqui o loop itera o tamanho dos dados (N)
        for (int N : TAMANHOS_DADOS) {
            ArrayList<Registro> conjuntoDados = gerarDados(N);

            // Loop intermediário: Já neste aqui, é iterado sob o tamanho do vetor (M)
            for (int M : TAMANHOS_VETOR) {

                // Aqui é feita uma checagem para evitar loops infinitos do DoubleHash
                if (M % 7 == 0 && M != 7) {
                    System.out.println("--- Pulando testes para M=" + M + " devido à restrição do Hash Duplo (não é co-primo de 7) ---");
                    continue;
                }

                // Loop interno: Este loop garante que todas as 3 funções hash (h1, h2 e h3) sejam testadas
                for (int i = 0; i < funcoesHash.length; i++) {
                    HashFunction hFunc = funcoesHash[i];
                    String nHash = nomesHash[i];


                    // - AQUI é onde os 3 testes são obrigatóriamente executados -

                    // Teste 1: SONDAGEM LINEAR
                    testarDesempenhoRehashing(
                            "S. Linear", nHash, M, N, conjuntoDados, hFunc
                    );

                    // Teste 2: ENCADEAMENTO
                    testarDesempenhoEncadeamento(
                            "Encadeamento", nHash, M, N, conjuntoDados, hFunc
                    );

                    // Teste 3: DOUBLE HASH
                    testarDesempenhoHashDuplo(
                            "Hash Duplo", nHash, M, N, conjuntoDados, hFunc
                    );
                }
            }
        }
    }

    // Testa o desempenho da Sondagem Linear (Rehashing).
    // Esta parte é dedicada ao rehashing, medindo as metricas solicitadas: Tempo, Colisões, Tempo de busca e os Gaps
    public static void testarDesempenhoRehashing(
            String nomeEstrategia, String nomeFuncao, int M, int N,
            ArrayList<Registro> dados, HashFunction hashFunc) {

        TabelaHashLinearProbing tabela = new TabelaHashLinearProbing(M, hashFunc);

        // Medição do Tempo de Inserção
        // Aqui a medição é feita em nanosegundos e convertida à milissegundos
        long startTimeInsert = System.nanoTime();
        for (Registro reg : dados) { tabela.inserir(reg); }
        long endTimeInsert = System.nanoTime();
        long tempoInsercaoMS = (endTimeInsert - startTimeInsert) / 1_000_000;
        long colisoes = tabela.getTotalColisoes();
        // As colisões representam o número de realocações (tentativas de encontrar um slot vazio)

        // Medição do Tempo de Busca de TODOS os elementos
        // Aqui é medido o tempo de busca e a performance em localizar todos os N elementos inseridos
        long startTimeSearch = System.nanoTime();
        for (Registro reg : dados) { tabela.buscar(reg.getCodigo()); }
        long endTimeSearch = System.nanoTime();
        long tempoBuscaMS = (endTimeSearch - startTimeSearch) / 1_000_000;

        // Análise de Gaps: Métrica que mede a clusterização (espaços entre elementos no vetor).
        // Aqui é onde é medida a clusterização, que mostra a diferença de espaço entre os elementos do vetor, ou seja, o gap
        String analiseGaps = "Gaps: " + tabela.getAnaliseGaps();

        // Impressão da linha de resultados
        // Aqui imprime a linha completa de resultados no formato anteriormente definido para fins de facilitar a visualização
        System.out.printf("%-15s | %-20s | %-10d | %-15d | %-15d | %-15d | %-15d | %-45s%n",
                nomeEstrategia, nomeFuncao, M, N, tempoInsercaoMS, colisoes, tempoBuscaMS, analiseGaps);
    }

    // Testa o desempenho do Encadeamento. Mede o Pior Caso (Max Lista)
    // Este method é exclusivo para o Encadeamento, sendo responsável por medir o pior caso
    public static void testarDesempenhoEncadeamento(
            String nomeEstrategia, String nomeFuncao, int M, int N,
            ArrayList<Registro> dados, HashFunction hashFunc) {

        TabelaHashEncadeamento tabela = new TabelaHashEncadeamento(M, hashFunc);

        long startTimeInsert = System.nanoTime();
        for (Registro reg : dados) { tabela.inserir(reg); }
        long endTimeInsert = System.nanoTime();
        long tempoInsercaoMS = (endTimeInsert - startTimeInsert) / 1_000_000;
        long colisoes = tabela.getTotalColisoes();
        // Aqui a colisão é considerada como cada elemento inserido após o primeiro nó

        long startTimeSearch = System.nanoTime();
        for (Registro reg : dados) { tabela.buscar(reg.getCodigo()); }
        long endTimeSearch = System.nanoTime();
        long tempoBuscaMS = (endTimeSearch - startTimeSearch) / 1_000_000;

        // Max Lista: Métrica do Pior Caso, mostrando o tamanho da maior lista formada.
        // Aqui é onde se busca o pior caso de busca no encadeamento, mostrando a maior lista formada
        String maxLista = String.format("Max Lista: %d", tabela.getMaxTamanhoLista());


        System.out.printf("%-15s | %-20s | %-10d | %-15d | %-15d | %-15d | %-15d | %-45s%n",
                nomeEstrategia, nomeFuncao, M, N, tempoInsercaoMS, colisoes, tempoBuscaMS, maxLista);
    }

    //Testa o desempenho do Hash Duplo (Rehashing).
    // Aqui foi desenvolvido o method para testar o desenpenho do Hash Duplo
    public static void testarDesempenhoHashDuplo(
            String nomeEstrategia, String nomeFuncao, int M, int N,
            ArrayList<Registro> dados, HashFunction hashFunc) {

        TabelaHashHashDuplo tabela = new TabelaHashHashDuplo(M, hashFunc);

        long startTimeInsert = System.nanoTime();
        for (Registro reg : dados) { tabela.inserir(reg); }
        long endTimeInsert = System.nanoTime();
        long tempoInsercaoMS = (endTimeInsert - startTimeInsert) / 1_000_000;
        long colisoes = tabela.getTotalColisoes();

        long startTimeSearch = System.nanoTime();
        for (Registro reg : dados) { tabela.buscar(reg.getCodigo()); }
        long endTimeSearch = System.nanoTime();
        long tempoBuscaMS = (endTimeSearch - startTimeSearch) / 1_000_000;

        // Contagem de Gaps para avaliar a clusterização, assim como na Sondagem Linear
        // Aqui é feita a contagem de gaps, assim como na Sonadagem Linear
        String analiseGaps = "Gaps: " + tabela.getAnaliseGaps();


        System.out.printf("%-15s | %-20s | %-10d | %-15d | %-15d | %-15d | %-15d | %-45s%n",
                nomeEstrategia, nomeFuncao, M, N, tempoInsercaoMS, colisoes, tempoBuscaMS, analiseGaps);
    }
}