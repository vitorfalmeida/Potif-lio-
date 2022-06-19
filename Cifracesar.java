
public class Cifracesar {
		// Metodo de cifrar um texto
		public static byte[] cifraTexto (String texto, int chave) throws Exception{
			char[] letras = texto.toCharArray();
			for(int i = 0; i<letras.length; i++) {
				letras[i] = (char)(letras[i] + chave);			
			}
			
			String textoCifradoString = new String(letras);
			byte[] textoCifradoByte = textoCifradoString.getBytes("UTF-8");
			return textoCifradoByte;
		}
		
		public static  String decifrarTexto (byte[] letrasByte, int chave, int tamanho)throws Exception{
			String letrasString = new String(letrasByte);
			char[] letras = letrasString.toCharArray();
			for(int i = 0; i<tamanho; i++) {
				letras[i] = (char)(letras[i] - chave);			
			}
			
			String textoDecifradoString = new String(letras);
			return textoDecifradoString;
		}
}