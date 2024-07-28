package test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestParser {

    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
        String line = reader.readLine();

        if (line == null) {
            throw new IOException("The request is empty");
        }

        String[] requestParts = line.split(" ");
        String httpCommand = requestParts[0];

        String uri = requestParts[1];

        ArrayList<String> adr = new ArrayList<>();
        for (String split : uri.split("/")) {
            if (!Objects.equals(split, "")) {
                if (split.contains("?")) {
                    adr.add(split.split("\\?")[0]);
                }
                else {
                    adr.add(split);
                }
            }
        }

        String[] uriSegments = new String[adr.size()];
        uriSegments = adr.toArray(uriSegments);

        Map<String, String> parameters = new HashMap<>();

        if (uri.contains("?")) {
            String[] uriParts = uri.split("\\?");
            String[] params = uriParts[1].split("&");
            for (String param : params) {
                String[] paramParts = param.split("=");
                parameters.put(paramParts[0], paramParts[1]);
            }
        }

        line = "";

        Map<String, String> headers = new HashMap<>();
        int contentLen = 0;
        while(!(line = reader.readLine()).equals("")) {
            String[] headerParts = line.split(": ");
            if(headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
                if (headerParts[0].equalsIgnoreCase("content-Length")) {
                    contentLen = Integer.parseInt(headerParts[1]);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        if (contentLen > 0) {
            while(!(line = reader.readLine()).equals("")) {
                if (line.contains("filename=")) {
                    parameters.put("filename", line.split("filename=")[1]);
                }
            }

            while (!(line = reader.readLine()).equals("")) {
                sb.append(line).append("\n");
            }
            while (reader.ready()) {
                line = reader.readLine();
            }
        }

        return new RequestInfo(httpCommand, uri, uriSegments, parameters, sb.toString().getBytes());

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
