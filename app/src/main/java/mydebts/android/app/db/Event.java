package mydebts.android.app.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Event {
    @Id private long id;
    private String name;
    private Date date;

    @Generated(hash = 1394579662)
    public Event(long id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    @Generated(hash = 344677835)
    public Event() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
