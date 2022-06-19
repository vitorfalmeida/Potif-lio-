import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cliente {
	public static void main(String[] args) throws Exception {
		//Declarações
		Scanner teclado = new Scanner(System.in);
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm");
		Cifracesar cripto = new Cifracesar();
		int tamanho = 1, chave = 10;
		String mensagemCliente="", mensagemServidorString="", auxMensg="", nome, nomeContato="", hora;
		byte[] mensagemClienteByte, dadosServidor;
		Boolean primeiraMensg = true;
		
		System.out.print("Digite seu primeiro nome: ");
		nome = teclado.nextLine();

		//Realiza a conexão socket com o servidor
		Socket conexao = new Socket("172.16.221.233", 9090);
		
		
		//Apresentando dados sobre a conexão
		System.out.println("=========================================================================");
		System.out.println("Conexão estabelecida!");
		System.out.println("=========================================================================");
		
		//Define objetos de entrada e saida de dados
		InputStream entrada = conexao.getInputStream();
		OutputStream saida = conexao.getOutputStream();
		
		while (auxMensg.equals("sair") != true && mensagemServidorString.equals("sair") != true) {
			//Atualizando a hora
			hora = dtf2.format(LocalDateTime.now());
			
			if(mensagemServidorString != "sair") {

				if(primeiraMensg == true) {
					//Enviando o nome do contato Cliente
					mensagemCliente = "nome;" + nome + ";";
					mensagemClienteByte = cripto.cifraTexto(mensagemCliente, chave);
					saida.write(mensagemClienteByte);
					saida.flush();
					
					//Recebendo e tratando o nome do contato Servidor
					dadosServidor = new byte[100];
					tamanho = entrada.read(dadosServidor);
					mensagemServidorString = cripto.decifrarTexto(dadosServidor, chave, tamanho);
					String[] auxNomeContato = mensagemServidorString.split(";");
					nomeContato = auxNomeContato[1];
					
					
					primeiraMensg = false; //Para não cair novamente nessa condição
				} else {
				
				
					System.out.print("\t\t\t\t\t      " + hora + " " + nome + " diz: \n\t\t\t\t\t");
					mensagemCliente = teclado.nextLine();
					auxMensg = mensagemCliente;
					//Se o usuário digitar sair, ele não ficará aguardando receber mensagem
					if(auxMensg.equals("sair") != true) {
						
						
						//Criptografia da mensagem a ser enviada
						mensagemClienteByte = cripto.cifraTexto(mensagemCliente, chave);
						
						//Transformando em byte os dados de saida já criptografados e envia
						saida.write(mensagemClienteByte);
						saida.flush();
						
						
						//Layout
						//System.out.println("\n-------------------------------------------------------------------------");
						System.out.println("\n");
						System.out.println("<Aguardando mensagem>");
			
						//Armazenamento e leitura dos dados recebidos
						dadosServidor = new byte[100];
						tamanho = entrada.read(dadosServidor);
						
						//Descriptografia dos dados recebidos
						mensagemServidorString = cripto.decifrarTexto(dadosServidor, chave, tamanho);
						
						//Apresentação da mensagem descriptografada
						System.out.println(hora + " " + nomeContato + " diz: \n" + mensagemServidorString);
						
						//Layout
						//System.out.println("\n-------------------------------------------------------------------------");
						System.out.println("\n");
					}
				}
			}
		}
		System.out.println("=========================================================================");
		System.out.println("<CONVERSA FINALIZADA>");
	}

}