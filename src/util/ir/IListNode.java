package util.ir;

public interface IListNode<V, P> {
    IListNode<V,P> getNext();
    IListNode<V,P> getPrev();
    V getVal();
    P getParent();
    boolean setNext(IListNode<V,P> node);
    boolean setPrev(IListNode<V,P> node);
}
