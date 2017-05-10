package mydebts.android.app.data.model;

import android.support.annotation.NonNull;

import java.util.Date;

public class Event {
    private Long id;
    private String name;
    private Date date;

    public Event(Long id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(@NonNull Event source) {
        return builder()
                .id(source.getId())
                .name(source.getName())
                .date(source.getDate());
    }

    public static class Builder {
        private Long id;
        private String name;
        private Date date;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        Event build() {
            return new Event(id, name, date);
        }
    }
}
