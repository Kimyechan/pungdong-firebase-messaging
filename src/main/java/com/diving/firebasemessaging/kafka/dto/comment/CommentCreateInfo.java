package com.diving.firebasemessaging.kafka.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateInfo {
    private String postWriterId;
    private String postId;
    private String postTitle;
    private String commentWriterNickname;
}
