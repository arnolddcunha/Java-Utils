package org.skillfactory.utils;

import java.util.ArrayList;
import java.util.List;

public final class LoopOperation<T> {

  public List<T> loop(int count, Operation<T> operation) throws Exception {
    List<T> results = new ArrayList<>(count);
    for (int ctr = 0; ctr < count; ctr++) {
      results.add(operation.run(ctr));
    }
    return results;
  }

}
