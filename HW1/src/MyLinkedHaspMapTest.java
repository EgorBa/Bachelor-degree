import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.*;

public class MyLinkedHaspMapTest {

    MyLinkedHashMap<Integer, String> map;

    @Before
    public void createMyLinkedHashMap() {
        map = new MyLinkedHashMap<>();
    }

    @Test
    public void putOneElement() {
        map.put(1, "1");
        assertEquals(map.get(1), "1");
    }

    @Test
    public void putSomeElements() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        assertEquals(map.get(1), "1");
        assertEquals(map.get(2), "2");
        assertEquals(map.get(3), "3");
    }

    @Test
    public void putTwoElementsWithSameKeys() {
        map.put(1, "1");
        map.put(1, "2");
        assertEquals(map.get(1), "2");
        assertEquals(map.size(), 1);
    }

    @Test
    public void putOneElementAndRemove() {
        map.put(1, "1");
        map.remove(1);
        assertNull(map.get(1));
        assertEquals(map.size(), 0);
    }

    @Test
    public void putSomeElementsAndRemove() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.remove(1);
        map.remove(2);
        map.remove(3);
        assertNull(map.get(1));
        assertNull(map.get(2));
        assertNull(map.get(3));
        assertEquals(map.size(), 0);
    }

    @Test
    public void getFirst() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        assertEquals(map.getFirstValue(), "1");
    }

    @Test
    public void getLast() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        assertEquals(map.getLastValue(), "3");
    }

    @Test
    public void putAndRemoveFirst() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.removeFirstValue();
        assertEquals(map.getFirstValue(), "2");
    }

    @Test
    public void putAndRemoveLast() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.removeLastValue();
        assertEquals(map.getLastValue(), "2");
    }

    @Test(expected = AssertionError.class)
    public void removeLastFromEmptyMap() {
        map.removeLastValue();
    }

    @Test(expected = AssertionError.class)
    public void removeFirstFromEmptyMap() {
        map.removeFirstValue();
    }

    @Test
    public void removeNonExistingKey() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.remove(4);
        assertEquals(map.size(), 3);
    }

    @Test
    public void createMapFromMap() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        MyLinkedHashMap<Integer, String> map1 = new MyLinkedHashMap<>(map);
        assertEquals(map1.size(), 3);
        assertEquals(map1.getLastValue(), "3");
        assertEquals(map1.getFirstValue(), "1");
    }

    @Test
    public void testClear() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.clear();
        assertEquals(map.size(), 0);
    }

    @Test
    public void checkPutMaxSizeElements() {
        for (int i = 0; i <= map.MAX_SIZE; i++) {
            map.put(i, String.valueOf(i));
        }
        assertEquals(map.getLastValue(), String.valueOf(map.MAX_SIZE));
        assertEquals(map.getFirstValue(), "1");
    }

    @Test
    public void stressTest() {
        for (int i = 0; i < 100000; i++) {
            int op = (int) (Math.random() * 4);
            int key = (int) (Math.random() * 50);
            String value = String.valueOf((int) (Math.random() * 50));
            switch (op) {
                case 0:
                    if (map.size() > 0) {
                        int oldSize = map.size();
                        map.removeFirstValue();
                        assertEquals(map.size() + 1, oldSize);
                    }
                    break;
                case 1:
                    map.remove(key);
                    assertNull(map.get(key));
                    break;
                case 2:
                    map.put(key, value);
                    assertEquals(value, map.get(key));
                    break;
                case 3:
                    if (map.size() > 0) {
                        int oldSize = map.size();
                        map.removeLastValue();
                        assertEquals(map.size() + 1, oldSize);
                    }
                    break;
                default:
            }
        }
    }

}
