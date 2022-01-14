/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.skillfactory.utils;

/**
 *
 * @author Julian
 */
public final class RetryUtil<T> {

  public T doWithRetry(int maxAttempts, Operation<T> operation) throws MaximumAttemptsReachedException {
    Exception ex = null;
    for (int count = 0; count < maxAttempts; count++) {
      try {
        return operation.run(count);
        // count = maxAttempts; //don't retry
      } catch (Exception e) {
        ex = e;
        operation.handleException(count + 1, e);
      }
    }
    throw new MaximumAttemptsReachedException(ex);
  }

}
