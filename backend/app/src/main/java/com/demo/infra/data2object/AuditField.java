package com.demo.infra.data2object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Jacksonized
@SuperBuilder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class AuditField implements Serializable {
    @Serial
    private static final long serialVersionUID = 114514L;

    @Column(isLogicDelete = true, onInsertValue = "0")
    @JsonIgnore
    private Boolean isDeleted;

    @Column(onInsertValue = "now()")
    private Date createdTime;

    private String creator;
}
