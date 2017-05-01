package mydebts.android.app.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

@Entity
public class Participant {
    @Id private Long id;

    private Long personId;
    @ToOne private Person person;

    private Long eventId;
    @ToOne private Event event;

    private double debt;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1537769566)
    private transient ParticipantDao myDao;

    @Generated(hash = 630828827)
    public Participant(Long id, Long personId, Long eventId, double debt) {
        this.id = id;
        this.personId = personId;
        this.eventId = eventId;
        this.debt = debt;
    }

    @Generated(hash = 1200154759)
    public Participant() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return this.personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public double getDebt() {
        return this.debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    @Generated(hash = 1689085377)
    private transient boolean person__refreshed;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1413817282)
    public Person getPerson() {
        if (person != null || !person__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PersonDao targetDao = daoSession.getPersonDao();
            targetDao.refresh(person);
            person__refreshed = true;
        }
        return person;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 1134129394)
    public Person peakPerson() {
        return person;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1432851435)
    public void setPerson(Person person) {
        synchronized (this) {
            this.person = person;
            person__refreshed = true;
        }
    }

    @Generated(hash = 1320491280)
    private transient boolean event__refreshed;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1616166669)
    public Event getEvent() {
        if (event != null || !event__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EventDao targetDao = daoSession.getEventDao();
            targetDao.refresh(event);
            event__refreshed = true;
        }
        return event;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 1249143670)
    public Event peakEvent() {
        return event;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 516084365)
    public void setEvent(Event event) {
        synchronized (this) {
            this.event = event;
            event__refreshed = true;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1996592993)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getParticipantDao() : null;
    }
}
