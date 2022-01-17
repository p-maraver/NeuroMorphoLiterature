/*
 * Copyright (c) 2015-2022, Patricia Maraver
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */

package org.neuromorpho.literature.evaluate.model;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Thresholds {
   
    private Float positiveH;
    private Float positiveL;
    private Float negativeH;
    private Float negativeL;
    private Float positiveLaborSavedH;
    private Float positiveLaborSavedL;
    private Float negativeLaborSavedH;
    private Float negativeLaborSavedL;
    private Float falseNegativesL;
    private Float falseNegativesH;
    private Float falsePositivesL;
    private Float falsePositivesH;

    
    public Thresholds() {
    }

    public Float getPositiveH() {
        return positiveH;
    }

    public void setPositiveH(Float positiveH) {
        this.positiveH = positiveH;
    }

    public Float getPositiveL() {
        return positiveL;
    }

    public void setPositiveL(Float positiveL) {
        this.positiveL = positiveL;
    }

    public Float getNegativeH() {
        return negativeH;
    }

    public void setNegativeH(Float negativeH) {
        this.negativeH = negativeH;
    }

    public Float getNegativeL() {
        return negativeL;
    }

    public void setNegativeL(Float negativeL) {
        this.negativeL = negativeL;
    }

    public Float getPositiveLaborSavedH() {
        return positiveLaborSavedH;
    }

    public void setPositiveLaborSavedH(Float positiveLaborSavedH) {
        this.positiveLaborSavedH = positiveLaborSavedH;
    }

    public Float getPositiveLaborSavedL() {
        return positiveLaborSavedL;
    }

    public void setPositiveLaborSavedL(Float positiveLaborSavedL) {
        this.positiveLaborSavedL = positiveLaborSavedL;
    }

    public Float getNegativeLaborSavedH() {
        return negativeLaborSavedH;
    }

    public void setNegativeLaborSavedH(Float negativeLaborSavedH) {
        this.negativeLaborSavedH = negativeLaborSavedH;
    }

    public Float getNegativeLaborSavedL() {
        return negativeLaborSavedL;
    }

    public void setNegativeLaborSavedL(Float negativeLaborSavedL) {
        this.negativeLaborSavedL = negativeLaborSavedL;
    }

    public Float getFalseNegativesL() {
        return falseNegativesL;
    }

    public void setFalseNegativesL(Float falseNegativesL) {
        this.falseNegativesL = falseNegativesL;
    }

    public Float getFalseNegativesH() {
        return falseNegativesH;
    }

    public void setFalseNegativesH(Float falseNegativesH) {
        this.falseNegativesH = falseNegativesH;
    }

    public Float getFalsePositivesL() {
        return falsePositivesL;
    }

    public void setFalsePositivesL(Float falsePositivesL) {
        this.falsePositivesL = falsePositivesL;
    }

    public Float getFalsePositivesH() {
        return falsePositivesH;
    }

    public void setFalsePositivesH(Float falsePositivesH) {
        this.falsePositivesH = falsePositivesH;
    }
}
