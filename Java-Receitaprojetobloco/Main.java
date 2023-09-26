import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

class Ingrediente {
    String nome;
    double preco;
    double quantidade;
    double quantidadeUtilizada;

    public Ingrediente(String nome, double preco, double quantidade, double quantidadeUtilizada) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.quantidadeUtilizada = quantidadeUtilizada;
    }

    public double calcularCusto() {
        double custoUnitario = preco / quantidade; // Calcula o custo por unidade do ingrediente
        return custoUnitario * quantidadeUtilizada; // Calcula o custo total do ingrediente utilizado
    }
}

class Receita {
    String nome;
    ArrayList<Ingrediente> ingredientes = new ArrayList<>();

    public Receita(String nome) {
        this.nome = nome;
    }

    public void adicionarIngrediente(String nome, double preco, double quantidade, double quantidadeUtilizada) {
        Ingrediente ingrediente = new Ingrediente(nome, preco, quantidade, quantidadeUtilizada);
        ingredientes.add(ingrediente);
    }
    public void editarIngrediente(int indice, String nome, double preco, double quantidade, double quantidadeUtilizada) {
        Ingrediente ingrediente = ingredientes.get(indice);
        ingrediente.nome = nome;
        ingrediente.preco = preco;
        ingrediente.quantidade = quantidade;
        ingrediente.quantidadeUtilizada = quantidadeUtilizada;
    }

    public double calcularCustoTotal() {
        double custoTotal = 0;
        for (Ingrediente ingrediente : ingredientes) {
            custoTotal += ingrediente.calcularCusto();
        }
        return custoTotal;
    }

    public void mostrarIngredientes() {
        System.out.println("Ingredientes da receita '" + nome + "':");
        for (Ingrediente ingrediente : ingredientes) {
            System.out.println("- " + ingrediente.nome + " (" + ingrediente.quantidadeUtilizada + ") - R$" + ingrediente.calcularCusto());
        }
    }
public static void salvarReceitasCSV(ArrayList<Receita> receitas) {
    try {
        FileWriter writer = new FileWriter("receitas.csv");
        writer.write("Nome da Receita,Valor,Ingredientes\n");

        for (Receita receita : receitas) {
            StringBuilder ingredientes = new StringBuilder();
            for (Ingrediente ingrediente : receita.ingredientes) {
                ingredientes.append(ingrediente.nome)
                           .append(" (")
                           .append(ingrediente.quantidadeUtilizada)
                           .append("), ");
            }
            if (ingredientes.length() > 0) {
                ingredientes.delete(ingredientes.length() - 2, ingredientes.length()); // Remover a vírgula final
            }

            double total = receita.calcularCustoTotal();
            writer.write(receita.nome + "," + "R$: " + total + "," + "Ingredientes: " + ingredientes.toString() + "\n");
        }

        writer.close();
        System.out.println("Receitas salvas com sucesso em 'receitas.csv'!");
    } catch (IOException e) {
        System.err.println("Erro ao salvar as receitas em 'receitas.csv': " + e.getMessage());
    }
}
public static class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Receita> receitas = new ArrayList<>();

        // Definir o Locale para usar ponto como separador decimal
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", dfs);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Adicionar Receita");
            System.out.println("2. Ver Receitas");
            System.out.println("3. Editar Receita");
            System.out.println("4. Salvar Receitas em CSV");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            int escolha = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            if (escolha == 1) {
                System.out.print("Digite o nome da receita: ");
                String nomeReceita = scanner.nextLine();
                Receita receita = new Receita(nomeReceita);

                while (true) {
                    System.out.print("Digite o nome do ingrediente (ou 'sair' para concluir): ");
                    String nomeIngrediente = scanner.nextLine();
                    if (nomeIngrediente.equalsIgnoreCase("sair")) {
                        break;
                    }
                    System.out.print("Digite o preço do ingrediente: ");
                    double precoIngrediente = scanner.nextDouble();
                    scanner.nextLine(); // Limpar o buffer
                    System.out.print("Digite a quantidade do ingrediente (em gramas ou ml): ");
                    double quantidadeIngrediente = scanner.nextDouble();
                    scanner.nextLine(); // Limpar o buffer
                    System.out.print("Digite a quantidade utilizada (em gramas ou ml): ");
                    double quantidadeUtilizada = scanner.nextDouble();
                    scanner.nextLine(); // Limpar o buffer

                    receita.adicionarIngrediente(nomeIngrediente, precoIngrediente, quantidadeIngrediente, quantidadeUtilizada);
                }

                receitas.add(receita);
                System.out.println("Receita adicionada com sucesso!");
            } else if (escolha == 2) {
                System.out.println("Receitas:");
                for (int i = 0; i < receitas.size(); i++) {
                    Receita receita = receitas.get(i);
                    System.out.println((i + 1) + ". " + receita.nome);
                }

                System.out.print("Escolha uma receita para ver os ingredientes (ou 'sair' para voltar): ");
                String escolhaReceita = scanner.nextLine();
                if (escolhaReceita.equalsIgnoreCase("sair")) {
                    continue;
                }

                int receitaEscolhida = Integer.parseInt(escolhaReceita);
                if (receitaEscolhida >= 1 && receitaEscolhida <= receitas.size()) {
                    Receita receita = receitas.get(receitaEscolhida - 1);
                    receita.mostrarIngredientes();

                    System.out.println("Total gasto na receita '" + receita.nome + "': R$" + receita.calcularCustoTotal());
                } else {
                    System.out.println("Escolha inválida.");
                }
            } else if (escolha == 3) {
                System.out.println("Editar Receita:");
                System.out.println("Receitas:");
                for (int i = 0; i < receitas.size(); i++) {
                    Receita receita = receitas.get(i);
                    System.out.println((i + 1) + ". " + receita.nome);
                }

                System.out.print("Escolha uma receita para editar (ou 'sair' para voltar): ");
                String escolhaEdicao = scanner.nextLine();
                if (escolhaEdicao.equalsIgnoreCase("sair")) {
                    continue;
                }

                int receitaEditar = Integer.parseInt(escolhaEdicao);
                if (receitaEditar >= 1 && receitaEditar <= receitas.size()) {
                    Receita receita = receitas.get(receitaEditar - 1);
                    receita.mostrarIngredientes();

                    System.out.println("Escolha o ingrediente para editar:");
                    for (int i = 0; i < receita.ingredientes.size(); i++) {
                        Ingrediente ingrediente = receita.ingredientes.get(i);
                        System.out.println((i + 1) + ". " + ingrediente.nome);
                    }
                    System.out.println((receita.ingredientes.size() + 1) + ". Adicionar novo ingrediente");
                    System.out.print("Digite o número do ingrediente para editar ou adicionar um novo: ");
                    int escolhaIngrediente = scanner.nextInt();
                    scanner.nextLine(); // Limpar o buffer

                    if (escolhaIngrediente >= 1 && escolhaIngrediente <= receita.ingredientes.size()) {
                        int indiceIngrediente = escolhaIngrediente - 1;
                        Ingrediente ingrediente = receita.ingredientes.get(indiceIngrediente);

                        // Resto do código para editar o ingrediente...
                        

                        System.out.print("Digite o novo nome do ingrediente: ");
                        String novoNome = scanner.nextLine();
                        System.out.print("Digite o novo preço do ingrediente: ");
                        double novoPreco = scanner.nextDouble();
                        scanner.nextLine(); // Limpar o buffer
                        System.out.print("Digite a nova quantidade do ingrediente (em gramas ou ml): ");
                        double novaQuantidade = scanner.nextDouble();
                        scanner.nextLine(); // Limpar o buffer
                        System.out.print("Digite a nova quantidade utilizada do ingrediente (em gramas ou ml): ");
                        double novaQuantidadeUtilizada = scanner.nextDouble();
                        scanner.nextLine(); // Limpar o buffer

receita.editarIngrediente(indiceIngrediente, novoNome, novoPreco, novaQuantidade, novaQuantidadeUtilizada);
System.out.println("Ingrediente editado com sucesso!");
                    } else if (escolhaIngrediente == receita.ingredientes.size() + 1) {
                        System.out.print("Digite o nome do novo ingrediente: ");
                        String novoNome = scanner.nextLine();
                        System.out.print("Digite o preço do novo ingrediente: ");
                        double novoPreco = scanner.nextDouble();
                        scanner.nextLine(); // Limpar o buffer
                        System.out.print("Digite a quantidade do novo ingrediente (em gramas ou ml): ");
                        double novaQuantidade = scanner.nextDouble();
                        scanner.nextLine(); // Limpar o buffer
                        System.out.print("Digite a quantidade utilizada do novo ingrediente (em gramas ou ml): ");
                        double novaQuantidadeUtilizada = scanner.nextDouble();
                        scanner.nextLine(); // Limpar o buffer

                        receita.adicionarIngrediente(novoNome, novoPreco, novaQuantidade, novaQuantidadeUtilizada);
                        System.out.println("Novo ingrediente adicionado com sucesso!");
                    } else {
                        System.out.println("Escolha de ingrediente inválida.");
                    }
                } else {
                    System.out.println("Escolha inválida.");
                }
            } else if (escolha == 4) {
                salvarReceitasCSV(receitas);
            } else if (escolha == 5) {
                System.out.println("Saindo do programa.");
                break;
            } else {
                System.out.println("Escolha inválida. Tente novamente.");
            }
        }

        scanner.close();
    }
}
}