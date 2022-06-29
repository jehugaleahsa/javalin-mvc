package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.Named;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class CollectionModel {
    private Iterable<String> stringValues;
    private Collection<Boolean> booleanValues;
    private List<Integer> integerValues;
    private Set<Double> doubleValues;
    private ArrayList<Byte> byteValues;
    private LinkedList<Short> shortValues;
    private HashSet<Float> floatValues;
    private LinkedHashSet<Character> characterValues;
    private Iterable<Long> boxedLongValues;
    private Collection<Date> dateValues;
    private List<Instant> instantValues;
    private Set<ZonedDateTime> zonedDateTimeValues;
    private ArrayList<OffsetDateTime> offsetDateTimeValues;
    private LinkedList<LocalDateTime> localDateTimeValues;
    private HashSet<LocalDate> localDateValues;
    private LinkedHashSet<YearMonth> yearMonthValues;
    private Iterable<Year> yearValues;
    private Collection<BigInteger> bigIntegerValues;
    private List<BigDecimal> bigDecimalValues;
    private Set<UUID> uuidValues;

    public Iterable<String> getStringValues() {
        return stringValues;
    }

    @Named("String")
    public void setStringValues(Iterable<String> stringValues) {
        this.stringValues = stringValues;
    }

    public Collection<Boolean> getBooleanValues() {
        return booleanValues;
    }

    @Named("Boolean")
    public void setBooleanValues(Collection<Boolean> booleanValues) {
        this.booleanValues = booleanValues;
    }

    public List<Integer> getIntegerValues() {
        return integerValues;
    }

    @Named("Integer")
    public void setIntegerValues(List<Integer> integerValues) {
        this.integerValues = integerValues;
    }

    public Set<Double> getDoubleValues() {
        return doubleValues;
    }

    @Named("Double")
    public void setDoubleValues(Set<Double> doubleValues) {
        this.doubleValues = doubleValues;
    }

    public ArrayList<Byte> getByteValues() {
        return byteValues;
    }

    @Named("Byte")
    public void setByteValues(ArrayList<Byte> byteValues) {
        this.byteValues = byteValues;
    }

    public LinkedList<Short> getShortValues() {
        return shortValues;
    }

    @Named("Short")
    public void setShortValues(LinkedList<Short> shortValues) {
        this.shortValues = shortValues;
    }

    public HashSet<Float> getFloatValues() {
        return floatValues;
    }

    @Named("Float")
    public void setFloatValues(HashSet<Float> floatValues) {
        this.floatValues = floatValues;
    }

    public LinkedHashSet<Character> getCharacterValues() {
        return characterValues;
    }

    @Named("Character")
    public void setCharacterValues(LinkedHashSet<Character> characterValues) {
        this.characterValues = characterValues;
    }

    public Iterable<Long> getBoxedLongValues() {
        return boxedLongValues;
    }

    @Named("Long")
    public void setBoxedLongValues(Iterable<Long> boxedLongValues) {
        this.boxedLongValues = boxedLongValues;
    }

    public Collection<Date> getDateValues() {
        return dateValues;
    }

    @Named("Date")
    public void setDateValues(Collection<Date> dateValues) {
        this.dateValues = dateValues;
    }

    public List<Instant> getInstantValues() {
        return instantValues;
    }

    @Named("Instant")
    public void setInstantValues(List<Instant> instantValues) {
        this.instantValues = instantValues;
    }

    public Set<ZonedDateTime> getZonedDateTimeValues() {
        return zonedDateTimeValues;
    }

    @Named("ZonedDateTime")
    public void setZonedDateTimeValues(Set<ZonedDateTime> zonedDateTimeValues) {
        this.zonedDateTimeValues = zonedDateTimeValues;
    }

    public ArrayList<OffsetDateTime> getOffsetDateTimeValues() {
        return offsetDateTimeValues;
    }

    @Named("OffsetDateTime")
    public void setOffsetDateTimeValues(ArrayList<OffsetDateTime> offsetDateTimeValues) {
        this.offsetDateTimeValues = offsetDateTimeValues;
    }

    public LinkedList<LocalDateTime> getLocalDateTimeValues() {
        return localDateTimeValues;
    }

    @Named("LocalDateTime")
    public void setLocalDateTimeValues(LinkedList<LocalDateTime> localDateTimeValues) {
        this.localDateTimeValues = localDateTimeValues;
    }

    public HashSet<LocalDate> getLocalDateValues() {
        return localDateValues;
    }

    @Named("LocalDate")
    public void setLocalDateValues(HashSet<LocalDate> localDateValues) {
        this.localDateValues = localDateValues;
    }

    public LinkedHashSet<YearMonth> getYearMonthValues() {
        return yearMonthValues;
    }

    @Named("YearMonth")
    public void setYearMonthValues(LinkedHashSet<YearMonth> yearMonthValues) {
        this.yearMonthValues = yearMonthValues;
    }

    public Iterable<Year> getYearValues() {
        return yearValues;
    }

    @Named("Year")
    public void setYearValues(Iterable<Year> yearValues) {
        this.yearValues = yearValues;
    }

    public Collection<BigInteger> getBigIntegerValues() {
        return bigIntegerValues;
    }

    @Named("BigInteger")
    public void setBigIntegerValues(Collection<BigInteger> bigIntegerValues) {
        this.bigIntegerValues = bigIntegerValues;
    }

    public List<BigDecimal> getBigDecimalValues() {
        return bigDecimalValues;
    }

    @Named("BigDecimal")
    public void setBigDecimalValues(List<BigDecimal> bigDecimalValues) {
        this.bigDecimalValues = bigDecimalValues;
    }

    public Set<UUID> getUuidValues() {
        return uuidValues;
    }

    @Named("UUID")
    public void setUuidValues(Set<UUID> uuidValues) {
        this.uuidValues = uuidValues;
    }
}
