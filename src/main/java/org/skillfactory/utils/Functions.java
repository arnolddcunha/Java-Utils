package org.skillfactory.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Functions {

  private static boolean debug = false;

  public static void setDebug(boolean debug) {
    Functions.debug = debug;
  }

  public static void debug(Object obj) {
    debug(obj.toString());
  }

  public static void debug(String s) {
    if (debug) {
      System.out.println(s);
    }
  }

  public static boolean isNull(String s) {
    return s == null || s.isEmpty();
  }

  public static String trimz(String s) {
    if (s != null)
      s = s.trim();
    if (isNull(s)) {
      return null;
    }
    return s;
  }

  public static String stringz(String s) {
    if (isNull(s)) {
      return null;
    }
    return s;
  }

  public static String string(String s) {
    if (isNull(s)) {
      return "";
    }
    return s;
  }

  public static void sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException ex) {
    }
  }

  public static <T extends Enum<T>> Optional<T> toEnum(Class<T> enumType, String name) {
    try {
      return Optional.of(Enum.valueOf(enumType, name));
    } catch (IllegalArgumentException ex) {
      return Optional.empty();
    }
  }

  public static void close(Writer writer) {
    if (writer == null) {
      return;
    }
    try {
      writer.close();
    } catch (Exception ex) {
    }
  }

  public static void close(Reader reader) {
    if (reader == null) {
      return;
    }
    try {
      reader.close();
    } catch (Exception ex) {
    }
  }

  public static void close(OutputStream outputStream) {
    if (outputStream == null) {
      return;
    }
    try {
      outputStream.close();
    } catch (Exception ex) {
    }
  }

  public static void close(InputStream inputStream) {
    if (inputStream == null) {
      return;
    }
    try {
      inputStream.close();
    } catch (Exception ex) {
    }
  }

  public static <T> T randomItem(List<T> items) {
    int size = items.size();
    if (size < 1)
      throw new RuntimeException("Size cannot be 0");
    int randomValue = randomNumberBetween(1, size);
    return items.get(randomValue - 1);
  }

  public static int randomValue(Collection<Integer> list) {
    if (list == null || list.isEmpty()) {
      throw new NullPointerException("List cannot be empty");
    }

    Map<Integer, Integer> map = new HashMap<>(list.size());

    int ctr = 0;
    for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
      Integer next = iterator.next();
      map.put(ctr, next);
      ctr++;
    }

    return map.get(randomNumberBetween(0, list.size() - 1));
  }

  public static int randomNumberBetween(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }

  public static long randomLongBetween(long min, long max) {
    return ThreadLocalRandom.current().nextLong(min, max + 1);
  }

  public static double randomDoubleBetween(double min, double max) {
    return ThreadLocalRandom.current().nextDouble(min, max);
  }

  public static ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
    return objectMapper;
  }

  public static void main1(String[] args) {
    for (int ctr = 1; ctr <= 6; ctr++) {
      System.out.println(ctr - 1);
    }
  }

  public static void main2(String[] args) {
    List<Integer> list = new ArrayList<>();
    for (int ctr = 0; ctr < 100; ctr++) {
      list.add(ctr);
    }

    Collections.shuffle(list);

    System.out.println("Before");
    for (int ctr = 0; ctr < list.size(); ctr++) {
      System.out.println(ctr + " : " + list.get(ctr));
    }
    System.out.println("After");
    System.out.println();

    // while (!list.isEmpty()) {
    // int randomValue = randomValue(list);
    // System.out.println(randomValue);
    // System.out.println();
    // list.remove( list.indexOf(randomValue));
    // }
  }

  public static void main(String[] args) throws MaximumAttemptsReachedException {
    RetryUtil<String> retryUtil = new RetryUtil<String>();
    String value = retryUtil.doWithRetry(5, new OperationImpl());
    System.out.println("PRINT: " + value);
  }

  private static class OperationImpl implements Operation<String> {

    public OperationImpl() {
    }

    @Override
    public String run(int count) {
      System.out.println(count);
      if (count == 5) {
        return "1";
      }

      count++;
      throw new RuntimeException("Test Retry" + count);
    }
  }

  public static boolean onlyDigits(String txt) {
    int len = txt.length();
    for (int ctr = 0; ctr < len; ctr++) {
      if (!Character.isDigit(txt.charAt(ctr))) {
        return false;
      }
    }
    return true;
  }

  public static boolean existsInArray(String item, String[] array) {
    for (String aItem : array) {
      if (aItem.equals(item)) {
        return true;
      }
    }
    return false;
  }

  public static String getStackTrace(Throwable ex) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    ex.printStackTrace(printWriter);
    return stringWriter.toString();
  }
}
