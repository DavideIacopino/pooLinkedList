package poo.progetto;
import java.io.*;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public interface List<T> extends Iterable<T>{
	@SuppressWarnings("unused")
	default int size() {
		int c=0;
		for(T x:this) c++;
		return c;
	}
	default boolean contains(T x) {
		for(T i:this) {
			if(i.equals(x)) return true;
		}
			return false;
	}
	default void clear() {
		ListIterator<T> it= listIterator();
		while(it.hasNext()) {
			it.next();
			it.remove();
		}
	}
	default void add(T x) {
		addLast(x);
	}
	default void addFirst( T x ) {
		ListIterator<T> it= listIterator();//di default si mette prima del primo elemento
		it.add(x);
	}
	default void addLast( T x ) {
		ListIterator<T> it= listIterator();
		while(it.hasNext()) {
			it.next();
		}
		it.add(x);
	}
	default T getFirst() {
		ListIterator<T> it= listIterator();//di default si mette prima del primo elemento
		if(!it.hasNext()) throw new NoSuchElementException();
		return it.next();
	}
	default T getLast() {
		if(isEmpty()) throw new NoSuchElementException();
		ListIterator<T> it= listIterator();
		T x=null;
		while(it.hasNext()) {
			x=it.next();
		}
		return x;
	}
	default T removeFirst() {
		if(isEmpty()) throw new NoSuchElementException();//lista vuota
		ListIterator<T> it= listIterator();
		T r=it.next();
		it.remove();
		return r;
	}
	default T removeLast() {
		if(isEmpty()) throw new NoSuchElementException();//lista vuota
		ListIterator<T> it= listIterator();
		while(it.hasNext()) {
			it.next();
		}
		T r=it.previous();
		it.remove();
		return r;
	}
	default void remove( T x ) {//non possiamo usare il for-each perchè abbiamo bisogno della remove
		ListIterator<T> it= listIterator();
		while (it.hasNext()) {
			T r=it.next();
			if(r.equals(x)) {
				it.remove();
				break;
			}
		}
	}
	default boolean isEmpty() {
		ListIterator<T> it= listIterator();
		return !it.hasNext();
	}
	default boolean isFull() {
		return false;
	}

	static <T> void sort(List<T> l, Comparator<T> c) {
		if(l.isEmpty() || l.size()==1) return;
		T x=null, y=null;
		boolean scambi=true;
		while(scambi) {
			ListIterator<T> it= l.listIterator();
			x=it.next();
			scambi=false;
			while(it.hasNext()) {
				y=it.next();
				if(c.compare(x,y)>0) {
					scambi=true;
					it.remove();//si può fare anche con 1 set, 2 previous e 1 set
					it.previous();
					it.add(y);
					x=it.next();
				} else x=y;
			}
		}
	}
	static <T extends Serializable> void salva(String nomeFile, List<T> l) throws IOException{
		ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(nomeFile));
		for( T i:l) oos.writeObject(i);
		oos.close();
	}
	@SuppressWarnings("unchecked")
	static <T extends Serializable> void ripristina(String nomeFile, List<T> l) throws IOException{
		ObjectInputStream ois=new ObjectInputStream(new FileInputStream( nomeFile ) );
		LinkedList<T> park=new LinkedList<>();//scriviamo prima su park e poi copiamo su l solo se tutto è andato a buon fine
		boolean letturaCompletata=false;
		T n=null;
		try {
			for(;;){
				n=(T)ois.readObject();
				park.add(n);
			}
		}
		 catch( EOFException e1 ){
			letturaCompletata=true;
		}
		catch(Exception ex){
			throw new IOException();
		}
		finally{
			ois.close();
		}
		if(letturaCompletata) {
			l.clear();//svuoto la lista
			for(T i:park) l.add(i);
		}
	}
	ListIterator<T> listIterator();
	ListIterator<T> listIterator( int from );
}//List
