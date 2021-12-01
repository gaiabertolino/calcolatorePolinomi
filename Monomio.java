package Bertolino.polinomi;

public class Monomio implements Comparable<Monomio> {
	
	// VARIABILI D'ISTANZA
	private final int COEFF, GRADO;
	
	// COSTRUTTORE
	public Monomio( final int coeff, final int grado ) {
		if( grado<0 ) throw  new IllegalArgumentException();
		this.COEFF=coeff; 
		this.GRADO=grado;
	}//Monomio
	
	// COSTRUTTORE
	public Monomio( Monomio m ) {
		this.COEFF=m.COEFF;
		this.GRADO=m.GRADO;
	}//Monomio
	
	// GETTERS
	public int getCoeff() { 
		return COEFF; 
	}//getCoeff
	
	public int getGrado() { 
		return GRADO; 
	}//getGrado
	
	// METODI VARI
	public Monomio add( Monomio m ) {
		if( !this.equals(m) ) 
			throw new RuntimeException("Monomi non simili!");
		return new Monomio( COEFF+m.COEFF, GRADO );
	}//add
	
	public Monomio mul( int s ) {
		return new Monomio( COEFF*s, GRADO );
	}//mul
	
	public Monomio mul( Monomio m ) {
		return new Monomio( COEFF*m.COEFF, GRADO+m.GRADO );
	}//mul
	
	@Override
	public boolean equals( Object x ) {
		if( !(x instanceof Monomio) ) return false;
		if( x==this ) return true;
		Monomio m=(Monomio)x;
		return this.GRADO==m.GRADO;
	}//equals
	
	@Override
	public int hashCode() {
		return GRADO;
	}//hashCode
	
	public int compareTo( Monomio m ) {
		if( this.GRADO>m.GRADO ) return -1;
		if( this.equals(m) ) return 0;
		return 1;
	}//compareTo
	
	public String toString() {
		StringBuilder sb=new StringBuilder(30);
		if( COEFF==0 ) 
			sb.append(0);
		else {
			if( COEFF<0 ) //base negativa
				sb.append('-');
			if( Math.abs(COEFF)!=1 || GRADO==0 ) //base diversa da uno o grado uguale a 0
				sb.append( Math.abs(COEFF) );
			if( COEFF!=0 && GRADO>0 ) //base diversa da zero e grado maggiore di 0
				sb.append('x');
			if( COEFF!=0 && GRADO>1 ) { //base diversa da zero e grado maggiore di 1
				sb.append('^'); 
				sb.append(GRADO); 
			}
		}
		return sb.toString();
	}//toString
	
}//Monomio
