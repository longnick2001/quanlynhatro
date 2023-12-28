package longpdph30373.poly.qunlnhtr.models;

public class NotificationData {
    private String status;
    private String name;
    private Float similarity;
    private Long timestamp;

    // Constructors, getters và setters
    public NotificationData() {
    }

    public NotificationData(String status, String name, Float similarity, Long timestamp) {
        this.status = status;
        this.name = name;
        this.similarity = similarity;
        this.timestamp = timestamp;
    }

    // Getters và setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Float similarity) {
        this.similarity = similarity;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

