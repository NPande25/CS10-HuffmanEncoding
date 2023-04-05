import java.util.Comparator;

/**
 * @author nikhilpande
 * @author nathanmcallister
 */
class TreeComparator<E> implements Comparator<BinaryTree<CodeTreeElement>> {
    public int compare(BinaryTree<CodeTreeElement> t1, BinaryTree<CodeTreeElement> t2) {
        if (t1.getData().getFrequency() < t2.getData().getFrequency()) return -1; // if t2 is bigger than t1
        else if (t1.getData().getFrequency() > t2.getData().getFrequency()) return 1; // if t1 is bigger than t2
        return 0;
    }
}