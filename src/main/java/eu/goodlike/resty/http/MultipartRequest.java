package eu.goodlike.resty.http;

import eu.goodlike.neat.Null;
import eu.goodlike.neat.string.Str;
import eu.goodlike.resty.misc.Param;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import static eu.goodlike.misc.Constants.DEFAULT_CHARSET;

/**
 * <pre>
 * multipart/form-data version of HttpRequest
 *
 * Should be created by HttpRequest if and when needed
 *
 * Objects of this class are immutable, unless the collections/maps passed to it are modified externally
 * </pre>
 */
public final class MultipartRequest {

    /**
     * Connecting and writing to the connection are performed by this method
     * @return HttpResponse of this MultipartRequest
     * @throws IOException if connection or writing fails
     */
    public HttpResponse perform() throws IOException {
        HttpURLConnection request = (HttpURLConnection) (new URL(url).openConnection());
        request.setUseCaches(false);
        request.setDoOutput(true);
        for (Map.Entry<String, String> header : headers.entrySet())
            request.addRequestProperty(header.getKey(), header.getValue());

        OutputStream outputStream = request.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, DEFAULT_CHARSET), true);
        multipartParams.stream().filter(Param::hasValue)
                .forEach(param -> writeNextParam(writer, param.name(), param.valueNullable()));
        multipartFiles.entrySet()
                .forEach(entry -> writeNextFile(writer, outputStream, entry.getKey(), entry.getValue()));
        return HttpResponse.from(request);
    }

    // CONSTRUCTORS

    /**
     * <pre>
     * In general, only HttpRequest should use this constructor
     *
     * If you choose to use it, take care not to modify the given collections/maps
     * </pre>
     */
    public MultipartRequest(HttpMethod httpMethod, String url, Map<String, String> headers,
                            String boundary, Set<Param> multipartParams, Map<String, File> multipartFiles) {
        Null.check(httpMethod, url, headers, boundary, multipartParams, multipartFiles)
                .ifAny("HttpMethod, url, headers, boundary, params and files cannot be null");
        this.httpMethod = httpMethod;
        this.url = url;
        this.headers = headers;
        this.boundary = boundary;
        this.multipartParams = multipartParams;
        this.multipartFiles = multipartFiles;
    }

    // PRIVATE

    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> headers;
    private final String boundary;
    private final Set<Param> multipartParams;
    private final Map<String, File> multipartFiles;

    /**
     * Prints a value of a form field, kind of like a param
     */
    private void writeNextParam(PrintWriter writer, String paramName, String paramValue) {
        writer.append("--").append(boundary).append(LINE_FEED)
                .append("Content-Disposition: form-data; name=\"").append(paramName).append("\"").append(LINE_FEED)
                .append("Content-Type: text/plain; charset=").append(DEFAULT_CHARSET).append(LINE_FEED)
                .append(LINE_FEED)
                .append(paramValue).append(LINE_FEED)
                .flush();
    }

    /**
     * Prints a file as a form field
     * @throws IllegalArgumentException if IOException was throw when file was attempted to read
     */
    private void writeNextFile(PrintWriter writer, OutputStream outputStream, String fieldName, File file) {
        String fileName = file.getName();
        writer.append("--").append(boundary).append(LINE_FEED)
                .append("Content-Disposition: form-data; name=\"").append(fieldName)
                .append("\"; filename=\"").append(fileName).append("\"").append(LINE_FEED)
                .append("Content-Type: ").append(URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED)
                .append("Content-Transfer-Encoding: binary").append(LINE_FEED)
                .append(LINE_FEED)
                .flush();

        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, bytesRead);

            outputStream.flush();
        } catch (IOException e) {
            throw new IllegalArgumentException("File could not be read");
        }
        writer.append(LINE_FEED).flush();
    }

    private static final String LINE_FEED = "\r\n";
    private static final int BUFFER_SIZE = 4096;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return Str.of(httpMethod, " ", url, "\n")
                .andSome(headers.entrySet(),
                        (builder, header) -> builder.append(header.getKey()).append(": ").append(header.getValue()).append("\n"))
                .andSome("\n", multipartParams)
                .andSome("\n", multipartFiles.keySet())
                .toString();
    }

}
