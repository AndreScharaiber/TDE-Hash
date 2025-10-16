public class FuncoesHash {

    // Aqui e a constante A usada no metodo da multiplicacao
    private static final double A = 0.6180339887;

    // FORMULA 1: MULTIPLICACAO (h1)
    // Esta e a formula que evita a divisao (chave % M) conforme a restricao
    public static final HashFunction HASH_MULTIPLICACAO = (chave, M) -> {
        // Multiplica a chave pela constante A
        double val = chave * A;
        // Pega so a parte fracionaria do resultado
        val = val - Math.floor(val);
        // Multiplica a parte fracionaria por M para achar o indice
        return (int) (M * val);
    };

    // FORMULA 2: DOBRAMENTO (h2)
    // Esta formula quebra a chave em blocos e soma eles
    public static final HashFunction HASH_DOBRAMENTO = (chave, M) -> {
        long temp = chave;
        long soma = 0;
        // Define o tamanho de cada bloco com base no M
        int numDigitos = (int) Math.log10(M) + 1;
        long bloco = (long) Math.pow(10, numDigitos);

        // Loop que quebra a chave em blocos e soma
        while (temp > 0) {
            soma += temp % bloco; // Pega o bloco inferior e soma
            temp /= bloco;        // Remove o bloco inferior para o proximo loop
        }
        // O resultado e a soma total modulo M
        return (int) (soma % M);
    };

    // FORMULA 3: QUADRADO CENTRAL (h3)
    // Eleva a chave ao quadrado e pega os digitos do meio
    public static final HashFunction HASH_QUADRADO_CENTRAL = (chave, M) -> {
        // Passo 1: Eleva a chave ao quadrado
        long quadrado = chave * chave;
        String s = String.valueOf(quadrado);
        int len = s.length();
        // Aqui e onde definimos quantos digitos centrais queremos (tamanho de M)
        int M_len = String.valueOf(M).length();

        // Pega o ponto de inicio e fim para extrair os digitos do meio
        int start = (len - M_len) / 2;
        int end = start + M_len;

        // Ajustes para chaves muito pequenas que nao geram digitos suficientes
        if (start < 0) start = 0;
        if (end > len) end = len;

        // Converte os digitos centrais para um numero
        String centroStr = s.substring(start, end);
        long centro = Long.parseLong(centroStr);
        // O resultado e o valor central modulo M
        return (int) (centro % M);
    };

    // FUNCAO SECUNDARIA PARA HASH DUPLO (h2')
    // Deve ser co-primo com M, geralmente um primo menor que M
    public static int h_passo_duplo(long chave) {
        // Usa um primo fixo P=7 para calcular o passo
        // Este calculo garante um valor maior que zero para o passo
        return (int) (7 - (chave % 7));
    }
}