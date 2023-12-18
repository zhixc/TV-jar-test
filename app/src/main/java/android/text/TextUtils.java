package android.text;

import java.util.Iterator;

public class TextUtils {
    public static String join(CharSequence delimiter, Iterable tokens) {
        final Iterator<?> it = tokens.iterator();
        if (!it.hasNext()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(it.next());
        while (it.hasNext()) {
            sb.append(delimiter);
            sb.append(it.next());
        }
        return sb.toString();
    }

    public static boolean isEmpty(String s) {
        if (s == null || s.length() == 0){
            return true;
        }
        return false;
    }
}