package poo.progetto;

import java.util.Iterator;
import java.util.ListIterator;

public abstract class AbstractList<T> implements List<T> {
	
	public String toString() {
		StringBuilder sb=new StringBuilder(500);
		sb.append('[');
		Iterator<T> it=iterator();
		while( it.hasNext() ){
			sb.append( it.next() );
			if( it.hasNext() ) sb.append(',');
		}
		sb.append(']');
		return sb.toString();		
	}
	
	@SuppressWarnings({ "unchecked" })
	public boolean equals(Object x) {
		if( !(x instanceof List) ) return false;
		if( x==this ) return true;
		List<T> s=(List<T>)x;
		if( s.size()!=this.size() ) return false;
		Iterator<T> it1=this.iterator();
		Iterator<T> it2=s.iterator();
		while( it1.hasNext() ){
			T x1=it1.next();
			T x2=it2.next();
			if( !x1.equals(x2) ) return false;//confronto elemento per elemento(devono essere uguali e nelle stesse posizioni)
		}
		return true;
	}
	public int hashCode() {
		int h=0;
		final int MP=73;
		ListIterator<T> it= listIterator();
		while(it.hasNext()) {
			h=h*MP+it.next().hashCode();
		}
		return h;
	}
}//AbstractList
