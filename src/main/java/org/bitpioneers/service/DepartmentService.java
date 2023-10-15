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

/**
 * The DepartmentService class is a critical component of a software system designed to retrieve and manage data
 * related to a bank's departments. It makes HTTP requests to obtain information about the bank's branches
 *
 * @since 1.0
 * @author Mirolim Mirzayev
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentService {

    /**
    * At this url there is a request to VTB bank api to receive all its branches
    * */
    private final String vtbDepartsUrl = "https://headless-cms3.vtb.ru/projects/atm/models/default/items/departments";

    private final ObjectMapper objectMapper;

    /**
    * This method sends a request to the api of vtb bank to receive all its branches
    * and also deserializes them and puts them in a list of DepartmentInfo
    * @return List of DepartmentInfo
     * @see DepartmentInfo
    * */
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
