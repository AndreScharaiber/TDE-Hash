@FunctionalInterface
public interface HashFunction {
    // Interface usada para passar diferentes funções hash (h1, h2, h3)
    int aplicar(long codigo, int tamanhoM);
}