import java.util.Random;
import java.util.ArrayList;

public class Main {

    private static final long SEED = 999;
    private static final int[] TAMANHOS_VETOR = {1000, 10000, 100000};
    private static final int[] TAMANHOS_DADOS = {100_000, 1_000_000, 10_000_000};

    private static final HashFunction[] funcoesHash = {
            FuncoesHash.HASH_MULTIPLICACAO,
            FuncoesHash.HASH_DOBRAMENTO,
            FuncoesHash.HASH_QUADRADO_CENTRAL
    };
    private static final String[] nomesHash = {
            "Multiplicacao (h1)", "Dobramento (h2)", "Quadrado Central (h3)"
    };

    public static ArrayList<Registro> gerarDados(int N) {
        ArrayList<Registro> dados = new ArrayList<>(N);
        Random rand = new Random(SEED);

        for (int i = 0; i < N; i++) {
            long codigo = 1 + (long) (rand.nextDouble() * 999_999_999L);
            dados.add(new Registro(codigo));
        }
        return dados;
    }

    public static void main(String[] args) {
        System.out.println(" TESTE DE HASH! ");

        System.out.printf("%-15s | %-20s | %-10s | %-15s | %-15s | %-15s | %-15s | %-45s%n",
                "Estrategia", "Funcao Hash", "Tamanho M", "Tamanho N", "Tempo Ins (ms)", "Colisoes", "Tempo Busca (ms)", "Analise Extra (Max Lista / Gaps)");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (int N : TAMANHOS_DADOS) {
            ArrayList<Registro> conjuntoDados = gerarDados(N);

            for (int M : TAMANHOS_VETOR) {

                if (M % 7 == 0 && M != 7) {
                    System.out.println("--- Pulando testes para M=" + M + " devido à restrição do Hash Duplo (não é co-primo de 7) ---");
                    continue;
                }
                for (int i = 0; i < funcoesHash.length; i++) {
                    HashFunction hFunc = funcoesHash[i];
                    String nHash = nomesHash[i];

                    testarDesempenhoRehashing(
                            "S. Linear", nHash, M, N, conjuntoDados, hFunc
                    );

                    testarDesempenhoEncadeamento(
                            "Encadeamento", nHash, M, N, conjuntoDados, hFunc
                    );

                    testarDesempenhoHashDuplo(
                            "Hash Duplo", nHash, M, N, conjuntoDados, hFunc
                    );
                }
            }
        }
    }

    public static void testarDesempenhoRehashing(
            String nomeEstrategia, String nomeFuncao, int M, int N,
            ArrayList<Registro> dados, HashFunction hashFunc) {

        TabelaHashLinearProbing tabela = new TabelaHashLinearProbing(M, hashFunc);

        long startTimeInsert = System.nanoTime();
        for (Registro reg : dados) { tabela.inserir(reg); }
        long endTimeInsert = System.nanoTime();
        long tempoInsercaoMS = (endTimeInsert - startTimeInsert) / 1_000_000;
        long colisoes = tabela.getTotalColisoes();

        long startTimeSearch = System.nanoTime();
        for (Registro reg : dados) { tabela.buscar(reg.getCodigo()); }
        long endTimeSearch = System.nanoTime();
        long tempoBuscaMS = (endTimeSearch - startTimeSearch) / 1_000_000;

        String analiseGaps = "Gaps: " + tabela.getAnaliseGaps();

        System.out.printf("%-15s | %-20s | %-10d | %-15d | %-15d | %-15d | %-15d | %-45s%n",
                nomeEstrategia, nomeFuncao, M, N, tempoInsercaoMS, colisoes, tempoBuscaMS, analiseGaps);
    }


    public static void testarDesempenhoEncadeamento(
            String nomeEstrategia, String nomeFuncao, int M, int N,
            ArrayList<Registro> dados, HashFunction hashFunc) {

        TabelaHashEncadeamento tabela = new TabelaHashEncadeamento(M, hashFunc);

        long startTimeInsert = System.nanoTime();
        for (Registro reg : dados) { tabela.inserir(reg); }
        long endTimeInsert = System.nanoTime();
        long tempoInsercaoMS = (endTimeInsert - startTimeInsert) / 1_000_000;
        long colisoes = tabela.getTotalColisoes();

        long startTimeSearch = System.nanoTime();
        for (Registro reg : dados) { tabela.buscar(reg.getCodigo()); }
        long endTimeSearch = System.nanoTime();
        long tempoBuscaMS = (endTimeSearch - startTimeSearch) / 1_000_000;

        String maxLista = String.format("Max Lista: %d", tabela.getMaxTamanhoLista());


        System.out.printf("%-15s | %-20s | %-10d | %-15d | %-15d | %-15d | %-15d | %-45s%n",
                nomeEstrategia, nomeFuncao, M, N, tempoInsercaoMS, colisoes, tempoBuscaMS, maxLista);
    }

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

        String analiseGaps = "Gaps: " + tabela.getAnaliseGaps();


        System.out.printf("%-15s | %-20s | %-10d | %-15d | %-15d | %-15d | %-15d | %-45s%n",
                nomeEstrategia, nomeFuncao, M, N, tempoInsercaoMS, colisoes, tempoBuscaMS, analiseGaps);
    }
}