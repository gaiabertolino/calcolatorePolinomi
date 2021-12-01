package Bertolino.polinomi;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

@SuppressWarnings("serial")
class FinestraGUI extends JFrame implements ActionListener{
	
	private LinkedList<PolinomioSet> polinomi = new LinkedList<>();
	private JMenuBar menu;
	private JMenu file, operazioni;
	private JMenuItem salvataggio, apertura, soluzione, derivata, add, mul, rimuovi;
	private JTextField campo, input;
	private JButton okay;
	private JPanel scroll;
	private JButton calcola, calcolaDue;
	private double val;
	private JFrame finOut, inputName;
	private File testo;
	
	public FinestraGUI() {
		setTitle("Calcolatrice per polinomi");
		setSize(700,500);
		setLocation(400,200);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { 
			if (uscita()) System.exit(0); } } );
		
		menu = new JMenuBar();
		file = new JMenu("File"); 
			salvataggio = new JMenuItem("Salva su file"); salvataggio.addActionListener(this); file.add(salvataggio); 
			apertura = new JMenuItem("Apri da file"); apertura.addActionListener(this); file.add(apertura);
		operazioni = new JMenu("Operazioni"); 
			soluzione = new JMenuItem("Calcola una soluzione"); soluzione.addActionListener(this); operazioni.add(soluzione);
			derivata = new JMenuItem("Calcola la derivata prima"); derivata.addActionListener(this); operazioni.add(derivata); operazioni.addSeparator();
			add = new JMenuItem("Addizione"); add.addActionListener(this); operazioni.add(add);
			mul = new JMenuItem("Moltiplicazione");  mul.addActionListener(this); operazioni.add(mul);
			rimuovi = new JMenuItem("Rimuovi");  rimuovi.addActionListener(this); operazioni.add(rimuovi);
		menu.add(file); menu.add(operazioni);
		
		JLabel titolo = new JLabel("Benvenuto! Scrivi dei polinomi e selezionali per fare delle operazioni", JLabel.CENTER); titolo.setForeground(Color.red);     
		JLabel nome = new JLabel("Digita un polinomio>> ");

		campo = new JTextField(20); 
		campo.addActionListener(this);

		okay = new JButton("Salva"); 
		okay.addActionListener(this);
		
		this.setJMenuBar(menu);
		
		JPanel barra = new JPanel(); barra.add(titolo, BorderLayout.CENTER); barra.setBackground(Color.white); this.add(barra, BorderLayout.NORTH); 
		JPanel fisso = new JPanel(); fisso.add(nome); fisso.add(campo); fisso.add(okay); fisso.setBackground(Color.white); 
		this.add(fisso, BorderLayout.SOUTH);
			
		scroll = new JPanel(new GridLayout(8,8,2,2)); add(scroll);
		
		menuIniziale();

	}//FinestraGUI

	private void refresh() {
         if (counter() == 0)
        	menuIniziale();
        else if (counter() == 1)
        	menuOne();
        else if (counter() == 2)
        	menuTwo();
        else
        	menuMore();
        revalidate(); repaint();
	}//refresh
	
	private boolean uscita(){
		   int scelta=JOptionPane.showConfirmDialog( null, "Uscire ?", "Perderai i dati non salvati!", JOptionPane.YES_NO_OPTION);
		   return scelta==JOptionPane.YES_OPTION;
	}//uscita
	
	private void menuIniziale() {
		salvataggio.setEnabled(false);
		soluzione.setEnabled(false);
		derivata.setEnabled(false);
		add.setEnabled(false);
		mul.setEnabled(false);
		rimuovi.setEnabled(false);
	}//menuIniziale()
	
	private void menuOne() {
		salvataggio.setEnabled(true);
		soluzione.setEnabled(true);
		derivata.setEnabled(true);
		add.setEnabled(false);
		mul.setEnabled(false);
		rimuovi.setEnabled(true);
	}//menuOne()
	
	private void menuTwo() {
		salvataggio.setEnabled(true);
		soluzione.setEnabled(false);
		derivata.setEnabled(true);
		add.setEnabled(true);
		mul.setEnabled(true);
		rimuovi.setEnabled(true);
	}//menuTwo()
	
	private void menuMore() {
		salvataggio.setEnabled(true);
		soluzione.setEnabled(false);
		derivata.setEnabled(true);
		add.setEnabled(false);
		mul.setEnabled(false);
		rimuovi.setEnabled(true);
	}//menuMore()
	
	public void actionPerformed(ActionEvent e) {
		refresh();
		if (e.getSource() == okay) 
			creaPolinomio();
		if (e.getSource() == rimuovi)
			rimuovi(); 
		if ( e.getSource() == derivata)
			derivazione();
		if (e.getSource() == add)
			addizione();
		if (e.getSource() == soluzione)
			finestraSoluzione();
		if (e.getSource() == calcola)
			soluzione();
		if (e.getSource() == mul)
			moltiplicazione();
		if (e.getSource() == calcolaDue)
			try {
				inputName.setVisible(false);
				creaFile();
			} catch (IOException e1) {
				JFrame f = new FinestraErrore("Problemi con la creazione del file. Riprova");
				f.setVisible(true);
			}
		if (e.getSource() == apertura)
			try {
				apriFile();
			} catch (IOException e1) {
				JFrame f = new FinestraErrore("Problemi con l'apertura del file. Riprova");
				f.setVisible(true);
			}
		if (e.getSource() == salvataggio)
				nuovo();
			
	}//actionPerformed()

	private void salvaFile() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new TxtFileFilter());
		int n = fileChooser.showSaveDialog(this);
		try {
			if (n == JFileChooser.APPROVE_OPTION) {
				testo = fileChooser.getSelectedFile();
				BufferedWriter write = new BufferedWriter(new FileWriter(testo));
				for (int i=0; i<scroll.getComponentCount(); i++) {
					if (((JCheckBox) scroll.getComponent(i)).isSelected()) {
						write.append(polinomi.get(i).toString());
						write.newLine();
						}
					}
				write.flush();
				write.close();
	      }
		}
		catch (FileNotFoundException e) {
			JFrame error = new FinestraErrore("Il file non esiste");
			error.setVisible(true);
		}
	}//salvaFile()
		
	private void creaFile() throws IOException {
		String path = input.getText() + ".txt"; 
		testo = new File(path);
		if (!testo.exists())
			testo.createNewFile();
		salvaFile();
	}//creaFile()
	
	private void nuovo(){
	   int scelta=JOptionPane.showConfirmDialog( null, "Vuoi creare un nuovo file?", "File", JOptionPane.YES_NO_OPTION);
	   if (scelta==JOptionPane.YES_OPTION) {
		   input = new JTextField(10);
		   input.addActionListener(this);
		   calcolaDue = new JButton("Crea");
		   calcolaDue.addActionListener(this);
		   inputName = new FinestraInput2();
		   inputName.setVisible(true);
		} else
		try {
			salvaFile();
		} catch (IOException e) {
			JFrame f = new FinestraErrore("Problemi col file. Riprova");
			f.setVisible(true);
		}
	}//nuovo()
	
	private void apriFile() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new TxtFileFilter());
		int n = fileChooser.showOpenDialog(this);
		File testo;
		try {
			if (scroll.getComponentCount() >= 64) throw new IndexOutOfBoundsException();
			if (n == JFileChooser.APPROVE_OPTION) {
	          testo = fileChooser.getSelectedFile();
	          @SuppressWarnings("resource")
	          BufferedReader read = new BufferedReader(new FileReader(testo));
	          String line = "";
	          while(line != null) {
	            line = read.readLine();
	            if (line == null) break;
	            if (Regex.riconoscimento(line) == false) throw new IllegalArgumentException();
	            PolinomioSet p = Regex.divisore(line); polinomi.add(p); JCheckBox c = new JCheckBox(p.toString()); c.addActionListener(this); scroll.add(c); refresh(); 
	          }
	          read.close();
	        }		
		} 
		catch (FileNotFoundException e) {
			JFrame error = new FinestraErrore("Il file non esiste");
			error.setVisible(true);
		} 
		catch (IndexOutOfBoundsException e) {
			JFrame errore = new FinestraErrore("Hai superato il limite. Elimina qualche polinomio");
			errore.setVisible(true); 
		}
		catch (IllegalArgumentException e) {
			JFrame errore = new FinestraErrore("Nel file non ci sono solo polinomi. Riprova");
			errore.setVisible(true);
		}
	}//apriFile()
	
	final class TxtFileFilter extends FileFilter {
		  public boolean accept(File file) {
		    if (file.isDirectory()) return true;
		    String fname = file.getName().toLowerCase();
		    return fname.endsWith("txt");
		  }
		  public String getDescription() {
		    return "File di testo";
		  }
	}//TxtFileFilter()
	
	private void soluzione() {
		finOut.setVisible(false);
		try { val = Double.parseDouble(input.getText()); 
			for (int i=0; i<scroll.getComponentCount(); i++)
				if (((JCheckBox) scroll.getComponent(i)).isSelected()) {
					val = polinomi.get(i).valore(val);
				}
			FinestraOutput f = new FinestraOutput(); 
			f.setVisible(true);
		}
		catch (NumberFormatException e) {
			JFrame error = new FinestraErrore("Hai digitato un valore non numerico!");
			error.setVisible(true);
		}
	}//soluzione()
		
	private void addizione() {
		int fine = scroll.getComponentCount();
		int i = 0;
		Polinomio p1 = new PolinomioSet();
		Polinomio p2 = new PolinomioSet();
		boolean flag = false;
		while (i<fine) {
			if (((JCheckBox) scroll.getComponent(i)).isSelected() && !flag) {
				p1 = (PolinomioSet) polinomi.get(i);
				flag = true;
			}
			else if (((JCheckBox) scroll.getComponent(i)).isSelected()) {
				p2 = (PolinomioSet) polinomi.get(i);
				break;
			}
			i++;
		} 
		p1 = p1.add(p2); JCheckBox c = new JCheckBox(p1.toString()); scroll.add(c); c.addActionListener(this); polinomi.add((PolinomioSet) p1); refresh();
	}//addizione
	
	private int counter() {
		int ris = 0;
		for (int i=0; i<scroll.getComponentCount(); i++)
			if (((JCheckBox) scroll.getComponent(i)).isSelected())
				ris++;
		return ris;
	}//counter()
	
	private void derivazione() {
		int fine = scroll.getComponentCount();
		int i = 0;
		while (i<fine) {
			JCheckBox c = (JCheckBox) scroll.getComponent(i);
			if (c.isSelected()) {
				PolinomioSet p = (PolinomioSet) polinomi.get(i).derivata();
				if (p.size() == 0) {
					JFrame f = new FinestraErrore("Il polinomio " + polinomi.get(i) + " è una costante. La derivata è nulla");
					f.setVisible(true);
				}
				else {
					polinomi.add(p);
					JCheckBox d = new JCheckBox(p.toString()); scroll.add(d); d.addActionListener(this);
				} 
			}
			i++;
		}
	 refresh();
	}//derivazione()
	
	private void moltiplicazione() {
		int fine = scroll.getComponentCount();
		int i = 0;
		Polinomio p1 = new PolinomioSet();
		Polinomio p2 = new PolinomioSet();
		boolean flag = false;
		while (i<fine) {
			if (((JCheckBox) scroll.getComponent(i)).isSelected() && !flag) {
				p1 = (PolinomioSet) polinomi.get(i);
				flag = true;
			}
			else if (((JCheckBox) scroll.getComponent(i)).isSelected()) {
				p2 = (PolinomioSet) polinomi.get(i);
				break;
			}
			i++;
		} 
		p1 = p1.mul(p2); JCheckBox c = new JCheckBox(p1.toString()); scroll.add(c); c.addActionListener(this); polinomi.add((PolinomioSet) p1); refresh();
	}//moltiplicazione()
	
	private void rimuovi() {
		int fine = scroll.getComponentCount();
		int i = 0;
		while (i<fine) {
			JCheckBox c = (JCheckBox) scroll.getComponent(i);
			if (c.isSelected()) {
				polinomi.remove(i);
				fine--;
				scroll.remove(c);
			}
			else
				i++;
		}
		refresh();
	}//rimuovi()
	
	private void creaPolinomio() {
		PolinomioSet p = new PolinomioSet();
		String letta = campo.getText();
		try {
			if (scroll.getComponentCount() >= 64) throw new IndexOutOfBoundsException();
			if (letta.equals("")) throw new NoSuchElementException();
			if (Regex.riconoscimento(letta) == false) throw new IllegalArgumentException();
			p = Regex.divisore(letta); polinomi.add(p); JCheckBox c = new JCheckBox(p.toString()); c.addActionListener(this); scroll.add(c); refresh(); 
		}
		catch (IndexOutOfBoundsException e) {
			JFrame errore = new FinestraErrore("Hai superato il limite. Elimina qualche polinomio");
			errore.setVisible(true); 
		}
		catch (IllegalArgumentException e) {
			JFrame errore = new FinestraErrore("Hai digitato un polinomio non corretto. Riprova");
			errore.setVisible(true);
		}
		catch (NoSuchElementException ex) { 
			JFrame errore = new FinestraErrore("Digita un polinomio prima di procedere");
			errore.setVisible(true);
		}
	}//creaPolinomio()
	
	private class FinestraErrore extends JFrame{
		public FinestraErrore(String errore) {
			setTitle("Errore!");
			setSize(400,100);
			setLocation(550,350);
			Font f = new Font("Helvetica", Font.PLAIN, 15);
			JLabel messaggio = new JLabel(errore); messaggio.setFont(f); messaggio.setForeground(Color.red); messaggio.setHorizontalAlignment(SwingConstants.CENTER);
			add(messaggio, BorderLayout.CENTER);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { setVisible(false);} } );
		}
	}//FinestraErrore
	
	private class FinestraOutput extends JFrame {
		public FinestraOutput() {
			setTitle("Risultato");
			setSize(400,100);
			setLocation(550,350);
			Font f = new Font("Helvetica", Font.PLAIN, 15);
			JLabel messaggio = new JLabel("La soluzione del polinomio è " + val); messaggio.setFont(f); messaggio.setForeground(Color.black); messaggio.setHorizontalAlignment(SwingConstants.CENTER);
			add(messaggio, BorderLayout.CENTER);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { setVisible(false);} } );
		}
	}//FinestraOutput
	
	private void finestraSoluzione() {
		input = new JTextField(10);
		input.addActionListener(this);
		calcola = new JButton("Calcola");
		calcola.addActionListener(this);
		finOut = new FinestraInput1();
		finOut.setVisible(true);
	}//soluzione()
	
	private class FinestraInput1 extends JFrame {
		public FinestraInput1() {
			setTitle("Input");
			setSize(400,100);
			setLocation(550,350);
			Font f = new Font("Helvetica", Font.PLAIN, 15);
			JLabel messaggio = new JLabel("Inserisci un valore di x>> "); messaggio.setFont(f); messaggio.setForeground(Color.black); messaggio.setHorizontalAlignment(SwingConstants.CENTER);
			add(messaggio, BorderLayout.CENTER); add(input, BorderLayout.EAST); add(calcola, BorderLayout.SOUTH);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { setVisible(false);} } );
		}
	}//FinestraInput1
	
	private class FinestraInput2 extends JFrame {
		public FinestraInput2() {
			setTitle("File");
			setSize(400,100);
			setLocation(550,350);
			Font f = new Font("Helvetica", Font.PLAIN, 20);
			JLabel messaggio = new JLabel("Inserisci il percorso del file>> "); messaggio.setFont(f); messaggio.setForeground(Color.black); messaggio.setHorizontalAlignment(SwingConstants.CENTER);
			add(messaggio, BorderLayout.CENTER); add(input, BorderLayout.EAST); add(calcolaDue, BorderLayout.SOUTH);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { setVisible(false);} } );
		}
	}//FinestraInput2
	
}//FinestraGUI

public class PolinomioGUI {
	public static void main(String[] args) {
		JFrame finestra = new FinestraGUI();
		finestra.setVisible(true);
	}//main
	
}//PolinomioGUI