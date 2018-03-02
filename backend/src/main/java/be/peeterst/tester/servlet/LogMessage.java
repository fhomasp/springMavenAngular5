package be.peeterst.tester.servlet;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 2/03/2018
 * Time: 15:05
 */
public class LogMessage {
    private int httpStatus;
    private String httpMethod;
    private String path;
    private String clientIp;
    private String javaMethod;
    private String response;

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setJavaMethod(String javaMethod) {
        this.javaMethod = javaMethod;
    }

    public String getJavaMethod() {
        return javaMethod;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "httpStatus=" + httpStatus +
                ", httpMethod='" + httpMethod + '\'' +
                ", path='" + path + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", javaMethod='" + javaMethod + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
