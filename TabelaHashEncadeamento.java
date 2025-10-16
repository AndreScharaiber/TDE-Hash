public class TabelaHashEncadeamento {

    private Node[] tabela;
    private final int tamanhoArray;
    private long totalColisoes;
    private final HashFunction hashFunction;
    private int maxTamanhoLista;

    public TabelaHashEncadeamento(int capacidade, HashFunction hashFunc) {
        this.tamanhoArray = capacidade;
        this.tabela = new Node[capacidade];
        this.totalColisoes = 0;
        this.hashFunction = hashFunc;
        this.maxTamanhoLista = 0;
    }

    public long getTotalColisoes() { return totalColisoes; }
    public int getMaxTamanhoLista() { return maxTamanhoLista; }
    private int hash(long codigo) { return hashFunction.aplicar(codigo, tamanhoArray); }

    public boolean inserir(Registro registro) {
        long codigo = registro.getCodigo();
        int index = hash(codigo);
        Node novoNo = new Node(registro);


        if (tabela[index] == null) {
            tabela[index] = novoNo;


            if (1 > maxTamanhoLista) { maxTamanhoLista = 1; }
            return true;
        }

        Node atual = tabela[index];
        Node anterior = null;
        int comparacoes = 0;
        int tamanhoAtual = 0;

        while (atual != null) {
            if (atual.registro.getCodigo() == codigo) {
                atual.registro = registro;
                return true;
            }

            comparacoes++;
            tamanhoAtual++;

            anterior = atual;
            atual = atual.proximo;
        }

        totalColisoes += comparacoes;
        anterior.proximo = novoNo;

        if (tamanhoAtual + 1 > maxTamanhoLista) {
            maxTamanhoLista = tamanhoAtual + 1;
        }
        return true;
    }

    public Registro buscar(long codigo) {
        int index = hash(codigo);
        Node atual = tabela[index];
        while (atual != null) {
            if (atual.registro.getCodigo() == codigo) { return atual.registro; }
            atual = atual.proximo;
        }
        return null;
    }
}