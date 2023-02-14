package nl.han.dea.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ConnectionHandler {
    HtmlPageReader reader = new HtmlPageReader();
    String pagename = "index.html";


    private final String HTTP_HEADERS = "HTTP/1.1 200 OK\n" +
            "Date: " + OffsetDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.RFC_1123_DATE_TIME ) + "\n" +
            "HttpServer: Simple DEA Webserver\n" +
            "Content-Length: " + reader.calcPageContentLength(pagename) + "\n" +
            "Content-Type: text/html\n";

    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        handle();
    }

    public void handle() {
        try {
            var inputStreamReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
            var outputStreamWriter = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));

            parseRequest(inputStreamReader);
            writeResponse(outputStreamWriter);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseRequest(BufferedReader inputStreamReader) throws IOException {
        String request = inputStreamReader.readLine();

        //TODO crash door favicon.ico (deze moet dan worden toegevoegd)
        String temp = request.substring(request.indexOf("/") + 1);
        pagename = temp.substring(0, temp.indexOf(" "));

        while (request != null && !request.isEmpty()) {
            System.out.println(request);
            request = inputStreamReader.readLine();
        }
    }

    private void writeResponse(BufferedWriter outputStreamWriter) {
        try {
            outputStreamWriter.write(HTTP_HEADERS);
            outputStreamWriter.newLine();

            outputStreamWriter.write(
                    reader.readFile(pagename)
            );

            outputStreamWriter.newLine();
            outputStreamWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
