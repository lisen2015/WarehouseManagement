package com.job.manager.util;

import com.job.manager.exception.WrappedException;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil
{

  public static String md5Hex(String sData)
  {

	byte[] bytes;
    AssertUtil.notNull(sData, "sData is null.");
    try
    {
      bytes = sData.getBytes("ISO-8859-1");
    }
    catch (UnsupportedEncodingException e)
    {
      throw new WrappedException(e);
    }
    return StringHelper.toString(md5Checksum(bytes));
  }

  public static String md5Hex(File file)
  {

	InputStream is;
    AssertUtil.notNull(file, "file is null.");
    try
    {
      is = new FileInputStream(file);
    }
    catch (FileNotFoundException e)
    {
      throw new WrappedException(e);
    }
    return StringHelper.toString(md5Checksum(is));
  }

  static byte[] md5Checksum(byte[] data)
  {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new WrappedException(e);
    }
    md5.update(data);
    return md5.digest();
  }

  static byte[] md5Checksum(InputStream is)
  {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new WrappedException(e);
    }

    DigestInputStream dis = null;
    try {
      dis = new DigestInputStream(is, md5);

      byte[] buffer = new byte[1024];
      int read = dis.read(buffer, 0, 1024);

      while (read > -1)
        read = dis.read(buffer, 0, 1024);
    }
    catch (IOException e)
    {
      throw new WrappedException(e);
    } finally {
      CloseUtil.closeInputStream(dis);
    }
    return md5.digest();
  }

}