package org.bouncycastle.tls;

class Timeout
{
    private long durationMillis;
    private long startMillis;

    Timeout(long durationMillis)
    {
        this(durationMillis, System.currentTimeMillis());
    }

    Timeout(long durationMillis, long currentTimeMillis)
    {
        this.durationMillis = Math.max(0, durationMillis);
        this.startMillis = Math.max(0, currentTimeMillis);
    }

    long remainingMillis()
    {
        return remainingMillis(System.currentTimeMillis());
    }

    synchronized long remainingMillis(long currentTimeMillis)
    {
        // If clock jumped backwards, reset start time 
        if (startMillis > currentTimeMillis)
        {
            startMillis = currentTimeMillis;
            return durationMillis;
        }

        long elapsed = currentTimeMillis - startMillis;
        long remaining = durationMillis - elapsed;

        // Once timeout reached, lock it in
        if (remaining <= 0)
        {
            return durationMillis = 0L;
        }

        return remaining;
    }
}
