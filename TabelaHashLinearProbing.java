public class TabelaHashLinearProbing {

    private Registro[] tabela;
    private final int tamanhoArray;
    private int elementosInseridos;
    private long totalColisoes;
    private final HashFunction hashFunction;

    public TabelaHashLinearProbing(int capacidade, HashFunction hashFunc) {
        this.tamanhoArray = capacidade;
        this.tabela = new Registro[capacidade];
        this.elementosInseridos = 0;
        this.totalColisoes = 0;
        this.hashFunction = hashFunc;
    }

    public long getTotalColisoes() { return totalColisoes; }
    private int hash(long codigo) { return hashFunction.aplicar(codigo, tamanhoArray); }

    public boolean inserir(Registro registro) {
        long codigo = registro.getCodigo();
        int indexInicial = hash(codigo);
        int indexAtual = indexInicial;
        int i = 0;

        while (tabela[indexAtual] != null) {
            if (tabela[indexAtual].getCodigo() == codigo) {
                tabela[indexAtual] = registro;
                return true;
            }

            i++;
            indexAtual = (indexInicial + i) % tamanhoArray;

            if (i >= tamanhoArray) { return false; }
        }

        totalColisoes += i;

        tabela[indexAtual] = registro;
        elementosInseridos++;
        return true;
    }

    public Registro buscar(long codigo) {
        int indexInicial = hash(codigo);
        int indexAtual = indexInicial;
        int i = 0;

        while (tabela[indexAtual] != null) {
            if (tabela[indexAtual].getCodigo() == codigo) { return tabela[indexAtual]; }

            i++;
            indexAtual = (indexInicial + i) % tamanhoArray;

            if (i >= tamanhoArray) { return null; }
        }
        return null;
    }

    public String getAnaliseGaps() {
        int menorGap = tamanhoArray + 1;
        int maiorGap = 0;
        long somaGaps = 0;
        int totalGaps = 0;

        int gapAtual = 0;

        for (int i = 0; i < tamanhoArray; i++) {
            if (tabela[i] == null) {
                gapAtual++;
            } else {
                if (gapAtual > 0) {
                    if (gapAtual < menorGap) { menorGap = gapAtual; }
                    if (gapAtual > maiorGap) { maiorGap = gapAtual; }
                    somaGaps += gapAtual;
                    totalGaps++;
                }
                gapAtual = 0;
            }
        }

        if (totalGaps == 0) {
            if (elementosInseridos == tamanhoArray) {
                return "Menor: 0 | Maior: 0 | Media: 0.00";
            }
            return "Nadinha";
        }

        double mediaGap = (double) somaGaps / totalGaps;

        return String.format("Menor: %d | Maior: %d | Media: %.2f", menorGap, maiorGap, mediaGap);
    }
}