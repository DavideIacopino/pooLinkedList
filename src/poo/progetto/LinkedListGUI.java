package poo.progetto;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

class FrameGUI extends JFrame{
	private String title="LinkedList di interi";
	private LinkedList<Integer> l=null;
	private ListIterator<Integer> lit=null;
	private File fileDiSalvataggio=null;
	private JMenuItem nuovo, apri, salva, ScN, esci;//menu file
	private JMenuItem add, addFirst, addLast, remove, removeFirst, removeLast, size, hashCode,
					  contains, getFirst, getLast, clear, sort, isEmpty, isFull;//menu Comandi
	private JMenuItem about;//menu ?
	private JMenuItem iteratorDef, iteratorFrom, hasNext, next, rimuovi, hasPrevious, previous,
					  aggiungi, set;//menu iterator
	private int from, x;
	private int indexIteratore;
	private enum Move{ UNKNOWN, FORWARD, BACKWARD }
	private Move lastMove=Move.UNKNOWN;
	private JPanel p1;
	private JTextArea ta;
	private boolean flag=false;
	AscoltatoreEventi listener=new AscoltatoreEventi();
	
	public FrameGUI() {
		setTitle(title);
		setSize(1000,500);
		setLocation(100,100);
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		 
        addWindowListener( new WindowAdapter() {
	        public void windowClosing(WindowEvent e){
	       	 if(consensoUscita()) System.exit(0);
	        }
	     } );
		
		//creazione barra dei menu
		JMenuBar menuBar=new JMenuBar();
        this.setJMenuBar( menuBar );
        
        //creazione menu file
        JMenu menuFile=new JMenu("File");
        menuBar.add(menuFile);
        nuovo=new JMenuItem("Nuovo");
        menuFile.add(nuovo);
        nuovo.addActionListener(listener);
        apri=new JMenuItem("Apri");
        menuFile.add(apri);
        apri.addActionListener(listener);
        salva=new JMenuItem("Salva");
        menuFile.add(salva);
        salva.addActionListener(listener);
        ScN=new JMenuItem("Salva con nome");
        menuFile.add(ScN);
        ScN.addActionListener(listener);
        esci=new JMenuItem("Esci");
        menuFile.add(esci);
        esci.addActionListener(listener);
        //creazione menu comandi
        JMenu menuCMD=new JMenu("Comandi");
        menuBar.add(menuCMD);
        add=new JMenuItem("Add");
        menuCMD.add(add);
        add.addActionListener(listener);
        addFirst=new JMenuItem("Add First");
        menuCMD.add(addFirst);
        addFirst.addActionListener(listener);
        addLast=new JMenuItem("Add Last");
        menuCMD.add(addLast);
        addLast.addActionListener(listener);
        size=new JMenuItem("Size");
        menuCMD.add(size);
        size.addActionListener(listener);
        hashCode=new JMenuItem("HashCode");
        menuCMD.add(hashCode);
        hashCode.addActionListener(listener);
        contains=new JMenuItem("Contains");
        menuCMD.add(contains);
        contains.addActionListener(listener);
        clear=new JMenuItem("Clear");
        menuCMD.add(clear);
        clear.addActionListener(listener);
        remove=new JMenuItem("Remove");
        menuCMD.add(remove);
        remove.addActionListener(listener);
        removeFirst=new JMenuItem("Remove First");
        menuCMD.add(removeFirst);
        removeFirst.addActionListener(listener);
        removeLast=new JMenuItem("Remove Last");
        menuCMD.add(removeLast);
        removeLast.addActionListener(listener);
        getFirst=new JMenuItem("Get First");
        menuCMD.add(getFirst);
        getFirst.addActionListener(listener);
        getLast=new JMenuItem("Get Last");
        menuCMD.add(getLast);
        getLast.addActionListener(listener);
        sort=new JMenuItem("Sort");
        menuCMD.add(sort);
        sort.addActionListener(listener);
        isFull=new JMenuItem("Is Full");
        menuCMD.add(isFull);
        isFull.addActionListener(listener);
        isEmpty=new JMenuItem("Is Empty");
        menuCMD.add(isEmpty);  
        isEmpty.addActionListener(listener);
        //creazione menu a tendina iterator
        JMenu iterator=new JMenu("Iterator");
        menuCMD.add(iterator);
        
        iteratorDef=new JMenuItem("Nuovo iteratore di default");
        iterator.add(iteratorDef);
        iteratorDef.addActionListener(listener);
        iteratorFrom=new JMenuItem("Nuovo iteratore a partire dalla posizione indicata");
        iterator.add(iteratorFrom);
        iteratorFrom.addActionListener(listener);
        iterator.addSeparator();
        hasNext= new JMenuItem("Has Next");
        iterator.add(hasNext);
        hasNext.addActionListener(listener);
        next= new JMenuItem("Next");
        iterator.add(next);
        next.addActionListener(listener);
        rimuovi= new JMenuItem("Remove");
        iterator.add(rimuovi);
        rimuovi.addActionListener(listener);
        iterator.addSeparator();
        hasPrevious= new JMenuItem("Has Previous");
        iterator.add(hasPrevious);
        hasPrevious.addActionListener(listener);
        previous= new JMenuItem("Previous");
        iterator.add(previous);
        previous.addActionListener(listener);
        set=new JMenuItem("Set");
        iterator.add(set);
        set.addActionListener(listener);
        aggiungi=new JMenuItem("Add");
        iterator.add(aggiungi);
        aggiungi.addActionListener(listener);
        
        //creazione menu help
        JMenu menuHelp=new JMenu("?");
        menuBar.add(menuHelp);
        about=new JMenuItem("About");
        menuHelp.add(about);
        about.addActionListener(listener);
        
        MenuIniziale();
        p1=new JPanel();
        ta=new JTextArea();
        JScrollPane textAreaScrollable= new JScrollPane(ta);
        ta.setFont(new Font("Times New Roman", Font.BOLD,20));
        ta.setEditable(false);
        p1.add(ta);
    }//costruttore
	
    private boolean consensoUscita(){
	   int uscita=JOptionPane.showConfirmDialog(
			   null, "Sei sicuro di voler uscire? Se non salvi, la lista verrà persa.","Uscita",
			   JOptionPane.YES_NO_OPTION);
	   return uscita==JOptionPane.YES_OPTION;
	 }//consensoUscita
	
	private void MenuIniziale(){
		salva.setEnabled(false);
		ScN.setEnabled(false);
		add.setEnabled(false);
		addFirst.setEnabled(false);
		addLast.setEnabled(false);
		aggiungi.setEnabled(false);
		remove.setEnabled(false);
		removeFirst.setEnabled(false);
		rimuovi.setEnabled(false);
		removeLast.setEnabled(false);
		next.setEnabled(false);
		hasNext.setEnabled(false);
		hasPrevious.setEnabled(false);
		previous.setEnabled(false);
		size.setEnabled(false);
		hashCode.setEnabled(false);
		clear.setEnabled(false);
		contains.setEnabled(false);
		sort.setEnabled(false);
		set.setEnabled(false);
		iteratorDef.setEnabled(false);
		iteratorFrom.setEnabled(false);
		isFull.setEnabled(false);
		isEmpty.setEnabled(false);
		getFirst.setEnabled(false);
		getLast.setEnabled(false);
	}
	private void MenuAvviato() {
		salva.setEnabled(true);
		ScN.setEnabled(true);
		add.setEnabled(true);
		addFirst.setEnabled(true);
		addLast.setEnabled(true);
		remove.setEnabled(true);
		removeFirst.setEnabled(true);
		removeLast.setEnabled(true);
		size.setEnabled(true);
		hashCode.setEnabled(true);
		clear.setEnabled(true);
		contains.setEnabled(true);
		sort.setEnabled(true);
		iteratorDef.setEnabled(true);
		iteratorFrom.setEnabled(true);
		isFull.setEnabled(true);
		isEmpty.setEnabled(true);
		getFirst.setEnabled(true);
		getLast.setEnabled(true);
	}
	private void MenuIteratore() {
		//Per evitare ConcurrentModificationException
		salva.setEnabled(true);
		ScN.setEnabled(true);
		add.setEnabled(false);
		addFirst.setEnabled(false);
		addLast.setEnabled(false);
		aggiungi.setEnabled(true);
		remove.setEnabled(false);
		removeFirst.setEnabled(false);
		rimuovi.setEnabled(true);
		removeLast.setEnabled(false);
		next.setEnabled(true);
		hasNext.setEnabled(true);
		hasPrevious.setEnabled(true);
		previous.setEnabled(true);
		size.setEnabled(true);
		hashCode.setEnabled(true);
		clear.setEnabled(true);
		contains.setEnabled(true);
		sort.setEnabled(true);
		set.setEnabled(true);
		iteratorDef.setEnabled(true);
		iteratorFrom.setEnabled(true);
		isFull.setEnabled(true);
		isEmpty.setEnabled(true);
		getFirst.setEnabled(true);
		getLast.setEnabled(true);
	}
	public String stampaIteratore(LinkedList<Integer> li) {
		String s=li.toString();
		int c=0, finoA=1;//finoA parte da 1 perchè va considerata la [ iniziale
		 StringBuilder sb=new StringBuilder(300);
		 StringTokenizer st=new StringTokenizer(s,", []");
		 if(li.isEmpty()) {
			sb.append('[');
			sb.append('^');
			sb.append(']');
			return sb.toString();
		 }
		 if(!lit.hasNext()) {
				sb.append(s.substring(0, s.length()-1));
				sb.append('^');
				sb.append(']');
				return sb.toString();
		 }
		 while(c!=indexIteratore) {
			 finoA+=st.nextToken().length()+2;//+2 per virgola e spazio
			 c++;
		 }
		 sb.append(s.substring(0, finoA));
		 sb.append('^');
		 sb.append(s.substring(finoA));
		 return sb.toString();
	 }

	private class AscoltatoreEventi implements ActionListener{
	  	   public void actionPerformed(ActionEvent e){
	  		   if(e.getSource()==nuovo) {
	  			   l=new LinkedList<Integer>();
	  			   add(p1);
	  			   flag=false;
	  			   ta.setText(l.toString());
	  			   MenuAvviato();
	  		   }
	  		 
	  		 else if(e.getSource()==esci) {
	  			 if(consensoUscita()) System.exit(0);
	  		 }
	  		 else if (e.getSource()==about) {
	  			JOptionPane.showMessageDialog( null, "Programma di Creazione e Gestione di una LinkedList\n",
	  						"About", JOptionPane.PLAIN_MESSAGE );
	  		 }
	  		 else if(e.getSource()==add || e.getSource()==addLast) {
	  			for(;;) {
					String txt=JOptionPane.showInputDialog("Immetti l'elemento da inserire nella lista");
						try {
							x=Integer.parseInt(txt); break;
						}catch(RuntimeException ex) {
							JOptionPane.showMessageDialog(null, "Immettere un intero");
						}
					}
	  			 l.addLast(x);
	  			 ta.setText(l.toString());
	  		 }
	  		 else if(e.getSource()==addFirst) {
	  			for(;;) {
					String txt=JOptionPane.showInputDialog("Immetti l'elemento da inserire nella lista");
						try {
							x=Integer.parseInt(txt); break;
						}catch(RuntimeException ex) {
							JOptionPane.showMessageDialog(null, "Immettere un intero");
						}
					}
	  			l.addFirst(x);
	  			ta.setText(l.toString());
	  		 }
	  		 else if(e.getSource()==aggiungi) {
	  			for(;;) {
					String txt=JOptionPane.showInputDialog("Immetti l'elemento da inserire nella lista");
						try {
							x=Integer.parseInt(txt); break;
						}catch(RuntimeException ex) {
							JOptionPane.showMessageDialog(null, "Immettere un intero");
						}
	  			}
	    	 	lit.add(x);	
				indexIteratore++;
				ta.setText(stampaIteratore(l));
				lastMove=Move.UNKNOWN;
	  		 }
	  		 else if(e.getSource()==removeFirst) {
	  			 try {
		  			 l.removeFirst();
		  			 }
		  			 catch(NoSuchElementException ex){
		  				JOptionPane.showMessageDialog(null, "La lista è vuota!");
		  			 }
	  			ta.setText(l.toString());
	  		 }
	  		 else if(e.getSource()==removeLast) {
	  			 try {
	  				 l.removeLast();
	  			 }
	  			 catch(RuntimeException ex){
	  				JOptionPane.showMessageDialog(null, "La lista è vuota!");
	  			 }
	  			 ta.setText(l.toString());
	  		 }
	  		 else if(e.getSource()==rimuovi) {
	  			try{
	  				if(lastMove==Move.FORWARD) indexIteratore--;
			 		lit.remove();
			 	}
	  			catch(IllegalStateException ex) {
	  				JOptionPane.showMessageDialog(null, "Non puoi fare una remove senza aver fatto next o previous prima");
	  			}
	  			ta.setText(stampaIteratore(l));
	  			lastMove=Move.UNKNOWN;
	  		 }
	  		 else if(e.getSource()==remove) {
	  			for(;;) {
					String txt=JOptionPane.showInputDialog("Immetti l'elemento da rimuovere dalla lista");
						try {
							x=Integer.parseInt(txt); break;
						}catch(RuntimeException ex) {
							JOptionPane.showMessageDialog(null, "Immettere un intero");
						}
	  			}
	  			l.remove(x);
	  			ta.setText(l.toString());
	  		 }
	  		 else if(e.getSource()==clear) {
	  			l.clear();
	  			MenuAvviato();
	  			lit=null;
	  			flag=false;
	  			lastMove=Move.UNKNOWN;
	  			indexIteratore=0;
	  			ta.setText(l.toString());
	  		 }
	  		 else if(e.getSource()==sort) {
	  			 l.sort(new Comparator<Integer>(){
	  				public int compare(Integer x1, Integer x2) {
	  					if(x1<x2) return -1;
	  					if (x1==x2) return 0;
	  					return 1;
	  				}
	  			});
	  			if(flag)ta.setText(stampaIteratore(l));
	  			 else ta.setText(l.toString());
	  		 }
	  		 else if(e.getSource()==getFirst) {
	  			try{
	  				JOptionPane.showMessageDialog(null,"L'intero in testa alla lista è: " + l.getFirst());
	  			}catch(NoSuchElementException ex) {
	  				JOptionPane.showMessageDialog(null,"la lista è vuota!");
	  			}
	  		 }
	  		else if(e.getSource()==getLast) {
	  			try {
	  			JOptionPane.showMessageDialog(null,"L'intero in coda alla lista è: " + l.getLast());
	  			}catch(NoSuchElementException ex) {
	  				JOptionPane.showMessageDialog(null,"la lista è vuota!");
	  			}
	  		 }
	  		else if(e.getSource()==size) {
	  			JOptionPane.showMessageDialog(null,"La dimensione è: "+l.size());
	  		}
	  		else if(e.getSource()==hashCode) {
	  			JOptionPane.showMessageDialog(null,"L'hashCode è: "+l.hashCode());
	  		}
	  		else if(e.getSource()==contains) {
	  			String input=JOptionPane.showInputDialog("Fornire l'intero di cui verificare l'appartenenza alla lista");
	  			x=Integer.parseInt(input);
	  			if(l.contains(x)) JOptionPane.showMessageDialog(null,"L'intero "+x+ " è contenuto nella lista");
	  			else JOptionPane.showMessageDialog(null, "L'intero "+x+ " non è contenuto nella lista");
	  		}
	  	   else if(e.getSource()==isFull) {
	  		 JOptionPane.showMessageDialog(null,"La lista non è piena");
	  	   }
	  	   else if(e.getSource()==isEmpty) {
	  		   if(l.isEmpty()) JOptionPane.showMessageDialog(null,"La lista è vuota");
	  		   else JOptionPane.showMessageDialog(null,"La lista non è vuota");
	  	   }
	  	 else if(e.getSource()==apri){
			   JFileChooser chooser=new JFileChooser();
			   try{
				   if( chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION ){
					   if( !chooser.getSelectedFile().exists() ){
						   JOptionPane.showMessageDialog(null,"File inesistente!"); 
					   }
					   else{	
						   fileDiSalvataggio=chooser.getSelectedFile();
						   FrameGUI.this.setTitle(title+" - "+fileDiSalvataggio.getName());
						   try{
							   if(l==null) l=new LinkedList<>();//evito NullPointerException()
							   List.ripristina( fileDiSalvataggio.getAbsolutePath(), l);
						   }catch(IOException ioe){
							   JOptionPane.showMessageDialog(null,"Fallimento apertura. File malformato!");
						   }
					   }
				   }
				   else
					   JOptionPane.showMessageDialog(null,"Nessuna apertura!");
			   }catch( Exception exc ){
				   exc.printStackTrace();
			   }
			   add(p1);
			   ta.setText(l.toString());
			   MenuAvviato();
		   } 
	  	 else if (e.getSource()==salva){
			   JFileChooser chooser=new JFileChooser();
			   try{
				   if( fileDiSalvataggio!=null ){
					   int ans=JOptionPane.showConfirmDialog(null,"Sovrascrivere "+fileDiSalvataggio.getAbsolutePath()+" ?");
					   if(ans==0)
						   List.salva(fileDiSalvataggio.getAbsolutePath(), l);
					   else
						   JOptionPane.showMessageDialog(null,"Nessun salvataggio!");
					   return;
				   }
				   if( chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION ){
					   fileDiSalvataggio=chooser.getSelectedFile();
					   FrameGUI.this.setTitle(title+" - "+fileDiSalvataggio.getName());
				   }
				   if( fileDiSalvataggio!=null ){
					   List.salva(fileDiSalvataggio.getAbsolutePath(), l);
				   }
				   else
					   JOptionPane.showMessageDialog(null,"Nessun Salvataggio!");
			   }catch( Exception exc ){
				   exc.printStackTrace();
			   }
		   }
		   else if(e.getSource()==ScN){
			   JFileChooser chooser=new JFileChooser();
			   try{
				   if( chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION ){
						   fileDiSalvataggio=chooser.getSelectedFile();
						   FrameGUI.this.setTitle(title+" - "+fileDiSalvataggio.getName());
					   }
				   if(fileDiSalvataggio!=null){
					   List.salva(fileDiSalvataggio.getAbsolutePath(), l);
				   }
				   else
					   JOptionPane.showMessageDialog(null,"Nessun Salvataggio!");
			   }catch( Exception exc ){
				   exc.printStackTrace();
			   }  			   
		   }
	  	   else if(e.getSource()==iteratorDef) {
	  		   lit=l.listIterator();
	  		   flag=true;
	  		   MenuIteratore();
	  		   indexIteratore=0;
	  		   ta.setText(stampaIteratore(l));
	  	   }
	  	   else if(e.getSource()==iteratorFrom) {
	  		 for(;;) {
					String msg= JOptionPane.showInputDialog("Fornire la posizione da cui far partire il listIterator");
					try {
						from=Integer.parseInt(msg);
						lit=l.listIterator(from);
						break;
					}catch(IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(null, "Immettere una posizione valida");
					}
				}
				flag=true;
				indexIteratore=from;
				MenuIteratore();
				ta.setText(stampaIteratore(l));
	  	   }
	  	   else if(e.getSource()==hasNext) {
	  		   if(lit.hasNext()) JOptionPane.showMessageDialog(null,"Sì,c'è un prossimo elemento");
	  		   else JOptionPane.showMessageDialog(null,"No, non c'è un prossimo elemento");
	  	   }
	  	   else if(e.getSource()==hasPrevious) {
	  		   if(lit.hasPrevious()) JOptionPane.showMessageDialog(null,"Sì, c'è un elemento precedente");
	  		   else JOptionPane.showMessageDialog(null,"No, non c'è un elemento precedente");
	  	   }
	  	   else if(e.getSource()==set) {
	  		 for(;;) {
	  			 String txt=JOptionPane.showInputDialog("Fornisci il nuovo intero ");
				try {
					x=Integer.parseInt(txt); break;
				}catch(RuntimeException ex) {
					JOptionPane.showMessageDialog(null, "Immettere un intero");
				}
	  		}
	  		try{
		 		lit.set(x);
		 	}
  			catch(IllegalStateException ex) {
  				JOptionPane.showMessageDialog(null, "Non puoi fare una set senza aver fatto next o previous prima");
  			}
	  		ta.setText(stampaIteratore(l));
	  	   }
	  	   else if(e.getSource()==next) {
	  		   try{
	  			   lit.next();
		  		   lastMove=Move.FORWARD;
		  		   indexIteratore++;
	  		   }catch(RuntimeException ex) {
		  			 JOptionPane.showMessageDialog(null, "Non puoi fare una next in questa posizione");
	  		   } 
	  		   ta.setText(stampaIteratore(l)); 
	  	   }
	  	   else if(e.getSource()==previous) {
	  		   try {
	  			   lit.previous();
	  			  lastMove=Move.BACKWARD;
		  		   indexIteratore--;
	  		   }catch(RuntimeException ex) {
	  			 JOptionPane.showMessageDialog(null, "Non puoi fare una previous in questa posizione");
	  		   }
	  		 ta.setText(stampaIteratore(l));
	  	   }
	     }//ActionPerformed
	}//Ascoltatore
}//FrameGUI
public class LinkedListGUI {
	public static void main(String[] args){
		EventQueue.invokeLater( new Runnable(){
			public void run(){
				FrameGUI f=new FrameGUI();
				f.setVisible(true);
			}
		});
	}//main
}//LinkedListGUI