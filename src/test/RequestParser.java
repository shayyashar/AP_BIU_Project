package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
        String initialLine = reader.readLine();
        if (initialLine == null) {
            throw new IOException("Empty request");
        }

        String[] initialParts = initialLine.split(" ");
        String httpCommanmd = initialParts[0];
        String uri = initialParts[1];

        String[] fullUriParts = uri.split("\\?")[0].split("/");
        String[] uriParts = new String[fullUriParts.length - 1];

        int index = 0;
        for (String str : fullUriParts) {
            if (!str.isEmpty()) {
                uriParts[index++] = str;
            }
        }

        Map<String, String> queryParams = new HashMap<>();
        if (uri.contains("?")) {
            String uriParams = uri.split("\\?")[1];
            if (!uriParams.equals("")) {
                String[] params = uriParams.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }

        String line;
        int contentLength = 0;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(line.split(": ")[1]);
            }
        }


        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] keyValue = line.split("=");
            queryParams.put(keyValue[0], keyValue[1]);
        }

        StringBuilder content = new StringBuilder();
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            content.append(line + '\n');
        }



        return new RequestInfo(httpCommanmd, uri, uriParts, queryParams, content.toString().getBytes("UTF-8"));
    }

    // RequestInfo given internal class
    public static class RequestInfo {
        private final String httpCommand;
        private final String uri;
        private final String[] uriSegments;
        private final Map<String, String> parameters;
        private final byte[] content;

        public RequestInfo(String httpCommand, String uri, String[] uriSegments, Map<String, String> parameters, byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriSegments = uriSegments;
            this.parameters = parameters;
            this.content = content;
        }

        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriSegments;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public byte[] getContent() {
            return content;
        }
    }
}