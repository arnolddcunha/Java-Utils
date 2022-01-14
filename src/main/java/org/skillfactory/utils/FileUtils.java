/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.skillfactory.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Julian
 */
public final class FileUtils {

  public static void deleteDir(Path folder) throws IOException {
    if (!Files.exists(folder)) {
      return;
    }
    if (!Files.isDirectory(folder)) {
      throw new RuntimeException(folder.toString() + " is not a directory: ");
    }
    _deleteDir(folder);
  }

  private static void _deleteDir(Path folder) throws IOException {
    List<Path> children = getChildren(folder);
    for (Path path : children) {
      if (Files.isDirectory(path)) {
        _deleteDir(path);
      } else {
        Files.delete(path);
      }
    }
    Files.delete(folder);
  }

  public static void moveDir(Path source, Path destination) throws IOException {
    moveDir(source, destination, true);
  }

  public static void moveDir(Path source, Path destination, boolean createDestParentFolders) throws IOException {
    if (!Files.exists(source)) {
      throw new IOException("source folder does not exist '" + source + "'");
    }
    if (!Files.isDirectory(source)) {
      throw new IOException("source is not a directory");
    }
    if (Files.exists(destination)) {
      if (Files.isSameFile(source, destination)) {
        return;
      }
      _deleteDir(destination);

    } else {
      // create destination parents if parent does not exists
      Path parent = destination.getParent();
      if (!Files.exists(parent)) {
        if (!createDestParentFolders) {
          throw new IOException("folder does not exist '" + parent + "'");
        }
        Files.createDirectories(parent);
      }
    }
    // Files.move fails when moving between drives. i.e. we chave to do copy-delete
    try {
      Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
      return;
    } catch (IOException ex) {
    }
    copyDir(source, destination, createDestParentFolders);
    deleteDir(source);
  }

  public static void copyDir(Path source, Path destination) throws IOException {
    FileUtils.copyDir(source, destination, true);
  }

  public static void copyDir(Path source, Path destination, boolean createDestParentFolders) throws IOException {
    if (!Files.exists(source)) {
      throw new IOException("source folder does not exist '" + source + "'");
    }
    if (!Files.isDirectory(source)) {
      throw new IOException("source is not a directory");
    }
    if (Files.exists(destination)) {
      if (Files.isSameFile(source, destination)) {
        return;
      }
      _deleteDir(destination);
    } else {
      // create destination parents if parent does not exists
      Path parent = destination.getParent();
      if (!Files.exists(parent)) {
        if (!createDestParentFolders) {
          throw new IOException("folder does not exist '" + parent + "'");
        }
        Files.createDirectories(parent);
      }
    }
    if (!Files.exists(destination)) {
      Files.createDirectory(destination);
    }
    List<Path> children = getChildren(source);
    for (Path path : children) {
      if (Files.isDirectory(path)) {
        _copyDir(path, destination.resolve(path.getFileName()));
      } else {
        if (Files.copy(path, destination.resolve(path.getFileName())) == null) {
          throw new IOException("failed to copy file '" + source + "' to '" + destination + "'");
        }
      }
    }
  }

  private static void _copyDir(Path source, Path destination) throws IOException {
    Files.createDirectory(destination);
    List<Path> children = getChildren(source);
    for (Path path : children) {
      if (Files.isDirectory(path)) {
        _copyDir(path, destination.resolve(path.getFileName()));
      } else {
        if (Files.copy(path, destination.resolve(path.getFileName())) == null) {
          throw new IOException("failed to copy file '" + source + "' to '" + destination + "'");
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static List<Path> getChildren(Path folder) throws IOException {
    File[] children = folder.toFile().listFiles();
    if (children == null || children.length == 0) {
      return Collections.EMPTY_LIST;
    }
    List<Path> list = new ArrayList<>(children.length);
    for (File file : children) {
      list.add(file.toPath());
    }
    return list;
  }

  public static Path findInCollection(Collection<Path> paths, String name) {
    if (paths != null) {
      for (Path path : paths) {
        if (path.getFileName().toString().equals(name)) {
          return path;
        }
      }
    }
    return null;
  }

  public static Path findInArray(Path[] paths, String name) {
    if (paths != null) {
      for (Path path : paths) {
        if (path.getFileName().toString().equals(name)) {
          return path;
        }
      }
    }
    return null;
  }

}
