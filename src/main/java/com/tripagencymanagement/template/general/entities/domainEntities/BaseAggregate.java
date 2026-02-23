package com.tripagencymanagement.template.general.entities.domainEntities;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseAggregate { //Use as an aggregation dependency
    private List<Object> domainEvents;
}
