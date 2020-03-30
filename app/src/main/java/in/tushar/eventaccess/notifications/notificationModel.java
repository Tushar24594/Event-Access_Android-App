package in.tushar.eventaccess.notifications;

public class notificationModel {
    private String dateTime;
    private String imageUrl;
    private String notificationHeader;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNotificationHeader() {
        return notificationHeader;
    }

    public void setNotificationHeader(String notificationHeader) {
        this.notificationHeader = notificationHeader;
    }

    public String getNotificationDes() {
        return notificationDes;
    }

    public void setNotificationDes(String notificationDes) {
        this.notificationDes = notificationDes;
    }



    private String notificationDes;
}
