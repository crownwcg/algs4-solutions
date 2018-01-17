import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] rqArrays;
    private int size;

    private class RandomIterator implements Iterator<Item> {
        private int rank;
        private Item[] iterArrays;

        public RandomIterator() {
            rank = size;
            iterArrays = (Item[]) new Object[rank];
            for(int i = 0; i < size; i++)
                iterArrays[i] = rqArrays[i];
        }

        public boolean hasNext() {
            return rank > 0;
        }

        public Item next() {
            if (rank == 0)
                throw new NoSuchElementException("there are no more items");
            int r = StdRandom.uniform(0, rank);
            rank--;
            Item item = iterArrays[r];
            iterArrays[r] = iterArrays[rank];
            iterArrays[rank] = item;
            return item;
        }
    }

    public RandomizedQueue() {
        rqArrays = (Item[]) new Object[1];
        size = 0;
    }                
    // construct an empty randomized queue

    private void valivate(Item item) {
        if(item == null)
            throw new IllegalArgumentException("the item is null");
    }

    public boolean isEmpty() {
        return size == 0;
    }                
    // is the queue empty?

    public int size() {
        return size;
    }                        
    // return the number of items on the queue

    private void resize(int cap) {
        Item[] temp = (Item[]) new Object[cap];
        for(int i = 0; i < size; i++)
            temp[i] = rqArrays[i];
        rqArrays = temp;
    }

    public void enqueue(Item item) {
        valivate(item);
        rqArrays[size++] = item;
        if (size == rqArrays.length)
            resize(2 * rqArrays.length);
    }           
    // add the item

    public Item dequeue() {
        if (size == 0) 
            throw new NoSuchElementException("the RandomizedQueue is empty");
        int r = StdRandom.uniform(0, size);
        size--;
        Item delItem = rqArrays[r];
        rqArrays[r] = rqArrays[size];
        rqArrays[size] = null;
        if (size > 0 && size == rqArrays.length/4)
            resize(rqArrays.length / 2);
        return delItem;
    }                    
    // remove and return a random item

    public Item sample() {
        if (size == 0) 
            throw new NoSuchElementException("the RandomizedQueue is empty");
        return rqArrays[StdRandom.uniform(0, size)];
    }
    // return (but do not remove) a random item
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }         
    // return an independent iterator over items in random order

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("a");
        rq.enqueue("b");
        rq.enqueue("c");
        Iterator<String> i1 = rq.iterator();
        Iterator<String> i2 = rq.iterator();
        while(i1.hasNext()) {
            System.out.print(i1.next() + ",");
        }
        System.out.println();
        rq.dequeue();
        while(i2.hasNext()) {
            System.out.print(i2.next() + ",");
        }
        System.out.println();
    }   
    // unit testing (optional)
}



















