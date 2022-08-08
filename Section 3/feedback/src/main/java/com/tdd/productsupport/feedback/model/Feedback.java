package com.tdd.productsupport.feedback.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Feedback")
@Getter
@Setter
@NoArgsConstructor
public class Feedback {

    private String id;

    private Integer productId;

    private Integer userId;

    private String status;

    private Integer version = 1;

    private String message;

    public Feedback(String id, Integer productId, Integer userId, String status, String message) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.status = status;
        this.message = message;
    }

    public Feedback(String id, Integer productId, Integer userId, String status, Integer version, String message) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.status = status;
        this.version = version;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id='" + id + '\'' +
                ", productId=" + productId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", version=" + version +
                ", message='" + message + '\'' +
                '}';
    }
}
