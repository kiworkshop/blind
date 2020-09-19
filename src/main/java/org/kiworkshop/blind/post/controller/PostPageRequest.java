package org.kiworkshop.blind.post.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostPageRequest {
    private int page;
    private int size;
}
