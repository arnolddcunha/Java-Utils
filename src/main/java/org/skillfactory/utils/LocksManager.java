/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.skillfactory.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author hoshi
 */
public class LocksManager {

  private Map<String, StripedLock> locksMap = new HashMap<>(40);
  public final boolean caseInsensitiveKey;

  public LocksManager(boolean caseInsensitiveKey) {
    this.caseInsensitiveKey = caseInsensitiveKey;
  }

  private class StripedLock extends ReentrantLock {
    private int lockHolders = 0;
    private String key;

    public StripedLock(String key) {
      this.key = key;
    }

    @Override
    public void unlock() {
      super.unlock();
      lockHolders--;
      releaseLock(this);
    }
  }

  public synchronized ReentrantLock getLock(String key) {
    if (caseInsensitiveKey)
      key = key.toUpperCase();
    StripedLock lock = locksMap.get(key);
    if (lock == null) {
      lock = new StripedLock(key);
      locksMap.put(key, lock);
    }
    lock.lockHolders++;
    return lock;
  }

  private synchronized void releaseLock(StripedLock lock) {
    if (lock.lockHolders < 1)
      locksMap.remove(lock.key);
  }
}
