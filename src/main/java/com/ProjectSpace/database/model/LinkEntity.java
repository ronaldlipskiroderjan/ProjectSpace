package com.ProjectSpace.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkEntity {

    @Id
    private UUID linkId;

    private UUID projetoId;

    private String url;
}
