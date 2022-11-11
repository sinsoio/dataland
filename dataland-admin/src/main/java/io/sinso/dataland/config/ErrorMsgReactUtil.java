package io.sinso.dataland.config;


/**
 * @author alibeibei
 */
public class ErrorMsgReactUtil {

    private static ThreadLocal<Boolean> clientTypeLocal = new ThreadLocal();

    /**
     * 获取-react
     *
     * @return
     */
    public static Boolean getReact() {
        Boolean react = clientTypeLocal.get();
        return react;
    }

    /**
     * setReact
     */
    public static void setReact() {
        clientTypeLocal.set(true);
    }

}
