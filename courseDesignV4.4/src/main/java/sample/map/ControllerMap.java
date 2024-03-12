package sample.map;

import sample.controller.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过Controller名字可以在全局中得到Controller
 * 单例
 * @author 12242
 * @date 2021/05/14
 */
public class ControllerMap {
    private static Map<String, Object> ctrlMap = new HashMap<>();

    public static Map<String, Object> getCtrlMap() {
        return ctrlMap;
    }

    public static void put(Object controller) {
        ctrlMap.put(controller.getClass().getSimpleName(), controller);
    }

    public static Object get(String name) {
        return ctrlMap.get(name);
    }
}
