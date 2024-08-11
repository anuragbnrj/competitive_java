package library;

import java.util.Arrays;

public class TemplateDebug {

    // Should be the only required piece of code
    public static void debug(Object... o){
        System.out.println(Arrays.deepToString(o));
    }
    
    //change to System.getProperty("ONLINE_JUDGE")==null; for CodeForces
    public static final boolean LOCAL = System.getProperty("LOCAL")!=null;
    private static <T> String ts(T t) {
        if(t==null) {
            return "null";
        }
        try {
            return ts((Iterable) t);
        }catch(ClassCastException e) {
            if(t instanceof int[]) {
                String s = Arrays.toString((int[]) t);
                return "{"+s.substring(1, s.length()-1)+"}";
            }else if(t instanceof long[]) {
                String s = Arrays.toString((long[]) t);
                return "{"+s.substring(1, s.length()-1)+"}";
            }else if(t instanceof char[]) {
                String s = Arrays.toString((char[]) t);
                return "{"+s.substring(1, s.length()-1)+"}";
            }else if(t instanceof double[]) {
                String s = Arrays.toString((double[]) t);
                return "{"+s.substring(1, s.length()-1)+"}";
            }else if(t instanceof boolean[]) {
                String s = Arrays.toString((boolean[]) t);
                return "{"+s.substring(1, s.length()-1)+"}";
            }
            try {
                return ts((Object[]) t);
            }catch(ClassCastException e1) {
                return t.toString();
            }
        }
    }
    private static <T> String ts(T[] arr) {
        StringBuilder ret = new StringBuilder();
        ret.append("{");
        boolean first = true;
        for(T t: arr) {
            if(!first) {
                ret.append(", ");
            }
            first = false;
            ret.append(ts(t));
        }
        ret.append("}");
        return ret.toString();
    }
    private static <T> String ts(Iterable<T> iter) {
        StringBuilder ret = new StringBuilder();
        ret.append("{");
        boolean first = true;
        for(T t: iter) {
            if(!first) {
                ret.append(", ");
            }
            first = false;
            ret.append(ts(t));
        }
        ret.append("}");
        return ret.toString();
    }
    public static void dbg(Object... o) {
        if(LOCAL) {
            System.err.print("Line #"+Thread.currentThread().getStackTrace()[2].getLineNumber()+": [");
            for(int i = 0; i<o.length; i++) {
                if(i!=0) {
                    System.err.print(", ");
                }
                System.err.print(ts(o[i]));
            }
            System.err.println("]");
        }
    }

}
