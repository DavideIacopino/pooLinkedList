package poo.progetto;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedList<T> extends AbstractList<T>{

	private enum Move{ UNKNOWN, FORWARD, BACKWARD }
	private static class Nodo<E>{
		
		E info;
		Nodo<E> next, prior;
	}//Nodo
	private Nodo<T> first=null, last=null;
	private int size=0;
	private int cm=0;//contatore modifiche
	
	@Override
	public int size(){
		return size;
	}//size
	@Override
	public boolean contains(T x) {
		Nodo<T> cor=first;
		while( cor!=null && !cor.info.equals(x) ) cor=cor.next;
		if( cor!=null ) return true;
		return false;
	}
	@Override
	public void clear(){
		first=null;
		last=null; 
		size=0;
		cm++;
	}//clear
	@Override
	public void addFirst( T elem ){
		//creazione nuovo nodo
		Nodo<T> n=new Nodo<>();
		n.info=elem;
		n.next=first;
		n.prior=null;
		if(first!=null) //c'è almeno un elemento
			first.prior=n;
		first=n;//modifico il first
		if(last==null)//caso particolare:lista vuota
			last=n;
		size++;
		cm++;
	}
	@Override
	public void addLast( T elem ){
		Nodo<T> n=new Nodo<>();
		n.info=elem;
		n.next=null;
		n.prior=last;
		if( last!=null ) last.next=n;
		last=n;
		if( first==null ) first=n;
		size++;
		cm++;
	}//addLast
	@Override
	public T getFirst(){
		if(isEmpty()) throw new NoSuchElementException();
		return first.info;
	}
	@Override
	public T getLast(){
		if(isEmpty()) throw new NoSuchElementException();
		return last.info;
	}
	@Override
	public T removeFirst(){
		if(first==null)//lista vuota
			throw new NoSuchElementException();
		T ris=first.info;
		if(first==last) {//lista formata da 1 solo elemento
			last=null;
			first=null;
		} else {
			first=first.next;
			first.prior=null;
		}
		size--;
		cm++;
		return ris;
	}
	@Override
	public T removeLast(){
		if(isEmpty()) throw new NoSuchElementException();
		T ris=last.info;
		if(first==last) {//lista formata da 1 solo elemento
			first=null;
			last=null;
		} else {
			last.prior.next=null;
			last=last.prior;
		}
		size--;
		cm++;
		return ris;
	}
	@Override
	public void remove( T x ) {
		Nodo<T> cor=first;
		while( cor!=null && !cor.info.equals(x) ) cor=cor.next;
		if( cor!=null ) {//trovato elemento da rimuovere
			if( cor==first ) {//è il primo elemento
				first=first.next;
				if( first==null ) last=null;
				else first.prior=null;
			}
			else if( cor==last ) {//è l'ultimo e certamente esistono almeno 2 nodi
				last=last.prior;
				last.next=null;
			}
			else {//intermedio
				cor.prior.next=cor.next;
				cor.next.prior=cor.prior;
			}
			size--;
			cm++;
		}
	}//remove
	@Override
	public boolean isEmpty(){
		return first==null;
	}
	public void sort( Comparator<T> c ){
		if( first==null || first.next==null ) return;
		boolean scambi=true;
		Nodo<T> limite=null, pus=null;
		while( scambi ){
			Nodo<T> cor=first.next;
			scambi=false;
			while( cor!=limite ){
				if( c.compare(cor.info,cor.prior.info)<0 ){
					T park=cor.info;
					cor.info=cor.prior.info;
					cor.prior.info=park;
					pus=cor;
					scambi=true;
				}
				cor=cor.next;
			}//while
			limite=pus;
		}//while
	}//sort
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder(500);
		sb.append('[');	
		Nodo<T> cor=first;
		while( cor!=null ) {
			sb.append(cor.info);
			cor=cor.next;
			if( cor!=null ) sb.append(", ");
		}
		sb.append(']');
		return sb.toString();
	}//toString
	public Iterator<T> iterator(){
		return new ListIteratorImpl();
	}//iterator
	public ListIterator<T> listIterator(){
		return new ListIteratorImpl();
	}//listIterator
	public ListIterator<T> listIterator( int from ){
		return new ListIteratorImpl( from );
	}//listIterator
	private class ListIteratorImpl implements ListIterator<T>{
		private Nodo<T> previous, next;
//l'iteratore è tra i nodi puntati da previous e next;
//l'elemento corrente, nel movimento FORWARD, è previous;
//nel movimento BACKWARD è next.
		private Move lastMove=Move.UNKNOWN;
		private int cmi;//contatore modifiche iteratore
		public ListIteratorImpl(){
			previous=null; next=first; cmi=cm;
		}//ListIterator
		public ListIteratorImpl(int pos) {
			if(pos<0 || pos>size) throw new IllegalArgumentException();
			int c=0;
			previous=null;
			next=first;
			while(c<pos) {
				previous=next;
				next=next.next;
				c++;
			}
			cmi=cm;
		}
		public boolean hasNext(){
			return next!=null;
		}//hasNext
		public T next(){
			if(cmi!=cm) throw new ConcurrentModificationException();
			if( !hasNext() ) throw new NoSuchElementException();
			lastMove=Move.FORWARD;
			previous=next;
			next=next.next;
			return previous.info;
		}//next
		public boolean hasPrevious() {
			return previous!=null;
		}
		public T previous(){
			if(cmi!=cm) throw new ConcurrentModificationException();
			if(!hasPrevious()) throw new NoSuchElementException();
			lastMove=Move.BACKWARD;
			next=previous;
			previous=previous.prior;
			return next.info;
		}
		public void remove(){
			if(cmi!=cm) throw new ConcurrentModificationException();
			if( lastMove==Move.UNKNOWN )
				throw new IllegalStateException();
			Nodo<T> r=null; //r e’ il nodo da rimuovere
			if( lastMove==Move.FORWARD )
				r=previous;
			else
				r=next;
			//rimozione del nodo r
			if( r==first ){
				first=first.next;
				if( first==null ) last=null;
				else first.prior=null;
			}
			else if( r==last ){
				last=last.prior;
				last.next=null;
			}
			else{//intermediate
				r.prior.next=r.next;
				r.next.prior=r.prior;
			}
			//update iterator
			if( lastMove==Move.FORWARD )
				previous=r.prior;
			else
				next=r.next;
			size--;
			cm++;
			cmi++;
			lastMove=Move.UNKNOWN;
		}//remove
		public void add( T elem ){
			if(cmi!=cm) throw new ConcurrentModificationException();
			//creazione nuovo nodo
			Nodo<T> n=new Nodo<>();
			n.info=elem;
			n.next=next;
			n.prior=previous;
			//aggiunta
			if(next==null) {//non ci sono elementi oppure è dopo l'ultimo elemento
				if(previous==null) first=n;//0 elementi
				else previous.next=n; // dopo l'ultimo elemento
				last=n;
			}
			else if (next==first && first!=null) {//add prima del 1° elemento
				next.prior=n;
				first=n;
			}
			else {//intermedio
				previous.next=n;
				next.prior=n;
			}
			previous=n;//update iterator
			lastMove=Move.UNKNOWN;
			size++;
			cm++;
			cmi++;
		}
		public void set( T elem ){
			if(cmi!=cm) throw new ConcurrentModificationException();
			if(lastMove==Move.UNKNOWN) throw new IllegalStateException();
			Nodo<T> cor=null;//cor è l'elemento corrente quindi da modificare
			if(lastMove==Move.FORWARD) cor=previous;
			else cor=next;
			cor.info=elem;	
			cm++;
			cmi++;
		}
		public int nextIndex(){
			throw new UnsupportedOperationException();
		}//nextIndex
		public int previousIndex(){
			throw new UnsupportedOperationException();
		}//previousIndex
	}//ListIteratore
}//LinkedList