package org.bitpioneers.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitpioneers.data.BranchesInfo;
import org.bitpioneers.data.DepartmentInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentService {

    private final String vtbDepartsUrl = "https://headless-cms3.vtb.ru/projects/atm/models/default/items/departments";

    private final ObjectMapper objectMapper;

    public List<DepartmentInfo> load(){
        log.info("Loading departments");
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(vtbDepartsUrl))
                    .build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.debug(httpResponse.body());
            return objectMapper.readValue(httpResponse.body(), BranchesInfo.class).getBranches();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
