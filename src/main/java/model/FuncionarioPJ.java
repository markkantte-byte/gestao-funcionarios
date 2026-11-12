package model;

/**
 * Funcionário prestador de serviço (PJ): recebe um valor por projeto realizado.
 * Não possui salário-base (fica em 0) e não implementa {@link Tributavel},
 * pois recolhe os próprios impostos.
 */
public class FuncionarioPJ extends Funcionario {

    private double valorPorProjeto;
    private int quantidadeProjetos;

    public FuncionarioPJ(int codigo, String nome, String cpf,
                         double valorPorProjeto, int quantidadeProjetos) {
        super(codigo, nome, cpf, 0.0);
        this.valorPorProjeto = validarNaoNegativo(valorPorProjeto, "valor por projeto");
        this.quantidadeProjetos = validarQuantidade(quantidadeProjetos);
    }

    public double getValorPorProjeto() {
        return valorPorProjeto;
    }

    public void setValorPorProjeto(double valorPorProjeto) {
        this.valorPorProjeto = validarNaoNegativo(valorPorProjeto, "valor por projeto");
    }

    public int getQuantidadeProjetos() {
        return quantidadeProjetos;
    }

    public void setQuantidadeProjetos(int quantidadeProjetos) {
        this.quantidadeProjetos = validarQuantidade(quantidadeProjetos);
    }

    /**
     * Remuneração PJ = valor por projeto x quantidade de projetos.
     */
    @Override
    public double calcularRemuneracao() {
        return valorPorProjeto * quantidadeProjetos;
    }

    private static int validarQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException(
                    "A quantidade de projetos não pode ser negativa: " + quantidade);
        }
        return quantidade;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Valor por projeto: " + formatarMoeda(valorPorProjeto)
                + " | Projetos: " + quantidadeProjetos;
    }
}
