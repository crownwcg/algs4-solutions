import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        private Node preNode;
        private Item item;
        private Node nextNode;
    }

    private class ListIterator implements Iterator<Item> {
        private Node curr = first;

        public boolean hasNext() {
            return curr != null;
        }

        public Item next() {
            if (curr == null)
                throw new NoSuchElementException("there are no more items");

            Item item = curr.item;
            curr = curr.nextNode;
            return item;
        }
    }

    public Deque() {
        size = 0; first = null; last = null;
    }
    // construct an empty deque

    public boolean isEmpty()
    {
        return size == 0;
    }                 
    // is the deque empty?

    public int size() {
        return size;
    }                       
    // return the number of items on the deque

    public void addFirst(Item item){
        valivate(item);
        Node newNode = new Node();
        newNode.item = item;
        if (size == 0) {
            newNode.preNode = null;
            newNode.nextNode = null;
            first = newNode;
            last = newNode;
        } else {
            newNode.preNode = null;
            newNode.nextNode = first;
            first.preNode = newNode;
            first = newNode;
        }
        size++;
    }          
    // add the item to the front

    public void addLast(Item item) {
        valivate(item);
        Node newNode = new Node();
        newNode.item = item;
        if (size == 0) {
            newNode.preNode = null;
            newNode.nextNode = null;
            first = newNode;
            last = newNode;
        } else {
            newNode.preNode = last;
            newNode.nextNode = null;
            last.nextNode = newNode;
            last = newNode;
        }
        size++;
    }          
    // add the item to the end

    public Item removeFirst() {
        if (size == 0) 
            throw new NoSuchElementException("the deque is empty");
        Item returnItem = null;
        if (size == 1) {
            returnItem = first.item;
            first = null;
            last = null;
        } else {
            Node oldfirst = first;
            returnItem = oldfirst.item;
            first = oldfirst.nextNode;
            first.preNode = null;
            oldfirst.nextNode = null;
            oldfirst.item = null;
        }
        size--;
        return returnItem;
    }                
    // remove and return the item from the front

    public Item removeLast() {
        if (size == 0)
            throw new NoSuchElementException("the deque is empty");
        Item returnItem = null;
        if (size == 1) {
            returnItem = first.item;
            first = null;
            last = null;
        } else {
            Node oldlast = last;
            returnItem = oldlast.item;
            last = oldlast.preNode;
            last.nextNode = null;
            oldlast.preNode = null;
            oldlast.item = null;
        }
        size--;
        return returnItem;
    }                 
    // remove and return the item from the end

    public Iterator<Item> iterator() {
        return new ListIterator();
    }         
    // return an iterator over items in order from front to end

    private void valivate(Item item) {
        if (item == null) 
            throw new IllegalArgumentException("the item is null");
    }

    public static void main(String[] args) {
        Deque<String> dq = new Deque<String>();
        System.out.println(dq.size);
        dq.addFirst("b");
        dq.addFirst("a");
        dq.addLast("c");
        dq.addLast("d");
        System.out.println(dq.size);
        Iterator<String> i = dq.iterator();
        while(i.hasNext()) {
            System.out.print(i.next());
        }
        System.out.println();

    }   
    // unit testing (optional)
}































