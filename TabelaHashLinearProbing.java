// Tabela Hash usando a estrategia de Sondagem Linear (Linear Probing)
// Esta e a implementacao para o Teste 1 e mede a clusterizacao atraves da analise de Gaps
public class TabelaHashLinearProbing {

    // O vetor principal onde os Registros sao armazenados
    private Registro[] tabela;
    // O tamanho M (capacidade) do vetor hash
    private final int tamanhoArray;
    // Conta quantos elementos estao de fato no vetor
    private int elementosInseridos;
    // Contador obrigatorio de colisoes (aqui, tentativas de rehashing)
    private long totalColisoes;
    // A funcao hash primaria usada
    private final HashFunction hashFunction;

    // Construtor: Aqui a tabela e inicializada com o tamanho M
    public TabelaHashLinearProbing(int capacidade, HashFunction hashFunc) {
        this.tamanhoArray = capacidade;
        this.tabela = new Registro[capacidade];
        this.elementosInseridos = 0;
        this.totalColisoes = 0;
        this.hashFunction = hashFunc;
    }

    public long getTotalColisoes() { return totalColisoes; }
    // Aplica a funcao hash para obter o indice inicial
    private int hash(long codigo) { return hashFunction.aplicar(codigo, tamanhoArray); }

    // Insere um novo Registro usando Sondagem Linear
    public boolean inserir(Registro registro) {
        long codigo = registro.getCodigo();
        int indexInicial = hash(codigo);
        int indexAtual = indexInicial;
        int i = 0; // Contador de passos (que e a colisao)

        // Aqui o loop percorre o vetor a partir do indexInicial ate encontrar um slot vazio ou o elemento
        while (tabela[indexAtual] != null) {
            // Se encontrar o elemento, ele e apenas atualizado e retornamos
            if (tabela[indexAtual].getCodigo() == codigo) {
                tabela[indexAtual] = registro;
                return true;
            }

            i++; // Se nao achou e nao estava vazio, houve mais uma tentativa (colisao)
            // Esta e a formula da Sondagem Linear: (H(k) + i) mod M
            indexAtual = (indexInicial + i) % tamanhoArray;

            // Se o contador i for maior ou igual ao tamanho, o vetor esta cheio ou ocorreu um loop
            if (i >= tamanhoArray) { return false; }
        }

        // A contagem final e o numero de passos que ele precisou dar (i)
        totalColisoes += i;

        // Inserimos o registro no slot vazio encontrado
        tabela[indexAtual] = registro;
        elementosInseridos++;
        return true;
    }

    // Busca um registro seguindo a mesma sequencia de rehashing da insercao
    public Registro buscar(long codigo) {
        int indexInicial = hash(codigo);
        int indexAtual = indexInicial;
        int i = 0;

        // O loop e igual ao da insercao, percorrendo a partir do ponto inicial
        while (tabela[indexAtual] != null) {
            // Se o elemento for encontrado, retornamos ele
            if (tabela[indexAtual].getCodigo() == codigo) { return tabela[indexAtual]; }

            i++;
            // Seguimos o passo linear para encontrar o proximo slot
            indexAtual = (indexInicial + i) % tamanhoArray;

            // Se passamos por todos os slots e nao achamos, ele nao existe
            if (i >= tamanhoArray) { return null; }
        }
        // Se encontramos um null, o elemento com certeza nao esta mais a frente
        return null;
    }

    // Análise de Gaps: Métrica que mede a clusterizacao (espacos entre elementos no vetor)
    // Aqui e onde e medida a clusterizacao, que mostra a diferenca de espaco entre os elementos do vetor, ou seja, o gap
    public String getAnaliseGaps() {
        int menorGap = tamanhoArray + 1;
        int maiorGap = 0;
        long somaGaps = 0;
        int totalGaps = 0;

        int gapAtual = 0; // Contador para a sequencia de slots vazios (o gap)

        // Loop que percorre todo o vetor da tabela
        for (int i = 0; i < tamanhoArray; i++) {
            // Se o slot estiver vazio (null), aumentamos o gap
            if (tabela[i] == null) {
                gapAtual++;
            } else {
                // Se encontramos um elemento (e o gapAtual e > 0)
                if (gapAtual > 0) {
                    // Aqui atualizamos as estatisticas (menor, maior e soma)
                    if (gapAtual < menorGap) { menorGap = gapAtual; }
                    if (gapAtual > maiorGap) { maiorGap = gapAtual; }
                    somaGaps += gapAtual;
                    totalGaps++;
                }
                // Zeramos o contador para comecar a contar o proximo gap
                gapAtual = 0;
            }
        }

        // Se nao houver gaps (vetor totalmente cheio ou totalmente vazio)
        if (totalGaps == 0) {
            if (elementosInseridos == tamanhoArray) {
                // Vetor totalmente cheio
                return "Menor: 0 | Maior: 0 | Media: 0.00";
            }
            return "Nadinha";
        }

        double mediaGap = (double) somaGaps / totalGaps;

        // Retornamos os resultados no formato obrigatorio para o CSV
        return String.format("Menor: %d | Maior: %d | Media: %.2f", menorGap, maiorGap, mediaGap);
    }
}