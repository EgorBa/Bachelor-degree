package ru.ifmo.rain.bazhenov.arrayset;

import java.util.*;

@SuppressWarnings("unused")
public class ArraySet<E> extends AbstractSet<E> implements NavigableSet<E> {
    private final ReversibleList<E> elements;
    private final Comparator<? super E> comparator;

    public ArraySet() {
        this((Comparator<E>) null);
    }

    public ArraySet(Comparator<? super E> comparator) {
        this(Collections.emptyList(), comparator);
    }

    public ArraySet(Collection<? extends E> elements) {
        this(elements, null);
    }

    public ArraySet(Collection<? extends E> elements, Comparator<? super E> comparator) {
        this.comparator = comparator;
        Set<E> sortedList = new TreeSet<>(comparator);
        sortedList.addAll(elements);
        this.elements = new ReversibleList<>(sortedList);
    }

    private ArraySet(ReversibleList<E> elements, Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.elements = elements;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object object) {
        return Collections.binarySearch(elements, (E) object, comparator) >= 0;
    }

    @Override
    public E lower(E e) {
        return getValueOrNull(e, false, true);
    }

    @Override
    public E floor(E e) {
        return getValueOrNull(e, true, true);
    }

    @Override
    public E higher(E e) {
        return getValueOrNull(e, false, false);
    }

    @Override
    public E ceiling(E e) {
        return getValueOrNull(e, true, false);
    }

    private E getValueOrNull(E e, boolean inclusive, boolean lower) {
        int index = getIndex(e, inclusive, lower);
        return (0 <= index && index < size()) ? elements.get(index) : null;
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return Collections.unmodifiableList(elements).iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    public void checkSize() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public E first() {
        checkSize();
        return elements.get(0);
    }

    @Override
    public E last() {
        checkSize();
        return elements.get(elements.size() - 1);
    }

    @SuppressWarnings("unchecked")
    private int compare(E e1, E e2) {
        return (comparator == null) ? ((Comparable<E>) e1).compareTo(e2) : comparator.compare(e1, e2);
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return new ArraySet<>(new ReversibleList<>(elements), Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }
        return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
    }

    private NavigableSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        int fromIndex = getIndex(fromElement, fromInclusive, false);
        int toIndex = getIndex(toElement, toInclusive, true);
        if (fromIndex > toIndex) {
            return new ArraySet<>(comparator);
        }
        return new ArraySet<>(elements.subList(fromIndex, toIndex + 1), comparator);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        if (isEmpty()) {
            return this;
        }
        return subSetImpl(first(), true, toElement, inclusive);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        if (isEmpty()) {
            return this;
        }
        return subSetImpl(fromElement, inclusive, last(), true);
    }

    private int getIndex(E e, boolean inclusive, boolean lower) {
        int index = Collections.binarySearch(elements, e, comparator);
        if (index < 0) {
            int currentIndex = -index - 1;
            return lower ? currentIndex - 1 : currentIndex;
        } else {
            return inclusive ? index : (lower ? (index - 1) : (index + 1));
        }
    }

    public static class ReversibleList<E> extends AbstractList<E> implements RandomAccess {
        private final List<E> elements;
        private boolean flip;

        private ReversibleList(List<E> list, boolean flip) {
            this.elements = Collections.unmodifiableList(list);
            this.flip = flip;
        }

        public ReversibleList(Collection<E> collection) {
            this.elements = List.copyOf(collection);
            this.flip = false;
        }

        public ReversibleList(ReversibleList<E> reversibleList) {
            this.elements = reversibleList.elements;
            this.flip = !reversibleList.flip;
        }

        private int getIndex(int index) {
            if (!flip) {
                return index;
            } else {
                return size() - 1 - index;
            }
        }

        public ReversibleList<E> subList(int fromIndex, int toIndex) {
            if (!flip) {
                return new ReversibleList<>(elements.subList(getIndex(fromIndex), getIndex(toIndex)), flip);
            } else {
                return new ReversibleList<>(elements.subList(getIndex(toIndex - 1), getIndex(fromIndex) + 1), flip);
            }
        }

        public E get(int index) {
            return elements.get(getIndex(index));
        }

        public int size() {
            return elements.size();
        }
    }
}

