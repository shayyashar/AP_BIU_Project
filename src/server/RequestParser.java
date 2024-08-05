package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {

        String initialLine = reader.readLine();
        if (initialLine == null) {
            throw new IOException("Empty request");
        }

        // extract the command ('GET', 'POST') + the uri
        String[] initialParts = initialLine.split(" ");
        String httpCommanmd = initialParts[0];
        String uri = initialParts[1];

        // get the uri segments
        String[] fullUriParts = uri.split("\\?")[0].split("/");
        String[] uriParts = new String[fullUriParts.length - 1];

        // the first cell is "" so drop it
        int index = 0;
        for (String str : fullUriParts) {
            if (!str.isEmpty()) {
                uriParts[index++] = str;
            }
        }

        // get the uri params
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

        // extract the Content-Length
        String line;
        int contentLength = 0;
        while (!(line = reader.readLine()).equals("")) {
            if (line.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(line.split(": ")[1]);
            }
        }

        // content part
        StringBuilder content = new StringBuilder();
        if (contentLength > 0) {
            // extract the filename
            while (!(line = reader.readLine()).equals("")) {
                if (line.contains("filename=")) {
                    String[] keyValue = line.split("filename=");
                    queryParams.put("filename", keyValue[1]);
                }
            }

            // extarct the content
            while (!(line = reader.readLine()).equals("") && !line.startsWith("------WebKitFormBoundary")) {
                content.append(line + "\n");
            }

            // tails of the request
            while (reader.ready()) {
                line = reader.readLine();
            }
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