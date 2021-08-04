package poo.progetto_sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class SudokuGUI extends JFrame implements ActionListener
{
	private JTextField[][] matrice;
	private JButton next, previous, imposta;
	private JMenuBar menuBar;
	private JMenu file, comandi,  help;
	private JMenuItem apri, salva, esci, svuota,  info;
	private int n = 9;
	private Sudoku sudoku;
	private List<Integer[][]> soluzioni;
	private Integer[][] soluzioneCorrente;
	private File fileSalvataggio;
	
	public SudokuGUI()
	{	setTitle("Sudoku");
		setLocation(550, 200);
		setSize(400, 460);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{	public void windowClosing(WindowEvent e)
			{	int cd = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler uscire?", "Exit", 0, 2);
				if(cd == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		
		//barra
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		//menuFile
		file = new JMenu("File");
		menuBar.add(file);
		apri = new JMenuItem("Apri");
		apri.addActionListener(this);
		salva = new JMenuItem("Salva");
		salva.addActionListener(this);
		salva.setEnabled(false);
		esci = new JMenuItem("Esci");
		esci.addActionListener(this);
		file.add(apri);
		file.add(salva);
		file.addSeparator();
		file.add(esci);
		
		//menuComandi
		comandi = new JMenu("Comandi");
		menuBar.add(comandi);
		svuota = new JMenuItem("Svuota");
		svuota.addActionListener(this);
		svuota.setEnabled(false);
		comandi.add(svuota);
		
		//menuHelp
		help = new JMenu("Help");
		menuBar.add(help);
		info = new JMenuItem("Informazioni");
		info.addActionListener(this);
		help.add(info);
		
		//pannelloMatrice
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(n, n));
		matrice = new JTextField[n][n];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
			{	JTextField campoTesto = new JTextField("");
				p1.add(campoTesto);
				matrice[i][j] = campoTesto;
				matrice[i][j].setFont(new Font(null, Font.PLAIN, 25));
				matrice[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				if(j == 2 || j == 5)
					matrice[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.BLACK));
				if(i == 2 || i == 5)
					matrice[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.BLACK));
				if( (i == 2 && j == 2) || (i == 2 && j == 5) || (i == 5 && j == 2) || (i == 5 && j == 5))
					matrice[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.BLACK));
			}
		add(p1, BorderLayout.NORTH);
		
		//pannelloBottoni
		JPanel p2 = new JPanel();
		previous = new JButton("Precedente");
		imposta = new JButton("Imposta");
		next = new JButton("Successiva");
		previous.addActionListener(this);
		imposta.addActionListener(this);
		next.addActionListener(this);
		p2.add(previous);
		p2.add(imposta);
		p2.add(next);
		add(p2, BorderLayout.SOUTH);

	}//SudokuGUI
	
	public static void main(String[] args)
	{
		JFrame s = new SudokuGUI();
		s.setVisible(true);
	}//main

	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == imposta)
		{	sudoku = new Sudoku();
			for(int i = 0; i < n; i++)
				for(int j = 0; j < n; j++)
					try
					{	if(!matrice[i][j].getText().equals(""))
						{	sudoku.imposta(i, j, Integer.parseInt(matrice[i][j].getText()));
							matrice[i][j].setFont(new Font(null, Font.BOLD, 30));
							matrice[i][j].setText(matrice[i][j].getText());
						}
						matrice[i][j].setEditable(false);
					}
					catch(RuntimeException ex)
					{	JOptionPane.showMessageDialog(null, "Assegnamento/i non consentito/i, ripetere...", "Errore", 0);
						for(int h = 0; h < n; h++)
							for(int k = 0; k < n; k++)
							{	matrice[h][k].setEditable(true);
								matrice[h][k].setFont(new Font(null, Font.PLAIN, 25));
							}
						return;
					}
			imposta.setEnabled(false);
			sudoku.risolvi();
			soluzioni = sudoku.getSoluzioni();
			Collections.shuffle(soluzioni);
			svuota.setEnabled(true);
			salva.setEnabled(true);
			return;
		}//imposta
		
		if(e.getSource() == next)
		{	if(soluzioneCorrente == null)
				soluzioneCorrente = soluzioni.get(0);
			else
			{	try
				{	soluzioneCorrente = soluzioni.get(soluzioni.indexOf(soluzioneCorrente) + 1);}
				catch(IndexOutOfBoundsException ex)
				{	return;}
			}
			for(int i = 0; i < n; i++)
				for(int j = 0; j < n; j++)
					matrice[i][j].setText(soluzioneCorrente[i][j] + "");
			salva.setEnabled(false);
			return;
		}//next
		
		if(e.getSource() == previous)
		{	if(soluzioneCorrente == null || soluzioneCorrente.equals(soluzioni.get(0)))
				return;
			try
			{	soluzioneCorrente = soluzioni.get(soluzioni.indexOf(soluzioneCorrente) - 1);}
			catch(IndexOutOfBoundsException ex)
			{	return;}
			for(int i = 0; i < n; i++)
				for(int j = 0; j < n; j++)
					matrice[i][j].setText(soluzioneCorrente[i][j] + "");
			return;
		}//previous
		
		if(e.getSource() == svuota)
		{	svuota();
			svuota.setEnabled(false);
			salva.setEnabled(false);
			imposta.setEnabled(true);
			return;
		}//svuota
		
		if(e.getSource() == salva)
		{	JFileChooser chooser=new JFileChooser();
			try
			{	if(fileSalvataggio != null)
				{	int ans = JOptionPane.showConfirmDialog(null,"Sovrascrivere "+fileSalvataggio.getAbsolutePath()+" ?");
					if(ans == 0 )//se si vuole sovrascrivere
						salva(fileSalvataggio.getAbsolutePath());
					return;
				}
				if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				{	fileSalvataggio = chooser.getSelectedFile();
					this.setTitle("Sudoku - " + fileSalvataggio.getName());
				}
				if(fileSalvataggio != null )
					salva(fileSalvataggio.getAbsolutePath());
			}
			catch( Exception exc )
			{	exc.printStackTrace();}
			return;
		}//salva
		
		if(e.getSource() == apri)
		{	if(sudoku != null)
			{	int cd = JOptionPane.showConfirmDialog(null, "Aprendo un altro file perderai perderai i dati attuali, sei sicuro di voler continuare?", "Attenzione", 0, 2);
				if(cd == JOptionPane.NO_OPTION)
					return;
			}
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("Documenti TXT", "txt");
			jfc.setFileFilter(filtro);
			int val = jfc.showOpenDialog(null);
			if(val == JFileChooser.APPROVE_OPTION)
				try
				{	ripristina(jfc.getSelectedFile().getAbsolutePath());}
				catch (IOException e1)
				{	e1.printStackTrace();}
			salva.setEnabled(true);
			svuota.setEnabled(true);
			return;
		}//apri
		
		if(e.getSource() == info)
		{	JOptionPane.showMessageDialog(null, "Questo programma consente di visualizzare 100 soluzioni, se esistono, per un problema pre-impostato di Sudoku.\n"
				+ "								- Per impostare un problema inserire le cifre da 1 a 9 nelle caselle desiderate e premere su Imposta.\n"
				+ "								- Solo dopo aver impostato un problema è possibile salvare l'attuale configurazione in un file di testo.\n"
				+ "								- E' possibile ripristinare una nuova configurazione da un file di testo in qualunque momento.\n"
				+ "								- Il comando Svuota consente di svuotare tutte le caselle.\n"
				+ "								- I bottoni Precedente e Successiva consentono di cambiare soluzione.", "Informazioni", 1);
			return;
		}
		
		if(e.getSource() == esci)
		{	int cd = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler uscire?", "Exit", 0, 2);
			if(cd == JOptionPane.YES_OPTION)
			System.exit(0);
		}//esci
		
	}//actionPerformed
	
	private void svuota()
	{	sudoku = null;
		soluzioni = null;
		soluzioneCorrente = null;
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
			{	matrice[i][j].setText("");
				matrice[i][j].setEditable(true);
				matrice[i][j].setFont(new Font(null, Font.PLAIN, 25));
			}
	}//svuota
	
	private void salva(String nomeFile) throws IOException{
		PrintWriter pw = new PrintWriter(new FileWriter(nomeFile));
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				if(matrice[i][j].getText().equals(""))
					pw.println("0");
				else
				pw.println(matrice[i][j].getText());
		pw.close();
	}//salva
	
	private void ripristina(String nomeFile) throws IOException{
		svuota();
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(nomeFile));
		loop: for(int i = 0; i < n; i++)
				for(int j = 0; j < n; j++)
				{	String linea = br.readLine();
					if(linea == null)//fine file
						break;
					StringTokenizer st = new StringTokenizer(linea);
					if(st.hasMoreTokens())
					{	String s = st.nextToken();
						if(!s.matches("[0-9]"))
						{	JOptionPane.showMessageDialog(null, "Il file deve contenere solo 81 cifre da 1 a 9, una per linea.", "Errore", 0);
							break loop;
						}
						if(!s.equals("0"))
							matrice[i][j].setText(s);
					}
					else
					{	JOptionPane.showMessageDialog(null, "Il file deve contenere solo 81 cifre da 1 a 9, una per linea.", "Errore", 0);
						break loop;
					}
				}
		br.close();
	}//ripristina
	
	
}//SudokuGUI
