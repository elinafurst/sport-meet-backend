package se.elfu.sportprojectbackend.controller.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@Builder
public class Param {
    private int page;
    private int size;
    private String fromDate;
    private String toDate;
    private String type;
    private String city;
    private String area;
    private boolean active;

    public PageRequest getEventPageRequest(){
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "eventStart"));
    }

    public PageRequest getUnitPageRequest(){
        return PageRequest.of(page, size);
    }

    public PageRequest getCommentPageRequest(){
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "timeStamp"));
    }

    public Pageable getRequestPageRequest() {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "messages.timeStamp"));

    }
}
