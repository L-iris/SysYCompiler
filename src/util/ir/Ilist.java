package util.ir;

import java.util.Iterator;

public class Ilist<V,P> implements Iterable<Inode<V,P>>{
    private int numNode;
    private Inode<V,P> first;
    private Inode<V,P> last;

    public Ilist() {

    }

    public Inode<V,P> get(int i) {
        int index = 0;
        var node = first;
        while(index < i) {
            node = node.getNext();
            index ++;
        }
        return node;
    }

    @Override
    public Iterator<Inode<V, P>> iterator() {
        return null;
    }
}
