/*															
 * FileName：StringHelper.java	
 * 					
 * Description：String工具类，提供字符串判断、字符串转日期等功能
 * 				
 * History：
 * 版本号		作者			日期       		简介
 *  1.0   	chenchen 	2013-09-18  create
 *  1.1		chenchen	2014-08-09	增加方法toDateString
 *  1.2		chenchen	2014-10-12	增加方法isNullToFirstDay、isNullToLastDay
 *  1.3		chenchen	2014-12-09	增加方法toStartDate、toEndDate
 *  1.4		chenchen	2014-12-09	删除toDateString1 toDateString2 toDateString3 toDateString4方法
 *  1.5		chenchen	2014-12-09	增加方法toDate
 */
package com.job.manager.util;


import com.alibaba.fastjson.JSON;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
  *字符串处理：<br />
  *分割(split、splitAndTrim)<br />
  *反转(reverse)<br />
  *空处理(avoidNull、avoidEmpty)<br />
  *去空格(assertNotEmptyAndTrim、trim、trimAllSpace)<br />
  *数组转字符串(join)<br />
  *首字母大小写转换(capitalize、uncapitalize、changeFirstCharacterCase)<br />
  *截断(truncateAndTrim、truncate、adjustLength、substring)<br />
  *转码(getStringByEncoding)
  *替换（replaceAll）
  *string转数组(split2IntArray、split2LongArray)<br />
  *int转string(int2String)<br />
  *string转int(parseInt、parseIntUsingFormat)<br />
  *string转map(parseAsMap)<br />
  *验证(isEmpty、isNotEmpty、containsWhitespace、isNumeric、isInteger、isPositiveInteger、<br />
  *isTelphone、isMobile、isAccountName、limitedLength、isEmail、isIP、isIPv4、isMultiIPs、<br />
  *isHttpUrl、isRtmpUrl、isQQ、isStringArrayEmpty、equalIngoreCaseAndSpace)<br />
  *正则处理(regexReplace、regexFind、regexGet)<br />
  *数组处理(mergeArrary)<br />
  *日期处理(toDateString、toDate、toStartDate、toEndDate、isNullToMonthFirstDay、isNullToMonthLastDay)<br />
  *json转string(jsonToString)
 * @author CC
 */
public class StringHelper {

	public static final String EMPTY = "";
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	/**
	 * yyyy-MM-dd
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
    /**
     * 把输入的字节树组，逐字节转化为其16进制的文本描述形式
     *
     * @param buf
     *            输入的字节树组
     * @param off
     *            需要转化的开始位置
     * @param len
     *            需要转化的结束位置
     * @return 转化结果
     */
    public final static String bytesToHex(byte[] buf, int off, int len) {
        char[] out = new char[len * 2];

        for (int i = 0, j = 0; i < len; i++) {
            int a = buf[off++];

            out[j++] = DIGITS[(a >>> 4) & 0X0F];
            out[j++] = DIGITS[a & 0X0F];
        }
        return (new String(out));
    }
    /**
	 * 字符串分割
	 * @param origin 字符串
	 * @param token	分隔符
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String[] split(String origin, String token) {
		if (origin == null) {
			return null;
		}
		StringTokenizer st = token == null ? new StringTokenizer(origin)
				: new StringTokenizer(origin, token);
		int countTokens = st.countTokens();
		if (countTokens <= 0) {
			return new String[] { origin };
		}
		String[] results = new String[countTokens];
		for (int i = 0; i < countTokens; i++) {
			results[i] = st.nextToken();
		}
		return results;
	}
	/**
	 * 字符串反转
	 * @param origin 字符串
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String reverse(String origin) {
		if (origin == null) {
			return null;
		}
		return new StringBuilder(origin).reverse().toString();
	}

    // from commons-codec: char[] Hex.encodeHex(byte[])
    /**
     * 将给定的字节数组用十六进制字符串表示.
     */
    public static String toString(byte[] data) {
        if (data == null) {
            return "null!";
        }
        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return new String(out);
    }
    /**
     * 过滤单引号以外的XSS字符
     *
     * @param value
     *            待过滤字符串
     * @return 过滤后字符串
     * @since v5.0
     * @creator SHIXIN-dev4 @ 2014-8-13
     */
    public static String filterXSSWithoutQuotation(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        return value;
    }
	public static byte[] toBytes(String str) {
		if (str == null) {
			return null;
		}
		char[] data = str.toCharArray();
		int len = data.length;

		if ((len & 0x1) != 0) {
			throw new RuntimeException("Odd number of characters!");
		}

		byte[] out = new byte[len >> 1];

		int i = 0;
		for (int j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f |= toDigit(data[j], j);
			j++;
			out[i] = ((byte) (f & 0xFF));
		}

		return out;
	}

	public static String filterForHTMLValue(String _sContent) {
		if (_sContent == null) {
			return "";
		}

		char[] srcBuff = _sContent.toCharArray();
		int nLen = srcBuff.length;
		if (nLen == 0) {
			return "";
		}
		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8D));

		for (int i = 0; i < nLen; i++) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '&':
				if (i + 1 < nLen) {
					cTemp = srcBuff[(i + 1)];
					if (cTemp == '#')
						retBuff.append("&");
					else
						retBuff.append("&amp;");
				} else {
					retBuff.append("&amp;");
				}
				break;
			case '<':
				retBuff.append("&lt;");
				break;
			case '>':
				retBuff.append("&gt;");
				break;
			case '"':
				retBuff.append("&quot;");
				break;
			default:
				retBuff.append(cTemp);
			}
		}

		return retBuff.toString();
	}

	public static String toString(Object[] objs) {
		return toString(objs, false, ", ");
	}

	public static String toString(Object[] objs, boolean showOrder) {
		return toString(objs, showOrder, ",");
	}

	public static String toString(Object[] objs, boolean showOrder, String token) {
		if (objs == null) {
			return "null";
		}
		int len = objs.length;
		StringBuffer sb = new StringBuffer(10 * len);
		for (int i = 0; i < len; i++) {
			if (showOrder) {
				sb.append(i).append(':');
			}
			sb.append(objs[i]);
			if (i < len - 1) {
				sb.append(token);
			}
		}
		return sb.toString();
	}
	/**
	 * 字符串空处理，为空返回“”
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String avoidNull(String str) {
		return str == null ? "" : str;
	}
	/**
	 * 字符串去空格处理（不允许空）
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String assertNotEmptyAndTrim(String str) {
		AssertUtil.notNullOrEmpty(str, "the string is empty!");
		return str.trim();
	}
	/**
	 * 字符串空处理，为空返回defaultStr
	 * @param str
	 * @param defaultStr
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String avoidEmpty(String str, String defaultStr) {
		return isEmpty(str) ? defaultStr : str;
	}

	/**
	 * json转string
	 * 
	 * @param obj
	 * @return
	 * @version 2014-11-28 chenchen create
	 */
	public static String jsonToString(Object obj) {
        return JSON.toJSONString(obj);
	}
	/**
	 * 根据分隔符int数组转String<br />
	 * eg：<br />
	 * 	int i=[1,3,5,6];<br />
	 * 	join(i,",")<br />
	 * 	输出1，3，5，6<br />
	 * @param array	数组
	 * @param separator	分隔符
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String join(int[] array, String separator) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = "";
		}

		StringBuilder buf = new StringBuilder(200);

		if (array.length > 0) {
			buf.append(array[0]);
		}
		for (int i = 1; i < array.length; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}
	/**
	 * 对象数组转字符串
	 * eg：<br />
	 * 	int i=[1,3,5,6];<br />
	 * 	join(i)<br />
	 * 	输出1，3，5，6<br />
	 * @param array
	 * @return 
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String join(Object[] array) {
		return join(array, ",");
	}
	/**
	 * 根据分隔符对象数组转字符串
	 * eg：<br />
	 * 	Object[] i=[1,3,5,6];<br />
	 * 	join(i)<br />
	 * 	输出1，3，5，6<br />
	 * @param array	数组
	 * @param separator	分隔符
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String join(Object[] array, char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}
	/**
	 * 根据分隔符对象数组转字符串
	 * eg：<br />
	 * 	Object[] i=[1,3,5,6];<br />
	 * 	join(i)<br />
	 * 	输出1，3，5，6<br />
	 * @param array
	 * @param separator
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String join(Object[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}
	/**
	 * 根据分隔符、索引对象数组转字符串 * eg：<br />
	 * 	Object[] i=[1,3,5,6];<br />
	 * 	join(i,",",1,2)<br />
	 * 	输出3，5<br />
	 * @param array 数组
	 * @param separator 分隔符
	 * @param startIndex 开始位置
	 * @param endIndex 结束位置
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	static String join(Object[] array, char separator, int startIndex,
			int endIndex) {
		if (array == null) {
			return null;
		}
		int bufSize = endIndex - startIndex;
		if (bufSize <= 0) {
			return "";
		}

		bufSize = bufSize
				* ((array[startIndex] == null ? 16 : array[startIndex]
						.toString().length()) + 1);
		StringBuffer buf = new StringBuffer(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}
	/**
	 * 根据分隔符、索引对象数组转字符串 * eg：<br />
	 * 	Object[] i=[1,3,5,6];<br />
	 * 	join(i,",",1,2)<br />
	 * 	输出3，5<br />
	 * @param array 数组
	 * @param separator 分隔符
	 * @param startIndex 开始位置
	 * @param endIndex 结束位置
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	static String join(Object[] array, String separator, int startIndex,
			int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = "";
		}

		int bufSize = endIndex - startIndex;
		if (bufSize <= 0) {
			return "";
		}

		bufSize = bufSize
				* ((array[startIndex] == null ? 16 : array[startIndex]
						.toString().length()) + separator.length());

		StringBuffer buf = new StringBuffer(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}
	/**
	 * 字符串首字母转大写
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}
	/**
	 * 字符串首字母转小写
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}
	/**
	 * 字符串首字母大小写转换，capitalize=true转大写，capitalize=false转小写
	 * @param str
	 * @param capitalize
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	private static String changeFirstCharacterCase(String str,
			boolean capitalize) {
		if ((str == null) || (str.length() == 0)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize)
			buf.append(Character.toUpperCase(str.charAt(0)));
		else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}
	/**
	 * 字符串去空格
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}
	
	static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new RuntimeException("Illegal hexadecimal charcter " + ch
					+ " at index " + index);
		}
		return digit;
	}
	/**
	 * 分割并去空格
	 * @param origin
	 * @param token
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] splitAndTrim(String origin, String token) {
		if (origin == null) {
			return null;
		}
		origin = origin.trim();
		StringTokenizer st = new StringTokenizer(origin, token);
		int countTokens = st.countTokens();
		if (countTokens <= 0) {
			return new String[] { origin };
		}
		List strs = new ArrayList(countTokens);

		for (int i = 0; i < countTokens; i++) {
			String str = st.nextToken().trim();
			if (str.length() > 0) {
				strs.add(str);
			}
		}
		return (String[]) strs.toArray(new String[0]);
	}
	/**
	 * hex转string
	 * @param hex
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String hexToStr(String hex) {
		return new String(toBytes(hex));
	}
	/**
	 * 字符串截断并去空格
	 * @param str
	 * @param delim
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String truncateAndTrim(String str, String delim) {
		if ((str == null) || (delim == null)) {
			return str;
		}
		int nStart = str.indexOf(delim);
		if (nStart < 0) {
			return str;
		}
		return str.substring(nStart + delim.length()).trim();
	}
	/**
	 * 字符串转码 ISO8859-1==>encoding
	 * @param originalStr 
	 * @param encoding
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String getStringByEncoding(String originalStr, String encoding) {
		return getStringByEncoding(originalStr, "ISO8859-1", encoding);
	}
	/**
	 * 字符串转码 fromEncoding==>encoding
	 * @param str
	 * @param toEncoding
     * @param fromEncoding
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String getStringByEncoding(String str, String fromEncoding,
			String toEncoding) {
		if (str == null) {
			return null;
		}
		if (isEmpty(fromEncoding))
			return str;
		try {
			return new String(str.getBytes(fromEncoding), toEncoding);
		} catch (UnsupportedEncodingException e) {
		}
		return str;
	}
	/**
	 * 字符串空或“”判断
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isEmpty(String str) {
		return (str == null) || (str.trim().length() == 0);
	}
	/**
	 * 截断字符串
	 * @param str
	 * @param maxLength
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String truncate(String str, int maxLength) {
		if (str == null) {
			return str;
		}
		if (maxLength <= 0) {
			throw new IllegalArgumentException("Illegal value of maxLength: "
					+ maxLength + "! It must be a positive integer.");
		}
		int strLength = str.length();
		if (maxLength >= strLength) {
			return str;
		}
		StringBuilder sb = new StringBuilder(maxLength);
		int splitPos = maxLength - "...".length();
		sb.append(str.substring(0, splitPos));
		sb.append("...");
		return sb.toString();
	}
	/**
	 * 调整字符串长度，超出长度部分显示...
	 * @param str
	 * @param maxLength
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String adjustLength(String str, int maxLength) {
		if (str == null) {
			return str;
		}
		if (maxLength <= 0) {
			throw new IllegalArgumentException("Illegal value of maxLength: "
					+ maxLength + "! It must be a positive integer.");
		}
		int strLength = str.length();
		if (maxLength > strLength) {
			return str;
		}
		StringBuilder sb = new StringBuilder(maxLength);
		int splitPos = (maxLength - "...".length()) / 2;
		sb.append(str.substring(0, splitPos));
		sb.append("...");
		sb.append(str.substring(strLength - splitPos));
		return sb.toString();
	}
	/**
	 * 获取安全url，url中去除< >
	 * @param url
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String getURLSafe(String url) {
		if ((url == null) || ("".equals(url))) {
			return "";
		}
		StringBuffer strBuff = new StringBuffer();
		char[] charArray = url.toCharArray();
		for (int i = 0; i < charArray.length; i++){
			if ((charArray[i] != '<') && (charArray[i] != '>')) {
				strBuff.append(charArray[i]);
			}
		}
		return strBuff.toString();
	}
	/**
	 * 去除字符串结尾 “/”
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String removeLastSlashChar(String str) {
		if (str == null) {
			return null;
		}
		if (str.endsWith("/")) {
			return str.substring(0, str.length() - 1);
		}
		return str;
	}
	/**
	 * 字符串结尾+“/”
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String smartAppendSlashToEnd(String str) {
		return smartAppendSuffix(str, "/");
	}
	/**
	 * 字符串结尾+endingStr
	 * @param str
	 * @param endingStr
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String smartAppendSuffix(String str, String endingStr) {
		if (str == null) {
			return null;
		}
		return str + endingStr;
	}
	/**
	 * 字符串替换
	 * @param origin 字符串
	 * @param oldPart 被替换内容
	 * @param replacement 替换内容
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String replaceAll(String origin, String oldPart,
			String replacement) {
		if ((origin == null) || (replacement == null)) {
			return origin;
		}

		if ((oldPart == null) || (oldPart.length() == 0)) {
			return origin;
		}

		int index = origin.indexOf(oldPart);
		if (index < 0) {
			return origin;
		}

		StringBuffer sb = new StringBuffer(origin);
		do {
			sb.replace(index, index + oldPart.length(), replacement);
			origin = sb.toString();
			index = origin.indexOf(oldPart);
		} while (index != -1);
		return origin;
	}
	/**
	 * 根据,截取字符串并转换为int数组
	 * @param data
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static int[] split2IntArray(String data) {
		String[] splited = splitAndTrim(data, ",");
		return ArrayUtil.toIntArray(splited);
	}
	/**
	 * 根据,截取字符串并转换为int数组
	 * @param data
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static Long[] split2LongArray(String data) {
		String[] splited = splitAndTrim(data, ",");
		return ArrayUtil.toLongArray(splited);
	}
	/**
	 * int转string
	 * @param num
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String int2String(int num) {
		try {
			return String.valueOf(num);
		} catch (Exception e) {
		}
		return "";
	}
	/**
	 * 字符串截取
	 * @param origin 字符串
	 * @param begin	开始字符串
	 * @param end 结束字符串
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String substring(String origin, String begin, String end) {
		if (origin == null) {
			return origin;
		}
		int beginIndex = begin == null ? 0 : origin.indexOf(begin)
				+ begin.length();
		int endIndex = end == null ? origin.length() : origin.indexOf(end,
				beginIndex);
		if (endIndex == -1) {
			return origin.substring(beginIndex);
		}

		if (origin.length() <= beginIndex) {
			return origin;
		}
		return origin.substring(beginIndex, endIndex);
	}
	/**
	 * 字符串截取
	 * @param origin 字符串
	 * @param begin 开始字符串
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String substring(String origin, String begin) {
		return substring(origin, begin, null);
	}
	/**
	 * String转int，为空或转换错误返回-1
	 * @param value
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static int parseInt(String value) {
		if (value == null)
			return -1;
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
		}
		return -1;
	}
	/**
	 * 按固定格式将String装int
	 * @param source
	 * @param formatPattern
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static int parseIntUsingFormat(String source, String formatPattern) {
		DecimalFormat df = new DecimalFormat(formatPattern);
		try {
			return df.parse(source).intValue();
		} catch (ParseException e) {
		}
		return -1;
	}
	/**
	 * String转map<br />
	 * EG:<br />
	 * String s="1001|102,1002|103,1003|103,1004|103,1005|103";<br />
	 *	Map<String, String> map=parseAsMap(s,",","|");<br />
	 *	for(Map.Entry<String, String> entry: map.entrySet()) {<br />
	 *		 System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");<br />
	 *	}<br />
	 *	输出：1003:103	1004:103	1001:102	1002:103	1005:103<br />
	 * @param src
	 * @param propertyDelimiter
	 * @param fieldDelimiter
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, String> parseAsMap(String src,
			String propertyDelimiter, String fieldDelimiter) {
		Map parsedMap = new HashMap();
		if (isEmpty(src)) {
			return parsedMap;
		}

		String[] properties = split(src, propertyDelimiter);
		for (String property : properties) {
			int keyIndex = property.indexOf(fieldDelimiter);
			if (keyIndex != -1) {
				String key = property.substring(0, keyIndex);
				String value = property.substring(keyIndex
						+ fieldDelimiter.length());
				parsedMap.put(key, value);
			}
		}
		return parsedMap;
	}
	public static Map<String, String> parseAsMap(String src) {
		return parseAsMap(src, ";", "=");
	}

	public static String trimQuote(String value) {
		if (value == null) {
			return value;
		}
		value = value.trim();
		int length = value.length();
		if (length < 2) {
			return value;
		}
		if ((value.charAt(0) != '"') || (value.charAt(length - 1) != '"')) {
			return value;
		}
		return value.substring(1, length - 1);
	}
	/**
	 * 如果字符串包含" ",则在字符串两端增加“
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String addQuote(String str) {
		if (str.indexOf(' ') > 0) {
			str = "\"" + str + "\"";
		}

		return str;
	}
	/**
	 * 判断字符串是否包含空格
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean containsWhitespace(String str) {
		if ((str == null) || (str.length() == 0)) {
			return false;
		}
		char[] chs = str.toCharArray();
		for (char ch : chs) {
			if (Character.isWhitespace(ch)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断字符串是否数字
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isNumeric(String input) {
		if (isEmpty(input))
			return false;
		Pattern pattern = Pattern.compile("^[-|+]?(\\d+)(\\.\\d+)?");
		return pattern.matcher(input).matches();
	}
	/**
	 * 判断字符串是否整数
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isInteger(String input) {
		if (isEmpty(input))
			return false;
		Pattern pattern = Pattern.compile("^[-|+]?[1-9][0-9]*");
		return pattern.matcher(input).matches();
	}
	/**
	 * 判断字符串是否正整数
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isPositiveInteger(String input) {
		if (isEmpty(input))
			return false;
		Pattern pattern = Pattern.compile("^[1-9][0-9]*");
		return pattern.matcher(input).matches();
	}
	/**
	 * 判断字符串是否电话号码
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isTelphone(String input) {
		if (isEmpty(input))
			return false;
		Pattern pattern = Pattern
				.compile("^(((\\+|0)[0-9]{2,3})-)?((0[0-9]{2,3})-)?([0-9]{8})(-([0-9]{3,4}))?$");
		return pattern.matcher(input).matches();
	}
	/**
	 * 判断字符串是否手机号码
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isMobile(String input) {
		if (!isNumeric(input))
			return false;
		return (input.length() == 11) && (input.charAt(0) == '1');
	}
	/**
	 * 判断是否帐号，规则字母开头，4-15的长度，当中只能是字母、数字、_，且长度在minLength-maxLength之间
	 * @param input 字符串
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isAccountName(String input, int minLength,
			int maxLength) {
		if (isEmpty(input))
			return false;
		try{
			Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{"+(minLength-1)+","+(maxLength)+"}$");
			return pattern.matcher(input).matches();
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 长度判断
	 * @param input 字符串
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean limitedLength(String input, int minLength,
			int maxLength) {
		if (isEmpty(input))
			return minLength <= 0;
		return (input.length() >= minLength) && (input.length() <= maxLength);
	}
	/**
	 * 是否email
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isEmail(String input) {
		if (isEmpty(input))
			return false;
		Pattern pattern = Pattern
				.compile("^[A-Za-z0-9._%+-]+@(\\w+\\.)+[a-zA-Z]{2,3}");
		return pattern.matcher(input).matches();
	}

	/**
	 * 是否IP
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isIP(String input) {
		if (isEmpty(input))
			return false;
		Pattern pattern = Pattern
				.compile("((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)(\\.((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)){3}");
		return pattern.matcher(input).matches();
	}
	/**
	 * 是否IPV4
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isIPv4(String input) {
		if (isEmpty(input))
			return false;
		Pattern pattern = Pattern
				.compile("((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)(\\.((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)){3}");
		return pattern.matcher(input).matches();
	}
	/**
	 * 是否多个IP,通过，分割
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isMultiIPs(String input) {
		if (isEmpty(input))
			return false;
		StringTokenizer st = new StringTokenizer(input, ",");
		boolean isMultiIPs = true;
		while ((st.hasMoreTokens()) && (isMultiIPs)) {
			String token = st.nextToken();
			isMultiIPs &= isIP(token);
		}
		return isMultiIPs;
	}
	/**
	 * 是否http url，http://开头
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isHttpUrl(String input) {
		if (isEmpty(input))
			return false;
		Pattern pattern = Pattern
				.compile("http://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(:\\d{2,5})?(\\/[^#$]+)*");
		return pattern.matcher(input).matches();
	}
	/**
	 * 是否rtmp url，rtmp://开头
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isRtmpUrl(String input) {
		if (isEmpty(input)) {
			return false;
		}
		input = removeLastSlashChar(input);
		Pattern pattern = Pattern
				.compile("rtmp://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(:\\d{2,5})?(\\/[^#$]+)*");
		return pattern.matcher(input).matches();
	}
	
	/**
	 * 是否qq号码
	 * @param input
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isQQ(String input) {
		if (isEmpty(input))
			return false;
		Pattern pattern = Pattern.compile("^[1-9]\\d{4,8}$");
		return pattern.matcher(input).matches();
	}
	/**
	 * 根据正则替换字符串
	 * @param source
	 * @param findRegex
	 * @param replaceRegex
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String regexReplace(String source, String findRegex,
			String replaceRegex) {
		return Pattern.compile(findRegex).matcher(source)
				.replaceAll(replaceRegex);
	}
	/**
	 * 根据正则查找字符串
	 * @param source
	 * @param findRegex
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean regexFind(String source, String findRegex) {
		return Pattern.compile(findRegex).matcher(source).find();
	}
	/**
	 * 根据正则获取字符串中的匹配内容
	 * @param source
	 * @param findRegex
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String regexGet(String source, String findRegex) {
		Matcher matcher = Pattern.compile(findRegex).matcher(source);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}
	
	
	/**
	 * 字符串不为 空或“”判断
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	/**
	 * 字符串数组拼接
	 * @param firstArray
	 * @param secondArray
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String[] mergeArrary(String[] firstArray, String[] secondArray) {
		if ((isStringArrayEmpty(firstArray))
				&& (isStringArrayEmpty(secondArray)))
			return null;
		if ((isStringArrayEmpty(firstArray)) && (secondArray != null))
			return secondArray;
		if ((firstArray != null) && (isStringArrayEmpty(secondArray))) {
			return firstArray;
		}
		int firstLength = firstArray.length;
		int secondLength = secondArray.length;
		int resultLength = firstLength + secondLength;
		String[] resultArray = new String[resultLength];
		for (int i = 0; i < firstLength; i++) {
			resultArray[i] = firstArray[i];
		}
		for (int j = 0; j < secondLength; j++) {
			resultArray[(firstLength + j)] = secondArray[j];
		}
		return resultArray;
	}
	/**
	 * 是否空字符串数组
	 * @param strArray
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean isStringArrayEmpty(String[] strArray) {
		return (strArray == null) || (strArray.length == 0);
	}
	/**
	 * 忽略前后空格，及大小写判断字符是否相等
	 * @param one
	 * @param another
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static boolean equalIngoreCaseAndSpace(String one, String another) {
		if (one == null) {
			return another == null;
		}
		if (another == null) {
			return false;
		}
		return one.trim().equalsIgnoreCase(another.trim());
	}
	/**
	 * 去除字符串中的所有空格
	 * @param str
	 * @return
	 * @version
	 * 	2014-12-9		chenchen	create
	 */
	public static String trimAllSpace(String str) {
		return str == null ? str : str.replaceAll(
				"^[\\s\u3000]*|[\\s\u3000]*$", "");
	}

	/**
	 * 日期格式化
	 * @param date 日期
	 * @param format 表达式
	 * @return
	 */
	public static String toDateString(Date date, String format) {
		if (date != null) {
			return new SimpleDateFormat(format).format(date);
		} else {
			return null;
		}
	}

	/**
	 * 日期格式化
	 * @param date 日期
	 * @param format 表达式
	 * @return
	 */
	public static String toDateString(Timestamp date, String format) {
		if (date != null) {
			return new SimpleDateFormat(format).format(date);
		} else {
			return null;
		}
	}
	
	/**
	 * 日期格式的字符串进行格式化
	 * @param date 
	 * @param format
	 * @return
	 * @version 
	 * 	 2015-1-12	倪佳琦  创建
	 */
	public static String toStringString(String date, String format) {
		try {
			if (date != null) {
					return new SimpleDateFormat(format).format(dateFormat.parse(date));
			} else {
				return null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期格式化（yyyy-MM-dd）
	 * @param date 日期
	 * @return
	 */
	public static Date toDate(Date date) throws ParseException {
		if (date != null) {
			return dateFormat.parse(dateFormat.format(date));
		} else {
			return null;
		}
	}

	/**
	 * 字符串转日期
	 * @param date(yyyy-MM-dd)
	 * @return
	 * @throws ParseException
	 */
	public static Date toDate(String date) throws ParseException {
		if (date != null && date.length() > 0) {
			return dateFormat.parse(date);
		} else {
			return null;
		}
	}

	/**
	 * 返回开始日期，当前日期后面增加00:00:00.0
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date toStartDate(Date date) throws ParseException {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();
		} else {
			return null;
		}
	}

	/**
	 * 返回开始日期，当前日期后面增加00:00:00.0
	 * @param date(yyyy-MM-dd)
	 * @return
	 * @throws ParseException
	 */
	public static Date toStartDate(String date) throws ParseException {
		if (date != null && date.length() > 0) {
			return toStartDate(dateFormat.parse(date));
		} else {
			return null;
		}
	}

	/**
	 * 返回结束日期，当前日期后面增加23:59:59.999
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date toEndDate(Date date) throws ParseException {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			return calendar.getTime();
		} else {
			return null;
		}
	}

	/**
	 * 返回结束日期，当前日期后面增加23:59:59.999
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date toEndDate(String date) throws ParseException {
		if (date != null && date.length() > 0) {
			return toEndDate(dateFormat.parse(date));
		} else {
			return null;
		}
	}

	/**
	 * 返回当月第一天或当前日期（yyyy-MM-dd 00:00:00.000）<br />
	 * start==null && end==null ,返回当月第一天 <br />
	 * start==null && end!=null,返回end月的第一天<br />
	 * start!=null,返回startDate
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 开始日期
	 * @throws ParseException
	 */
	public static Date isNullToMonthFirstDay(Date start, Date end)
			throws ParseException {
		if (start == null) {
			if (end == null) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				return calendar.getTime();
			} else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(end);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				return calendar.getTime();
			}
		} else {
			return toStartDate(start);
		}

	}

	/**
	 * 返回当月第一天或当前日期（yyyy-MM-dd 00:00:00.000）<br />
	 * start==null && end==null ,返回当月第一天<br />
	 * start==null && end!=null,返回end月的第一天<br />
	 * start!=null,返回startDate
	 * @param start
     * @param end
	 * @return
	 * @throws ParseException
	 */
	public static Date isNullToMonthFirstDay(String start, String end)
			throws ParseException {
		return isNullToMonthFirstDay(toDate(start), toDate(end));
	}

	/**
	 * 返回当月最后一天或当前日期（yyyy-MM-dd 23:59:59.999）<br />
	 * end==null && start==null ,返回当月最后一天<br />
	 * end==null && start!=null,返回start月的最后一天<br />
	 * end!=null,返回endDate
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static Date isNullToMonthLastDay(Date start, Date end)
			throws ParseException {
		if (end == null) {
			if (start == null) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.MONTH, 1);
				calendar.add(Calendar.DATE, -1);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				calendar.set(Calendar.MILLISECOND, 999);
				return calendar.getTime();
			} else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(start);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.MONTH, 1);
				calendar.add(Calendar.DATE, -1);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				calendar.set(Calendar.MILLISECOND, 999);
				return calendar.getTime();
			}
		} else {
			return toEndDate(end);
		}
	}

	/**
	 * 返回当月最后一天或当前日期（yyyy-MM-dd 23:59:59.999）<br />
	 * end==null && start==null ,返回当月最后一天<br />
	 * end==null && start!=null,返回start月的最后一天<br />
	 * end!=null,返回endDate
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static Date isNullToMonthLastDay(String start, String end)
			throws ParseException {
		return isNullToMonthLastDay(toDate(start), toDate(end));
	}

	/**
	 * 返回当月最后一天或当前日期（yyyy-MM-dd 23:59:59.999）<br />
	 * end==null && start==null ,返回当月最后一天<br />
	 * end==null && start!=null,返回start月的最后一天<br />
	 * end!=null,返回endDate
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static Date isNullToMonthLastDay(Date start, String end)
			throws ParseException {
		return isNullToMonthLastDay(start, toDate(end));
	}

	/**
	 * 验证URL链接是否有效
	 * @param link 链接
	 * @author Li.chen
	 * @return
	 */
	public static boolean UrlIsValid(String link){
		try {
			URL url = new URL(link);
			InputStream in = url.openStream();
			return true;
		} catch (Exception e1) {
			return false;
		}
	}
}