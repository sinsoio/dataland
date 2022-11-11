package io.sinso.dataland.util;


/**
 * @author alibeibei
 */
public class UserUtil {

    private static ThreadLocal<Integer> accountLocal = new ThreadLocal();

    /**
     * 获取-uid
     *
     * @return
     */
    public static Integer getUid() {
        Object o = accountLocal.get();
        if (o != null) {
            return (Integer) o;
        }
        return null;
    }


    /**
     * 设置uid
     *
     * @param uid
     */
    public static void setUid(Integer uid) {
        accountLocal.set(uid);
    }

    /**
     * 移除-uid
     */
    public static void removeUid() {
        accountLocal.remove();
    }
}
