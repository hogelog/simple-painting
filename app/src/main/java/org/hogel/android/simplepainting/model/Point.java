package org.hogel.android.simplepainting.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class Point implements Serializable {
    public float x;
    public float y;
}
