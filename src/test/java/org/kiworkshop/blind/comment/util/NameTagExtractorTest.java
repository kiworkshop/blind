package org.kiworkshop.blind.comment.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NameTagExtractorTest {

    @Test
    public void findTagsTest() {
        //Given
        String content = "@existUser @existUser 내용 @nonExistUser 내용 @";

        //When
        List<String> tags = NameTagExtractor.extractNameTags(content);

        //Then
        assertThat(tags).hasSize(4);
    }
}