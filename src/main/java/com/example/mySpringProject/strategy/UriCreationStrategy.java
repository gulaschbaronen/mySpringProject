package com.example.mySpringProject.strategy;

import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UriCreationStrategy {

    public static final String URI = "https://api.resrobot.se/v2.1/trip?";

    /**
     * create URI
     * @param _originId
     * @param _destId
     * @return
     */
    public String createUri(final String _originId, final String _destId){
        return URI + createQueryParamMapDefault(_originId, _destId).entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));
    }

    public String createUri(final String _originId, final String _destId, LocalTime localTime){
        return URI + createQueryParamMapWithTime(_originId, _destId, localTime.toString()).entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));
    }

    /**
     * Default search - searches without time parameter
     * @param _originId
     * @param _destId
     * @return
     */
    private Map<String,String> createQueryParamMapDefault(final String _originId, final String _destId){
        Map<String,String> queryParameters = new HashMap<String, String>();

        queryParameters.put("format","json");
        queryParameters.put("originId", _originId);
        queryParameters.put("destId", _destId);
        queryParameters.put("passlist","false");
        queryParameters.put("numF","6");
        queryParameters.put("numB","0");
        queryParameters.put("accessId","ce7b1956-d486-4372-b06e-548fd918ac7d");

        return queryParameters;
    }

    /**
     * search from specified time
     * @param _originId
     * @param _destId
     * @param time
     * @return
     */
    private Map<String,String> createQueryParamMapWithTime(final String _originId, final String _destId, final String time){
        Map<String,String> queryParameters = new HashMap<String, String>();

        queryParameters.put("format","json");
        queryParameters.put("originId", _originId);
        queryParameters.put("destId", _destId);
        queryParameters.put("passlist","false");
        queryParameters.put("numF","6");
        queryParameters.put("numB","0");
        queryParameters.put("accessId","ce7b1956-d486-4372-b06e-548fd918ac7d");
        queryParameters.put("time",time);

        return queryParameters;
    }
}
