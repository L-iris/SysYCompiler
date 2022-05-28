package util.ir;

import java.util.Iterator;

public class IList<V,P> implements Iterable<IListNode<V,P>> {

    class IlistIterator implements Iterator<IListNode<V, P>> {
        private int cursor = 0;
        @Override
        public boolean hasNext() {
            return cursor < IList.this.getNumNode();
        }

        @Override
        public IListNode<V, P> next() {
            return IList.this.get(cursor++);
        }
    }
    private int numNode;
    private IListNode<V,P> first;
    private IListNode<V,P> last;

    public IList() {
        numNode = 0;
    }

    public IListNode<V,P> get(int i) {
        int index = 0;
        var node = first;
        while(index < i) {
            node = node.getNext();
            index ++;
        }
        return node;
    }

    public boolean insertBefore(IListNode<V,P> node) {
        var i = this.first;
        while(i != null && !i.equals(node)){
            i = i.getNext();
        }
        if(i == null)
            return false;
        boolean b = i.getPrev().setNext(node) || i.setPrev(node);
        if(b)
            numNode++;
        return b;
    }

    public boolean insertAtEnd(IListNode<V,P> node) {
        if(numNode == 0) {
            this.first = node;
            this.last = node;
            numNode++;
            return true;
        }
        boolean b = node.setPrev(this.last) || this.last.setNext(node);
        if(b)
            numNode++;
        return b;
    }

    @Override
    public Iterator<IListNode<V, P>> iterator() {
        return new IlistIterator();
    }

    public int getNumNode() {
        return numNode;
    }

    public IListNode<V, P> getFirst() {
        return first;
    }

    public IListNode<V, P> getLast() {
        return last;
    }
}
