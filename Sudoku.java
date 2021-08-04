package poo.progetto_sudoku;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Ps
{	
	int riga, colonna;
	
	public Ps(int riga, int colonna)
	{	this.riga = riga;
		this.colonna = colonna;
	}//Ps
	
	@Override
	public boolean equals(Object o)
	{	if(!(o instanceof Ps))
			return false;
		if(o == this)
			return true;
		Ps p = (Ps)o;
		return riga == p.riga && colonna == p.colonna;
	}//equals
	
	@Override
	public int hashCode()
	{	final int M = 83;
		int h = riga * M + colonna;
		return h;
	}//hashCode
}//Ps

public class Sudoku extends Backtracking<Ps, Integer>
{	
	private int[][] board;
	private int n, nrSol = 0;
	private Set<Ps> bloccati = new HashSet<>();
	private List<Integer[][]> soluzioni = new ArrayList<>();
	
	public Sudoku()
	{	board = new int[9][9];
		n = board.length;
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				board[i][j] = 0;//superfluo
	}//Sudoku
	
	public int getNumeroSoluzioni()
	{	return nrSol;
	}//getNumeroSoluzioni
	
	public ArrayList<Integer[][]> getSoluzioni()
	{	return new ArrayList<>(soluzioni);
	}
	
	public void imposta(int i, int j, int v)
	{	if(v < 1 || v > 9 || i < 0 || i > 8 || j < 0 || j > 8 || !assegnabile(new Ps(i, j), v))
			throw new RuntimeException("Assegnamento non consentito");
		board[i][j] = v;
		bloccati.add(new Ps(i, j));
	}//imposta
	
	@Override
	protected List<Ps> puntiDiScelta()
	{	List<Ps> ps = new ArrayList<>();
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
			{	Ps tmp = new Ps(i, j);
				if(!bloccati.contains(tmp))
					ps.add(tmp);//aggiunge tutte le celle non preimpostate
			}
		return ps;
	}//puntiDiScelta

	@Override
	protected Collection<Integer> scelte(Ps p)
	{	List<Integer> s = new ArrayList<>();
		for(int i = 1; i <= n; i++)
			s.add(i);
		return s;
	}//scelte
	
	@Override
	protected boolean assegnabile(Ps p, Integer s)
	{	//controlla la riga
		for(int j = 0; j < n; j++)
			if(board[p.riga][j] == s)
				return false;
		//controlla la colonna
		for(int i = 0; i < n; i++)
			if(board[i][p.colonna] == s)
				return false;
		//controlla la sottomatrice
		int sqrt = (int)Math.sqrt(n);
		int row = p.riga - p.riga % sqrt;		//indice di riga di partenza
		int col = p.colonna - p.colonna % sqrt; //indice di colonna di partenza
		for(int r = row; r < row + sqrt; r++) 
			for(int c = col; c < col + sqrt; c++) 
				if(board[r][c] == s) 
					return false;
		return true;
	}//assegnabile

	@Override
	protected void assegna(Ps ps, Integer s)
	{	board[ps.riga][ps.colonna] = s;
	}//assegna

	@Override
	protected void deassegna(Ps ps, Integer s)
	{	board[ps.riga][ps.colonna] = 0;
	}//deassegna
	
	@Override
	protected boolean esisteSoluzione( Ps p )
	{	for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				if(board[i][j] == 0)
					return false;
		return true;
	}//esisteSoluzione
	
	@Override
	protected boolean ultimaSoluzione( Ps p ) {
		return nrSol == 100;
	}//ultimaSoluzione
	
/*	@Override
	protected void scriviSoluzione(Ps p)
	{	nrSol++;
		System.out.println("Soluzione " + nrSol + ":");
		for(int i = 0; i < n; i++) 
		{	for(int j = 0; j < n; j++)
			{	System.out.print(board[i][j]);
				System.out.print(" ");
				if(j == 2 || j == 5)
					System.out.print(" ");
				if( (j == 8) && (i == 2 || i == 5))
					System.out.println();
			}
			System.out.println();
		}
		System.out.println();
	}//scriviSoluzione
*/
	
	@Override
	protected void scriviSoluzione(Ps p)
	{	nrSol++;
		Integer[][] ris = new Integer[n][n];
		for(int i = 0; i < n; i++) 
			for(int j = 0; j < n; j++)
				ris[i][j] = board[i][j];
		soluzioni.add(ris);
	}//scriviSoluzione
	
	@Override
	protected void risolvi()
	{	List<Ps> ps = puntiDiScelta();
		tentativo(ps, ps.get(0));
	}//risolvi
	
	public static void main( String[] args )
	{	Sudoku s = new Sudoku();
		s.imposta(0, 7, 2);
		s.imposta(1, 1, 3);
		s.imposta(1, 5, 7);
		s.imposta(2, 2, 5);
		s.imposta(3, 4, 1);
		s.imposta(5, 0, 9);
		s.imposta(6, 8, 4);
		s.imposta(8, 3, 5);
		s.risolvi();
		
		Integer[][] h = s.soluzioni.get(1);
		for(int i = 0; i < h.length; i++)
			System.out.println(java.util.Arrays.deepToString(h[i]));
		
		
	}//main

}
