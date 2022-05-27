package util.ir;

public interface Inode<V, P> {
    Inode<V,P> getNext();
    Inode<V,P> getPrev();
    V getVal();
    P getParent();
}
