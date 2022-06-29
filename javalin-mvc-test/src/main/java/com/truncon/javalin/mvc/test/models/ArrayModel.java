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
import java.util.Date;
import java.util.UUID;

public final class ArrayModel {
    private boolean[] booleanValues;
    private int[] intValues;
    private double[] doubleValues;
    private byte[] byteValues;
    private short[] shortValues;
    private float[] floatValues;
    private char[] charValues;
    private long[] longValues;
    private String[] stringValues;
    private Boolean[] boxedBooleanValues;
    private Integer[] boxedIntegerValues;
    private Double[] boxedDoubleValues;
    private Byte[] boxedByteValues;
    private Short[] boxedShortValues;
    private Float[] boxedFloatValues;
    private Character[] boxedCharacterValues;
    private Long[] boxedLongValues;
    private Date[] dateValues;
    private Instant[] instantValues;
    private ZonedDateTime[] zonedDateTimeValues;
    private OffsetDateTime[] offsetDateTimeValues;
    private LocalDateTime[] localDateTimeValues;
    private LocalDate[] localDateValues;
    private YearMonth[] yearMonthValues;
    private Year[] yearValues;
    private BigInteger[] bigIntegerValues;
    private BigDecimal[] bigDecimalValues;
    private UUID[] uuidValues;

    public boolean[] getBooleanValues() {
        return booleanValues;
    }

    @Named("boolean")
    public void setBooleanValues(boolean[] booleanValues) {
        this.booleanValues = booleanValues;
    }

    public int[] getIntValues() {
        return intValues;
    }

    @Named("int")
    public void setIntValues(int[] intValues) {
        this.intValues = intValues;
    }

    public double[] getDoubleValues() {
        return doubleValues;
    }

    @Named("double")
    public void setDoubleValues(double[] doubleValues) {
        this.doubleValues = doubleValues;
    }

    public byte[] getByteValues() {
        return byteValues;
    }

    @Named("byte")
    public void setByteValues(byte[] byteValues) {
        this.byteValues = byteValues;
    }

    public short[] getShortValues() {
        return shortValues;
    }

    @Named("short")
    public void setShortValues(short[] shortValues) {
        this.shortValues = shortValues;
    }

    public float[] getFloatValues() {
        return floatValues;
    }

    @Named("float")
    public void setFloatValues(float[] floatValues) {
        this.floatValues = floatValues;
    }

    public char[] getCharValues() {
        return charValues;
    }

    @Named("char")
    public void setCharValues(char[] charValues) {
        this.charValues = charValues;
    }

    public long[] getLongValues() {
        return longValues;
    }

    @Named("long")
    public void setLongValues(long[] longValues) {
        this.longValues = longValues;
    }

    public String[] getStringValues() {
        return stringValues;
    }

    @Named("String")
    public void setStringValues(String[] stringValues) {
        this.stringValues = stringValues;
    }

    public Boolean[] getBoxedBooleanValues() {
        return boxedBooleanValues;
    }

    @Named("Boolean")
    public void setBoxedBooleanValues(Boolean[] boxedBooleanValues) {
        this.boxedBooleanValues = boxedBooleanValues;
    }

    public Integer[] getBoxedIntegerValues() {
        return boxedIntegerValues;
    }

    @Named("Integer")
    public void setBoxedIntegerValues(Integer[] boxedIntegerValues) {
        this.boxedIntegerValues = boxedIntegerValues;
    }

    public Double[] getBoxedDoubleValues() {
        return boxedDoubleValues;
    }

    @Named("Double")
    public void setBoxedDoubleValues(Double[] boxedDoubleValues) {
        this.boxedDoubleValues = boxedDoubleValues;
    }

    public Byte[] getBoxedByteValues() {
        return boxedByteValues;
    }

    @Named("Byte")
    public void setBoxedByteValues(Byte[] boxedByteValues) {
        this.boxedByteValues = boxedByteValues;
    }

    public Short[] getBoxedShortValues() {
        return boxedShortValues;
    }

    @Named("Short")
    public void setBoxedShortValues(Short[] boxedShortValues) {
        this.boxedShortValues = boxedShortValues;
    }

    public Float[] getBoxedFloatValues() {
        return boxedFloatValues;
    }

    @Named("Float")
    public void setBoxedFloatValues(Float[] boxedFloatValues) {
        this.boxedFloatValues = boxedFloatValues;
    }

    public Character[] getBoxedCharacterValues() {
        return boxedCharacterValues;
    }

    @Named("Character")
    public void setBoxedCharacterValues(Character[] boxedCharacterValues) {
        this.boxedCharacterValues = boxedCharacterValues;
    }

    public Long[] getBoxedLongValues() {
        return boxedLongValues;
    }

    @Named("Long")
    public void setBoxedLongValues(Long[] boxedLongValues) {
        this.boxedLongValues = boxedLongValues;
    }

    public Date[] getDateValues() {
        return dateValues;
    }

    @Named("Date")
    public void setDateValues(Date[] dateValues) {
        this.dateValues = dateValues;
    }

    public Instant[] getInstantValues() {
        return instantValues;
    }

    @Named("Instant")
    public void setInstantValues(Instant[] instantValues) {
        this.instantValues = instantValues;
    }

    public ZonedDateTime[] getZonedDateTimeValues() {
        return zonedDateTimeValues;
    }

    @Named("ZonedDateTime")
    public void setZonedDateTimeValues(ZonedDateTime[] zonedDateTimeValues) {
        this.zonedDateTimeValues = zonedDateTimeValues;
    }

    public OffsetDateTime[] getOffsetDateTimeValues() {
        return offsetDateTimeValues;
    }

    @Named("OffsetDateTime")
    public void setOffsetDateTimeValues(OffsetDateTime[] offsetDateTimeValues) {
        this.offsetDateTimeValues = offsetDateTimeValues;
    }

    public LocalDateTime[] getLocalDateTimeValues() {
        return localDateTimeValues;
    }

    @Named("LocalDateTime")
    public void setLocalDateTimeValues(LocalDateTime[] localDateTimeValues) {
        this.localDateTimeValues = localDateTimeValues;
    }

    public LocalDate[] getLocalDateValues() {
        return localDateValues;
    }

    @Named("LocalDate")
    public void setLocalDateValues(LocalDate[] localDateValues) {
        this.localDateValues = localDateValues;
    }

    public YearMonth[] getYearMonthValues() {
        return yearMonthValues;
    }

    @Named("YearMonth")
    public void setYearMonthValues(YearMonth[] yearMonthValues) {
        this.yearMonthValues = yearMonthValues;
    }

    public Year[] getYearValues() {
        return yearValues;
    }

    @Named("Year")
    public void setYearValues(Year[] yearValues) {
        this.yearValues = yearValues;
    }

    public BigInteger[] getBigIntegerValues() {
        return bigIntegerValues;
    }

    @Named("BigInteger")
    public void setBigIntegerValues(BigInteger[] bigIntegerValues) {
        this.bigIntegerValues = bigIntegerValues;
    }

    public BigDecimal[] getBigDecimalValues() {
        return bigDecimalValues;
    }

    @Named("BigDecimal")
    public void setBigDecimalValues(BigDecimal[] bigDecimalValues) {
        this.bigDecimalValues = bigDecimalValues;
    }

    public UUID[] getUuidValues() {
        return uuidValues;
    }

    @Named("UUID")
    public void setUuidValues(UUID[] uuidValues) {
        this.uuidValues = uuidValues;
    }
}
