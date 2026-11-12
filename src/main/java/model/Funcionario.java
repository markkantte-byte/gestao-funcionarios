package model;

import java.util.Locale;

/**
 * Classe abstrata que representa um funcionário da empresa.
 * Cada categoria define a sua própria regra de remuneração.
 */
public abstract class Funcionario implements Identificavel {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private final int codigo;
    private String nome;
    private String cpf; // guardado somente com os 11 dígitos
    private double salarioBase;

    /**
     * @param codigo      identificador do funcionário (maior que zero)
     * @param nome        nome (obrigatório)
     * @param cpf         CPF válido, com ou sem máscara
     * @param salarioBase salário-base (não pode ser negativo)
     * @throws IllegalArgumentException se algum dado for inválido
     */
    protected Funcionario(int codigo, String nome, String cpf, double salarioBase) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("O código do funcionário deve ser maior que zero.");
        }
        this.codigo = codigo;
        this.nome = validarNome(nome);
        this.cpf = validarCpf(cpf);
        this.salarioBase = validarNaoNegativo(salarioBase, "salário-base");
    }

    /**
     * Calcula a remuneração do funcionário conforme a regra da sua categoria.
     *
     * @return remuneração em reais
     */
    public abstract double calcularRemuneracao();

    @Override
    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = validarNome(nome);
    }

    /**
     * @return o CPF somente com os 11 dígitos
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @return o CPF no formato 000.000.000-00
     */
    public String getCpfFormatado() {
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    public void setCpf(String cpf) {
        this.cpf = validarCpf(cpf);
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = validarNaoNegativo(salarioBase, "salário-base");
    }

    /**
     * Valida um CPF (formato 00000000000 ou 000.000.000-00) e seus dígitos verificadores.
     *
     * @param cpf texto do CPF
     * @return true se o CPF for válido
     */
    public static boolean cpfValido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            return false;
        }
        String digitos = cpf.replaceAll("\\D", "");
        if (digitos.chars().distinct().count() == 1) {
            return false; // 000.000.000-00, 111.111.111-11 etc.
        }
        return calcularDigitoVerificador(digitos, 9) == digitos.charAt(9) - '0'
                && calcularDigitoVerificador(digitos, 10) == digitos.charAt(10) - '0';
    }

    private static int calcularDigitoVerificador(String digitos, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (digitos.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do funcionário é obrigatório.");
        }
        return nome;
    }

    private static String validarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("O CPF do funcionário é obrigatório.");
        }
        if (!cpfValido(cpf)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
        return cpf.replaceAll("\\D", "");
    }

    /**
     * Garante que um valor monetário/numérico não seja negativo nem inválido (NaN/infinito).
     *
     * @return o próprio valor, se válido
     */
    protected static double validarNaoNegativo(double valor, String campo) {
        if (Double.isNaN(valor) || Double.isInfinite(valor) || valor < 0) {
            throw new IllegalArgumentException(
                    "Valor inválido para " + campo + ": " + valor + " (deve ser maior ou igual a zero).");
        }
        return valor;
    }

    /**
     * Formata um valor em reais no padrão brasileiro, ex.: R$ 1.500,00.
     */
    protected static String formatarMoeda(double valor) {
        return String.format(PT_BR, "R$ %,.2f", valor);
    }

    /**
     * Formata um percentual no padrão brasileiro, ex.: 10,00%.
     */
    protected static String formatarPercentual(double valor) {
        return String.format(PT_BR, "%.2f%%", valor);
    }

    @Override
    public String toString() {
        return String.format(PT_BR, "Código: %d | Nome: %s | CPF: %s | Tipo: %s | Remuneração: %s",
                codigo, nome, getCpfFormatado(), getClass().getSimpleName(),
                formatarMoeda(calcularRemuneracao()));
    }
}
