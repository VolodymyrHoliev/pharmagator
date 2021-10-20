package com.eleks.academy.pharmagator.converters.request;

public interface RequestToEntityConverter <T,R>{
    R toEntity(T requestObject);
}
