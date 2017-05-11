package mydebts.android.app.data.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "PARTICIPANTS")
public class ParticipantsTable {
    @Id private Long id;

    private Long personId;
    @ToOne(joinProperty = "personId") private PersonsTable person;

    private Long eventId;
    @ToOne(joinProperty = "eventId") private EventsTable event;

    private double debt;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 279985025)
    private transient ParticipantsTableDao myDao;



    @Generated(hash = 473971503)
    public ParticipantsTable(Long id, Long personId, Long eventId, double debt) {
        this.id = id;
        this.personId = personId;
        this.eventId = eventId;
        this.debt = debt;
    }



    @Generated(hash = 2066322617)
    public ParticipantsTable() {
    }



    @Generated(hash = 1154009267)
    private transient Long person__resolvedKey;

    @Generated(hash = 520039006)
    private transient Long event__resolvedKey;



    @Keep
    public PersonsTable peekPerson() {
        return person;
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



    /** To-one relationship, resolved on first access. */
    @Generated(hash = 791311809)
    public PersonsTable getPerson() {
        Long __key = this.personId;
        if (person__resolvedKey == null || !person__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PersonsTableDao targetDao = daoSession.getPersonsTableDao();
            PersonsTable personNew = targetDao.load(__key);
            synchronized (this) {
                person = personNew;
                person__resolvedKey = __key;
            }
        }
        return person;
    }



    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1530064136)
    public void setPerson(PersonsTable person) {
        synchronized (this) {
            this.person = person;
            personId = person == null ? null : person.getId();
            person__resolvedKey = personId;
        }
    }



    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1836273066)
    public EventsTable getEvent() {
        Long __key = this.eventId;
        if (event__resolvedKey == null || !event__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EventsTableDao targetDao = daoSession.getEventsTableDao();
            EventsTable eventNew = targetDao.load(__key);
            synchronized (this) {
                event = eventNew;
                event__resolvedKey = __key;
            }
        }
        return event;
    }



    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 700771234)
    public void setEvent(EventsTable event) {
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
    @Generated(hash = 1197250900)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getParticipantsTableDao() : null;
    }

    
}
