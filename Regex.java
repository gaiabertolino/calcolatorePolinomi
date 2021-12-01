package Bertolino.polinomi;

public class Regex {

	static String POLINOMIO = "\\-?\\d*((x\\^){1}\\d+|x)?(([\\-\\+]{1})\\d*((x\\^){1}\\d+|x)?)*";
	
	static boolean riconoscimento (String s){
		if (s.matches(POLINOMIO))
			return true;
		return false;
	}//riconoscimento
	
	private static Monomio monomio(String s, int i) {
		String coefficiente = "";
		String grado = "";
		boolean x = false;
		if (s.charAt(i) == '-' || s.charAt(i) == '+') {
			coefficiente += s.charAt(i);
			i++;
		}
		while (i<s.length()) {
			if (s.charAt(i) == '-' || s.charAt(i) == '+')
				break;
			else if (s.charAt(i) == 'x')
				x = true;
			else if (!x)
				coefficiente += s.charAt(i);
			else if (x && s.charAt(i) != '^') 
				grado += s.charAt(i);
			
			i++;
		}
		if (grado.equals("") && !x)
			 grado += 0;
		else if (grado.equals("") && x)
			 grado += 1;
		if (coefficiente.equals("+") || coefficiente.equals("-"))
			coefficiente += 1;
		return new Monomio(Integer.parseInt(coefficiente), Integer.parseInt(grado));
	}//monomio

	static PolinomioSet divisore (String s) {
		PolinomioSet polinomio = new PolinomioSet();
		int i = 0;
		while (i<s.length()) {
			if (s.charAt(i) == '-' || s.charAt(i) == '+')
				polinomio.add(monomio(s,i));
			else if (i==0)
				polinomio.add(monomio(s,i));
			i++;
		}
		return polinomio;
	}//divisore

}//Regex
