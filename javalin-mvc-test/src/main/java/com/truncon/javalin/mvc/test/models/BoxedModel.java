package com.truncon.javalin.mvc.test.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;
import java.util.UUID;

public final class BoxedModel {
    private String stringValue;
    private Boolean booleanValue;
    private Integer integerValue;
    private Double doubleValue;
    private Byte byteValue;
    private Short shortValue;
    private Float floatValue;
    private Character characterValue;
    private Long longValue;
    private Date dateValue;
    private Instant instantValue;
    private ZonedDateTime zonedDateTimeValue;
    private OffsetDateTime offsetDateTimeValue;
    private LocalDateTime localDateTimeValue;
    private LocalDate localDateValue;
    private YearMonth yearMonth;
    private Year year;
    private BigInteger bigIntegerValue;
    private BigDecimal bigDecimalValue;
    private UUID uuidValue;

    public String getString() {
        return stringValue;
    }

    public void setString(String stringValue) {
        this.stringValue = stringValue;
    }

    public Boolean getBoolean() {
        return booleanValue;
    }

    public void setBoolean(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Integer getInteger() {
        return integerValue;
    }

    public void setInteger(Integer integerValue) {
        this.integerValue = integerValue;
    }

    public Double getDouble() {
        return doubleValue;
    }

    public void setDouble(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Byte getByte() {
        return byteValue;
    }

    public void setByte(Byte byteValue) {
        this.byteValue = byteValue;
    }

    public Short getShort() {
        return shortValue;
    }

    public void setShort(Short shortValue) {
        this.shortValue = shortValue;
    }

    public Float getFloat() {
        return floatValue;
    }

    public void setFloat(Float floatValue) {
        this.floatValue = floatValue;
    }

    public Character getCharacter() {
        return characterValue;
    }

    public void setCharacter(Character characterValue) {
        this.characterValue = characterValue;
    }

    public Long getLong() {
        return longValue;
    }

    public void setLong(Long longValue) {
        this.longValue = longValue;
    }

    public Date getDate() {
        return dateValue;
    }

    public void setDate(Date dateValue) {
        this.dateValue = dateValue;
    }

    public Instant getInstant() {
        return instantValue;
    }

    public void setInstant(Instant instantValue) {
        this.instantValue = instantValue;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTimeValue;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTimeValue) {
        this.zonedDateTimeValue = zonedDateTimeValue;
    }

    public OffsetDateTime getOffsetDateTime() {
        return offsetDateTimeValue;
    }

    public void setOffsetDateTime(OffsetDateTime offsetDateTimeValue) {
        this.offsetDateTimeValue = offsetDateTimeValue;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTimeValue;
    }

    public void setLocalDateTime(LocalDateTime localDateTimeValue) {
        this.localDateTimeValue = localDateTimeValue;
    }

    public LocalDate getLocalDate() {
        return localDateValue;
    }

    public void setLocalDate(LocalDate localDateValue) {
        this.localDateValue = localDateValue;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(YearMonth value) {
        this.yearMonth = value;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public BigInteger getBigInteger() {
        return bigIntegerValue;
    }

    public void setBigInteger(BigInteger bigIntegerValue) {
        this.bigIntegerValue = bigIntegerValue;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimalValue;
    }

    public void setBigDecimal(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }

    public UUID getUuid() {
        return uuidValue;
    }

    public void setUuid(UUID uuidValue) {
        this.uuidValue = uuidValue;
    }
}
