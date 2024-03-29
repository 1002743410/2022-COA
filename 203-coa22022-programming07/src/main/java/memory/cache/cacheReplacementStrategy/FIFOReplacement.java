package memory.cache.cacheReplacementStrategy;


import memory.Memory;
import memory.cache.Cache;

/**
 * TODO 先进先出算法
 */
public class FIFOReplacement implements ReplacementStrategy {

    @Override
    public void hit(int rowNO) {
        // 重置该行时间戳
        Cache.getCache().setTimeStamp(rowNO);
    }

    @Override
    public int replace(int start, int end, char[] addrTag, byte[] input) {
        long minTime = Long.MAX_VALUE;
        int minIndex = -1;
        for (int i = start; i <= end; i++) {
            long curTime = Cache.getCache().getTimeStamp(i);
            if (curTime < minTime) {
                minTime = curTime;
                minIndex = i;
            }
        }
        if (Cache.isWriteBack) {
            if (Cache.getCache().isDirty(minIndex) && Cache.getCache().isValid(minIndex)) {
                String addr = Cache.getCache().calculatePAddr(minIndex);
                Memory.getMemory().write(addr, Cache.LINE_SIZE_B, Cache.getCache().getData(minIndex));
            }
        }
        Cache.getCache().update(minIndex, addrTag, input);
        return minIndex;
    }

}
