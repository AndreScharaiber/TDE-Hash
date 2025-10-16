public class FuncoesHash {

    private static final double A = 0.6180339887;

    public static final HashFunction HASH_MULTIPLICACAO = (chave, M) -> {
        double val = chave * A;
        val = val - Math.floor(val);
        return (int) (M * val);
    };

    public static final HashFunction HASH_DOBRAMENTO = (chave, M) -> {
        long temp = chave;
        long soma = 0;
        int numDigitos = (int) Math.log10(M) + 1;
        long bloco = (long) Math.pow(10, numDigitos);

        while (temp > 0) {
            soma += temp % bloco;
            temp /= bloco;
        }
        return (int) (soma % M);
    };

    public static final HashFunction HASH_QUADRADO_CENTRAL = (chave, M) -> {
        long quadrado = chave * chave;
        String s = String.valueOf(quadrado);
        int len = s.length();
        int M_len = String.valueOf(M).length();

        int start = (len - M_len) / 2;
        int end = start + M_len;

        if (start < 0) start = 0;
        if (end > len) end = len;

        String centroStr = s.substring(start, end);
        long centro = Long.parseLong(centroStr);
        return (int) (centro % M);
    };

    public static int h_passo_duplo(long chave) {
        return (int) (7 - (chave % 7));
    }
}