package qs.config.db;

import java.util.Stack;

public class DataSourceContextHolder {
    private static final ThreadLocal<Stack<String>> local = new InheritableThreadLocal<>();

    public static void push(EnumDataSourceName dbName) {
        Stack<String> queue = local.get();
        if (queue == null)
            queue = new Stack<>();
        queue.push(dbName.getName());
        local.set(queue);
    }

    public static void pop() {
        Stack<String> queue = local.get();
        if (queue != null)
            queue.pop();
    }

    public static String get() {
        Stack<String> queue = local.get();
        if (queue == null || queue.empty())
            return null;
        return queue.peek();
    }


}
