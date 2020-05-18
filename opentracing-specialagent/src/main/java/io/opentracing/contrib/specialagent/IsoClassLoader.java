/* Copyright 2019 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentracing.contrib.specialagent;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

public class IsoClassLoader extends URLClassLoader {
  private static final Logger logger = Logger.getLogger(IsoClassLoader.class);
  private static BiConsumer<String,HashSet<String>> classNameConsumer = new BiConsumer<String,HashSet<String>>() {
    @Override
    public void accept(final String name, final HashSet<String> arg) {
      if (BootProxyClassLoader.INSTANCE.getResource(name) == null)
        arg.add(name);
    }
  };

  private static class IsoParentClassLoader extends ClassLoader {
    private final AtomicReference<HashSet<String>> isoNames = new AtomicReference<>();
    private final URL[] isoClassPaths;

    private IsoParentClassLoader(final URL[] isoClassPaths, final ClassLoader parent) {
      super(parent);
      this.isoClassPaths = isoClassPaths;
      if (logger.isLoggable(Level.FINEST))
        logger.finest("new IsoParentClassLoader(" + AssembleUtil.toIndentedString(isoClassPaths) + ")");
    }

    private HashSet<String> getNames() {
      if (isoNames.get() != null)
        return isoNames.get();

      synchronized (this) {
        if (isoNames.get() != null)
          return isoNames.get();

        final HashSet<String> names = new HashSet<>();
        try {
          AssembleUtil.<HashSet<String>>forEachClass(isoClassPaths, names, classNameConsumer);
          isoNames.set(names);
          return names;
        }
        catch (final IOException e) {
          throw new IllegalStateException(e);
        }
      }
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
      final boolean isNameIso = getNames().contains(AssembleUtil.classNameToResource(name));
      final Class<?> cls = isNameIso ? null : super.loadClass(name, resolve);
      if (logger.isLoggable(Level.FINEST))
        logger.finest("~~~~~~~~ IsoParentClassLoader.loadClass(\"" + name + "\", " + resolve + ") [" + isNameIso + "]: " + cls);

      return cls;
    }

    @Override
    public Enumeration<URL> getResources(final String name) throws IOException {
      final boolean isNameIso = getNames().contains(name);
      final Enumeration<URL> resources = isNameIso ? Collections.<URL>emptyEnumeration() : super.getResources(name);
      if (logger.isLoggable(Level.FINEST))
        logger.finest("~~~~~~~~ IsoParentClassLoader.getResources(\"" + name + "\") [" + isNameIso + "]: " + resources);

      return resources;
    }
  }

  public IsoClassLoader(final URL[] urls, final ClassLoader parent) {
    super(urls, new IsoParentClassLoader(urls, parent));
  }

  public Class<?> loadClassOrNull(final String name) {
    try {
      return loadClass(name);
    }
    catch (final ClassNotFoundException e) {
      return null;
    }
  }
}