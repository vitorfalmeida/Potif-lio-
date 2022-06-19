Socket - Java
import java.util.Scanner;
import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Servidor {

	public static void main(String[] args) throws Exception {
		//Declarações
		Scanner in = new Scanner(System.in);
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm");
		Cifracesar cripto = new Cifracesar();
		int tamanho = 1, chave = 10;
		String mensagemCliente="", mensagemServidor, auxMensg="", nome, nomeContato="", hora;
		byte[] mensagemServidorByte, dadosCliente;
		Boolean primeiraMensg = true;
		
		System.out.print("Digite seu primeiro nome: ");
		nome = in.nextLine();
		
		
		// Abre a porta para conexões no socket
		ServerSocket servidor = new ServerSocket(9090);
		
		//Apresentando dados sobre a conexão
		System.out.println("=========================================================================");
		System.out.println("Aguardando cliente se conectar...");
		Socket conexao = servidor.accept();
		System.out.println("Conexão estabelecida!");
		System.out.println("=========================================================================");
		
		//Defindo objetos de entrada e saida de dados
		InputStream entrada = conexao.getInputStream();
		OutputStream saida = conexao.getOutputStream();
		
		
		while (auxMensg.equals("sair") != true && mensagemCliente.equals("sair") != true) {
			//Atualizando a hora
			hora = dtf2.format(LocalDateTime.now());
			
			//Armazenamento e leitura dos dados recebidos
			dadosCliente = new byte[100];
			tamanho = entrada.read(dadosCliente);
			
			//Descriptografia dos dados recebidos
			mensagemCliente = cripto.decifrarTexto(dadosCliente, chave, tamanho);
			
			
			if(primeiraMensg == true) {
				//Recebendo e tratando o nome do contato
				String[] auxNomeContato = mensagemCliente.split(";");
				nomeContato = auxNomeContato[1];
				
				//Enviando o nome do contato Servidor
				mensagemServidor = "nome;" + nome + ";";
				mensagemServidorByte = cripto.cifraTexto(mensagemServidor, chave);
				saida.write(mensagemServidorByte);
				saida.flush();
				
				
				
				primeiraMensg = false; //Para não cair novamente nessa condição
			} else {
				//Apresentação da mensagem descriptografada
				System.out.println(hora + " " + nomeContato + " diz: \n" + mensagemCliente);

				//Layout
				//System.out.println("\n-------------------------------------------------------------------------");
				System.out.println("\n");
				
				//Enviar resposta para o cliente:
				System.out.print("\t\t\t\t\t      " + hora + " " + nome + " diz: \n\t\t\t\t\t");
				mensagemServidor = in.nextLine();
				auxMensg = mensagemServidor;
	
				//Criptografia da mensagem a ser enviada
				mensagemServidorByte = cripto.cifraTexto(mensagemServidor, chave);
				
				//Transformando em byte os dados de saida já criptografados e envia
				saida.write(mensagemServidorByte);
				saida.flush();
				
				//Layout
				//System.out.println("\n-------------------------------------------------------------------------");
				System.out.println("\n");

				}

			System.out.println("<Aguardando mensagem>");
		}
		System.out.println("=========================================================================");
		System.out.println("<CONVERSA FINALIZADA>");
		
	}

}
