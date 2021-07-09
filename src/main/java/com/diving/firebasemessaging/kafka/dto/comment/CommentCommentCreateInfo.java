package com.diving.firebasemessaging.kafka.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCommentCreateInfo {
    private String postWriterId;
    private String commentWriterId;
    private String postId;
    private String commentCommentWriterNickname;
}
