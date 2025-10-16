TDE-Hash
Este trabalho foi feito inteiramente por mim, sendo assim, o único que participará da prova de autoria.

1. Introdução ao tópico:
Para este projeto, foram escolhidos os métodos de encadeamento, Hash Duplo (Double Hashing) e Sondagem Linear. Desta forma, foi possível abordar e comparar diferentes tipos de hash, além de analisar o volume de dados e o comportamento das tabelas em vista à sua escalabilidade.

A implementação foi baseada na Colisão, visando principalmente lidar com este problema central nas tabelas hash. Para isso, foram usados diferentes métodos:

Encadeamento Separado (Separate Chaining):
Pesquisando a fundo, percebi que, além de ser simples, é uma ótima forma de resolver colisões. Ao invés de armazenar o elemento diretamente na posição da tabela, cada slot funciona como o início de uma "cadeia". Caso haja colisão, o novo elemento é enviado ao final dessa lista. Sua grande vantagem é a durabilidade em cenários de grandes quantidades de dados.

Endereçamento Aberto - Sondagem Linear (Linear Probing):
Neste método, não usamos listas ligadas. Se o slot que a função hash indicou estiver ocupado (houver colisão), a tabela busca o próximo slot vazio, seguindo sequencialmente: i+1,i+2,…. O ponto negativo e conhecido dessa técnica é que ela gera um problema chamado agrupamento primário, onde grandes blocos de slots ocupados se formam, aumentando o tempo de busca.

Endereçamento Aberto - Hash Duplo (Double Hashing):
Esta é uma tentativa de aprimorar a Sondagem Linear. Se houver uma colisão, em vez de apenas buscar o próximo slot, o método usa uma segunda função hash (h 
′
 (k)) para calcular um "salto" mais aleatório. A sequência de busca pelo slot vazio é determinada por h(k)+i⋅h 
′
 (k). Isso ajuda a espalhar melhor os dados na tabela e evita os agrupamentos gigantes da Sondagem Linear.

2. Funções Hash e Cenários de Teste
Para garantir que a performance não dependesse de uma única função, testei três técnicas distintas de dispersão de chaves.

2.1 Funções Hash Implementadas

h1 - Multiplicação - Função com número irracional -           
h2 - Dobramento - Quebra a chave em pedeços e as soma -            
h3 - Quadrado - Eleva a chave ao quadrado -          


2.2 Configuração dos Testes (Tamanhos e Fator de Carga)
Para avaliar a performance, eu variei os dois parâmetros principais:

M (Tamanho da Tabela): Variou entre 1.000, 10.000 e 100.000.
N (Número de Chaves): Variou entre 100.000, 1.000.000 e 10.000.000.

A combinação desses valores me permitiu analisar o Fator de Carga (α=N/M). O cenário mais importante foi o de alta carga (α≫1, como N=10M e M=1K), que é o momento em que as tabelas sofrem mais e as diferenças de desempenho ficam mais evidentes.

3. Análise de Resultados e Gráficos Comparativos
Os dados brutos estão na planilha, mas a melhor forma de visualizar o desempenho é através dos gráficos de escala.
Como não foi específicado, gerei 3 gráficos com a ajuda de uma IA generativa, com base nos dados fornecidos ao testar o código:

Análise do Gráfico A:
<img width="1536" height="1024" alt="Grafico3" src="https://github.com/user-attachments/assets/c5c769f9-1a33-419d-8edb-149c43b892d0" />

Encadeamento (Performance Ideal): Claramente, as linhas do Encandeamento se mantêm muito baixas e estáveis. O tempo de busca é o melhor porque o método converte o problema de colisão em uma simples navegação em lista ligada.

Sondagem Linear e Hash Duplo (O Efeito Agrupamento): As linhas que representam o Endereçamento Aberto (Sondagem Linear e Hash Duplo) disparam quando N se torna alto. Isso confirma que eles não são adequados para fatores de carga α>1, pois o tempo é gasto procurando (ou pulando) slots ocupados.

Análise do Gráfico B:
<img width="1536" height="1024" alt="Grafico2" src="https://github.com/user-attachments/assets/1ada2c5b-b264-4707-9f71-ba10bf1f7122" />

Hash Duplo é Superior à Sondagem Linear: O Hash Duplo demonstrou um número menor de colisões, o que valida a sua técnica de "salto". A segunda função hash realmente consegue dispersar os elementos de forma mais inteligente.

Qualidade da Função: Neste cenário de estresse, a função Multiplicação (h1) e Quadrado Central (h3) tenderam a gerar menos colisões no Endereçamento Aberto, mostrando que a distribuição de chaves foi ligeiramente melhor do que a função Dobramento (h2).

3.3 Gráfico C: Tempo de Inserção (ms) vs. N (O Custo para Construir a Tabela)
<img width="1536" height="1024" alt="Grafico1" src="https://github.com/user-attachments/assets/5a2cadab-0ece-477c-af2d-7a1eeab6958c" />


Custo do Encandeamento: O Encadeamento tem um tempo de inserção maior, especialmente no início. Isso acontece porque a criação de listas ligadas requer alocação dinâmica de memória a todo momento, um processo mais lento do que apenas calcular um índice de array (o que o Endereçamento Aberto faz).

Performance no Limite: Contudo, no volume máximo (N=10M), o Endereçamento Aberto também tem um custo de inserção que dispara, pois ele gasta muito tempo buscando slots vazios.

Os valores completos estão nesta planilha do Google Sheets: https://docs.google.com/spreadsheets/d/1bhFdrSLetbtr8Gf7o6z7rCcYs6uXLiJ6UE90NJWWGFw/edit?usp=sharing

4. Conclusão Final e Veredito
4.1. O Vencedor: Encandeamento Separado
Com base na estabilidade e escalabilidade dos gráficos, a estratégia vencedora é o Encadeamento Separado.

O Porquê:

Resistência Contra Carga: O Encandeamento é a única estratégia que se mantém com desempenho na busca, mesmo quando o número de chaves (N) é muito maior que o tamanho da tabela (M).

Busca Rápida: Embora a inserção seja mais lenta por causa da memória, a busca, que é a operação mais frequente em uma tabela hash após a montagem, é extremamente rápida e previsível.

4.2. O Uso do Endereçamento Aberto
O Hash Duplo provou ser a melhor opção entre as técnicas de Endereçamento Aberto.

Aplicação Ideal: Estas técnicas só são eficientes em aplicações onde se pode garantir que o Fator de Carga (α) nunca ultrapasse 1 e onde a economia de memória (evitando os ponteiros das listas ligadas) seja uma prioridade. Em todos os outros casos, o Encadeamento é a melhor escolha.
