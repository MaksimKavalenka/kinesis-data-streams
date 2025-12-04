package org.learning.kds.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestObject implements Serializable {

    private static final long serialVersionUID = 3103325936139905351L;

    private String value;

}
