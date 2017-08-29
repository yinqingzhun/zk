package qs;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
public class ReturnValue<T> {
    private int code = 0;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static <T> ReturnValue<T> buildSuccessResult(T result) {
        ReturnValue<T> returnValue = new ReturnValue<>();
        returnValue.setCode(0);
        returnValue.setResult(result);
        return returnValue;
    }

    public static <T> ReturnValue<T> buildErrorResult(int code, String message) {
        ReturnValue<T> returnValue = new ReturnValue<>();
        returnValue.setCode(code);
        returnValue.setMessage(message);
        return returnValue;
    }
}

