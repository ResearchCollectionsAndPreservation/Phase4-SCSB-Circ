package org.recap.las.model;

import lombok.Data;

import java.util.List;

/**
 * Created by sudhishk on 27/1/17.
 */
@Data
public class RetrieveItemRequest {
    private List<TtitemRequest> ttitem;
}
