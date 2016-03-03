package com.github.scribejava.core.utils;

import com.github.scribejava.core.exceptions.OAuthException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public final class Uninterruptibles {
  /**
   *
   * This is part of the code from guava, copied here as the project doesn't include one.
   *
   * Invokes {@code future.}{@link Future#get() get()} uninterruptibly.
   */
  public static <V> V getUninterruptibly(Future<V> future) {
    boolean interrupted = false;
    try {
      while (true) {
        try {
          return future.get();
        } catch (InterruptedException e) {
          interrupted = true;
        }
      }
    }
    catch( ExecutionException e ) {
      throw new OAuthException( e );
    }
    finally {
      if (interrupted) {
        Thread.currentThread().interrupt();
      }
    }
  }

}
