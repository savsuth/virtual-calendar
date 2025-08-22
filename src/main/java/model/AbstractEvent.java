package model;

import java.time.LocalDateTime;

/**
 * An abstract base class for events that provides common properties and methods.
 */
public abstract class AbstractEvent implements Event {

  protected LocalDateTime endDateTime;

  protected String subject;
  protected LocalDateTime startDateTime;
  protected String description;
  protected String location;
  protected boolean isPublic;
  protected boolean autoDecline;

  /**
   * Constructs an AbstractEvent with the specified details.
   *
   * @param subject       the subject of the event
   * @param startDateTime the start date and time of the event
   * @param description   a description of the event
   * @param location      the location of the event
   * @param isPublic      true if the event is public, false otherwise
   */
  public AbstractEvent(String subject, LocalDateTime startDateTime,
      String description, String location, boolean isPublic) {
    this.subject = subject;
    this.startDateTime = startDateTime;
    this.description = description;
    this.location = location;
    this.isPublic = isPublic;
    this.autoDecline = false;
  }

  /**
   * Returns the subject of the event.
   *
   * @return the event subject
   */
  @Override
  public String getSubject() {
    return subject;
  }

  /**
   * Updates the subject of the event.
   *
   * @param newSubject the new subject
   */
  public void setSubject(String newSubject) {
    this.subject = newSubject;
  }

  /**
   * Returns the start date and time of the event.
   *
   * @return the start date and time
   */
  @Override
  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  /**
   * Updates the start date and time of the event.
   *
   * @param newStartDateTime the new start date and time
   */
  public void setStartDateTime(LocalDateTime newStartDateTime) {
    this.startDateTime = newStartDateTime;
  }

  /**
   * Returns the description of the event.
   *
   * @return the event description
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Updates the description of the event.
   *
   * @param newDescription the new description
   */
  public void setDescription(String newDescription) {
    this.description = newDescription;
  }

  /**
   * Returns the location of the event.
   *
   * @return the event location
   */
  @Override
  public String getLocation() {
    return location;
  }

  /**
   * Updates the location of the event.
   *
   * @param newLocation the new location
   */
  public void setLocation(String newLocation) {
    this.location = newLocation;
  }

  /**
   * Indicates whether the event is public.
   *
   * @return true if the event is public, false otherwise
   */
  @Override
  public boolean isPublic() {
    return isPublic;
  }

  /**
   * Sets whether the event is public.
   *
   * @param newPublic true if the event should be public, false otherwise
   */
  public void setPublic(boolean newPublic) {
    this.isPublic = newPublic;
  }

  @Override
  public boolean isAutoDecline() {
    return autoDecline;
  }

  /**
   * Sets the {@code autoDecline} parameter.
   * @param autoDecline boolean autoDecline
   */
  public void setAutoDecline(boolean autoDecline) {
    this.autoDecline = autoDecline;
  }

  @Override
  public LocalDateTime getEffectiveEndDateTime() {
    if (endDateTime == null) {
      return startDateTime.toLocalDate().atTime(23, 59);
    }
    return endDateTime;
  }

  /**
   * Sets a new EndDateTime in DateTime format.
   *
   * @param newEndDateTime represents end date.
   * @throws InvalidDateException if end date is before start date.
   */
  public void setEndDateTime(LocalDateTime newEndDateTime) throws InvalidDateException {
    if (newEndDateTime != null && newEndDateTime.isBefore(this.startDateTime)) {
      throw new InvalidDateException("End date & time must be after start date & time.");
    }
    this.endDateTime = newEndDateTime;
  }
}
