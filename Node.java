
class Node {
    // Aqui é onde fica o dado do registro
    Registro registro;
    // Ponteiro para o próximo nó na lista ligada (o 'próximo' da cadeia de colisão)
    Node proximo;

    // Construtor do nó que cria um novo nó com o registro e inicia o próximo como nulo
    public Node(Registro registro) {
        this.registro = registro;
        this.proximo = null;
    }
}