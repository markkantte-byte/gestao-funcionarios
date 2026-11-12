package model;

/**
 * Funcionário contratado em regime CLT: salário-base mais adicional de benefícios.
 */
public class FuncionarioCLT extends Funcionario implements Tributavel {

    /** Desconto simplificado (tipo INSS) de 11% sobre a remuneração. */
    private static final double ALIQUOTA_TRIBUTOS = 0.11;

    private double adicionalBeneficios;

    public FuncionarioCLT(int codigo, String nome, String cpf,
                          double salarioBase, double adicionalBeneficios) {
        super(codigo, nome, cpf, salarioBase);
        this.adicionalBeneficios = validarNaoNegativo(adicionalBeneficios, "adicional de benefícios");
    }

    public double getAdicionalBeneficios() {
        return adicionalBeneficios;
    }

    public void setAdicionalBeneficios(double adicionalBeneficios) {
        this.adicionalBeneficios = validarNaoNegativo(adicionalBeneficios, "adicional de benefícios");
    }

    /**
     * Remuneração CLT = salário-base + adicional de benefícios.
     */
    @Override
    public double calcularRemuneracao() {
        return getSalarioBase() + adicionalBeneficios;
    }

    /**
     * Tributos CLT = 11% da remuneração.
     */
    @Override
    public double calcularTributos() {
        return calcularRemuneracao() * ALIQUOTA_TRIBUTOS;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Salário-base: " + formatarMoeda(getSalarioBase())
                + " | Adicional de benefícios: " + formatarMoeda(adicionalBeneficios);
    }
}
