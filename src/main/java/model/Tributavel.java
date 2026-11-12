package model;

/**
 * Contrato para funcionários cuja remuneração sofre tributos ou descontos.
 */
public interface Tributavel {

    /**
     * @return o valor (em reais) de tributos/descontos aplicáveis à remuneração
     */
    double calcularTributos();
}
