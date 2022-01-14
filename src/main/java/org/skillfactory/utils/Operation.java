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
public interface Operation<T> {

  T run(int count) throws Exception;

  default void handleException(int tryCount, Exception cause) {
    // default impl: do nothing, log the exception, etc.
  }
}
