public class TabelaHashEncadeamento {

    // O array principal onde ficam os cabeçalhos das listas ligadas
    private Node[] tabela;
    // O tamanho M (capacidade) do vetor hash
    private final int tamanhoArray;
    // Contador obrigatório de colisões
    private long totalColisoes;
    // A função hash primária a ser usada
    private final HashFunction hashFunction;
    // Métrica extra: Rastreia o tamanho da maior lista ligada formada (pior caso de busca)
    private int maxTamanhoLista;

    // Construtor: Aqui a tabela é inicializada com o tamanho M
    public TabelaHashEncadeamento(int capacidade, HashFunction hashFunc) {
        this.tamanhoArray = capacidade;
        this.tabela = new Node[capacidade];
        this.totalColisoes = 0;
        this.hashFunction = hashFunc;
        this.maxTamanhoLista = 0; // Inicialização
    }

    public long getTotalColisoes() { return totalColisoes; }
    // Retorna a métrica de Pior Caso (Max Lista)
    public int getMaxTamanhoLista() { return maxTamanhoLista; }
    // Aplica a função hash para obter o índice do vetor
    private int hash(long codigo) { return hashFunction.aplicar(codigo, tamanhoArray); }

    // Insere um novo Registro na tabela
    public boolean inserir(Registro registro) {
        long codigo = registro.getCodigo();
        int index = hash(codigo);
        Node novoNo = new Node(registro);

        // Caso 1: O bucket está vazio. Não há colisão
        if (tabela[index] == null) {
            tabela[index] = novoNo;

            // Se o bucket estava vazio, o tamanho agora é 1
            if (1 > maxTamanhoLista) { maxTamanhoLista = 1; }
            return true;
        }

        // Caso 2 O bucket já tem elementos, gerando uma colisão
        Node atual = tabela[index];
        Node anterior = null;
        int comparacoes = 0;
        int tamanhoAtual = 0;

        // Aqui é onde percorremos a lista ligada até o final
        while (atual != null) {
            // Se o registro já existir, ele é substituído
            if (atual.registro.getCodigo() == codigo) {
                atual.registro = registro;
                return true;
            }

            comparacoes++; // Conta a colisão/comparação
            tamanhoAtual++; // Aumenta o tamanho da lista atual

            anterior = atual;
            atual = atual.proximo;
        }

        // A contagem de colisões é o número de nós percorridos
        totalColisoes += comparacoes;
        // Insere o novo nó no final da lista
        anterior.proximo = novoNo;

        // LÓGICA CHAVE: Atualizamos a métrica de Pior Caso (Max Lista)
        // Aqui é onde comparamos o tamanho da lista (tamanhoAtual + 1 [o novo nó]) com o máximo anterior
        if (tamanhoAtual + 1 > maxTamanhoLista) {
            maxTamanhoLista = tamanhoAtual + 1;
        }
        return true;
    }

    //Busca um registro pelo código

    public Registro buscar(long codigo) {
        int index = hash(codigo);
        Node atual = tabela[index];
        // Percorremos a lista ligada (bucket) até encontrar ou chegar ao fim
        while (atual != null) {
            if (atual.registro.getCodigo() == codigo) { return atual.registro; }
            atual = atual.proximo;
        }
        return null;
    }
}