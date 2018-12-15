package com.job.manager.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;

public class CloseUtil
{
  private static final Logger LOG = Logger.getLogger(CloseUtil.class);

  public static void closeInputStream(InputStream inStream)
  {
    if (inStream != null)
      try {
        inStream.close();
      } catch (IOException e) {
        LOG.error("error on close the inputstream.", e);
      }
  }

  public static void closeOutputStream(OutputStream outStream)
  {
    if (outStream != null)
      try {
        outStream.close();
      } catch (IOException e) {
        LOG.error("error on close the outputstream.", e);
      }
  }

  public static void closeSocket(Socket socket)
  {
    if (socket != null)
      try {
        socket.close();
      } catch (IOException e) {
        LOG.error("fail on close socket: " + socket, e);
      }
  }

  public static void closeReader(Reader reader)
  {
    if (reader == null)
      return;
    try
    {
      reader.close();
    } catch (IOException e) {
      LOG.error("error on close reader, ignored!", e);
    }
  }

  public static void closeWriter(Writer writer) {
    if (writer == null)
      return;
    try
    {
      writer.close();
    } catch (IOException e) {
      LOG.error("error on close writer, ignored!", e);
    }
  }

  public static void close(Reader reader) {
    if (reader != null)
      try {
        reader.close();
      } catch (IOException e) {
        LOG.error("error on close the Reader.", e);
      }
  }

  public static void close(Connection conn)
  {
    if (conn != null)
      try {
        conn.close();
      } catch (Exception e) {
        LOG.error("error on close java.sql.Connection.", e);
      }
  }

  public static void close(Writer writer)
  {
    if (writer != null)
      try {
        writer.close();
      } catch (IOException e) {
        LOG.error("error on close the outputstream.", e);
      }
  }

  public static void close(InputStream inStream)
  {
    if (inStream != null)
      try {
        inStream.close();
      } catch (IOException e) {
        LOG.error("error on close the inputstream.", e);
      }
  }

  public static void close(OutputStream outStream)
  {
    if (outStream != null)
      try {
        outStream.close();
      } catch (IOException e) {
        LOG.error("error on close the outputstream.", e);
      }
  }

  public static void close(Closeable closeable)
  {
    if (closeable == null)
      return;
    try
    {
      closeable.close();
    } catch (IOException e) {
      LOG.error("error on close [" + closeable.getClass().getName() + "]", e);
    }
  }
}