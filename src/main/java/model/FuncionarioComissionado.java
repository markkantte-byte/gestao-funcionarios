package model;

/**
 * Funcionário comissionado: salário-base mais percentual de comissão sobre as vendas.
 */
public class FuncionarioComissionado extends Funcionario implements Tributavel {

    /** Desconto simplificado de 15% sobre a remuneração. */
    private static final double ALIQUOTA_TRIBUTOS = 0.15;

    private double percentualComissao; // de 0 a 100 (ex.: 10 = 10%)
    private double totalVendas;

    /**
     * @param percentualComissao percentual entre 0 e 100 (ex.: 10 significa 10%)
     * @param totalVendas        total vendido no período (não pode ser negativo)
     */
    public FuncionarioComissionado(int codigo, String nome, String cpf, double salarioBase,
                                   double percentualComissao, double totalVendas) {
        super(codigo, nome, cpf, salarioBase);
        this.percentualComissao = validarPercentual(percentualComissao);
        this.totalVendas = validarNaoNegativo(totalVendas, "total de vendas");
    }

    public double getPercentualComissao() {
        return percentualComissao;
    }

    public void setPercentualComissao(double percentualComissao) {
        this.percentualComissao = validarPercentual(percentualComissao);
    }

    public double getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(double totalVendas) {
        this.totalVendas = validarNaoNegativo(totalVendas, "total de vendas");
    }

    /**
     * Remuneração = salário-base + (total de vendas x percentual / 100).
     */
    @Override
    public double calcularRemuneracao() {
        return getSalarioBase() + totalVendas * percentualComissao / 100.0;
    }

    /**
     * Tributos = 15% da remuneração.
     */
    @Override
    public double calcularTributos() {
        return calcularRemuneracao() * ALIQUOTA_TRIBUTOS;
    }

    private static double validarPercentual(double percentual) {
        if (Double.isNaN(percentual) || percentual < 0 || percentual > 100) {
            throw new IllegalArgumentException(
                    "Percentual de comissão inválido: " + percentual + " (deve estar entre 0 e 100).");
        }
        return percentual;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Salário-base: " + formatarMoeda(getSalarioBase())
                + " | Comissão: " + formatarPercentual(percentualComissao)
                + " | Total de vendas: " + formatarMoeda(totalVendas);
    }
}
