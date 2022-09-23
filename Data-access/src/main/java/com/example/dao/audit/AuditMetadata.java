package com.example.dao.audit;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Getter
@Setter
public class AuditMetadata {

    @CreatedBy
    public String createdBy;

    @CreatedDate
    public Date createdDate;
}
