package se.elfu.sportprojectbackend.utils;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import se.elfu.sportprojectbackend.controller.model.EventDto;
import se.elfu.sportprojectbackend.repository.model.Event;
import se.elfu.sportprojectbackend.repository.model.Unit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HttpHeadersMapper {

    public static Map<String, Object> mapEventsWithHeaders(Page<Event> page, List<EventDto> events){
        Map<String, Object> map = new HashMap<>();
        map.put("headers", getHeaders(page));
        map.put("events", events);
        return map;
    }

    private static HttpHeaders getHeaders(Page<Event> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("pages", String.valueOf(page.getTotalPages()));
        headers.set("elements", String.valueOf(page.getTotalElements()));
        return headers;
    }
}
