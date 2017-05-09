package mydebts.android.app.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class Participant {
    @Id private Long id;

    private Long personId;
    @ToOne(joinProperty = "personId") private Person person;

    private Long eventId;
    @ToOne(joinProperty = "eventId") private Event event;

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

    @Keep
    public Person peekPerson() {
        return person;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1752520167)
    public Person getPerson() {
        Long __key = this.personId;
        if (person__resolvedKey == null || !person__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PersonDao targetDao = daoSession.getPersonDao();
            Person personNew = targetDao.load(__key);
            synchronized (this) {
                person = personNew;
                person__resolvedKey = __key;
            }
        }
        return person;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1834876435)
    public void setPerson(Person person) {
        synchronized (this) {
            this.person = person;
            personId = person == null ? null : person.getId();
            person__resolvedKey = personId;
        }
    }

    @Generated(hash = 1154009267)
    private transient Long person__resolvedKey;

    @Generated(hash = 520039006)
    private transient Long event__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1904304788)
    public Event getEvent() {
        Long __key = this.eventId;
        if (event__resolvedKey == null || !event__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EventDao targetDao = daoSession.getEventDao();
            Event eventNew = targetDao.load(__key);
            synchronized (this) {
                event = eventNew;
                event__resolvedKey = __key;
            }
        }
        return event;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 12102314)
    public void setEvent(Event event) {
        synchronized (this) {
            this.event = event;
            eventId = event == null ? null : event.getId();
            event__resolvedKey = eventId;
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
