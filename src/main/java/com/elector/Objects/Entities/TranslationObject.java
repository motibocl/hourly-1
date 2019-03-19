package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

/**
 * Created by Sigal on 9/2/2016.
 */
public class TranslationObject extends BaseEntity{
    private String translationKey;
    private String translationValue;
    private String en;

    public TranslationObject() {
    }

    public TranslationObject(String translationKey, String translationValue, String en) {
        this.translationKey = translationKey;
        this.translationValue = translationValue;
        this.en = en;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationValue() {
        return translationValue;
    }

    public void setTranslationValue(String translationValue) {
        this.translationValue = translationValue;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
