package simulatecache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author rm631
 */
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    
    private int maxSize;
    
    public LRULinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxSize = initialCapacity;
    }
    
    // this method usually just returns false, hence we override to return true sometimes..
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxSize;
    }
    
}
