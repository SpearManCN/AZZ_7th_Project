package com.azz.azz.DOMAIN;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.util.*;
@Data
public class Pictures {
    Posts posts;
    List<String> full_name;
}
